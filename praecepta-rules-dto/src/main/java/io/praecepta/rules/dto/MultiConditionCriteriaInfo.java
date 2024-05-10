package io.praecepta.rules.dto;

import java.util.Collection;

public class MultiConditionCriteriaInfo {

    private String ruleName;

    private Collection<RuleActionInfo> actionList;

    private Collection<RuleActionInfo> failureActionList;
    private Collection<MultiConditionInfo> multiConditionList;
    public Collection<MultiConditionInfo> getMultiConditionList() {
        return multiConditionList;
    }

    public void setMultiConditionList(Collection<MultiConditionInfo> multiConditionList) {
        this.multiConditionList = multiConditionList;
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
