package io.praecepta.rules.hub.dao.models;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.praecepta.core.data.intf.IModel;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

public class PraeceptaRuleGroup implements IModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -258411792696748625L;

	private String ruleGroupName;
	
	private boolean active;

	private Collection<PraeceptaCriteria> praeceptaCriterias;
	
	private List<PraeceptaCriteria> praeceptaOrderedCriterias;
	
	private Collection<PraeceptaActionDetails> actionToPerform;

	private Collection<PraeceptaActionDetails> actionToPerformOnFailure;
	
	private PraeceptaRuleSpaceCompositeKey ruleSpaceKey;
	public PraeceptaRuleGroup(){

	}
	public PraeceptaRuleGroup(String spaceName, String clientName, String appName) {
		this(new PraeceptaRuleSpaceCompositeKey(spaceName, clientName, appName));
		this.spaceName = spaceName;
		this.clientName = clientName;
		this.appName = appName;
	}
	
	public PraeceptaRuleGroup(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {
		this.ruleSpaceKey = ruleSpaceKey;
	}
	
	// Entity Name - BR
	private String spaceName;
	
	// Aka Client Id
	private String clientName;
	
	// Aka System Id 
	private String appName;
	
	// It can be a rule Group id or a combination of rule group id and node id
	private String uniqueId;

	/**
	 * @return the praeceptaCriterias
	 */
	public Collection<PraeceptaCriteria> getPraeceptaCriterias() {
		return praeceptaCriterias;
	}

	/**
	 * @param praeceptaCriterias the praeceptaCriterias to set
	 */
	public void setPraeceptaCriterias(Collection<PraeceptaCriteria> praeceptaCriterias) {
		this.praeceptaCriterias = praeceptaCriterias;
	}

	public String getRuleGroupName() {
		return ruleGroupName;
	}

	public void setRuleGroupName(String ruleGroupName) {
		this.ruleGroupName = ruleGroupName;
	}

	public String getClientName() {
		return clientName;
	}

	public String getAppName() {
		return appName;
	}
	
	public String getSpaceName() {
		return spaceName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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

	public List<PraeceptaCriteria> getPraeceptaOrderedCriterias() {
		
		if(praeceptaOrderedCriterias == null) {
			praeceptaOrderedCriterias = praeceptaCriterias.stream()
					.sorted(Comparator.comparing(PraeceptaCriteria::getOrderNumber))
					.collect(Collectors.toList());
		}
		return praeceptaOrderedCriterias;
	}
	
	public Collection<PraeceptaActionDetails> getActionToPerform() {
		return actionToPerform;
	}
	public void setActionToPerform(Collection<PraeceptaActionDetails> actionToPerform) {
		this.actionToPerform = actionToPerform;
	}
	public Collection<PraeceptaActionDetails> getActionToPerformOnFailure() {
		return actionToPerformOnFailure;
	}
	public void setActionToPerformOnFailure(
			Collection<PraeceptaActionDetails> actionToPerformOnFailure) {
		this.actionToPerformOnFailure = actionToPerformOnFailure;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + ((spaceName == null) ? 0 : spaceName.hashCode());
		result = prime * result + ((ruleGroupName == null) ? 0 : ruleGroupName.hashCode());
		result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PraeceptaRuleGroup other = (PraeceptaRuleGroup) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		if (ruleGroupName == null) {
			if (other.ruleGroupName != null)
				return false;
		} else if (!ruleGroupName.equals(other.ruleGroupName))
			return false;
		if (uniqueId == null) {
			if (other.uniqueId != null)
				return false;
		} else if (!uniqueId.equals(other.uniqueId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleGroup [ruleGroupName=" + ruleGroupName + ", active=" + active + ", ruleSpaceKey="
				+ ruleSpaceKey + ", spaceName=" + spaceName + ", clientName=" + clientName + ", appName=" + appName
				+ ", uniqueId=" + uniqueId + "]";
	}
	
}
