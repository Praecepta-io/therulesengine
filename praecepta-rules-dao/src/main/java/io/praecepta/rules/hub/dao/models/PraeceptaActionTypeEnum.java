package io.praecepta.rules.hub.dao.models;

import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;

public enum PraeceptaActionTypeEnum {
    ADD_TO_PAYLOAD(PraeceptaValueAssignAction.class),
    ;
    
    private Class className;

    PraeceptaActionTypeEnum(Class className){
        this.className = className;
    }



}
