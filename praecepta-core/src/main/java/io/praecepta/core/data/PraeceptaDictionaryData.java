package io.praecepta.core.data;

import java.util.HashMap;
import java.util.Map;

public class PraeceptaDictionaryData {
	
	private Map<String, Object> dictionaryDetails = new HashMap<>();

	public Map<String, Object> getDictionaryDetails() {
		return dictionaryDetails;
	}

	public void setDictionaryDetails(Map<String, Object> dictionaryDetails) {
		this.dictionaryDetails = dictionaryDetails;
	}
	
}
