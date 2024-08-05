package io.praecepta.dao.elastic.model.execution;

import java.util.Collection;

public class PraeceptaExecutionAuditPoints {
	
	private Collection<PraeceptaExecutionAuditPoint> ruleExecutionAuditPoints;

	public Collection<PraeceptaExecutionAuditPoint> getRuleExecutionAuditPoints() {
		return ruleExecutionAuditPoints;
	}

	public void setRuleExecutionAuditPoints(Collection<PraeceptaExecutionAuditPoint> ruleExecutionAuditPoints) {
		this.ruleExecutionAuditPoints = ruleExecutionAuditPoints;
	}

}
