package com.example.rsocketflux;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class JobStatusEmitter {

    @Bean
    public Sinks.Many<JobStatusMessage> jobStatusSink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Flux<JobStatusMessage> jobStatusStream(final Sinks.Many<JobStatusMessage> jobStatusSink) {
        return jobStatusSink.asFlux();
    }

}
