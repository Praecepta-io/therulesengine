package io.praecepta.rules.engine.sidecars;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class GenericPraeceptaRuleLevelInfoTrackerSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private final static Logger LOGGER = LoggerFactory.getLogger(GenericPraeceptaRuleLevelInfoTrackerSideCarInjector.class);
	
	private Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars = Collections.emptyMap();

	public Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> getRuleNameToExecutionSideCars() {
		return ruleNameToExecutionSideCars;
	}

	public void setRuleNameToExecutionSideCars(
			Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars) {
		this.ruleNameToExecutionSideCars = ruleNameToExecutionSideCars;
	}
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside executeSideCar of GenericPraeceptaRuleLevelInfoTrackerSideCarInjector");
		
		String ruleName = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME);

		LOGGER.info(" Getting the Side cars for Rule Name - {} ", ruleName); 
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleNameToExecutionSideCars) && !PraeceptaObjectHelper.isObjectEmpty(ruleName)) {
			
			List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars = ruleNameToExecutionSideCars.get(ruleName);
			
			LOGGER.debug(" Got the Side cars for the Rule Name - {} ", ruleName); 
			
			if(!PraeceptaObjectHelper.isObjectEmpty(executionSideCars)) {
				
				LOGGER.info(" Number of Side cars to trigger for Rule Name - {} is {} ", ruleName, executionSideCars.size()); 
				
				triggerSideCars(executionSideCars, ruleStore);
			}
		}
	}
	
}
