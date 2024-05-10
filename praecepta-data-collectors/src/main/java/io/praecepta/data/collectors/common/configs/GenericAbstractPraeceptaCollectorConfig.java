package io.praecepta.data.collectors.common.configs;

/**
 * @author rajasrikar
 * 
 */
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import io.praecepta.data.configs.common.IPraeceptaDataConfig;
import io.praecepta.data.configs.common.IPraeceptaDataConfigElementNameType;
import io.praecepta.data.configs.common.exception.PraeceptaDataConfigException;

public abstract class GenericAbstractPraeceptaCollectorConfig implements IPraeceptaDataConfig {

	private Map<String, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE> predefinedElementNameToDataTypeMap;

	private Map<ElementNameToDataTypePair, Object> collectorConfigurationWithValues;
	
	private boolean mandatoryAttributeSetUpPerformed;

	@Override
	public void preConfigSetup() {
		predefinedElementNameToDataTypeMap = new HashMap<>();

		collectorConfigurationWithValues = new HashMap<>();

	}

	public Map<String, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE> getMandatoryElementNameAndDataTypeMap() {
		mandatoryCheck();
		return ImmutableMap.copyOf(predefinedElementNameToDataTypeMap);
	}

	protected final void addNameAndDataType(IPraeceptaDataConfigElementNameType configElementName,
			COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType) {
		predefinedElementNameToDataTypeMap.put(configElementName.getElementName(), dataType);
	}

	@Override
	public final void addConfigElement(IPraeceptaDataConfigElementNameType configElementName,
			COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType, Object elementValue) {

		mandatoryCheck();
		
		if (validateConfiguration(configElementName, dataType)) {
			String elementName = configElementName.getElementName();
			
			addCollectorConfigValues(dataType, elementValue, elementName);
		}
	}

	private void mandatoryCheck() {
		if(!mandatoryAttributeSetUpPerformed) {
			preConfigSetup();
			setUpConfiguration();
			mandatoryAttributeSetUpPerformed = true;
		}
	}

	private void addCollectorConfigValues(COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType, Object elementValue, String elementName) {
		collectorConfigurationWithValues.put(new ElementNameToDataTypePair(elementName, dataType),
				elementValue);
	}

	@Override
	public boolean validateConfiguration(IPraeceptaDataConfigElementNameType configElementName,
			COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType) {

		if (predefinedElementNameToDataTypeMap.containsKey(configElementName.getElementName())
				&& predefinedElementNameToDataTypeMap.get(configElementName.getElementName()).equals(dataType)) {

			return true;
		}

		return false;
	}
	
	public final void addNonMandatoryConfigElements(String elementName, String value) {
		addNonMandatoryConfigElements(elementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, value);
	}

	public final void addNonMandatoryConfigElements(String elementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType, Object elementValue) {
		addCollectorConfigValues(dataType, elementValue, elementName);
	}
	
	@Override
	public final void postConfigSetup() {
		
		mandatoryCheck();
		
		// Check all Mandatory Config Values are set 
		predefinedElementNameToDataTypeMap.forEach( (NAME, TYPE) -> {
			if(collectorConfigurationWithValues.get(new ElementNameToDataTypePair(NAME, TYPE)) == null) {
				throw new PraeceptaDataConfigException(" Mandatory Data Collector Config Attribute "+ NAME +" is Mising ");
			}
		});
		
	}

	@Override
	public boolean cleanUpConfig() {
		collectorConfigurationWithValues.clear();
		return true;
	}
	
	public Map<String, String> getCollectorConfigurationWithValues() {

		return collectorConfigurationWithValues.entrySet().stream()
				.collect(Collectors.toMap(element -> element.getKey().elementName, element -> element.getValue().toString()));
	}

	public static class ElementNameToDataTypePair {

		private String elementName;

		private COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType;

		public ElementNameToDataTypePair(String elementName, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE dataType) {
			this.elementName = elementName;
			this.dataType = dataType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
			result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ElementNameToDataTypePair other = (ElementNameToDataTypePair) obj;
			if (dataType != other.dataType)
				return false;
			if (elementName == null) {
				if (other.elementName != null)
					return false;
			} else if (!elementName.equals(other.elementName))
				return false;
			return true;
		}

	}

}
