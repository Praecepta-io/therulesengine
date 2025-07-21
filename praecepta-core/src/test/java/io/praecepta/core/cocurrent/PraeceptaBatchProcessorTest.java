package io.praecepta.core.cocurrent;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class PraeceptaBatchProcessorTest {

	@Test
	public void test() {
		
		PraeceptaBatchProcessor<String> praeceptaBatchProcessor = new PraeceptaBatchProcessor<>(10, 500L);
		
		/*praeceptaBatchProcessor.setProcessor( (CollectionData) -> {
			
			
		});*/
		
		for(int i=0; i< 500; i++) {
			praeceptaBatchProcessor.addData("Data Id --> "+i);
		}
		
//		CountDownLatch latch = new CountDownLatch(1);
//		
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			latch.countDown();
//		}
		
		praeceptaBatchProcessor.shutDownBatchProcess();
		
	}

}
