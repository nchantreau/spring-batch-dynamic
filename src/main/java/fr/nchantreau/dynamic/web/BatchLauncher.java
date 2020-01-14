package fr.nchantreau.dynamic.web;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.nchantreau.dynamic.config.JobCreator;

@RestController
@Import(JobCreator.class)
public class BatchLauncher {
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobCreator jobCreator;

	@GetMapping("/launchjob")
	@ResponseBody
	public String launch(@RequestParam Integer chunkSize, @RequestParam(defaultValue = "5") Integer throttleLimit) throws Exception {

		Job job = jobCreator.createJob(chunkSize, throttleLimit);
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", new Date().getTime()).toJobParameters();
		jobLauncher.run(job, jobParameters);

		return "Done";
	}
}