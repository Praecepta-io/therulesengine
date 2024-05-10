package io.praecepta.rest.client.enums;

import java.util.ArrayList;
import java.util.List;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public enum VALUE_STRATEGY_TYPE {

	CONSTANT, FROM_PAY_LOAD;
	
	private static final List<String> uniqueTypes = getAllTypes();
	
	public static VALUE_STRATEGY_TYPE getType(String valueStrategy) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(valueStrategy) && uniqueTypes.contains(valueStrategy.toUpperCase())) {
			return VALUE_STRATEGY_TYPE.valueOf(valueStrategy.toUpperCase());
		}
		
		return FROM_PAY_LOAD;
	}

	private static List<String> getAllTypes() {
		
		List<String> uniqueTypes = new ArrayList<>(VALUE_STRATEGY_TYPE.values().length);
		
		for(VALUE_STRATEGY_TYPE eachStrategy : VALUE_STRATEGY_TYPE.values()) {
			uniqueTypes.add(eachStrategy.name());
		}
		
		return uniqueTypes;
	}
}
