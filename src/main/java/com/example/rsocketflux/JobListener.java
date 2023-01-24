package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class JobListener implements JobExecutionListener {

    private final Sinks.Many<JobStatusMessage> jobStatusSink;

    public JobListener(final Sinks.Many<JobStatusMessage> jobStatusSink) {
        this.jobStatusSink = jobStatusSink;
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        this.jobStatusSink.tryEmitNext(new JobStatusMessage(jobExecution.getStatus().toString(), jobExecution.getId()));
    }
}
