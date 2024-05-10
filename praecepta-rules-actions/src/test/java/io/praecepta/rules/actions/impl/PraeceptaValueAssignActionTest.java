package io.praecepta.rules.actions.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.enums.PraeceptaFactorActionType;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaValueAssignActionTest {

	@Test
	public void testValueAssign() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		//praeceptaValueAssignAction.setParameters(parameters);
		praeceptaValueAssignAction.setValueToAssign("test1");
		praeceptaValueAssignAction.setActionAttributeName("newAttribute");
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":\"val1\"}");
		//(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class
		praeceptaValueAssignAction.performAction(input);
		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		
		 // a new attribute with name newAttribute will be created and assigned with the value that is coming from Value to Assign Given above
		Assert.assertEquals(2, requestMap.size());
		Assert.assertNotNull(requestMap.get("newAttribute"));
		Assert.assertEquals("test1", requestMap.get("newAttribute"));
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
	}

	@Test
	public void testUpdateAValueOfAnAtttibuteByValueAssign() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		
		// In this case, Action attribute name is for which we wanted to assign a new Value
		praeceptaValueAssignAction.setActionAttributeName("attribute2");
		
		praeceptaValueAssignAction.setValueToAssign("UpdatedValue");
		
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":\"val1\", \"attribute2\":\"val2\"}");
		
		praeceptaValueAssignAction.performAction(input);
		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		
		// attribute2 will have an updated value that is coming from Value to Assign Given above
		Assert.assertEquals(2, requestMap.size());
		Assert.assertNotNull(requestMap.get("attribute2"));
		Assert.assertEquals("UpdatedValue", requestMap.get("attribute2")); 
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
	}
	
	@Test
	public void testUpdateAValueOfAnAttributeFromAnotherAttribute() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		
		String actionName = "UpdateAValueOfAnAttributeFromAnotherAttribute";
		
		// In this case, Get a Value from  an attribute [Source] and assign it to  - another attribute that exist in the input
		praeceptaValueAssignAction.setSourceValueAttributeName("attribute1");
		
		praeceptaValueAssignAction.setActionAttributeName("attribute2");
		
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		String actionMessage = "Get a Value from  an attribute [Source] and assign it to  - Action attribute name ";
		
		praeceptaValueAssignAction.setActionMessage(actionMessage);
		praeceptaValueAssignAction.setActionName(actionName);
		
		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":\"val1\", \"attribute2\":\"val2\"}");
		
		praeceptaValueAssignAction.performAction(input);
		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		
		// attribute2 will have an updated value that is coming from attribute1
		Assert.assertEquals(2, requestMap.size());
		Assert.assertNotNull(requestMap.get("attribute2"));
		Assert.assertEquals("val1", requestMap.get("attribute2")); 
		
		PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);
		
		Assert.assertNotNull(actionHolder);
		
		Assert.assertEquals(actionMessage, actionHolder.getActionResult().getActionResultStatusMessage());
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
		
		Assert.assertEquals(actionHolders.size(), 1);
		
		Assert.assertEquals(actionHolders.get(actionName), actionHolder);
		
	}
	
	@Test
	public void testAssignAValueToANewAttributeFromSourceAttribute() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		
		// In this case, Get a Value from  an attribute [Source] and assign it to a new Attribute defined with Action attribute name 
		praeceptaValueAssignAction.setSourceValueAttributeName("attribute1");
		
		praeceptaValueAssignAction.setActionAttributeName("attribute2");
		
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":\"val1\"}");
		
		praeceptaValueAssignAction.performAction(input);
		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		
		// a new attribute with name attribute2 will be created and assigned with the value from attribute1
		Assert.assertEquals(2, requestMap.size());
		Assert.assertNotNull(requestMap.get("attribute2"));
		Assert.assertEquals("val1", requestMap.get("attribute2")); 
		
		PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);
		
		Assert.assertNotNull(actionHolder);
		
		Assert.assertNotNull(actionHolder.getActionResult());
		
		Assert.assertNotNull(actionHolder.getActionResult().getActionResultStatusMessage());
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
	}
	
	@Test
	public void test_additionAction() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		
		Map<PraeceptaActionParametersType,Object> parameters = new HashMap<>();
		parameters.put(PraeceptaActionParametersType.FACTOR_ACTION_TYPE, PraeceptaFactorActionType.ADDITION.name());
		parameters.put(PraeceptaActionParametersType.FACTOR_VALUE, 20);
		praeceptaValueAssignAction.setParameters(parameters);
		
		praeceptaValueAssignAction.setSourceValueAttributeName("attribute1");
		praeceptaValueAssignAction.setActionAttributeName("newAttribute");
		
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":10}");
		//(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class
		praeceptaValueAssignAction.performAction(input);
		
//		Map<String,Object> requestMap = GsonHelper.toEntity(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
		Map<String,Object> requestMap = GsonHelper.toMapEntityPreserveNumber(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString());
		
		System.out.println(requestMap);
		
		PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);

		Assert.assertEquals(2, requestMap.size());
		Assert.assertEquals(30L, requestMap.get("newAttribute"));
		Assert.assertNotNull(actionHolder.getActionedValue());
		Assert.assertEquals(30, ((BigDecimal)actionHolder.getActionedValue()).intValue());
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
	}
	
	@Test
	public void test_subtractionAction() {
		
		PraeceptaAbstractAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
		
		Map<PraeceptaActionParametersType,Object> parameters = new HashMap<>();
		
		parameters.put(PraeceptaActionParametersType.FACTOR_ACTION_TYPE, PraeceptaFactorActionType.SUBTRACTION.name());
		parameters.put(PraeceptaActionParametersType.FACTOR_VALUE, 20);
		praeceptaValueAssignAction.setParameters(parameters);
		
		praeceptaValueAssignAction.setSourceValueAttributeName("attribute1");
		praeceptaValueAssignAction.setActionAttributeName("newAttribute");
		
		praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"attribute1\":10}");
		
		praeceptaValueAssignAction.performAction(input);
		
		Map<String,Object> requestMap = GsonHelper.toMapEntityPreserveNumber(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString());
		
		System.out.println(requestMap);
		
		PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);

		Assert.assertEquals(2, requestMap.size());
		Assert.assertEquals(-10L, requestMap.get("newAttribute"));
		Assert.assertNotNull(actionHolder.getActionedValue());
		Assert.assertEquals(-10, ((BigDecimal)actionHolder.getActionedValue()).intValue());
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
	}

}
