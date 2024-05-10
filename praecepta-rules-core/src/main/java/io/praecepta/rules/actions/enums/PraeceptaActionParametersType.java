package io.praecepta.rules.actions.enums;

public enum PraeceptaActionParametersType {
	
	FACTOR_ACTION_TYPE("factorActionType"),
	FACTOR_VALUE("factorValue"),
	TO_DATE_FORMAT("toDateFormat"),
	TO_TIME_ZONE("toTimeZone"),
	FROM_DATE_FORMAT("fromDateFormat"),
	FROM_TIME_ZONE("fromTimeZone"),
	;
	
	private PraeceptaActionParametersType(String attributeName) {
		this.attributeName = attributeName;
	}
	
	private String attributeName;

	public String getAttributeName() {
		return attributeName;
	}
	
}
