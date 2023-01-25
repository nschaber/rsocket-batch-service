package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.UUID;

import static io.rsocket.metadata.WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION;
import static java.time.Duration.ofSeconds;
import static org.springframework.messaging.rsocket.RSocketRequester.Builder;
import static org.springframework.util.MimeTypeUtils.parseMimeType;
import static reactor.util.retry.Retry.fixedDelay;

@Slf4j
@Controller
public class SocketConfig {

    @Value("${spring.rsocket.server.port}")
    private Integer socketPort;

    @Value("${spring.rsocket.server.hostname}")
    private String socketHost;

    @Value("${spring.rsocket.server.mapping-path}")
    private String socketPath;

    private final Flux<JobStatusMessage> jobStatusStream;

    private final Builder builder;

    public static final String ROUTE = "/execution";
    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private static final MimeType SIMPLE_AUTH = parseMimeType(MESSAGE_RSOCKET_AUTHENTICATION.getString());

    public SocketConfig(final Flux<JobStatusMessage> jobStatusStream, final Builder rSocketRequesterBuilder) {
        this.jobStatusStream = jobStatusStream;
        this.builder = rSocketRequesterBuilder;
    }

    @MessageMapping(value = ROUTE)
    public Flux<JobStatusMessage> requestStream(final Long id) {
        log.warn("Connecting new client for status updated");
        return this.jobStatusStream.filter((final var jobStatusMessage) -> jobStatusMessage.getId().equals(id));
    }

    @Bean
    public RSocketRequester rSocketRequester(){
        try {
            final var user = new UsernamePasswordMetadata("admin", "admin");
            return this.builder
                    .setupData(CLIENT_ID)
                    .setupMetadata(user, SIMPLE_AUTH)
                    .rsocketStrategies((final var strategies) -> strategies.encoder(new SimpleAuthenticationEncoder()))
                    .rsocketConnector((final var rSocketConnector) -> rSocketConnector.reconnect(fixedDelay(2, ofSeconds(2))))
                    .websocket(new URI("ws://%s:%s%s".formatted(this.socketHost, this.socketPort, this.socketPath)));
        } catch (final Exception e){
            throw new RuntimeException(e);
        }
    }

}
