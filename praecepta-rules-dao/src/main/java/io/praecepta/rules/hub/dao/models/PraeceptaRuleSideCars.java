package io.praecepta.rules.hub.dao.models;

import io.praecepta.core.data.intf.IModel;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCars;

public class PraeceptaRuleSideCars implements IModel {

	private PraeceptaRuleGroupSideCars ruleGroupSideCars;

	private PraeceptaRuleSpaceCompositeKey ruleSpaceKey;
	
	private boolean active;

	public PraeceptaRuleSideCars() {

	}

	public PraeceptaRuleSideCars(String spaceName, String clientName, String appName) {
		this(new PraeceptaRuleSpaceCompositeKey(spaceName, clientName, appName));
	}
	public PraeceptaRuleSideCars(String spaceName, String clientName, String appName, String version) {
		this(new PraeceptaRuleSpaceCompositeKey(spaceName, clientName, appName));
		this.ruleSpaceKey.setVersion(version);
	}

	public PraeceptaRuleSideCars(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
		this.ruleSpaceKey = ruleSpaceKey;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PraeceptaRuleSpaceCompositeKey getRuleSpaceKey() {
		return ruleSpaceKey;
	}

	public void setRuleSpaceKey(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
		this.ruleSpaceKey = ruleSpaceKey;
	}

	public PraeceptaRuleGroupSideCars getRuleGroupSideCars() {
		return ruleGroupSideCars;
	}

	public void setRuleGroupSideCars(PraeceptaRuleGroupSideCars ruleGroupSideCars) {
		this.ruleGroupSideCars = ruleGroupSideCars;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSideCars [ruleGroupSideCars=" + ruleGroupSideCars + ", ruleSpaceKey=" + ruleSpaceKey
				 + ", active=" + active + "]";
	}
}
