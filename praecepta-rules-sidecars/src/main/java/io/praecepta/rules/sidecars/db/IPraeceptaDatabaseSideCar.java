package io.praecepta.rules.sidecars.db;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

import java.util.Map;

public interface IPraeceptaDatabaseSideCar {

	void initializeDBConnection(PraeceptaDBInjestorConfig dbConfig);
	
	void processDbRecords(PraeceptaRequestStore ruleStore);

}
