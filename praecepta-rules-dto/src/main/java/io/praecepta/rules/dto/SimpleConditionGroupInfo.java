package io.praecepta.rules.dto;

import java.util.Collection;

public class SimpleConditionGroupInfo {

    private String ruleGroupName;
    private RuleSpaceInfo ruleSpaceInfo;

    private Collection<SimpleConditionCriteriaInfo> simpleConditionCriteriaInfos;

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

    public RuleSpaceInfo getRuleSpaceInfo() {
        return ruleSpaceInfo;
    }

    public void setRuleSpaceInfo(RuleSpaceInfo ruleSpaceInfo) {
        this.ruleSpaceInfo = ruleSpaceInfo;
    }
}
