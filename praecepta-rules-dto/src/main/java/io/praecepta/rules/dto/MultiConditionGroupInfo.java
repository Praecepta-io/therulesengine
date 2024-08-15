package io.praecepta.rules.dto;

import java.util.Collection;

public class MultiConditionGroupInfo {

    private String ruleGroupName;
    private Collection<MultiConditionCriteriaInfo> multiConditionCriteriaInfos;

    private String attributeSchemaTemplate;

    public Collection<MultiConditionCriteriaInfo> getMultiConditionCriteriaInfos() {
        return multiConditionCriteriaInfos;
    }

    public void setMultiConditionCriteriaInfos(Collection<MultiConditionCriteriaInfo> multiConditionCriteriaInfos) {
        this.multiConditionCriteriaInfos = multiConditionCriteriaInfos;
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
