package io.praecepta.rules.actions.enums;

import java.util.ArrayList;
import java.util.List;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyAction;
import io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyDBAction;
import io.praecepta.rules.actions.impl.PraeceptaOrchestrationAction;
import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;

public enum PraeceptaActionTypeEnum {
    /*VALUE_ASSIGN("io.praecepta.rules.actions.impl.PraeceptaValueAssignAction"),
    SEND_MESSAGE_TO_KAFKA("io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyAction"),
    SEND_MESSAGE_TO_DB("io.praecepta.rules.actions.impl.PraeceptaChoreogrhaphyDBAction"),
    ;

    private String className;

    PraeceptaActionTypeEnum(String className){
        this.className = className;
    }

    public String getClassName(){
        return this.className;
    }*/

	VALUE_ASSIGN(new PraeceptaValueAssignAction()),
    SEND_MESSAGE_TO_KAFKA(new PraeceptaChoreogrhaphyAction()),
    SEND_MESSAGE_TO_DB(new PraeceptaChoreogrhaphyDBAction()),
    CALL_AN_API(new PraeceptaOrchestrationAction()),
    ;
	
	private static List<String> pipeAndFiltersActionTypes = getUniqueActionTypes();

	private PraeceptaAbstractAction action;

    PraeceptaActionTypeEnum(PraeceptaAbstractAction action){
        this.action = action;
    }
    
    public PraeceptaAbstractAction getAction() {
    	return action;
    }

	private static List<String> getUniqueActionTypes() {

		List<String> actionTypes = new ArrayList<>();
		
		for(PraeceptaActionTypeEnum actionType : PraeceptaActionTypeEnum.values()) {
			actionTypes.add(actionType.name());
		}
				
		return actionTypes;
	}
	
	public static PraeceptaActionTypeEnum getActionType(String type) {
		
		PraeceptaActionTypeEnum typeToReturn = PraeceptaActionTypeEnum.VALUE_ASSIGN;
		
		if(! PraeceptaObjectHelper.isObjectEmpty(type) && pipeAndFiltersActionTypes.contains(type.toUpperCase())) {
			
			typeToReturn = PraeceptaActionTypeEnum.valueOf(type);
		}
		
		return typeToReturn;
	}

}
