package io.praecepta.data.configs.common.db;

import io.praecepta.data.configs.common.IPraeceptaDataConfigElementNameType;

public enum PraeceptaDBDataConfigType implements IPraeceptaDataConfigElementNameType {
	
	DB_DRIVER("db.driver"), DB_URL("db.url"), USERNAME("userName"), PASSWORD("password"), INSERT_QUERY("insert.query"), INSERT_ATTRIBUTE_NAMES("insert.attrubute.names"), SELECT_QUERY("select.query");

	private PraeceptaDBDataConfigType(String elementName) {
		this.elementName = elementName;
	}

	private String elementName;

	@Override
	public String getElementName() {
		return elementName;
	}

}
