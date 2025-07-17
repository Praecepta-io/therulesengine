package io.praecepta.data.configs.common.db;

import io.praecepta.data.configs.common.IPraeceptaDataConfigElementNameType;

public enum PraeceptaDBDataConfigType implements IPraeceptaDataConfigElementNameType {
	
	DB_DRIVER("db.driver"), DB_URL("db.url"), USERNAME("userName"), PASSWORD("password"),INSERT_QUERY("insert.query"), SELECT_QUERY("select.query");

	private PraeceptaDBDataConfigType(String elementName) {
		this.elementName = elementName;
	}

	private String elementName;

	@Override
	public String getElementName() {
		return elementName;
	}

}
