package io.praecepta.rules.hub.config.condition;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;

public abstract class DefaultConditionConfigSelector extends ConditionConfigSelector {

	@Override
	protected RULE_SET_UP_PERSISTANT_SUB_TYPE getPersistantSubType() {
		return null;
	}

	@Override
	public boolean isSubTypeCheckNeeded() {
		return false;
	}
	
}
