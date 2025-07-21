package io.praecepta.core.cocurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PraeceptaExecutor {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaExecutor.class);
	
	private Semaphore semaphore;
	
	private ExecutorService executorService;
	
	public PraeceptaExecutor( int maxNumberOfTasksInProcess ) {
		
		this(Runtime.getRuntime().availableProcessors(), maxNumberOfTasksInProcess);
	}

	public PraeceptaExecutor(int concurrentThreadSize, int maxNumberOfTasksInProcess ) {
		
		semaphore = new Semaphore(maxNumberOfTasksInProcess);
		
		executorService = Executors.newFixedThreadPool(concurrentThreadSize);
	}
	
	public void submitTask(final Runnable command) throws InterruptedException, RejectedExecutionException {
		LOG.debug("Number of Tasks Can be Submitted "+semaphore.availablePermits());
        semaphore.acquire(); // Acquire a permit, blocking if no permits are available.
        try {
        	executorService.submit(() -> {
                try {
                    command.run(); // Execute the actual task.
                } finally {
                    semaphore.release(); // Release the permit when the task completes (or fails).
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release(); // Release the permit if the task is rejected by the executor.
            throw e;
        }
    }
	
	public <T> Future<?> submitTask(final Callable<T> command) throws InterruptedException, RejectedExecutionException {
		
		semaphore.acquire(); // Acquire a permit, blocking if no permits are available.
		try {
			return executorService.submit(() -> {
				try {
						command.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					semaphore.release(); // Release the permit when the task completes (or fails).
				}
			});
		} catch (RejectedExecutionException e) {
			semaphore.release(); // Release the permit if the task is rejected by the executor.
			throw e;
		}
	}
	
	public void shutdown() {
		executorService.shutdown();
	}
	
	public void shutdownNow() {
		executorService.shutdownNow();
	}
	
	public boolean awaitTermination(long timeout, TimeUnit unit) {
		LOG.info("Inside Await Termination ");
		try {
			return executorService.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			
			return true;
		}
	}

}
