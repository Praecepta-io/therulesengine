package io.praecepta.audit.service;

public class PraeceptaAuditServiceConstants {
	
	public static final String AUDIT_CONTROLLER_NAME = "auditController";

	public static final String AUDIT_SERVICE_NAME = "praeceptaAuditService";
	
	public static final String AUDIT_EXECUTION_SERVICE_NAME = "praeceptaExecutionAuditService";
	
	public static final String AUDIT_SERVICE_PATH = "/audit";
	
	public static final String GET_RULE_GROUP_AUDIT = "/getRuleGroupAudit";
	
	public static final String GET_RULE_GROUP_AUDIT_FUNCTION_PATH = "/ruleGroupAudit/:spacename/:clientid/:appname/:version/:groupname";
	
	public static final String GET_RULE_GROUP_AUDIT_PATH = "/ruleGroupAudit/{spacename}/{clientid}/{appname}/{version}/{groupname}";

	public static final String DELETE_RULE_GROUP_AUDIT_FUNCTION_PATH = "/deleteRuleGroupAudit/:uniqueid";

	public static final String DELETE_RULE_GROUP_AUDIT_PATH = "/deleteRuleGroupAudit/{uniqueid}";

	public static final String DELETE_RULE_GROUP_AUDIT = "deleteRuleGroupAudit";
	
	public static final String ADD_RULE_GROUP_AUDIT = "addRuleGroupAudit";
	
	public static final String ADD_RULE_GROUP_AUDIT_FUNCTION_PATH = "/addRuleGroupAudit/:spacename/:clientid/:appname/:version/:groupname";
	
	public static final String ADD_RULE_GROUP_AUDIT_PATH = "/addRuleGroupAudit/{spacename}/{clientid}/{appname}/{version}/{groupname}";
	
	public static final String REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT = "updateRuleGroupAudit";
	
	public static final String REFURBISH_RULE_GROUP_AUDIT_FUNCTION_PATH = "/updateRuleGroupAudit/:uniqueId";
	
	public static final String REFURBISH_RULE_GROUP_AUDIT_PATH = "/updateRuleGroupAudit/{uniqueId}";
	
	public static final String PATH_PARAM_SPACE_NAME = ":spacename";

	public static final String PATH_PARAM_CLIENT_ID = ":clientid";

	public static final String PATH_PARAM_APP_NAME = ":appname";

	public static final String PATH_PARAM_VERSION = ":version";

	public static final String PATH_PARAM_RULEGROUPNAME = ":groupname";

	public static final String PATH_PARAM_UNIQUE_ID = ":uniqueid";
	

	public static final String JSON_PRODUCE = "application/json";
	 
	public static final String ADD_RULE_GROUP_EXECUTION_AUDIT = "addRuleGroupExecutionAudit";
	
	public static final String ADD_RULE_GROUP_EXECUTION_AUDIT_FUNCTION_PATH = "/addRuleGroupExecutionAudit/:spacename/:clientid/:appname/:version/:groupname";
	
	public static final String ADD_RULE_GROUP_EXECUTION_AUDIT_PATH = "/addRuleGroupExecutionAudit/{spacename}/{clientid}/{appname}/{version}/{groupname}";
	
	public static final String ADD_RULE_GROUP_EXECUTION_AUDITS = "addRuleGroupExecutionAudits";
	
	public static final String ADD_RULE_GROUP_EXECUTION_AUDITS_FUNCTION_PATH = "/addRuleGroupExecutionAudits/:spacename/:clientid/:appname/:version/:groupname";
	
	public static final String ADD_RULE_GROUP_EXECUTION_AUDITS_PATH = "/addRuleGroupExecutionAudits/{spacename}/{clientid}/{appname}/{version}/{groupname}";
	
	public static final String GET_RULE_GROUP_EXECUTION_AUDIT = "getRuleGroupExecutionAudit";
	
	public static final String GET_RULE_GROUP_EXECUTION_AUDIT_FUNCTION_PATH = "/getRuleGroupExecutionAudit/:spacename/:clientid/:appname/:version/:groupname";
	
	public static final String GET_RULE_GROUP_EXECUTION_AUDIT_PATH = "/getRuleGroupExecutionAudit/{spacename}/{clientid}/{appname}/{version}/{groupname}";
}
