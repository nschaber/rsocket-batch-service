package com.example.rsocketflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static java.lang.Thread.sleep;
import static org.springframework.batch.repeat.RepeatStatus.FINISHED;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Slf4j
@Configuration
public class BatchConfig {

    public static final String JOB_NAME = "job";
    public static final String STEP_NAME = "job";
    private final JobListener jobListener;
    private final StepListener stepListener;

    public BatchConfig(final JobListener jobListener, final StepListener stepListener) {
        this.jobListener = jobListener;
        this.stepListener = stepListener;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript("/org/springframework/batch/core/schema-h2.sql")
                .setName("jobs")
                .build();
    }

    @Bean
    public Job job(final JobRepository jobRepository, final Step step) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(this.jobListener)
                .start(step)
                .next(step)
                .next(step)
                .build();
    }

    @Bean
    public Step step(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .listener(this.stepListener)
                .tasklet((final var stepContribution, final var chunkContext) -> {
                    sleep(5000);
                    return FINISHED;
                }, transactionManager)
                .build();
    }

}
