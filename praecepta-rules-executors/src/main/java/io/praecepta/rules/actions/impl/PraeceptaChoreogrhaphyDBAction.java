package io.praecepta.rules.actions.impl;

import java.util.Map;

import io.praecepta.rules.dto.RuleGroupInfo;

public class PraeceptaChoreogrhaphyDBAction extends PraeceptaChoreogrhaphyAction {

	Map<String, Object> dbMetadata;

	public Map<String, Object> getDbMetadata() {
		return dbMetadata;
	}

	public void setDbMetadata(Map<String, Object> dbMetadata) {
		this.dbMetadata = dbMetadata;
	}
	
	@Override
	protected void performInitializeDataInjestor(RuleGroupInfo ruleGroupWithSpaceKey) {
		/*ruleGroupWithDataInjestorMap.putIfAbsent(ruleGroupWithSpaceKey, PraeceptaChoreogrhaphyActionHelper
				.initializeDataInjestor(getActionParameters(), getActionStrategy(),this.dbMetadata));*/
	}
	
}
