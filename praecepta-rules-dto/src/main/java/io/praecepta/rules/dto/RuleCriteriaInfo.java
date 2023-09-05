package io.praecepta.rules.dto;

import java.util.Collection;

public class RuleCriteriaInfo {

    private String ruleName;

    private Collection<RuleActionInfo> actionList;

    private RuleNestedConditionInfo conditions;

    private Collection<RuleActionInfo> failureActionList;

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

    public RuleNestedConditionInfo getConditions() {
        return conditions;
    }

    public void setConditions(RuleNestedConditionInfo conditions) {
        this.conditions = conditions;
    }

    public Collection<RuleActionInfo> getFailureActionList() {
        return failureActionList;
    }

    public void setFailureActionList(Collection<RuleActionInfo> failureActionList) {
        this.failureActionList = failureActionList;
    }
}
