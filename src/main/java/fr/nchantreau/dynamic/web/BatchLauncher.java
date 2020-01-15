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

import fr.nchantreau.dynamic.config.JobConfigurer;

@RestController
@Import(JobConfigurer.class)
public class BatchLauncher {
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobConfigurer jobConfigurer;

	@GetMapping("/launchjob")
	@ResponseBody
    public String launch(@RequestParam(defaultValue = "generatedJob") String jobName,
        @RequestParam(defaultValue = "generatedStep") String stepName, 
        @RequestParam Integer chunkSize,
        @RequestParam(defaultValue = "5") Integer throttleLimit) throws Exception {

        Job job = jobConfigurer.createJob(jobName, stepName, chunkSize, throttleLimit);
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", new Date().getTime()).toJobParameters();
		jobLauncher.run(job, jobParameters);

		return "Done";
	}
}