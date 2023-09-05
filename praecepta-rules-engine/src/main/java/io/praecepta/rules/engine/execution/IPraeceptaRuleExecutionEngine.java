package io.praecepta.rules.engine.execution;

import java.util.Collection;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public interface IPraeceptaRuleExecutionEngine<RULE_SPACE extends PraeceptaRuleSpace, PraeceptaRequestStore> {

	public RULE_SPACE buildRuleSapce(PraeceptaRuleSpaceCompositeKey ruleSpaceKey);

	public Collection<RULE_SPACE> buildRuleSapces();
	
	public void performRuleEngineExecution(PraeceptaRequestStore ruleStore);
	
	public boolean shutDownRuleEngineExecution(RULE_SPACE ruleSpace);
	
	public boolean shutDownRuleEngine();
}
