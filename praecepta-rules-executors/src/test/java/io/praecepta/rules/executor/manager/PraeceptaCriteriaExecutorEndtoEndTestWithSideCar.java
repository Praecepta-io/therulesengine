package io.praecepta.rules.executor.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.engine.rule.sidecars.PraeceptaDefaultPreRuleSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enums.PraeceptaDefaultSideCarClazzType;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.formatter.PraeceptaFormatterTypeRegistry.PraeceptaFormatterType;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;
import io.praecepta.rules.sidecars.parser.PraeceptaParserTypeRegistry.PraeceptaParserType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaCriteriaExecutorEndtoEndTestWithSideCar {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaExecutorEndtoEndTestWithSideCar.class);
	
	private static final String RULE_NAME = "RULE_WITH_PREDICATE_ACTION_AND_SIDECAR";
	private static final String RULE_NAME_2 = "NEGATIVE_RULE_TEST";

	@Test
	public void testACriteriaWithoutSideCars() {
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\r\n" + 
				"	\"emp\":\r\n" + 
				"	{\r\n" + 
				"		\"name\":\"Raja\",\r\n" + 
				"		\"company\":\"Xploretch\",\r\n" + 
				"		\"id\":25\r\n" + 
				"    },\r\n" + 
				"	\"zip\":\"08512\",\r\n" + 
				"	\"outerCompany\":\"Xploretch\",\r\n" + 
				"	\"location\":\"Cranbury\"\r\n" + 
				"} ");
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		PraeceptaActionDetails successActionDetails2 = new PraeceptaActionDetails();
		
		successActionDetails2.setValueToAssign("Positive 2 With "+RULE_NAME);
		successActionDetails2.setActionAttributeName("successActionAttribute2");
		successActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails2);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		PraeceptaCriteriaExecutor.executeCriteria(ruleStore, criteria);
		
		String jsonAfterPredicateAndActionRun = ruleStore.fetchFromPraeceptaStore((PraeceptaRuleRequestStoreType.RULES_REQUEST)).toString();
		
		logger.info(jsonAfterPredicateAndActionRun);
		
		assertNotNull(jsonAfterPredicateAndActionRun);
		
		Map<String, Object> outputMap = GsonHelper.toMapEntityPreserveNumber(jsonAfterPredicateAndActionRun);
		
		assertNotNull(outputMap.get("successActionAttribute"));
		assertEquals("Positive With "+RULE_NAME, outputMap.get("successActionAttribute"));
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
		
		Assert.assertEquals(actionHolders.size(), 2);
		
//		System.out.println(stimulateARule(criteria, null));
	}
	
	@Test
	public void testANegetiveCriteriaWithoutSideCars() {
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\r\n" + 
				"	\"emp\":\r\n" + 
				"	{\r\n" + 
				"		\"name\":\"Raja\",\r\n" + 
				"		\"company\":\"Xploretch\",\r\n" + 
				"		\"id\":25\r\n" + 
				"    },\r\n" + 
				"	\"zip\":\"08512\",\r\n" + 
				"	\"outerCompany\":\"Xploretch\",\r\n" + 
				"	\"location\":\"Cranbury\"\r\n" + 
				"} ");
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME_2);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition2();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME_2);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		PraeceptaActionDetails successActionDetails2 = new PraeceptaActionDetails();
		
		successActionDetails2.setValueToAssign("Positive 2 With "+RULE_NAME_2);
		successActionDetails2.setActionAttributeName("successActionAttribute2");
		successActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails2);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME_2);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		PraeceptaCriteriaExecutor.executeCriteria(ruleStore, criteria);
		
		String jsonAfterPredicateAndActionRun = ruleStore.fetchFromPraeceptaStore((PraeceptaRuleRequestStoreType.RULES_REQUEST)).toString();
		
		logger.info(jsonAfterPredicateAndActionRun);
		
		assertNotNull(jsonAfterPredicateAndActionRun);
		
		Map<String, Object> outputMap = GsonHelper.toMapEntityPreserveNumber(jsonAfterPredicateAndActionRun);
		
		assertNotNull(outputMap.get("failedActionAttribute"));
		assertEquals("Negative With "+RULE_NAME_2, outputMap.get("failedActionAttribute"));
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
		
		Assert.assertEquals(actionHolders.size(), 1);
		
