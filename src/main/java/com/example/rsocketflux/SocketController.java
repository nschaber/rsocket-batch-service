package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class SocketController {

    private final Flux<JobStatusMessage> jobStatusStream;

    public SocketController(final Flux<JobStatusMessage> jobStatusStream) {
        this.jobStatusStream = jobStatusStream;
    }

    @MessageMapping(value = "/execution")
    public Flux<JobStatusMessage> requestStream(final Long id) {
        return this.jobStatusStream.filter((final var jobStatusMessage) -> jobStatusMessage.getId().equals(id));
    }


}
