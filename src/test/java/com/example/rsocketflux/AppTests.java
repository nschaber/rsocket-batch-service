package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;

import static com.example.rsocketflux.SocketConfig.ROUTE;

@Slf4j
@SpringBootTest
class AppTests {

    @Autowired
    private RSocketRequester rSocketRequester;

    @Test
    void contextLoads() throws InterruptedException {
        rSocketRequester
                .route(ROUTE)
                .data(1L)
                .retrieveFlux(JobStatusMessage.class)
                .subscribe(this::print);
        Thread.sleep(10000);
    }

    private void print(final JobStatusMessage jobStatusMessage){
        log.info("Received status update for {}", jobStatusMessage.getId());
    }

}
