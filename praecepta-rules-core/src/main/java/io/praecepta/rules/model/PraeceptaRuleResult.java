package io.praecepta.rules.model;

import java.util.List;

public class PraeceptaRuleResult {
	
	private String ruleName;
	
	private PraeceptaConditionResult conditionResult;
	
	private List<PraeceptaActionResultDetails> allActionResultDetails;
	
	public PraeceptaRuleResult(String ruleName, PraeceptaConditionResult conditionResult, List<PraeceptaActionResultDetails> allActionResultDetails) {
		this.ruleName = ruleName;
		this.conditionResult = conditionResult;
		this.allActionResultDetails = allActionResultDetails;
	}
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public PraeceptaConditionResult getConditionResult() {
		return conditionResult;
	}

	public void setConditionResult(PraeceptaConditionResult conditionResult) {
		this.conditionResult = conditionResult;
	}

	public List<PraeceptaActionResultDetails> getAllActionResultDetails() {
		return allActionResultDetails;
	}

	public void setAllActionResultDetails(List<PraeceptaActionResultDetails> allActionResultDetails) {
		this.allActionResultDetails = allActionResultDetails;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleResult [ruleName=" + ruleName + ", conditionResult=" + conditionResult
				+ ", allActionResultDetails=" + allActionResultDetails + "]";
	}

}
