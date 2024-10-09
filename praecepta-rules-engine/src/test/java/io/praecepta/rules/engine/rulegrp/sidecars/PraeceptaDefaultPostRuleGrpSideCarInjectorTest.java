package io.praecepta.rules.engine.rulegrp.sidecars;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.PraeceptaRuleResult;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

public class PraeceptaDefaultPostRuleGrpSideCarInjectorTest {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPostRuleGrpSideCarInjectorTest.class);

	private static final String RULE_NAME_1 = "RULE_WITH_POSITIVE_ACTIONS";
	private static final String RULE_NAME_2 = "RULE_WITH_NEGATIVE_ACTIONS";
	@Test
	public void test() {
		
		PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();

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
		

		String ruleGrpName = "testRuleGrpName";
		
		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		
		PraeceptaCriteria criteria1 = captureCriteria1(ruleStore);
		praeceptaCriterias.add(criteria1);
		
		PraeceptaCriteria criteria2 = captureCriteria2(ruleStore);
		praeceptaCriterias.add(criteria2);
		
		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup("Space1","ClNT1", "APP1");
		ruleGrpToUse.setRuleGroupName(ruleGrpName);
		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		
		List<PraeceptaRuleGroup> praeceptaRuleGrps = new ArrayList<>();
		praeceptaRuleGrps.add(ruleGrpToUse);
		
		ruleSpace.setPraeceptaRuleGrps(praeceptaRuleGrps);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE, ruleSpace);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME, ruleGrpName);
		
		PraeceptaCriteriaExecutor.executeCriteriasOfARuleGroup(ruleStore, ruleGrpToUse);
		
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCarInjector = new PraeceptaDefaultPostRuleGrpSideCarInjector();
		
		postRuleGrpSideCarInjector.captureInInfo(ruleStore);
		
		List<PraeceptaRuleResult> resultInfoForEveryRule = (List<PraeceptaRuleResult>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE);
		
		logger.info("Results Info For Every Rule -> {} ",resultInfoForEveryRule);
		
		Assert.assertNotNull(resultInfoForEveryRule);
		
	}
	
	private PraeceptaCriteria captureCriteria1(PraeceptaRequestStore ruleStore) {
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME_1);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME_1);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		PraeceptaActionDetails successActionDetails2 = new PraeceptaActionDetails();
		
		successActionDetails2.setValueToAssign("Positive 2 With "+RULE_NAME_1);
		successActionDetails2.setActionAttributeName("successActionAttribute2");
		successActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails2);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME_1);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		return criteria;
		
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

	private PraeceptaCriteria captureCriteria2(PraeceptaRequestStore ruleStore) {
		
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
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME_2);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		PraeceptaActionDetails failActionDetails2 = new PraeceptaActionDetails();
		
		failActionDetails2.setValueToAssign("Negative 2 With "+RULE_NAME_2);
		failActionDetails2.setActionAttributeName("failedActionAttribute2");
		failActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails2);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		return criteria;
		
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
//				PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
	
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
//				PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
	
			// Final Nested Multi Condition 
				firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
				
				return firstSetMultNestedCondition;

	}
	


}
