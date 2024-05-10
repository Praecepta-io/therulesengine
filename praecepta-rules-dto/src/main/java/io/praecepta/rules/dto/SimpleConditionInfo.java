package io.praecepta.rules.dto;

import java.util.List;

import io.praecepta.rules.model.filter.JoinOperatorType;

public class SimpleConditionInfo {

    private List<ConditionInfo> conditionInfoList;

    private JoinOperatorType joinOperatorType;


    public List<ConditionInfo> getConditionInfoList() {
        return conditionInfoList;
    }

    public void setConditionInfoList(List<ConditionInfo> conditionInfoList) {
        this.conditionInfoList = conditionInfoList;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }
}
