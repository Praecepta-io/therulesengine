package io.praecepta.data.collectors.common;

import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.IPraeceptaDataConfigElementNameType;

/**
 * @author rajasrikar
 * 
 */

public interface IPraeceptaDataCollectorConfig {

	void preConfigSetup();
	
	void addConfigElement(IPraeceptaDataConfigElementNameType configElementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType, Object elementValue);
	
	void setUpConfiguration();
	
	boolean validateConfiguration(IPraeceptaDataConfigElementNameType configElementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType);
	
	void postConfigSetup();
	
	boolean cleanUpConfig();
}
