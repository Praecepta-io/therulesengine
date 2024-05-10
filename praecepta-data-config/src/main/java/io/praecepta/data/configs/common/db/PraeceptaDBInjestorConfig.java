package io.praecepta.data.configs.common.db;

import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;

public class PraeceptaDBInjestorConfig extends GenericAbstractPraeceptaDataConfig {

	@Override
	public void setUpConfiguration() {
		addNameAndDataType(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
		addNameAndDataType(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING);
	}

}
