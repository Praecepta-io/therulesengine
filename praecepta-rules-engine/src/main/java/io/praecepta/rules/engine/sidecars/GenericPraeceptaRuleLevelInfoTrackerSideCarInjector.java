package io.praecepta.rules.engine.sidecars;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericPraeceptaRuleLevelInfoTrackerSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars = Collections.emptyMap();

	public Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> getRuleNameToExecutionSideCars() {
		return ruleNameToExecutionSideCars;
	}

	public void setRuleNameToExecutionSideCars(
			Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars) {
		this.ruleNameToExecutionSideCars = ruleNameToExecutionSideCars;
	}
	
}
