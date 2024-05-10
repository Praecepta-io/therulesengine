package io.praecepta.data.collectors.common.configs.kafka;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;

public class PraeceptaKafkaCollectorConfigTest {

	@Test
	public void test() {

		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();

		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, 500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		kafkaConfig.postConfigSetup();
		
		System.out.println(kafkaConfig);
		assertNotNull(kafkaConfig);
		
	}

	@Test
	public void testNegative() {
		
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();
		
		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		// Pass a Wrong Data Type. Which is nothing but, not passing the right mandatory attributes
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "500");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		kafkaConfig.postConfigSetup();
		
		System.out.println(kafkaConfig);
		assertNotNull(kafkaConfig);
		
	}

	

}
