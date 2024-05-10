package io.praecepta.data.configs.common.kafka;

import io.praecepta.data.configs.common.IPraeceptaDataConfigElementNameType;

public enum PraeceptaKafkaDataConfigType implements IPraeceptaDataConfigElementNameType{

	BROKERS("bootstrap.servers"), PORT("port"), USERNAME("userName"), PASSWORD("password");
	
	private PraeceptaKafkaDataConfigType(String elementName) {
		this.elementName = elementName;
	}
	
	private String elementName;
	
	@Override
	public String getElementName() {
		return elementName;
	};
}
