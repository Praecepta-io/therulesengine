package io.praecepta.dao.elastic.model.execution;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.model.PraeceptaRuleResult;

import java.util.List;

public class PraeceptaRuleGroupExecutionAuditPoint extends PraeceptaProcessTracer{
	
	private EXECUTION_AUDIT_OPERATION_TYPE operationType;
	
	public PraeceptaRuleGroupExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}
	
	private RuleGroupInfo ruleGroupInfo;

	private List<PraeceptaRuleResult> ruleExecutionAuditPoints;

	private String failureRulesCount;

	private String successRulesCount;


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


	public List<PraeceptaRuleResult> getRuleExecutionAuditPoints() {
		return ruleExecutionAuditPoints;
	}

	public void setRuleExecutionAuditPoints(List<PraeceptaRuleResult> ruleExecutionAuditPoints) {
		this.ruleExecutionAuditPoints = ruleExecutionAuditPoints;
	}

	public String getFailureRulesCount() {
		return failureRulesCount;
	}

	public void setFailureRulesCount(String failureRulesCount) {
		this.failureRulesCount = failureRulesCount;
	}

	public String getSuccessRulesCount() {
		return successRulesCount;
	}

	public void setSuccessRulesCount(String successRulesCount) {
		this.successRulesCount = successRulesCount;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleGroupExecutionAuditPoint [operationType=" + operationType + ", ruleGroupInfo="
				+ ruleGroupInfo + ", getStartTime()=" + getStartTime() + ", getEndTime()=" + getEndTime()
				+", ruleExecutionAuditPoints=" + ruleExecutionAuditPoints
				+", getTraceId()=" + getTraceId() + "]";
	}
}
