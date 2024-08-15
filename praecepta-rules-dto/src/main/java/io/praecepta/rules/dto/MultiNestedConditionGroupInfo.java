package io.praecepta.rules.dto;

import java.util.Collection;

public class MultiNestedConditionGroupInfo {

    private String ruleGroupName;

    private Collection<MultiNestedConditionCriteriaInfo> multiNestedConditionCriteriaInfos;

    private String attributeSchemaTemplate;

    public Collection<MultiNestedConditionCriteriaInfo> getMultiNestedConditionCriteriaInfos() {
        return multiNestedConditionCriteriaInfos;
    }

    public void setMultiNestedConditionCriteriaInfos(Collection<MultiNestedConditionCriteriaInfo> multiNestedConditionCriteriaInfos) {
        this.multiNestedConditionCriteriaInfos = multiNestedConditionCriteriaInfos;
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
