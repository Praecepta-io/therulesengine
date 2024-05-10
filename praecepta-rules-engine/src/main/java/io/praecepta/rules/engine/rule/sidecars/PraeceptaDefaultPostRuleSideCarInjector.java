package io.praecepta.rules.engine.rule.sidecars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaDefaultPostRuleSideCarInjector extends GenericPraeceptaRuleLevelInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPostRuleSideCarInjector.class);
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		logger.debug(" Inside executeSideCar of PraeceptaDefaultPostRuleSideCarInjector");
		
		String ruleName = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME);
		
		logger.info(" About to start the Execution of Post Rule Side cars for Rule Name - {} ", ruleName); 
		
		super.executeSideCar(ruleStore);
		
		logger.debug(" Exiting executeSideCar of PraeceptaDefaultPostRuleSideCarInjector");
	}
}
