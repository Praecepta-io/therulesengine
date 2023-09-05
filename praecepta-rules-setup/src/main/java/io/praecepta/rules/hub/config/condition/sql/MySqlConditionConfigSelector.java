package io.praecepta.rules.hub.config.condition.sql;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultSqlConditionConfigSelector;

public class MySqlConditionConfigSelector extends DefaultSqlConditionConfigSelector {

	
	@Override
	protected RULE_SET_UP_PERSISTANT_SUB_TYPE getPersistantSubType() {
		return RULE_SET_UP_PERSISTANT_SUB_TYPE.MySQL;
	}

	@Override
	public boolean isSubTypeCheckNeeded() {
		return true;
	}
}
