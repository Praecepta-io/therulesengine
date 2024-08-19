package io.praecepta.dao.elastic.model.execution;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.model.PraeceptaRuleResult;

public class PraeceptaRuleExecutionAuditPoint extends PraeceptaProcessTracer{
	
	private EXECUTION_AUDIT_OPERATION_TYPE operationType;
	
	private String spanId;
	
	private PraeceptaRuleResult ruleExecutionAuditPoint;

	private RuleGroupInfo ruleGroupInfo;
	
	public PraeceptaRuleExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}

	public RuleGroupInfo getRuleGroupInfo() {
		return ruleGroupInfo;
	}

	public void setRuleGroupInfo(RuleGroupInfo ruleGroupInfo) {
		this.ruleGroupInfo = ruleGroupInfo;
	}

	public PraeceptaRuleResult getRuleExecutionAuditPoint() {
		return ruleExecutionAuditPoint;
	}

	public void setRuleExecutionAuditPoint(PraeceptaRuleResult ruleExecutionAuditPoint) {
		this.ruleExecutionAuditPoint = ruleExecutionAuditPoint;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public EXECUTION_AUDIT_OPERATION_TYPE getOperationType() {
		return operationType;
	}

	public void setOperationType(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleExecutionAuditPoint [operationType=" + operationType + ", spanId=" + spanId
				+ ", ruleExecutionAuditPoint=" + ruleExecutionAuditPoint + ", ruleGroupInfo=" + ruleGroupInfo + "]";
	}
	
}
