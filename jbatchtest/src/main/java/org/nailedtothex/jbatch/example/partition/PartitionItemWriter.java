package org.nailedtothex.jbatch.example.partition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class PartitionItemWriter implements ItemWriter {
	private static final Logger log = Logger.getLogger(PartitionItemWriter.class.getName());

	@Inject
	private StepContext stepContext;
	private int index;

	@Override
	public void open(Serializable checkpoint) throws Exception {
		index = checkpoint == null ? 0 : (Integer) checkpoint;
		log.log(Level.FINE, "open(): checkpoint={0}, index starts at={1}", new Object[] { checkpoint, index });
	}

	@Override
	public void close() throws Exception {
		log.log(Level.FINE, "close()");
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		log.log(Level.FINE, "checkpointInfo(): returns={0}", index);
		return index;
	}

	@Override
	public void writeItems(List<Object> items) throws Exception {
		writeItems0(items);
		index++;
	}

	@SuppressWarnings("unchecked")
	protected void writeItems0(List<Object> items) throws Exception {
		for (Object item : items) {
			log.log(Level.FINE, "writeItems(): index={0}, item={1}", new Object[]{index, item});
		}

		stepContext.setPersistentUserData(new ArrayList<Object>(items));
	}
}