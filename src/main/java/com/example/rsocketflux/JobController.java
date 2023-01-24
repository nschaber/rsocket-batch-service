package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.example.rsocketflux.BatchConfig.JOB_NAME;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/execution")
public class JobController {

    private final JobRepository jobRepository;
    private final JobLauncher jobLauncher;
    private final Job job;

    public JobController(final JobRepository jobRepository, final JobLauncher jobLauncher, final Job job) {
        this.jobRepository = jobRepository;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @GetMapping(value = "/start", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<Mono<?>> startJob() {
        try {
            final var execution = this.jobLauncher.run(this.job, new JobParameters());
            final var result = new JobStatusMessage();
            result.setId(requireNonNull(execution).getId());
            result.setData(requireNonNull(execution.getStatus().toString()));
            return ResponseEntity.ok(Mono.just(result));
        } catch (final Exception e) {
            final var error = "Could not execute request";
            log.error(error);
            return ResponseEntity.internalServerError().body(Mono.just(error));
        }
    }

    @GetMapping(value = "/status", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<Mono<?>> getStatusOfLast() {
        try {
            final var execution = this.jobRepository.getLastJobExecution(JOB_NAME, new JobParameters());
            final var result = new JobStatusMessage();
            result.setId(requireNonNull(execution).getId());
            result.setData(requireNonNull(execution.getStatus().toString()));
            return ResponseEntity.ok(Mono.just(result));
        } catch (final Exception e) {
            final var error = "Could not execute request";
            log.error(error);
            return ResponseEntity.internalServerError().body(Mono.just(error));
        }
    }

}
