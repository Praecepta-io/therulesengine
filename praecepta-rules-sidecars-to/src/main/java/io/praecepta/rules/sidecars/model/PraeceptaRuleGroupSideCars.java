package io.praecepta.rules.sidecars.model;

import java.io.Serializable;
import java.util.List;

public class PraeceptaRuleGroupSideCars implements Serializable {

	private String ruleGrpName;
	private List<PraeceptaSideCarMetadata> preRuleGrpSideCars;
	private List<PraeceptaSideCarMetadata> postRuleGrpSideCars;
	private List<PraeceptaRuleLevelSideCars> ruleLevelSideCars;

	public String getRuleGrpName() {
		return ruleGrpName;
	}

	public void setRuleGrpName(String ruleGrpName) {
		this.ruleGrpName = ruleGrpName;
	}

	public List<PraeceptaSideCarMetadata> getPreRuleGrpSideCars() {
		return preRuleGrpSideCars;
	}

	public void setPreRuleGrpSideCars(List<PraeceptaSideCarMetadata> preRuleGrpSideCars) {
		this.preRuleGrpSideCars = preRuleGrpSideCars;
	}

	public List<PraeceptaSideCarMetadata> getPostRuleGrpSideCars() {
		return postRuleGrpSideCars;
	}

	public void setPostRuleGrpSideCars(List<PraeceptaSideCarMetadata> postRuleGrpSideCars) {
		this.postRuleGrpSideCars = postRuleGrpSideCars;
	}

	public List<PraeceptaRuleLevelSideCars> getRuleLevelSideCars() {
		return ruleLevelSideCars;
	}

	public void setRuleLevelSideCars(List<PraeceptaRuleLevelSideCars> ruleLevelSideCars) {
		this.ruleLevelSideCars = ruleLevelSideCars;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleGroupSideCars [ruleGrpName=" + ruleGrpName + ", preRuleGrpSideCars="
				+ preRuleGrpSideCars + ", postRuleGrpSideCars=" + postRuleGrpSideCars + ", ruleLevelSideCars="
				+ ruleLevelSideCars + "]";
	}
}
