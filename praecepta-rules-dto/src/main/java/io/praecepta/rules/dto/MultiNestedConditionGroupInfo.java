package io.praecepta.rules.dto;

import java.util.Collection;

public class MultiNestedConditionGroupInfo {

    private String ruleGroupName;
    private RuleSpaceInfo ruleSpaceInfo;

    private Collection<MultiNestedConditionCriteriaInfo> multiNestedConditionCriteriaInfos;

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

    public RuleSpaceInfo getRuleSpaceInfo() {
        return ruleSpaceInfo;
    }

    public void setRuleSpaceInfo(RuleSpaceInfo ruleSpaceInfo) {
        this.ruleSpaceInfo = ruleSpaceInfo;
    }
}
