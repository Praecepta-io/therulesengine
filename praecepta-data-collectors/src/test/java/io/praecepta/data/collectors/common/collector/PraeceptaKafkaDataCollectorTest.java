package io.praecepta.data.collectors.common.collector;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import io.praecepta.data.collectors.common.configs.kafka.PraeceptaKafkaCollectorConfig;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;

public class PraeceptaKafkaDataCollectorTest {

	@Test
	public void test() {
		
		PraeceptaKafkaDataCollector kafkaCollector = new PraeceptaKafkaDataCollector();
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();

		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, 500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		
		kafkaCollector.openCollectorConnection(kafkaConfig);
		
		kafkaCollector.startDataCollector();
		
		assertEquals(kafkaCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		
		int counter = 0;
		
		while(kafkaCollector.isDataCollectable()) {
			System.out.println("Here in Data Collectible");
			
			Collection<PraeceptaDataRecord> recordsPolled = kafkaCollector.getCollectedData();
			counter++;
			if(counter == 10) {
				CONNECTION_STATUS status = kafkaCollector.getCollectorStatus();
				System.out.println("Status --> "+kafkaCollector.getCollectorStatus());
				
//				kafkaCollector.stopCollectingData();
				kafkaCollector.terminateDataCollector();
			}
		}
		
	}
	
	@Test
	public void testNegative1() {
		
		PraeceptaKafkaDataCollector kafkaCollector = new PraeceptaKafkaDataCollector();
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();
		
		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		
		kafkaCollector.openCollectorConnection(kafkaConfig);
		
		kafkaCollector.startDataCollector();
		
		assertEquals(kafkaCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		
	}
	
	@Test
	public void testNegative2() {
		
		PraeceptaKafkaDataCollector kafkaCollector = new PraeceptaKafkaDataCollector();
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();
		
		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		// Comment this
//		kafkaSink.openSinkConnection(kafkaConfig);
		
		// Start the Sink without OpenConnection
		kafkaCollector.startDataCollector();
		
		assertEquals(kafkaCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		
	}
	
	@Test
	public void testKafkaDataCollector() {
		
		PraeceptaKafkaDataCollector kafkaCollector = new PraeceptaKafkaDataCollector();
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();
		
		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "localhost:9092");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, 500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "test");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "test");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		kafkaConfig.addNonMandatoryConfigElements("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaConfig.addNonMandatoryConfigElements("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaConfig.addNonMandatoryConfigElements("group.id", "test");
		kafkaConfig.addNonMandatoryConfigElements("kafka.receiver.topic", "TEST.TOPIC");
		
		kafkaCollector.openCollectorConnection(kafkaConfig);

		// Start the Sink without OpenConnection
		kafkaCollector.startDataCollector();

		assertEquals(kafkaCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		int counter = 0;

		while (kafkaCollector.isDataCollectable()) {
			System.out.println("Here in Data Collectible");

			Collection<PraeceptaDataRecord> recordsPolled = kafkaCollector.getCollectedData();

			System.out.println("Number of Records Polled --> " + recordsPolled.size());
			try {
				Thread.sleep(1000*5L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			counter++;
			if (counter == 10) {
				CONNECTION_STATUS status = kafkaCollector.getCollectorStatus();
				System.out.println("Status --> " + kafkaCollector.getCollectorStatus());

//				testDataCollector.stopCollectingData();
				kafkaCollector.terminateDataCollector();

				// Call this method below to make sure we didn't miss any records to pull even
				// after the Stop or Terminate command executed
				while (!kafkaCollector.awaitForDataCollectionStop()) {

					try {
						Thread.sleep(10L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Collection<PraeceptaDataRecord> recordsPolledAfterTermination = kafkaCollector.getCollectedData();

					System.out.println("Number of Records Polled After Termination --> " + recordsPolledAfterTermination.size());

				}
			}
		}
		
	}

}
