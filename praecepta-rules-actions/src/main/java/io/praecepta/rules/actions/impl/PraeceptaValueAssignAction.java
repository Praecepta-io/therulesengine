package io.praecepta.rules.actions.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionFactorTypeToAction;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.enums.PraeceptaFactorActionType;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_RESULT_STATUS;

/**
 * 
 * @author rajasrikar
 *
 */
public final class PraeceptaValueAssignAction extends PraeceptaAbstractAction{

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaValueAssignAction.class); 
	
	
	@Override
	public String getActionName() {
		return PraeceptaValueAssignAction.class.getName();
	}
	
	@Override
	public final void doAction(PraeceptaRequestStore input) {
		LOG.info( "Inside the Value Assign Action");
		
		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		
		Map<PraeceptaActionParametersType,Object> parametersMap = getParameters();
		
		Object valueToAssign = getValueToAssign();
		
		// In the Input Payload, look for Any Source attribute name specified to use as part of Action 
		// It could be a an Attribute name from "Constant Section" in the input Josn or An Attribute Name in the Payload  
		String valueSourcingFrom = getSourceValueAttributeName();
		
		if(valueSourcingFrom == null) {
			// Incase the Value is not sourcing from any other attribute or constant, then use it's own attribute name as the Value source
			valueSourcingFrom = getActionAttributeName();
		}
		
		// Inacase the value to assign doesn't exist in the Action section, we can get it from the Source Attribute in the input payload json 
		if(valueToAssign == null) {
			valueToAssign = requestMap.get(valueSourcingFrom);
		}
		
		boolean actionSuccessStatusFlag = true;
		
		if(parametersMap != null && !ObjectHelper.isObjectEmpty(parametersMap.get(PraeceptaActionParametersType.FACTOR_ACTION_TYPE))) {
			
			if(!ObjectHelper.isObjectEmpty(parametersMap.get(PraeceptaActionParametersType.FACTOR_VALUE))) {
				
				PraeceptaUnderlyingFactorAction underlyingAction = 
						PraeceptaActionFactorTypeToAction.getActionFactor(
								PraeceptaFactorActionType.valueOf(parametersMap.get(PraeceptaActionParametersType.FACTOR_ACTION_TYPE).toString())); 
				
				if(underlyingAction != null) {
					
					PraeceptaActionHolder actionHolder = underlyingAction.performAction(valueToAssign, parametersMap);
					
					if(actionHolder == null || 
							(actionHolder.getActionResult() != null && ACTION_RESULT_STATUS.SUCCESS != 
							actionHolder.getActionResult().getActionResultStatus()) ) {
						LOG.error("Failed to Perform Factored Action for Attribute {}", getActionAttributeName());
						LOG.error("Provided Value {} and paramteres{}", valueToAssign, getParameters());
						actionSuccessStatusFlag = false; 
					}
					
					input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO, actionHolder);
				
				}else {
					LOG.warn("Action Factor Type Provided doesn't match with Predefined Types. No Action to perform ");
					actionSuccessStatusFlag = false; 
				}
			
			}else {
				LOG.error("Action Factore Value is Empty in the input. Unable to Perform the Action");
				actionSuccessStatusFlag = false; 
			}
		}

		applyStrategy(requestMap, valueToAssign);
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, GsonHelper.toJson(requestMap));
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_STATUS, actionSuccessStatusFlag);
		
		LOG.info( "Exiting from the Value Assign Action");
	}

	private void applyStrategy(Map<String, Object> requestMap, Object valueToAssign) {
		
		LOG.info( "Inside the Apply Strategy");
		
		if(getActionStrategy() != null) {
//			PraeceptaActionStrategyType strategyType = PraeceptaActionStrategyType.valueOf(getActionStrategy());
			PraeceptaActionStrategyType strategyType = getActionStrategy();
			
			switch (strategyType) {
			
			case ADD_TO_PAYLOAD:
			case ADD_TO_PAYLOAD_FROM_PAYLOAD:
			case ADD_TO_PAYLOAD_FROM_CONSTANT:
				requestMap.put(getActionAttributeName(), valueToAssign);
				break;

			default:
				break;
			}
		}
		
		LOG.info( "Exiting from Apply Strategy");
	}
}
