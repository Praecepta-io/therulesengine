package io.praecepta.rules.hub.dao.models;

import java.util.List;

import io.praecepta.rules.model.PraeceptaRuleResult;

public class PraeceptaRuleGrpResult {

	private PraeceptaRuleSpaceCompositeKey ruleSpaceKey;
	
	private String ruleGroupName;
	
	private List<PraeceptaRuleResult> ruleExecutionResult;

	public PraeceptaRuleSpaceCompositeKey getRuleSpaceKey() {
		return ruleSpaceKey;
	}

	public void setRuleSpaceKey(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
		this.ruleSpaceKey = ruleSpaceKey;
	}

	public String getRuleGroupName() {
		return ruleGroupName;
	}

	public void setRuleGroupName(String ruleGroupName) {
		this.ruleGroupName = ruleGroupName;
	}

	public List<PraeceptaRuleResult> getRuleExecutionResult() {
		return ruleExecutionResult;
	}

	public void setRuleExecutionResult(List<PraeceptaRuleResult> ruleExecutionResult) {
		this.ruleExecutionResult = ruleExecutionResult;
	}
	
}
