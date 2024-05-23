package io.praecepta.rules.model.filter;

public enum MetaDataAttributes {
	FROM_DATE_FORMAT("fromDateFormat"),
	TO_DATE_FORMAT("toDateFormat"),
	END_DATE_FORMAT("endDateFormat"),
	END_DATE("endDate"),
	MIDDLE_DATE("middleDate"),
	FROM_TIME_ZONE("fromTimeZone"),
	TO_TIME_ZONE("toTimeZone"),
	END_TIME_ZONE("endTimeZone"),
	;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private MetaDataAttributes(String description) {
		this.description = description;
	}

}
