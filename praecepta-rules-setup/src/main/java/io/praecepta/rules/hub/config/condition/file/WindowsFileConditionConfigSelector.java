package io.praecepta.rules.hub.config.condition.file;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultFileConditionConfigSelector;

public class WindowsFileConditionConfigSelector extends DefaultFileConditionConfigSelector {

	@Override
	protected RULE_SET_UP_PERSISTANT_SUB_TYPE getPersistantSubType() {
		return RULE_SET_UP_PERSISTANT_SUB_TYPE.Windows;
	}

	@Override
	public boolean isSubTypeCheckNeeded() {
		return true;
	}

}
