package io.praecepta.rules.actions.impl;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionFactorTypeToAction;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.enums.PraeceptaFactorActionType;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;

/**
 * 
 * @author rajasrikar
 *
 */
public final class PraeceptaValueAssignAction extends PraeceptaAbstractAction{

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaValueAssignAction.class); 
	
	public static final String DEFAULT_ACTION_MESSAGE = "Successfully Performed the Action ";
	public static final String DEFAULT_ERROR_ACTION_MESSAGE = "Unable to Perform the Needed Action ";
	
	/*@Override
	public String getActionName() {
		return PraeceptaValueAssignAction.class.getName();
	}*/
	
	@Override
	public final void doAction(PraeceptaRequestStore requestStore) {
		LOG.debug( "Inside the Value Assign Action");
		
		LOG.debug( "Performing the Value Assign Action for Rule Condition Status - {}", getActionTypeExecuted());
		
		boolean actionSuccessStatusFlag = false;

		if (!PraeceptaObjectHelper.isObjectEmpty(requestStore)) {
			
			Map<String,Object> requestMap = (Map<String, Object>) requestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);
			
			//If rulesRequestAsMap found null/empty fetching input from RulesRequest
			if (PraeceptaObjectHelper.isObjectEmpty(requestMap)) {
				
				Object rulesRequest = requestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
				
				if (PraeceptaObjectHelper.isObjectEmpty(requestMap) && rulesRequest instanceof String
						&& GsonHelper.isValidJson(rulesRequest.toString())) {
					requestMap = GsonHelper.toMapEntityPreserveNumber(rulesRequest.toString());
				}
			}
			
			LOG.debug( "Rules Request Received {} Inside the Value Assign Action", requestMap);
			
			
			if (!PraeceptaObjectHelper.isObjectEmpty(requestMap)) {
				
				
	
				Map<PraeceptaActionParametersType, Object> parametersMap = getParameters();
	
				Object valueToAssign = getValueToAssign();
	
				// Inacase the value to assign doesn't exist in the Action section, we can get
				// it from the Source Attribute in the input payload json
				if (valueToAssign == null) {
					// In the Input Payload, look for Any Source attribute name specified to use as
					// part of Action
					// It could be a an Attribute name from "Constant Section" in the input Josn or
					// An Attribute Name in the Payload
					String valueSourcingFrom = getSourceValueAttributeName();
	
					if (valueSourcingFrom == null) {
						// Incase the Value is not sourcing from any other attribute or constant, then
						// use it's own attribute name as the Value source
						valueSourcingFrom = getActionAttributeName();
					}
	
					valueToAssign = requestMap.get(valueSourcingFrom);
				}
	
				actionSuccessStatusFlag = true;
				
				PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) requestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);
				
				PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder, ACTION_EXECUTION_STATUS.SUCCESS, DEFAULT_ACTION_MESSAGE);
	
				// Look for Factoring if the Value to factor is not null
				if (valueToAssign != null && parametersMap != null && !PraeceptaObjectHelper
						.isObjectEmpty(parametersMap.get(PraeceptaActionParametersType.FACTOR_ACTION_TYPE))) {
	
					if (!PraeceptaObjectHelper.isObjectEmpty(parametersMap.get(PraeceptaActionParametersType.FACTOR_VALUE))) {
	
						PraeceptaUnderlyingFactorAction underlyingAction = PraeceptaActionFactorTypeToAction
								.getActionFactor(PraeceptaFactorActionType.valueOf(
										parametersMap.get(PraeceptaActionParametersType.FACTOR_ACTION_TYPE).toString()));
	
						if (underlyingAction != null) {
	
							actionHolder = underlyingAction.performAction(valueToAssign,
									parametersMap);
	
							if (actionHolder == null || (actionHolder.getActionResult() != null
									&& ACTION_EXECUTION_STATUS.SUCCESS != actionHolder.getActionResult()
											.getActionResultStatus())) {
								LOG.error("Failed to Perform Factored Action for Attribute {}", getActionAttributeName());
								LOG.error("Provided Value {} and paramteres{}", valueToAssign, getParameters());
	
								// Assigning Rule Action Status to False for Faliure scenario where It's unable to assign a Vlaue
								actionSuccessStatusFlag = false;
								
							} else {
	
								if (actionHolder != null) {
	
									LOG.debug(" Applied Factored Value Based on the Factor Action Type Received ");
	
									// Retrieving the Action value after Applying the Factor
									valueToAssign = actionHolder.getActionedValue();
								}
	
							}
	
						} else {
							LOG.warn("Action Factor Type Provided doesn't match with Predefined Types. "
									+ "No Action to perform. Assigning the Value Received from Constatn ot Source Attribute. Not Factor being Applied ");
						}
	
					} else {
						LOG.error("Action Factore Value is Empty in the input. Unable to Perform the Action");
					}
				}
				
				actionHolder.setActionedValue(valueToAssign);
				
				// Incase the Action attribute name is Empty, then assign the Action attribute that is same as the Source attribute name
				String actionAttributeName = getActionAttributeName() == null ? getSourceValueAttributeName() : getActionAttributeName();
				
				actionHolder.setActionAttributeName(actionAttributeName);
				
				if(valueToAssign != null) {
					
					LOG.debug(" Assigning Value is not Empty for Action Name {}" , getActionName());
					
					if(!PraeceptaObjectHelper.isObjectEmpty(getActionMessage())) {
						actionHolder.getActionResult().setActionResultStatusMessage(getActionMessage());
					}
					
					applyStrategy(requestMap, valueToAssign);
		
					requestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, GsonHelper.toJsonPreserveNumber(requestMap));
				
				} else {
					
					LOG.debug(" Assigning Value is Empty for Action Name {}" , getActionName());
					
					actionSuccessStatusFlag = false;
					
					actionHolder.getActionResult().setActionResultStatusMessage(DEFAULT_ERROR_ACTION_MESSAGE);
				}
				
				requestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO, actionHolder);
				
				Map<String, PraeceptaActionHolder> actionHolders = (Map<String, PraeceptaActionHolder>) 
						requestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
				
				String actionName = PraeceptaObjectHelper.isObjectEmpty(getActionName()) ? UUID.randomUUID().toString() : getActionName();
				
				LOG.debug(" Assigning Action Holder to All Actions Info for Action Name {}" , actionName);
				
				actionHolders.put( actionName ,actionHolder);
				
				requestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO, actionHolders);
				
			}
		}
		requestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_STATUS, actionSuccessStatusFlag);
		
		LOG.debug( "Exiting from the Value Assign Action");
	}

	private void applyStrategy(Map<String, Object> requestMap, Object valueToAssign) {
		
		LOG.debug( "Inside the Apply Strategy with Action Attribute Name {}  and Value To Assign {}", getActionAttributeName(), valueToAssign);
		
		LOG.debug( " Here is the Strategy to Apply - {} ", getActionStrategy());
		
		PraeceptaActionStrategyType actionStrategy = getActionStrategy() == null ? PraeceptaActionStrategyType.ADD_TO_PAYLOAD : getActionStrategy();

//		if(getActionStrategy() != null) {
			
//			switch (getActionStrategy()) {
			switch (actionStrategy) {
			
			case ADD_TO_PAYLOAD:
			case ADD_TO_PAYLOAD_FROM_PAYLOAD:
			case ADD_TO_PAYLOAD_FROM_CONSTANT:
				requestMap.put(getActionAttributeName(), valueToAssign);
				break;

			default:
				break;
			}
//		}
		
		LOG.debug( "Exiting from Apply Strategy");
	}
}
