package io.praecepta.rules.dto;

import java.util.List;

import io.praecepta.rules.model.filter.JoinOperatorType;

public class MultiConditionInfo {

    private List<SimpleConditionInfo> conditionInfo;

    private JoinOperatorType joinOperatorType;

    public MultiConditionInfo(){}

    public MultiConditionInfo(List<SimpleConditionInfo> conditionInfo){
        this.conditionInfo = conditionInfo;
    }

    public MultiConditionInfo(List<SimpleConditionInfo> conditionInfo, JoinOperatorType joinOperatorType){
        this.conditionInfo = conditionInfo;
        this.joinOperatorType = joinOperatorType;
    }
    public List<SimpleConditionInfo> getConditionInfo() {
        return conditionInfo;
    }

    public void setConditionInfo(List<SimpleConditionInfo> conditionInfo) {
        this.conditionInfo = conditionInfo;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }


}
