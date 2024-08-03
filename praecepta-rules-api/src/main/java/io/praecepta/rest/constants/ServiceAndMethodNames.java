package io.praecepta.rest.constants;

public interface ServiceAndMethodNames {

	public static final String RULE_SPACE_CONTROLLER_NAME = "ruleSpaceController";
	public static final String RULE_GROUP_CONTROLLER_NAME = "ruleGroupController";
	public static final String SIDE_CAR__CONTROLLER_NAME = "sidecarController";
	
	public static final String RULE_GROUP_NAMES_GET_METHOD_NAME = "getRuleGroupNames";
	
	public static final String RULE_GROUP_GET_METHOD_NAME = "getRuleGroups";

	public static final String SIDE_CARS_GET_METHOD_NAME = "getSidecars";

	public static final String SIDE_CARS_SAVE_METHOD_NAME = "saveSidecars";

	public static final String GET_RULE_SPACE_LIST_METHOD_NAME= "ruleSpaceList";
	
	public static final String RULE_GROUP_BY_NAME_GET_METHOD_NAME = "getRuleGroupByName";

	public static final String ADD_RULE_SPACE_METHOD_NAME = "addRuleSpace";

	public static final String DELETE_RULE_SPACE_METHOD_NAME = "deleteRuleSpace";

	public static final String GET_RULE_SPACE_METHOD_NAME = "getRuleSpace";

	public static final String GET_ALL_RULE_SPACE_METHOD_NAME = "getAllRuleSpaces";

	public static final String ADD_RULE_GROUP_METHOD_NAME = "addRuleGroup";
	
	public static final String UPDATE_RULE_GROUP_METHOD_NAME = "updateRuleGroup";

	public static final String ADD_MULTI_RULE_GROUP_METHOD_NAME = "addMultiRuleGroup";

	public static final String UPDATE_MULTI_RULE_GROUP_METHOD_NAME = "updateMultiRuleGroup";

	public static final String ADD_MULTI_NESTED_RULE_GROUP_METHOD_NAME = "addMultiNestedRuleGroup";

	public static final String UPDATE_MULTI_NESTED_RULE_GROUP_METHOD_NAME = "updateMultiNestedRuleGroup";
	
	public static final String DELETE_RULE_GROUP_METHOD_NAME = "deleteRuleGroup";
	
	public static final String RULE_BY_NAME_GET_METHOD_NAME = "getRuleByName";
	
	public static final String ADD_RULE_METHOD_NAME = "addRule";
	
	public static final String UPDATE_RULE_METHOD_NAME = "updateRule";
	
	public static final String DELETE_RULE_METHOD_NAME = "deleteRule";
	
	public static final String RULE_CONDITION_BY_NAME_GET_METHOD_NAME = "getRuleConditionByName";
	
	public static final String ADD_RULE_CONDITION_METHOD_NAME = "addRuleCondition";
	
	public static final String UPDATE_RULE_CONDITION_METHOD_NAME = "updateRuleCondition";
	
	public static final String DELETE_RULE_CONDITION_METHOD_NAME = "deleteRuleCondition";


	String PATH_PARAM_SPACE_NAME = ":spacename";

	String PATH_PARAM_CLIENT_ID = ":clientid";

	String PATH_PARAM_APP_NAME = ":appname";

	String PATH_PARAM_VERSION = ":version";

	String PATH_PARAM_GROUP_NAME = ":groupname";

	String GROUP_NAME = ":groupname";

	String PATH_PARAM_RULEGROUPNAME = ":groupname";

	String SIDE_CARS_UPDATE_METHOD_NAME = "updateSidecars";
	String SIDE_CARS_DELETE_METHOD_NAME = "deleteSidecars";
}
