package io.praecepta.rules.dto;

import java.util.Collection;

public class MultiNestedConditionCriteriaInfo {

    private String ruleName;

    private Collection<RuleActionInfo> actionList;

    private Collection<RuleActionInfo> failureActionList;
    private Collection<MultiNestedConditionInfo> multiNestedConditionList;
    public Collection<MultiNestedConditionInfo> getMultiNestedConditionList() {
        return multiNestedConditionList;
    }

    public void setMultiNestedConditionList(Collection<MultiNestedConditionInfo> multiNestedConditionList) {
        this.multiNestedConditionList = multiNestedConditionList;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Collection<RuleActionInfo> getActionList() {
        return actionList;
    }

    public void setActionList(Collection<RuleActionInfo> actionList) {
        this.actionList = actionList;
    }

    public Collection<RuleActionInfo> getFailureActionList() {
        return failureActionList;
    }

    public void setFailureActionList(Collection<RuleActionInfo> failureActionList) {
        this.failureActionList = failureActionList;
    }

    @Override
    public String toString() {
        return "MultiNestedConditionCriteriaInfo{" +
                "ruleName='" + ruleName + '\'' +
                ", actionList=" + actionList +
                ", failureActionList=" + failureActionList +
                ", multiNestedConditionList=" + multiNestedConditionList +
                '}';
    }
}
