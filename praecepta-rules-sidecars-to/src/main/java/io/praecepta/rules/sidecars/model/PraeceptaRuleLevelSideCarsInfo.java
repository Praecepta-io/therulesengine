package io.praecepta.rules.sidecars.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PraeceptaRuleLevelSideCarsInfo implements Serializable {
	private String ruleName;
	private List<PraeceptaSideCarMetadata> preRuleSideCars = Collections.emptyList();
	private List<PraeceptaSideCarMetadata> postRuleSideCars = Collections.emptyList();

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public List<PraeceptaSideCarMetadata> getPreRuleSideCars() {
		return preRuleSideCars;
	}

	public void setPreRuleSideCars(List<PraeceptaSideCarMetadata> preRuleSideCars) {
		this.preRuleSideCars = preRuleSideCars;
	}

	public List<PraeceptaSideCarMetadata> getPostRuleSideCars() {
		return postRuleSideCars;
	}

	public void setPostRuleSideCars(List<PraeceptaSideCarMetadata> postRuleSideCars) {
		this.postRuleSideCars = postRuleSideCars;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleLevelSideCars [RuleName=" + ruleName + ", preRuleSideCars=" + preRuleSideCars
				+ ", postRuleSideCars=" + postRuleSideCars + "]";
	}
}
