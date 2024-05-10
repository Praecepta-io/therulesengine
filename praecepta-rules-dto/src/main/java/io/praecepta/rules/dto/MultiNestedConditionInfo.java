package io.praecepta.rules.dto;

import java.util.List;

import io.praecepta.rules.model.filter.JoinOperatorType;

public class MultiNestedConditionInfo {

    private List<MultiConditionInfo> multiConditionsList;

    private JoinOperatorType joinOperatorType;


    public List<MultiConditionInfo> getMultiConditionsList() {
        return multiConditionsList;
    }

    public void setMultiConditionsList(List<MultiConditionInfo> multiConditionsList) {
        this.multiConditionsList = multiConditionsList;
    }

    public JoinOperatorType getJoinOperatorType() {
        return joinOperatorType;
    }

    public void setJoinOperatorType(JoinOperatorType joinOperatorType) {
        this.joinOperatorType = joinOperatorType;
    }

}
