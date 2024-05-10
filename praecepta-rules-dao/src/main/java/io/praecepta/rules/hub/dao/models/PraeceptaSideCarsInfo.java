package io.praecepta.rules.hub.dao.models;

import io.praecepta.core.data.intf.IModel;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class PraeceptaSideCarsInfo implements IModel {

	private PraeceptaRuleGroupSideCarsInfo ruleGroupSideCarsInfo;

	private PraeceptaRuleSpaceCompositeKey ruleSpaceKey;
	
	private boolean active;

	public PraeceptaSideCarsInfo() {

	}

	public PraeceptaSideCarsInfo(String spaceName, String clientName, String appName) {
		this(new PraeceptaRuleSpaceCompositeKey(spaceName, clientName, appName));
	}
	public PraeceptaSideCarsInfo(String spaceName, String clientName, String appName, String version) {
		this(new PraeceptaRuleSpaceCompositeKey(spaceName, clientName, appName));
		this.ruleSpaceKey.setVersion(version);
	}

	public PraeceptaSideCarsInfo(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
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

	public PraeceptaRuleGroupSideCarsInfo getRuleGroupSideCars() {
		return ruleGroupSideCarsInfo;
	}

	public void setRuleGroupSideCars(PraeceptaRuleGroupSideCarsInfo ruleGroupSideCars) {
		this.ruleGroupSideCarsInfo = ruleGroupSideCars;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSideCars [ruleGroupSideCars=" + ruleGroupSideCarsInfo + ", ruleSpaceKey=" + ruleSpaceKey
				 + ", active=" + active + "]";
	}
}
