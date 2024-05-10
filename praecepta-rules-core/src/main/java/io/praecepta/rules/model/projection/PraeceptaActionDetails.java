package io.praecepta.rules.model.projection;

import java.util.Map;

import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;

public class PraeceptaActionDetails {

	private PraeceptaActionStrategyType actionStrategy;
	private String actionAttributeName;
	private String valueToAssign;
	private String sourceValueAttributeName;
	private String actionType;
	private String actionMessage;
	private String actionName;

	private Map<PraeceptaActionParametersType, Object> additionalParameters;

	public PraeceptaActionStrategyType getActionStrategy() {
		return actionStrategy;
	}

	public void setActionStrategy(PraeceptaActionStrategyType actionStrategy) {
		this.actionStrategy = actionStrategy;
	}

	public String getActionAttributeName() {
		return actionAttributeName;
	}

	public void setActionAttributeName(String actionAttributeName) {
		this.actionAttributeName = actionAttributeName;
	}

	public String getValueToAssign() {
		return valueToAssign;
	}

	public void setValueToAssign(String valueToAssign) {
		this.valueToAssign = valueToAssign;
	}

	public String getSourceValueAttributeName() {
		return sourceValueAttributeName;
	}

	public void setSourceValueAttributeName(String sourceValueAttributeName) {
		this.sourceValueAttributeName = sourceValueAttributeName;
	}

	public Map<PraeceptaActionParametersType, Object> getAdditionalParameters() {
		return additionalParameters;
	}

	public void setAdditionalParameters(Map<PraeceptaActionParametersType, Object> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
}
