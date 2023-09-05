package io.praecepta.rules.dto;

import java.util.Map;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;

public class RuleConditionInfo {

    private String attributeName;

    private ConditionOperatorType operatorType;

    private Object valueToCompare;

    private String attributeToCompare;

    private Map<String,Object> additionalParameters;

    private JoinOperatorType joinOperatorType;

    private RuleConditionInfo nextConditionInfo;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public ConditionOperatorType getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(ConditionOperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public Object getValueToCompare() {
        return valueToCompare;
    }

    public void setValueToCompare(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public String getAttributeToCompare() {
        return attributeToCompare;
    }

    public void setAttributeToCompare(String attributeToCompare) {
        this.attributeToCompare = attributeToCompare;
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(Map<String, Object> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }

    public RuleConditionInfo getNextConditionInfo() {
        return nextConditionInfo;
    }

    public void setNextConditionInfo(RuleConditionInfo nextConditionInfo) {
        this.nextConditionInfo = nextConditionInfo;
    }
}
