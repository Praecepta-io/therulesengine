package io.praecepta.rules.sidecars.formatter;

import java.util.Map;

import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public interface IPraeceptaSideCarFormatter {

	void initializeFormatter();
	
	void format(PraeceptaSideCarDataHolder<Map<String, Object>, String> input);
	
}
