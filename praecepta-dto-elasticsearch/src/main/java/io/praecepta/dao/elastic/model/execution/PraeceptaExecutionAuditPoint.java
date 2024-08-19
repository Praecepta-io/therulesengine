package io.praecepta.dao.elastic.model.execution;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;

public class PraeceptaExecutionAuditPoint {
	
	private PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetails = (PraeceptaRuleGroupExecutionAuditPoint) 
			EXECUTION_AUDIT_OPERATION_TYPE.getExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE);
	
	private PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetails = (PraeceptaRuleGroupExecutionAuditPoint) 
			EXECUTION_AUDIT_OPERATION_TYPE.getExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE);
	
	private PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetails = (PraeceptaRuleExecutionAuditPoint) 
			EXECUTION_AUDIT_OPERATION_TYPE.getExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE);
	
	private PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetails = (PraeceptaRuleExecutionAuditPoint) 
			EXECUTION_AUDIT_OPERATION_TYPE.getExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE);

	public PraeceptaRuleGroupExecutionAuditPoint getPreRuleGroupExecutionAuditDetails() {
		return preRuleGroupExecutionAuditDetails;
	}

//	public void setPreRuleGroupExecutionAuditDetails(
//			PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetails) {
//		this.preRuleGroupExecutionAuditDetails = preRuleGroupExecutionAuditDetails;
//	}

	public PraeceptaRuleGroupExecutionAuditPoint getPostRuleGroupExecutionAuditDetails() {
		return postRuleGroupExecutionAuditDetails;
	}

//	public void setPostRuleGroupExecutionAuditDetails(
//			PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetails) {
//		this.postRuleGroupExecutionAuditDetails = postRuleGroupExecutionAuditDetails;
//	}

	public PraeceptaRuleExecutionAuditPoint getPreRuleExecutionAuditDetails() {
		return preRuleExecutionAuditDetails;
	}

//	public void setPreRuleExecutionAuditDetails(PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetails) {
//		this.preRuleExecutionAuditDetails = preRuleExecutionAuditDetails;
//	}

	public PraeceptaRuleExecutionAuditPoint getPostRuleExecutionAuditDetails() {
		return postRuleExecutionAuditDetails;
	}

//	public void setPostRuleExecutionAuditDetails(PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetails) {
//		this.postRuleExecutionAuditDetails = postRuleExecutionAuditDetails;
//	}
	
}
