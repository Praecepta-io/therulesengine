package io.praecepta.rules.dto;

import java.util.Collection;

public class SimpleConditionGroupInfo {

    private String ruleGroupName;

    private Collection<SimpleConditionCriteriaInfo> simpleConditionCriteriaInfos;

    private String attributeSchemaTemplate;

    public Collection<SimpleConditionCriteriaInfo> getSimpleConditionCriteriaInfos() {
        return simpleConditionCriteriaInfos;
    }

    public void setSimpleConditionCriteriaInfos(Collection<SimpleConditionCriteriaInfo> simpleConditionCriteriaInfos) {
        this.simpleConditionCriteriaInfos = simpleConditionCriteriaInfos;
    }

    public String getRuleGroupName() {
        return ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }

    public String getAttributeSchemaTemplate() {
        return attributeSchemaTemplate;
    }

    public void setAttributeSchemaTemplate(String attributeSchemaTemplate) {
        this.attributeSchemaTemplate = attributeSchemaTemplate;
    }
}
