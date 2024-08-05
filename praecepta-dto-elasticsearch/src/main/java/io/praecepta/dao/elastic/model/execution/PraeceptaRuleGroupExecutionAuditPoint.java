package io.praecepta.dao.elastic.model.execution;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.rules.dto.RuleGroupInfo;

public class PraeceptaRuleGroupExecutionAuditPoint extends PraeceptaProcessTracer{
	
	private EXECUTION_AUDIT_OPERATION_TYPE operationType;
	
	public PraeceptaRuleGroupExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}
	
	private RuleGroupInfo ruleGroupInfo;
	
	public RuleGroupInfo getRuleGroupInfo() {
		return ruleGroupInfo;
	}

	public void setRuleGroupInfo(RuleGroupInfo ruleGroupInfo) {
		this.ruleGroupInfo = ruleGroupInfo;
	}

	public EXECUTION_AUDIT_OPERATION_TYPE getOperationType() {
		return operationType;
	}

	public void setOperationType(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleGroupExecutionAuditPoint [operationType=" + operationType + ", ruleGroupInfo="
				+ ruleGroupInfo + ", getStartTime()=" + getStartTime() + ", getEndTime()=" + getEndTime()
				+ ", getTraceId()=" + getTraceId() + "]";
	}
	
}
