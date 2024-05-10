package io.praecepta.data.configs.common;

/**
 * @author rajasrikar
 * 
 */

public interface IPraeceptaDataConfig {

	void preConfigSetup();
	
	void addConfigElement(IPraeceptaDataConfigElementNameType configElementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType, Object elementValue);
	
	void setUpConfiguration();
	
	boolean validateConfiguration(IPraeceptaDataConfigElementNameType configElementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType);
	
	void postConfigSetup();
	
	boolean cleanUpConfig();
	
	public static enum COLLECTOR_CONFIG_DATA_ELEMENT_TYPE{
		STRING, CHAR, INTEGRER, LONG, FLOAT, DECIMAL;
	}
}
