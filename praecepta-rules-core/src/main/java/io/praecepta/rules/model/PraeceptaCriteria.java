package io.praecepta.rules.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

/**
 * 
 * @author rajasrikar
 *
 */

public class PraeceptaCriteria implements Serializable{

	private PraeceptaMultiNestedCondition predicates;
	
	private Collection<PraeceptaActionDetails> actionToPerform;

	private Collection<PraeceptaActionDetails> actionToPerformOnFailure;
	private int orderNumber;
	
	//Default value is UUID. This can be over written with value gets passed from Criteria configurations 
	private String ruleName =  UUID.randomUUID().toString();

	public PraeceptaMultiNestedCondition getPredicates() {
		return predicates;
	}

	public void setPredicates(PraeceptaMultiNestedCondition predicates) {
		this.predicates = predicates;
	}

	public Collection<PraeceptaActionDetails> getActionToPerform() {
		return actionToPerform;
	}

	public void setActionToPerform(
			Collection<PraeceptaActionDetails> actionToPerform) {
		this.actionToPerform = actionToPerform;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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
		result = prime * result + ((actionToPerform == null) ? 0 : actionToPerform.hashCode());
		result = prime * result + ((actionToPerformOnFailure == null) ? 0 : actionToPerformOnFailure.hashCode());
		result = prime * result + orderNumber;
		result = prime * result + ((predicates == null) ? 0 : predicates.hashCode());
		result = prime * result + ((ruleName == null) ? 0 : ruleName.hashCode());
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
		PraeceptaCriteria other = (PraeceptaCriteria) obj;
		if (actionToPerform == null) {
			if (other.actionToPerform != null)
				return false;
		} else if (!actionToPerform.equals(other.actionToPerform))
			return false;
		if (actionToPerformOnFailure == null) {
			if (other.actionToPerformOnFailure != null)
				return false;
		} else if (!actionToPerformOnFailure.equals(other.actionToPerformOnFailure))
			return false;
		if (orderNumber != other.orderNumber)
			return false;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} else if (!predicates.equals(other.predicates))
			return false;
		if (ruleName == null) {
			if (other.ruleName != null)
				return false;
		} else if (!ruleName.equals(other.ruleName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaCriteria [predicates=" + predicates + ", actionToPerform=" + actionToPerform
				+ ", actionToPerformOnFailure=" + actionToPerformOnFailure + ", orderNumber=" + orderNumber
				+ ", ruleName=" + ruleName + "]";
	}
	
}

