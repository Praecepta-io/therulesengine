package io.praecepta.rules.executor.inspira.manager;

import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;

public class PraeceptaInspiraRulesExecutionManager extends PraeceptaRulesExecutionManager{

	public PraeceptaInspiraRulesExecutionManager(IPraeceptaDataCollector<PraeceptaDBInjestorConfig> dataCollector,
			PraeceptaBasicRuleExecutionEngine rulesExecutionEngine) {
		super(dataCollector, rulesExecutionEngine);
		
	}
	
	

}
