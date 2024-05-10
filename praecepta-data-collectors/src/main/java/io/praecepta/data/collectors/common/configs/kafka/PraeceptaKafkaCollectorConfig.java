package io.praecepta.data.collectors.common.configs.kafka;

import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;

public class PraeceptaKafkaCollectorConfig extends GenericAbstractPraeceptaDataConfig{

	@Override
	public void setUpConfiguration() {
		addNameAndDataType(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER);
		addNameAndDataType(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
	}

}
