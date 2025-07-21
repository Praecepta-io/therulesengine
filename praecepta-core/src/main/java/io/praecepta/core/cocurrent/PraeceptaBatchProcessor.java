package io.praecepta.core.cocurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.intf.IPraeceptaDataProcessor;

public class PraeceptaBatchProcessor<E> {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaBatchProcessor.class);
	
	private long timeToDrain;
	private int maxDataRecordsToProcess;
	private PraeceptaExecutor praeceptaExecutor;
	private AtomicBoolean setUpIsDone = new AtomicBoolean(false); 
	private BlockingQueue<E> dataCaptureQueue;
	private boolean stopToProcess;
	private long lastDrainTime;
	private IPraeceptaDataProcessor<E> processor;
	
	public PraeceptaBatchProcessor(int maxDataRecordsToProcess, long timeToDrain) {
		this.maxDataRecordsToProcess = maxDataRecordsToProcess;
		this.timeToDrain = timeToDrain;
	}
	
	public PraeceptaBatchProcessor(int maxDataRecordsToProcess, long timeToDrain, IPraeceptaDataProcessor<E> processor) {
		this.maxDataRecordsToProcess = maxDataRecordsToProcess;
		this.timeToDrain = timeToDrain;
		this.processor = processor;
	}
	
	public void setProcessor(IPraeceptaDataProcessor<E> processor) {
		this.processor = processor;
	}

	public final void doTheSetUp() {
		
		if(!setUpIsDone.get()) {
			
			int runTimeProcessCount = Runtime.getRuntime().availableProcessors();
			
			praeceptaExecutor = new PraeceptaExecutor(runTimeProcessCount, runTimeProcessCount * 4);
			
			dataCaptureQueue = new LinkedBlockingDeque<>(maxDataRecordsToProcess * runTimeProcessCount);
		}
		
		if(processor == null) {
			// Create a Default Data Log Processor 
			processor = (data) -> {
				data.forEach( eachDataEntry -> LOG.info("Data Entry --> "+eachDataEntry));
			};
		}
		
		Runnable agentToCollectData = () -> {
			
			while(!stopToProcess && processor != null) {
				
				if(readyToProcess()) {
					
					triggerBatchTask();
					
					lastDrainTime = System.currentTimeMillis();
				}
				
				try{
					Thread.sleep(100L);
				}catch(InterruptedException ex){
					LOG.error("Exception in While Sleep in the Batch Processor ", ex);
				}
			}
			
			LOG.info("Shutdown Batch Process has Triggered ");
			
			
		};
		
		Thread agentToHandleData = new Thread(agentToCollectData);
		
		agentToHandleData.start();
		
		setUpIsDone.set(true);
		
	}

	private void performPostShutdown() {
		
		if(dataCaptureQueue.size() > 0) {
			LOG.info("Triggering the Task for Remaining Objects with Size --> "+dataCaptureQueue.size());
			// this is needed to process the remaining data entries sitting on the topic
			maxDataRecordsToProcess = dataCaptureQueue.size();
			triggerBatchTask();
		}
		
		praeceptaExecutor.shutdown();
		
		while(!praeceptaExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
			LOG.error(" Waiting for all the Tasks to Finish from the Batch Processor ");
		}
		praeceptaExecutor.shutdownNow();
	}

	private void triggerBatchTask() {
		List<E> dataRecordsToProcess = new ArrayList<>();
		
		dataCaptureQueue.drainTo(dataRecordsToProcess, maxDataRecordsToProcess);
		
		try {
			praeceptaExecutor.submitTask( () -> processor.processData(dataRecordsToProcess) );
		} catch (RejectedExecutionException e) {
			LOG.error("Submitted Task is rejected on the Executor", e);
		} catch (InterruptedException e) {
			LOG.error("Submitted Task is Interrupted on the Executor", e);
		}
	}
	
	private boolean readyToProcess() {
		
		if( dataCaptureQueue.size() >= maxDataRecordsToProcess || (System.currentTimeMillis() > (timeToDrain + lastDrainTime)) ) return true;
		else return false;

	}

	public void addData(E data) {
		
		if(!setUpIsDone.get()) {
			doTheSetUp();
		}
		
		try {
			dataCaptureQueue.put(data);
		} catch (InterruptedException e) {
			LOG.error("Exception While Adding the Data to Process ", e);
		}
	}
	
	public void shutDownBatchProcess() {
		stopToProcess = true;
		
		performPostShutdown();
	}

}
