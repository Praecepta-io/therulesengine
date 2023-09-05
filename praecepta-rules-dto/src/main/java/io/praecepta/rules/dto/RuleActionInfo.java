package io.praecepta.rules.dto;

import java.util.Map;

import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;

public class RuleActionInfo {

    private PraeceptaActionStrategyType actionStrategy;
    private String actionAttributeName;
    private String valueToAssign;
    private String sourceValueAttributeName;

    private Map<PraeceptaActionParametersType,Object> additionalParameters;

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
}
