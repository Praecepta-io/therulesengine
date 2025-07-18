package io.praecepta.rules.enums;

import io.praecepta.core.data.enums.IPraeceptaRequestStoreType;

public enum PraeceptaRuleRequestStoreType implements IPraeceptaRequestStoreType{
	
	ORIGINAL_RULE_STORE("originalRuleStore"), 
	CRITERIA_RULE_STORES("criteriaRuleStores"), 
	
	// Side Cars
	PRE_RULE_GROUP_SIDE_CAR("preRuleGrpSideCar"),
	PRE_BEFORE_RULE_GROUP_SIDE_CARS("preBeforeRuleGrpSideCars"),
	PRE_RULE_GROUP_EXECUTION_SIDE_CARS("preRuleGrpExecSideCars"),
	PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA("preRuleGrpExecSideCarsMetaData"),
	PRE_AFTER_RULE_GROUP_SIDE_CARS("preAfterRuleGrpSideCars"),
	
	POST_RULE_GROUP_SIDE_CAR("postRuleGrpSideCar"),
	POST_BEFORE_RULE_GROUP_SIDE_CARS("postBeforeRuleGrpSideCars"),
	POST_RULE_GROUP_EXECUTION_SIDE_CARS("postRuleGrpExecSideCars"),
	POST_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA("postRuleGrpExecSideCarsMetaData"),
	POST_AFTER_RULE_GROUP_SIDE_CARS("postAfterRuleGrpSideCars"),

	PRE_RULE_SIDE_CAR("preRuleSideCar"),
	PRE_BEFORE_RULE_SIDE_CARS("preBeforeRuleSideCars"),
	PRE_RULE_EXECUTION_SIDE_CARS("preRuleExecSideCars"),
	PRE_RULE_EXECUTION_SIDE_CARS_META_DATA("preRuleExecSideCarsMetaData"),
	PRE_AFTER_RULE_SIDE_CARS("preAfterRuleSideCars"),

	POST_RULE_SIDE_CAR("postRuleSideCar"),
	POST_BEFORE_RULE_SIDE_CARS("postBeforeRuleSideCars"),
	POST_RULE_EXECUTION_SIDE_CARS("postRuleExecSideCars"),
	POST_RULE_EXECUTION_SIDE_CARS_META_DATA("postRuleExecSideCarsMetaData"),
	POST_AFTER_RULE_SIDE_CARS("postAfterRuleSideCars"),
	RULE_SIDE_CARS_INFO("ruleSideCarsInfo"),
	
	// This Section is for Group Of Rules
	RULE_SPACE("ruleSpace"), 
	RULE_GROUP_NAME("ruleGroupName"), 
	RULE_GROUP("ruleGroup"),
	RULE_GROUP_TO_USE("ruleGroupToUse"),
	RULE_GROUP_METADATA("ruleGroupMetadata"), 
	RULES_REQUEST("rulesRequest"), 
	RULES_REQUEST_TRACE_ID("rulesRequestTraceId"), 
	RULES_GROUP_START_TIME("rulesGroupStartTime"), 
	RULES_GROUP_END_TIME("rulesGroupEndTime"), 
	RULES_REQUEST_DATA_TYPE("rulesRequestDataType"), 
	RULES_REQUEST_AS_KEY_VALUE_PAIR("rulesRequestAsKeyValuePair"), 
	IS_RULES_REQUEST_VALID_TO_RUN("rulesRequestValidToRun"), 
	RULES_REQUEST_METADATA("rulesRequestMetaData"), 
	RULES_REQUEST_VALIDATION_STATUS("rulesRequestValidationStatus"),
	RULES_RESPONSE("rulesResponse"), 
	RULES_RESPONSE_METADATA("rulesResponseMetaData"), 
	RULES_CONFIG("rulesResponse"), 
	RULES_OVERALL_EXECUTION_STATUS("rulesResponse"), 
	RULES_CRITERIA("rulesCriteria"), 
	RULES_CRITERIA_STATUS("rulesResponse"), 
	RULES_CRITERIA_INFO("rulesResponse"),
	
	// This Section is for an Individual Rule 
	RULES_REQUEST_SPAN_ID("rulesRequestSpanId"), 
	RULES_REQUEST_START_TIME("rulesRequestStartTime"), 
	RULES_REQUEST_END_TIME("rulesRequestEndTime"), 
	RULE_CONDITION_STATUS("ruleConditionStatus"), 
	RULE_CONDITION_INFO("ruleConditionInfo"),
	RULE_VALIDATION_STATUS("ruleValidationStatus"),
	RULE_INFO("ruleInfo"),
	RULE_NAME("ruleName"),
	
	// This Section is for all Actions in a Rule 
	RULE_ACTIONS_STATUS("ruleActionsStatus"), 
	RULE_ACTIONS_INFO("ruleActionsInfo"),
	
	// This Section is for an Individual Action in a Rule
	RULE_ACTION_STATUS("ruleActionStatus"), 
	RULE_ACTION_INFO("ruleActionInfo"),
	RULE_ALL_ACTIONS_INFO("ruleAllActionsInfo"),
	RULE_GROUP_ACTIONS("ruleGroupActions"),
	RULE_GROUP_ACTIONS_ON_FAILURE("ruleGroupActionsOnFailure"),
	RULE_GROUP_NAME_WITH_SPACE_KEY("ruleGroupNameWithSpaceKey"),
	SIDCAR_OUTPUT("SideCarOutput"),
	
	RULE_EXECUTION_ENGINE("ruleExecutionEngine"),
	;

	private PraeceptaRuleRequestStoreType(String rulesProcessingAttributeName) {
		this.rulesProcessingAttributeName = rulesProcessingAttributeName;
	}
	
	private String rulesProcessingAttributeName;
	
	@Override
	public String getStoringAttributeName() {
		return rulesProcessingAttributeName;
	}

}
