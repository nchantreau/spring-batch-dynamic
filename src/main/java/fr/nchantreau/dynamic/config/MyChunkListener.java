package fr.nchantreau.dynamic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class MyChunkListener implements ChunkListener {

	private static final Logger log = LoggerFactory.getLogger(MyChunkListener.class);
	
	@Override
	public void beforeChunk(ChunkContext context) {
	}

	@Override
	public void afterChunk(ChunkContext context) {
		int count = context.getStepContext().getStepExecution().getReadCount();
		log.info(String.format("%d reads", count));
	}

	@Override
	public void afterChunkError(ChunkContext context) {
	}

}
