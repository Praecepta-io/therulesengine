package io.praecepta.rules.hub.config.condition;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;

public class DefaultFileConditionConfigSelector extends DefaultConditionConfigSelector {

	@Override
	protected RULE_SET_UP_PERSISTANT_TYPE getPersistantType() {
		return RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM;
	}
}
