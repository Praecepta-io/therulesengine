package io.praecepta.core.helper;

public enum PraeceptaActionTypeEnum {
    ADD_TO_PAYLOAD("io.praecepta.rules.actions.impl.PraeceptaValueAssignAction"),
    SEND_MESSAGE_TO_KAFKA("io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyAction"),
    SEND_MESSAGE_TO_DB("io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyDBAction"),
    ;

    private String className;

    PraeceptaActionTypeEnum(String className){
        this.className = className;
    }

    public String getClassName(){
        return this.className;
    }



}
