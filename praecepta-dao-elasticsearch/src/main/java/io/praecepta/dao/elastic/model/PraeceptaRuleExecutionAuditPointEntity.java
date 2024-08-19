package io.praecepta.dao.elastic.model;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleExecutionAuditPoint;

public class PraeceptaRuleExecutionAuditPointEntity extends PraeceptaRuleExecutionAuditPoint{

	public PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		super(operationType);
	}

	private String ruleStartTime;
	
	private String ruleEndTime;

	public String getRuleStartTime() {
		return ruleStartTime;
	}

	public void setRuleStartTime(String ruleStartTime) {
		this.ruleStartTime = ruleStartTime;
	}

	public String getRuleEndTime() {
		return ruleEndTime;
	}

	public void setRuleEndTime(String ruleEndTime) {
		this.ruleEndTime = ruleEndTime;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleExecutionAuditPointEntity [ruleStartTime=" + ruleStartTime + ", ruleEndTime=" + ruleEndTime
				+ ", getRuleGroupInfo()=" + getRuleGroupInfo() + ", getRuleExecutionAuditPoint()="
				+ getRuleExecutionAuditPoint() + ", getSpanId()=" + getSpanId() + ", getOperationType()="
				+ getOperationType() + ", getStartTime()=" + getStartTime() + ", getEndTime()=" + getEndTime()
				+ ", getTraceId()=" + getTraceId() + "]";
	}
	
}
