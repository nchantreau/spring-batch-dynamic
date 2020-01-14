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
public class JobCreator {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private MyChunkListener listener;

	public Job createJob(int chunkSize, int throttleLimit) {
		return jobBuilderFactory
				.get("job")
				.incrementer(new RunIdIncrementer())
				.start(stepBuilderFactory
						.get("step")
						.<String, String>chunk(chunkSize)
						.reader(new ListItemReader<>(IntStream.range(1, 110).mapToObj(String::valueOf).collect(Collectors.toList())))
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
