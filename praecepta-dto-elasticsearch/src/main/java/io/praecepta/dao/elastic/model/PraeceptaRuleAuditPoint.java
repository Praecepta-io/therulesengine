package io.praecepta.dao.elastic.model;

import java.util.List;
import java.util.Map;

import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;

public class PraeceptaRuleAuditPoint {
	
	private String ruleName;
	
	// AUDIT_POINT_TYPE - CONDITION or ACTION or CONDITION_OPERATOR or JOIN_OPERATOR or MATA_DATAetc 
	// Map<String, ValueHolderType> - String - attribute Name and ValueHolderType - From and To Value
//	private Map<AUDIT_POINT_TYPE, Map<String, List<PraeceptaAuditElement>>> auditInfo;
	private Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditInfo;
	
	public PraeceptaRuleAuditPoint(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> getRuleAuditInfo() {
		return ruleAuditInfo;
	}

	public void setRuleAuditInfo(Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditInfo) {
		this.ruleAuditInfo = ruleAuditInfo;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleAuditPoint [ruleName=" + ruleName + ", ruleAuditInfo=" + ruleAuditInfo + "]";
	}

}
