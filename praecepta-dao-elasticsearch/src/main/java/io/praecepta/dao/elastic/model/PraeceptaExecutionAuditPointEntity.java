package io.praecepta.dao.elastic.model;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;

public class PraeceptaExecutionAuditPointEntity {
	
//	private final PraeceptaRuleGroupExecutionAuditPointEntity preRuleGroupExecutionAuditDetails = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE);
//	
//	private final PraeceptaRuleGroupExecutionAuditPointEntity postRuleGroupExecutionAuditDetails = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE);
//	
//	private final PraeceptaRuleExecutionAuditPointEntity preRuleExecutionAuditDetails = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE);
//	
//	private final PraeceptaRuleExecutionAuditPointEntity postRuleExecutionAuditDetails = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE);
	
	private PraeceptaRuleGroupExecutionAuditPointEntity preRuleGroupExecutionAuditDetails;// = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE);
	
	private PraeceptaRuleGroupExecutionAuditPointEntity postRuleGroupExecutionAuditDetails;// = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE);
	
	private PraeceptaRuleExecutionAuditPointEntity preRuleExecutionAuditDetails;// = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE);
	
	private PraeceptaRuleExecutionAuditPointEntity postRuleExecutionAuditDetails;// = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE);

	public PraeceptaRuleGroupExecutionAuditPointEntity getPreRuleGroupExecutionAuditDetails() {
		return preRuleGroupExecutionAuditDetails;
	}

	public PraeceptaRuleGroupExecutionAuditPointEntity getPostRuleGroupExecutionAuditDetails() {
		return postRuleGroupExecutionAuditDetails;
	}

	public PraeceptaRuleExecutionAuditPointEntity getPreRuleExecutionAuditDetails() {
		return preRuleExecutionAuditDetails;
	}

	public PraeceptaRuleExecutionAuditPointEntity getPostRuleExecutionAuditDetails() {
		return postRuleExecutionAuditDetails;
	}
	
	public void setPreRuleGroupExecutionAuditDetails(
			PraeceptaRuleGroupExecutionAuditPointEntity preRuleGroupExecutionAuditDetails) {
		
		if(preRuleGroupExecutionAuditDetails != null && EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE == preRuleGroupExecutionAuditDetails.getOperationType()) {
			this.preRuleGroupExecutionAuditDetails = preRuleGroupExecutionAuditDetails;
		}
	}

	public void setPostRuleGroupExecutionAuditDetails(
			PraeceptaRuleGroupExecutionAuditPointEntity postRuleGroupExecutionAuditDetails) {
		
		if(postRuleGroupExecutionAuditDetails != null && EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE == postRuleGroupExecutionAuditDetails.getOperationType()) {
			this.postRuleGroupExecutionAuditDetails = postRuleGroupExecutionAuditDetails;
		}
	}

	public void setPreRuleExecutionAuditDetails(PraeceptaRuleExecutionAuditPointEntity preRuleExecutionAuditDetails) {
		
		if(preRuleExecutionAuditDetails != null && EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE == preRuleExecutionAuditDetails.getOperationType()) {
			this.preRuleExecutionAuditDetails = preRuleExecutionAuditDetails;
		}
	}

	public void setPostRuleExecutionAuditDetails(PraeceptaRuleExecutionAuditPointEntity postRuleExecutionAuditDetails) {
		
		if(postRuleExecutionAuditDetails != null && EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE == postRuleExecutionAuditDetails.getOperationType()) {
			this.postRuleExecutionAuditDetails = postRuleExecutionAuditDetails;
		}
	}

	@Override
	public String toString() {
		return "PraeceptaExecutionAuditPointEntity [preRuleGroupExecutionAuditDetails="
				+ preRuleGroupExecutionAuditDetails + ", postRuleGroupExecutionAuditDetails="
				+ postRuleGroupExecutionAuditDetails + ", preRuleExecutionAuditDetails=" + preRuleExecutionAuditDetails
				+ ", postRuleExecutionAuditDetails=" + postRuleExecutionAuditDetails + "]";
	}
	
}
