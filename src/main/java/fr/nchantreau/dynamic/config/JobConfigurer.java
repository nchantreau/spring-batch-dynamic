package fr.nchantreau.dynamic.config;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EnableBatchProcessing
public class JobConfigurer {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private MyChunkListener listener;

    public Job createJob(String jobName, String stepName, int chunkSize, int throttleLimit) {
		return jobBuilderFactory
                .get(jobName)
				.incrementer(new RunIdIncrementer())
				.start(stepBuilderFactory
                        .get(stepName)
						.<String, String>chunk(chunkSize)
                        .reader(new ListItemReader<>(
                                IntStream.range(1, 10001).mapToObj(String::valueOf).collect(Collectors.toList())))
						.writer(items -> {
							for (String item : items) {
								System.out.println(">> " + item);
							}
						})
						.listener(listener)
						.throttleLimit(throttleLimit)
						.build())
				.build();
	}
}
