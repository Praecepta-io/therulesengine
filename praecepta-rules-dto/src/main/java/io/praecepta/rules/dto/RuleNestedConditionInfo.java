package io.praecepta.rules.dto;

import io.praecepta.rules.model.filter.JoinOperatorType;

public class RuleNestedConditionInfo {

    private RuleConditionInfo conditionInfo;

    private JoinOperatorType joinOperatorType;

    private RuleNestedConditionInfo nextNestedCondition;


    public RuleConditionInfo getConditionInfo() {
        return conditionInfo;
    }

    public void setConditionInfo(RuleConditionInfo conditionInfo) {
        this.conditionInfo = conditionInfo;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }

    public RuleNestedConditionInfo getNextNestedCondition() {
        return nextNestedCondition;
    }

    public void setNextNestedCondition(RuleNestedConditionInfo nextNestedCondition) {
        this.nextNestedCondition = nextNestedCondition;
    }

}
