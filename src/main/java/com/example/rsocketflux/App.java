package com.example.rsocketflux;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@EnableBatchProcessing
@EnableWebFluxSecurity
@SpringBootApplication
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class App {

    public static void main(final String... args) {
        SpringApplication.run(App.class, args);
    }

}