//		System.out.println(stimulateARule(criteria, null));
	}
	
	@Test
	public void testACriteriaWithSideCars() {
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\r\n" + 
				"	\"emp\":\r\n" + 
				"	{\r\n" + 
				"		\"name\":\"Raja\",\r\n" + 
				"		\"company\":\"Xploretch\",\r\n" + 
				"		\"id\":25\r\n" + 
				"    }\r\n" + 
				"} ");
		
		PraeceptaSideCarDataHolder<String, String> dataHolder = new PraeceptaSideCarDataHolder<>();
		dataHolder.addInput(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString());
		dataHolder.addOutput(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString());
		//(PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, dataHolder);
		
		// Adding Pre Rule Side Cars
		addPreRuleSidecar(ruleStore, RULE_NAME);
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		PraeceptaCriteriaExecutor.executeCriteria(ruleStore, criteria);
		
		String jsonAfterPredicateAndActionRun = ruleStore.fetchFromPraeceptaStore((PraeceptaRuleRequestStoreType.RULES_REQUEST)).toString();
		
		logger.info(jsonAfterPredicateAndActionRun);
		
		assertNotNull(jsonAfterPredicateAndActionRun);
		
		Map<String, Object> outputMap = GsonHelper.toMapEntityPreserveNumber(jsonAfterPredicateAndActionRun);
		
		assertNotNull(outputMap.get("successActionAttribute"));
		assertEquals("Positive With "+RULE_NAME, outputMap.get("successActionAttribute"));
		
		PraeceptaSideCarDataHolder<?, ?> dataHolderOutput = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);
		
		logger.info(dataHolderOutput.retriveOutput().toString());
		
		Map<String, PraeceptaActionHolder> actionHolders = 
				(Map<String, PraeceptaActionHolder>)ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		Assert.assertNotNull(actionHolders);
		
		Assert.assertEquals(actionHolders.size(), 1);
		
