package io.praecepta.rules.factor.actions;

import java.util.Map;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.model.PraeceptaActionResult;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_RESULT_STATUS;

public interface PraeceptaUnderlyingFactorAction {
	
	// Remove this 
	public PraeceptaActionHolder performAction(Object inputValue, Map<PraeceptaActionParametersType,Object> factorParams);
	
	boolean validateDataType(Object inputValue, Object factoreValue);
	
	public static PraeceptaActionHolder getActionHolder() {
		return new PraeceptaActionHolder();
	}
	
	public static void addStatusAndMessageToActonHolder(PraeceptaActionHolder actionHolder,ACTION_RESULT_STATUS status,  String message) {
		
		if(actionHolder == null) {
			actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		}
		
		PraeceptaActionResult actionResult = new PraeceptaActionResult();
		
		actionResult.setActionResultStatus(status);
		
		actionResult.setActionResultStatusMessage(message);
		
		actionHolder.setActionResult(actionResult);
	}
	
	default PraeceptaActionHolder prepareActionHolder(Object actionedValue) {
		
		PraeceptaActionHolder actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		
		actionHolder.setActionedValue(actionedValue);
		
		addStatusAndMessageToActonHolder(actionHolder, ACTION_RESULT_STATUS.SUCCESS, "Successfully added the Value ");
		
		return actionHolder;
	}
}
