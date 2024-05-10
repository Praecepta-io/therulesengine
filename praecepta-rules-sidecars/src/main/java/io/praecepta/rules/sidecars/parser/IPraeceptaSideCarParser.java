package io.praecepta.rules.sidecars.parser;

import java.util.Map;

import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public interface IPraeceptaSideCarParser {

	void initializeParser();
	
	void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> dataHolder);
	
	default boolean validate(String input) {
		return true;
	}
}