//		System.out.println(stimulateARule(criteria, null));
	}
	
	private void addPreRuleSidecar(PraeceptaRequestStore ruleStore, String ruleName) {

		
		Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars = new HashMap<>();
		
		List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars = new ArrayList<IPraeceptaInfoTrackerSideCarInjector>();
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector parserSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(getParserSideCarMetaData());
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector formatterSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(getFormatterSideCarMetaData());
		
		executionSideCars.add(parserSideCar);
		executionSideCars.add(formatterSideCar);
		executionSideCars.add(parserSideCar);
		
		ruleNameToExecutionSideCars.put(ruleName, executionSideCars);
		
		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleRunSideCar = new PraeceptaDefaultPreRuleSideCarInjector();
		preRuleRunSideCar.setRuleNameToExecutionSideCars(ruleNameToExecutionSideCars);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, preRuleRunSideCar);
		
	}
	
	private PraeceptaSideCarMetadata getFormatterSideCarMetaData() {
		
		PraeceptaSideCarMetadata formatterSideCarMetaData = new PraeceptaSideCarMetadata();
		formatterSideCarMetaData.setOrder(1);
		formatterSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.FORMATTER.name());
		formatterSideCarMetaData.setSideCarType(PraeceptaFormatterType.JSON.name());
		
		Map<String,Object> formatterSideCarInfo=new HashMap<>();
		
		formatterSideCarInfo.put("TEMPLATE_NAME", "JSON_FORMATTER_TEST");
		formatterSideCarInfo.put("FORMATTER_TEMPLATE", "{\r\n" + 
				"	\"emp\":\r\n" + 
				"	{\r\n" + 
				"		\"name\":\"${emp.name}\",\r\n" + 
				"		\"company\":\"${emp.company}\",\r\n" + 
				"		\"id\":${emp.id}\r\n" + 
				"    },\r\n" + 
				"	\"zip\":\"08512\",\r\n" + 
				"	\"outerCompany\":\"Xploretch\",\r\n" + 
				"	\"location\":\"Cranbury\"\r\n" + 
				"} ");
		
		formatterSideCarMetaData.setSideCarConfigs(formatterSideCarInfo);
		
		return formatterSideCarMetaData;
	}
	

	private PraeceptaSideCarMetadata getParserSideCarMetaData() {
		
		PraeceptaSideCarMetadata parserSideCarMetaData = new PraeceptaSideCarMetadata();
		parserSideCarMetaData.setOrder(1);
		parserSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.PARSER.name());
		parserSideCarMetaData.setSideCarType(PraeceptaParserType.JSON.name());
		
		Map<String, Object> sideCarMetadataMap=new HashMap<>();
		parserSideCarMetaData.setSideCarConfigs(sideCarMetadataMap);
		
		return parserSideCarMetaData;
	}

	// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
	private static PraeceptaMultiNestedCondition getMultiNestedCondition() {
		// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
		
		// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
		
				PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "Raja");
				PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 65);
				PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "RSN");
				
				
				firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
				firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
				
				PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
				
		// Multi Condition 2 - (name = "xyz" or id = 90) ) 
				
				PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
				PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 90);
				
				secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

				PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
				
			// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
				
				firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
				
				PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//				PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
	
		// Multi Condition 3 - (company = "abc" or zip = "12345")

				PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
				PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", null, ConditionOperatorType.EQUAL_CHARS, null, "08512");
				
				thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

				PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
				
		// Multi Condition 4 - (company = "efg" and location = "hyd")
				
				PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("outerCompany", null, ConditionOperatorType.EQUAL_CHARS, "emp.company", null);
				PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", null, ConditionOperatorType.EQUAL_CHARS, null, "Cranbury");
				
				fourthSetCondition1.setNextConditionInfo(JoinOperatorType.AND, fourthSetCondition2);

				PraeceptaMultiCondition fourthSetMultiCondion = new PraeceptaMultiCondition(fourthSetCondition1);
				
			// Prepare 2nd Set Multi Nested Condition - (company = "abc" or zip = "12345") or (company = "efg" and location = "hyd")	
				
				thirdSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, fourthSetMultiCondion);
				
				PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion);
//				PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
	
			// Final Nested Multi Condition 
				firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
				
				return firstSetMultNestedCondition;

	}
	
	// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
		private static PraeceptaMultiNestedCondition getMultiNestedCondition2() {
			// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
			
			// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
			
					PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "xxx");
					PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
					PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
					
					
					firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
					firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
					
					PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
					
			// Multi Condition 2 - (name = "xyz" or id = 90) ) 
					
					PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
					PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 6);
					
					secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

					PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
					
				// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
					
					firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
					
					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
		
			// Multi Condition 3 - (company = "abc" or zip = "12345")

					PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
					PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", null, ConditionOperatorType.EQUAL_CHARS, null, "08910");
					
					thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

					PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
					
			// Multi Condition 4 - (company = "efg" and location = "hyd")
					
					PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("outerCompany", null, ConditionOperatorType.EQUAL_CHARS, "emp.zip", null);
					PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", null, ConditionOperatorType.EQUAL_CHARS, null, "Woodbridge");
					
					fourthSetCondition1.setNextConditionInfo(JoinOperatorType.AND, fourthSetCondition2);

					PraeceptaMultiCondition fourthSetMultiCondion = new PraeceptaMultiCondition(fourthSetCondition1);
					
				// Prepare 2nd Set Multi Nested Condition - (company = "abc" or zip = "12345") or (company = "efg" and location = "hyd")	
					
					thirdSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, fourthSetMultiCondion);
					
					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion);
//					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
		
				// Final Nested Multi Condition 
					firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
					
					return firstSetMultNestedCondition;

		}
	

}
