package io.praecepta.rules.dto;

import java.util.Collection;

public class SimpleConditionCriteriaInfo {

    private String ruleName;

    private Collection<RuleActionInfo> actionList;

    private Collection<RuleActionInfo> failureActionList;

    private SimpleConditionInfo conditionInfo;
    public SimpleConditionInfo getConditionInfo() {
        return conditionInfo;
    }

    public void setConditionInfo(SimpleConditionInfo conditionInfo) {
        this.conditionInfo = conditionInfo;
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
}
