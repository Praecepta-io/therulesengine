package io.praecepta.rules.dto;

import java.util.Map;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;

public class ConditionInfo {

    private String attributeName;

    private Object attributeValue;

    private ConditionOperatorType operatorType;

    private Object valueToCompare;

    private String attributeToCompare;

    private Map<String,Object> additionalParameters;


    private JoinOperatorType joinOperatorType;

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

    public Object getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }
}
