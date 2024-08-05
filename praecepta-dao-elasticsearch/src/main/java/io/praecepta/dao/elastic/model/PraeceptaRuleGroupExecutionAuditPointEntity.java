package io.praecepta.dao.elastic.model;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleGroupExecutionAuditPoint;

public class PraeceptaRuleGroupExecutionAuditPointEntity extends PraeceptaRuleGroupExecutionAuditPoint{

	public PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		super(operationType);
	}
	
	private String ruleGrpStartTime;
	
	private String ruleGrpEndTime;
	
	public String getRuleGrpStartTime() {
		return ruleGrpStartTime;
	}

	public void setRuleGrpStartTime(String ruleGrpStartTime) {
		this.ruleGrpStartTime = ruleGrpStartTime;
	}

	public String getRuleGrpEndTime() {
		return ruleGrpEndTime;
	}

	public void setRuleGrpEndTime(String ruleGrpEndTime) {
		this.ruleGrpEndTime = ruleGrpEndTime;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleGroupExecutionAuditPointEntity [ruleGrpStartTime=" + ruleGrpStartTime + ", ruleGrpEndTime="
				+ ruleGrpEndTime + ", getRuleGroupInfo()=" + getRuleGroupInfo() + ", getOperationType()="
				+ getOperationType() + ", getStartTime()=" + getStartTime() + ", getEndTime()=" + getEndTime()
				+ ", getTraceId()=" + getTraceId() + "]";
	}
}
