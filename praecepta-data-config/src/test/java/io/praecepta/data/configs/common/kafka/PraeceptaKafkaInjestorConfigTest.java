package io.praecepta.data.configs.common.kafka;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;

public class PraeceptaKafkaInjestorConfigTest {

	@Test
	public void test() {

		PraeceptaKafkaInjestorConfig kafkaConfig = new PraeceptaKafkaInjestorConfig();

		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());

		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER,
				500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"Test Pass");

		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");

		kafkaConfig.postConfigSetup();

		System.out.println(kafkaConfig);
		assertNotNull(kafkaConfig);

	}
}
