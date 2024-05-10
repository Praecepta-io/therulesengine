package io.praecepta.rules.factor.actions;

import java.util.Map;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;
import io.praecepta.rules.model.PraeceptaActionResult;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;

public interface PraeceptaUnderlyingFactorAction {
	
	// Remove this 
	public PraeceptaActionHolder performAction(Object inputValue, Map<PraeceptaActionParametersType,Object> factorParams);
	
	boolean validateDataType(Object inputValue, Object factoreValue);
	
	public static PraeceptaActionHolder getActionHolder() {
		
		PraeceptaActionHolder actionHolder = new PraeceptaActionHolder();
		
		PraeceptaActionResult actionResult = new PraeceptaActionResult();
		
		actionHolder.setActionResult(actionResult);
		
		return actionHolder;
	}
	
	public static void addStatusAndMessageToActonHolder(PraeceptaActionHolder actionHolder,ACTION_EXECUTION_STATUS status,  String message) {
		
		if(actionHolder == null) {
			actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		}
		
		PraeceptaActionResult actionResult = new PraeceptaActionResult();
		
		actionResult.setActionResultStatus(status);
		
		actionResult.setActionResultStatusMessage(message);
		
		actionHolder.setActionResult(actionResult);
		
	}
	
	public static PraeceptaActionHolder addActionValueWithStatusAndMessage(Object actionedValue,ACTION_EXECUTION_STATUS status,  String message) {
		
		PraeceptaActionHolder actionHolder = assignActionedValueToActionHolder(actionedValue);
		
		addStatusAndMessageToActonHolder(actionHolder, status, message);
		
		return actionHolder;
		
	}
	
	default PraeceptaActionHolder prepareActionHolder(Object actionedValue) {
		
		PraeceptaActionHolder actionHolder = assignActionedValueToActionHolder(actionedValue);
		
		addStatusAndMessageToActonHolder(actionHolder, ACTION_EXECUTION_STATUS.SUCCESS, PraeceptaValueAssignAction.DEFAULT_ACTION_MESSAGE);
		
		return actionHolder;
	}

	public static PraeceptaActionHolder assignActionedValueToActionHolder(Object actionedValue) {
		
		PraeceptaActionHolder actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		
		actionHolder.setActionedValue(actionedValue);
		return actionHolder;
	}
}
