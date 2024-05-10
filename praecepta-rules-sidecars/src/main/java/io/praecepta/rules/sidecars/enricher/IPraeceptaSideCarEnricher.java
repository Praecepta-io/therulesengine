package io.praecepta.rules.sidecars.enricher;

import java.util.Map;

import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public interface IPraeceptaSideCarEnricher {

	void initializeEnricher();
	
	void enrich(PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> input);

}
