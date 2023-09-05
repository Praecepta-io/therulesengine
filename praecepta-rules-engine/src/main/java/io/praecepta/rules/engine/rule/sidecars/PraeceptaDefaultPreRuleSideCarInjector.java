package io.praecepta.rules.engine.rule.sidecars;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.model.PraeceptaCriteria;

public class PraeceptaDefaultPreRuleSideCarInjector extends GenericPraeceptaRuleLevelInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPreRuleSideCarInjector.class);
	
	public final void captureInInfo(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside captureInInfo of PraeceptaDefaultPreRuleSideCarInjector");
		
		Boolean ruleValidationStatus =  (Boolean) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS);
		
		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);
		
		PraeceptaCriteria ruleCriteria = (PraeceptaCriteria) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_CRITERIA);
		
		if(ruleValidationStatus && ruleGrpToUse != null && ruleCriteria != null) {
			
			
			
		}
		
		logger.debug("Exiting captureInInfo of PraeceptaDefaultPreRuleSideCarInjector");
	}
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars = getRuleNameToExecutionSideCars();
		
		String ruleName = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME);
		
		if(!ObjectHelper.isObjectEmpty(ruleName) && !ObjectHelper.isObjectEmpty(ruleNameToExecutionSideCars)) {
			
			List<IPraeceptaInfoTrackerSideCarInjector> preRuleSideCarsForARuleName = ruleNameToExecutionSideCars.get(ruleName);
			
			if(!ObjectHelper.isObjectEmpty(preRuleSideCarsForARuleName)) {
				triggerSideCars(preRuleSideCarsForARuleName, ruleStore);
			}
			
		}
		
	}

	public final void captureExitInfo(PraeceptaRequestStore ruleStore) {
		
	}
}
