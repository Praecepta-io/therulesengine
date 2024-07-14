package io.praecepta.dao.elastic.model;

import java.util.List;
import java.util.Map;

import io.praecepta.dao.elastic.enums.AUDIT_OPERATION_TYPE;

public class PraeceptaRuleGroupAuditPoint {

	private String ruleGrpName;
	
	private Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleOperationAuditPoints;
	
	public PraeceptaRuleGroupAuditPoint(String ruleGrpName) {
		this.ruleGrpName = ruleGrpName;
	}

	public String getRuleGrpName() {
		return ruleGrpName;
	}

	public void setRuleGrpName(String ruleGrpName) {
		this.ruleGrpName = ruleGrpName;
	}
	
	public Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> getRuleOperationAuditPoints() {
		return ruleOperationAuditPoints;
	}
	
	public void setRuleOperationAuditPoints(
			Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleOperationAuditPoints) {
		this.ruleOperationAuditPoints = ruleOperationAuditPoints;
	}

}

