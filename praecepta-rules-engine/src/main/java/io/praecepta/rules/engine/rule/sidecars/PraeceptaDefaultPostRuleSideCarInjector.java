package io.praecepta.rules.engine.rule.sidecars;

import java.util.List;
import java.util.Map;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaDefaultPostRuleSideCarInjector extends GenericPraeceptaRuleLevelInfoTrackerSideCarInjector{


	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars = getRuleNameToExecutionSideCars();
		
		String ruleName = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME);
		
		if(!ObjectHelper.isObjectEmpty(ruleName) && !ObjectHelper.isObjectEmpty(ruleNameToExecutionSideCars)) {
			
			List<IPraeceptaInfoTrackerSideCarInjector> postRuleSideCarsForARuleName = ruleNameToExecutionSideCars.get(ruleName);
			
			if(!ObjectHelper.isObjectEmpty(postRuleSideCarsForARuleName)) {
				triggerSideCars(postRuleSideCarsForARuleName, ruleStore);
			}
			
		}
		
	}
}
