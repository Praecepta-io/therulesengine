package io.praecepta.data.collectors.common.collector;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import io.praecepta.data.collectors.common.configs.kafka.PraeceptaKafkaCollectorConfig;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.exception.PraeceptaDataCollectorException;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;

public class PraeceptaDataCollectorTest {

	@Test
	public void test() {
		
		TestDataCollector testDataCollector = new TestDataCollector();
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();

		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, 500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		
		testDataCollector.openCollectorConnection(kafkaConfig);
		
		testDataCollector.startDataCollector();
		
		assertEquals(testDataCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		
		int counter = 0;
		
		int totalRecordsFromTest = 0;
		
		while(testDataCollector.isDataCollectable()) {
			System.out.println("Here in Data Collectible");
			
			Collection<PraeceptaDataRecord> recordsPolled = testDataCollector.getCollectedData();
			
			System.out.println("Number of Records Polled --> "+recordsPolled.size());
			totalRecordsFromTest = totalRecordsFromTest + recordsPolled.size();
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
			if(counter == 10) {
				CONNECTION_STATUS status = testDataCollector.getCollectorStatus();
				System.out.println("Status --> "+testDataCollector.getCollectorStatus());
				
//				testDataCollector.stopCollectingData();
				testDataCollector.terminateDataCollector();
				
				// Call this method below to make sure we didn't miss any records to pull even after the Stop or Terminate command executed
				while(!testDataCollector.awaitForDataCollectionStop()) {
					
					/*try {
						Thread.sleep(10L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
					
					Collection<PraeceptaDataRecord> recordsPolledAfterTermination = testDataCollector.getCollectedData();
					
					System.out.println("Number of Records Polled After Termination --> "+recordsPolledAfterTermination.size());
					
					totalRecordsFromTest = totalRecordsFromTest + recordsPolledAfterTermination.size();
					
				}
			}
		}
		
		System.out.println("Total Number of Records Received on Test Program --> "+totalRecordsFromTest);
	}
	
	public static class TestDataCollector extends PraeceptaAbstractDataCollector<PraeceptaKafkaCollectorConfig>{
		
		@Override
		protected PraeceptaDataRecord performCollection() {
//			System.out.println("In Perform Collection");

			// This method cannot be called directly. Must call startDataCollector(). Underlying will call this method to poll individual Collection 
			
			if(getCollectorStatus() == null || getCollectorStatus() == CONNECTION_STATUS.INITIALIZED) {
				throw new PraeceptaDataCollectorException("Perform Collector should be called only after Starting the Data Collector");
			}
			
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(5);
			
			dataRecord.addRecordEntry("Message 1", "Id 1", null);
			dataRecord.addRecordEntry("Message 2", "Id 2", null);
			dataRecord.addRecordEntry("Message 3", "Id 3", null);
			dataRecord.addRecordEntry("Message 4", "Id 4", null);
			dataRecord.addRecordEntry("Message 5", "Id 5", null);
			
			return dataRecord;
		}
	}
}


