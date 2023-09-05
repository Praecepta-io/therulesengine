package io.praecepta.rules.hub.config.condition.nosql;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultNoSqlConditionConfigSelector;

public class MongoDbConditionConfigSelector extends DefaultNoSqlConditionConfigSelector {

	@Override
	protected RULE_SET_UP_PERSISTANT_SUB_TYPE getPersistantSubType() {
		return RULE_SET_UP_PERSISTANT_SUB_TYPE.MongoDb;
	}

	@Override
	public boolean isSubTypeCheckNeeded() {
		return true;
	}

}
