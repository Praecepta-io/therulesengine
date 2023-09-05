package io.praecepta.rules.hub.dao.models;

import java.util.Collection;

import io.praecepta.core.data.intf.IModel;

public class PraeceptaRuleSpace implements IModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Collection<PraeceptaRuleGroup> praeceptaRuleGrps;
	
	PraeceptaRuleSpaceCompositeKey ruleSpaceKey;
	
	private boolean active;

	/**
	 * @return the praeceptaRuleGrps
	 */
	public Collection<PraeceptaRuleGroup> getPraeceptaRuleGrps() {
		return praeceptaRuleGrps;
	}

	/**
	 * @param praeceptaRuleGrps the praeceptaRuleGrps to set
	 */
	public void setPraeceptaRuleGrps(Collection<PraeceptaRuleGroup> praeceptaRuleGrps) {
		this.praeceptaRuleGrps = praeceptaRuleGrps;
	}

	public PraeceptaRuleSpaceCompositeKey getRuleSpaceKey() {
		return ruleSpaceKey;
	}

	public void setRuleSpaceKey(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
		this.ruleSpaceKey = ruleSpaceKey;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSpace [praeceptaRuleGrps=" + praeceptaRuleGrps + ", ruleSpaceKey=" + ruleSpaceKey + "]";
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
