package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class StepListener implements StepExecutionListener {

    private final Sinks.Many<JobStatusMessage> jobStatusSink;

    public StepListener(final Sinks.Many<JobStatusMessage> jobStatusSink) {
        this.jobStatusSink = jobStatusSink;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        this.jobStatusSink.tryEmitNext(new JobStatusMessage(stepExecution.getJobExecution().getStatus().toString(), stepExecution.getJobExecutionId()));
        return stepExecution.getExitStatus();
    }
}
