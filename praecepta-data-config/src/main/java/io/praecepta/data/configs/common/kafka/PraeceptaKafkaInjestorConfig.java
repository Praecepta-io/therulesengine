package io.praecepta.data.configs.common.kafka;

import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;

public class PraeceptaKafkaInjestorConfig extends GenericAbstractPraeceptaDataConfig{

	@Override
	public void setUpConfiguration() {
		addNameAndDataType(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER);
		addNameAndDataType(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
	}

}
