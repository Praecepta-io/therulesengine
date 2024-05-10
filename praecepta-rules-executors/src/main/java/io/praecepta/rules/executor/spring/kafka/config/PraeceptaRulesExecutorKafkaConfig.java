package io.praecepta.rules.executor.spring.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.collector.PraeceptaKafkaDataCollector;
import io.praecepta.data.collectors.common.configs.kafka.PraeceptaKafkaCollectorConfig;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;
import io.praecepta.rules.executor.config.condition.kafka.KafkaConditionConfigSelector;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_CONFIG_KEYS;

@Configuration
@Conditional(KafkaConditionConfigSelector.class)
public class PraeceptaRulesExecutorKafkaConfig {
	
	@Autowired
	private Environment env;

	@Bean(name = "dataCollector")
	public IPraeceptaDataCollector getDataCollector() {

		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();

		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS,
				COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, getConfigPropName(PraeceptaKafkaDataConfigType.BROKERS.getElementName()));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT,
				COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, getConfigPropName(PraeceptaKafkaDataConfigType.PORT.getElementName()));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME,
				COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, getConfigPropName(PraeceptaKafkaDataConfigType.USERNAME.getElementName()));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD,
				COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, getConfigPropName(PraeceptaKafkaDataConfigType.PASSWORD.getElementName()));

		kafkaConfig.addNonMandatoryConfigElements("max.timeout", getConfigPropName("max.timeout"));
		kafkaConfig.addNonMandatoryConfigElements("key.deserializer",getConfigPropName("key.deserializer"));
		kafkaConfig.addNonMandatoryConfigElements("value.deserializer",getConfigPropName("value.deserializer"));
		kafkaConfig.addNonMandatoryConfigElements("group.id", getConfigPropName("group.id"));
		kafkaConfig.addNonMandatoryConfigElements("kafka.receiver.topic", getConfigPropName("kafka.receiver.topic"));

		PraeceptaKafkaDataCollector kafkaCollector = new PraeceptaKafkaDataCollector();
		kafkaCollector.openCollectorConnection(kafkaConfig);
		return kafkaCollector;
	}

	private String getConfigPropName(String configPropertyKey) {
		return env.getProperty(getConnectionPrefix() + configPropertyKey);
	}

	private String getConnectionPrefix() {
		return System.getProperty(DATA_COLLECTOR_CONFIG_KEYS.CONN_PROPS_PREFIX.value) + ".";
	}
}
