package io.praecepta.rules.engine.execution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.engine.rule.sidecars.PraeceptaDefaultPostRuleSideCarInjector;
import io.praecepta.rules.engine.rule.sidecars.PraeceptaDefaultPreRuleSideCarInjector;
import io.praecepta.rules.engine.rulegrp.sidecars.PraeceptaDefaultPostRuleGrpSideCarInjector;
import io.praecepta.rules.engine.rulegrp.sidecars.PraeceptaDefaultPreRuleGrpSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

public class PraeceptaCriteriaExecutorTest {

	@Test
	public void test() {
		
		String ruleGrpName = "testRuleGrpName";
		
		GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCarInjector = new PraeceptaDefaultPreRuleGrpSideCarInjector();
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCarInjector = new PraeceptaDefaultPostRuleGrpSideCarInjector();

		GenericPraeceptaInfoTrackerSideCarInjector preRuleSideCarInjector = new PraeceptaDefaultPreRuleSideCarInjector();
		GenericPraeceptaInfoTrackerSideCarInjector postRuleSideCarInjector = new PraeceptaDefaultPostRuleSideCarInjector();
		List<IPraeceptaInfoTrackerSideCarInjector> beforeExecutionSideCars = Arrays.asList(new GenericPraeceptaInfoTrackerSideCarInjector(), new GenericPraeceptaInfoTrackerSideCarInjector());

		List<IPraeceptaInfoTrackerSideCarInjector> afterExecutionSideCars = Arrays.asList(new GenericPraeceptaInfoTrackerSideCarInjector(), new GenericPraeceptaInfoTrackerSideCarInjector());
	
		/*preRuleGrpSideCarInjector.setBeforeExecutionSideCars(beforeExecutionSideCars);
		preRuleGrpSideCarInjector.setAfterExecutionSideCars(afterExecutionSideCars);
		preRuleSideCarInjector.setBeforeExecutionSideCars(beforeExecutionSideCars);
		postRuleSideCarInjector.setBeforeExecutionSideCars(beforeExecutionSideCars);*/
		PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();
		
		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup("Space1","ClNT1", "APP1");
		ruleGrpToUse.setRuleGroupName(ruleGrpName);
		
		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		
		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		criteria1.setPredicates(getMultiNestedCondition());
		
		PraeceptaCriteria criteria2 = new PraeceptaCriteria();
		criteria2.setPredicates(getMultiNestedCondition());
		
		praeceptaCriterias.add(criteria1);
		praeceptaCriterias.add(criteria2);

		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		
		List<PraeceptaRuleGroup> praeceptaRuleGrps = new ArrayList<>();
		praeceptaRuleGrps.add(ruleGrpToUse);
		
		ruleSpace.setPraeceptaRuleGrps(praeceptaRuleGrps);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE, ruleSpace);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME, ruleGrpName);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_SIDE_CAR, preRuleGrpSideCarInjector);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_SIDE_CAR, postRuleGrpSideCarInjector);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS, true);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"name\":\"vara\"}");
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, preRuleSideCarInjector);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR, postRuleSideCarInjector);
		
		PraeceptaCriteriaExecutor.executeCriteriasOfARuleGroup(ruleStore, ruleGrpToUse);

		Assert.assertNotNull(ruleStore);
	}
	@Test
	public void test2(){
		PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();

		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup("Space1","ClNT1", "APP1");

		String ruleGrpName = "testRuleGrpName";
		ruleGrpToUse.setRuleGroupName(ruleGrpName);

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"emp\":{\"name\":\"vara\"}}");

		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		PraeceptaCriteria criteria1 = new PraeceptaCriteria();


		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("emp.name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("vara", ""));
		condition.setDefinedAttributeToCompare("emp.name");
		PraeceptaMultiNestedCondition praeceptaMultiNestedCondition = new PraeceptaMultiNestedCondition(new PraeceptaMultiCondition(condition));

		criteria1.setPredicates(praeceptaMultiNestedCondition);
		
		PraeceptaActionDetails actionToPerform = new PraeceptaActionDetails();
		actionToPerform.setValueToAssign("20");
		actionToPerform.setActionAttributeName("test");
		actionToPerform.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		List<PraeceptaActionDetails> actions = new ArrayList<>();
		actions.add(actionToPerform);
		criteria1.setActionToPerform(actions);

		praeceptaCriterias.add(criteria1);
		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);


		PraeceptaCriteriaExecutor.executeCriteriasOfARuleGroup(ruleStore, ruleGrpToUse);
		Assert.assertNotNull(ruleStore);
		Assert.assertNotNull(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES));
		List<PraeceptaRequestStore> criteriaRequestStores = (List<PraeceptaRequestStore>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES);
		Assert.assertEquals(1, criteriaRequestStores.size());
		String request = (String)criteriaRequestStores.get(0).fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
		Map<String,Object> updatedRequestMap = GsonHelper.toEntity(request, Map.class);
		Assert.assertNotNull(updatedRequestMap.get("test"));
		Assert.assertEquals("20", updatedRequestMap.get("test"));
	}


	@Test
	public void test3_invalidEvaluator(){
		PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();

		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup("Space1","ClNT1", "APP1");

		String ruleGrpName = "testRuleGrpName";
		ruleGrpToUse.setRuleGroupName(ruleGrpName);

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"emp\":{\"name\":\"vara\"}}");

		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		PraeceptaCriteria criteria1 = new PraeceptaCriteria();


		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("emp.name", null, new ConditionValueHolder<String>("vara", ""));
		condition.setDefinedAttributeToCompare("emp.name");
		PraeceptaMultiNestedCondition praeceptaMultiNestedCondition = new PraeceptaMultiNestedCondition(new PraeceptaMultiCondition(condition));

		criteria1.setPredicates(praeceptaMultiNestedCondition);
		
		PraeceptaActionDetails actionToPerform = new PraeceptaActionDetails();
		actionToPerform.setValueToAssign("20");
		actionToPerform.setActionAttributeName("test");
		actionToPerform.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		List<PraeceptaActionDetails> actions = new ArrayList<>();
		actions.add(actionToPerform);
		criteria1.setActionToPerform(actions);

		praeceptaCriterias.add(criteria1);
		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);


		PraeceptaCriteriaExecutor.executeCriteriasOfARuleGroup(ruleStore, ruleGrpToUse);
		Assert.assertNotNull(ruleStore);
		Assert.assertNotNull(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES));
		List<PraeceptaRequestStore> criteriaRequestStores = (List<PraeceptaRequestStore>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES);
		Assert.assertEquals(1, criteriaRequestStores.size());
		String request = (String)criteriaRequestStores.get(0).fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
		Map<String,Object> updatedRequestMap = GsonHelper.toEntity(request, Map.class);
		Assert.assertNull(updatedRequestMap.get("test"));
	}
	
	private static PraeceptaMultiNestedCondition getMultiNestedCondition() {
		
		// Multi Condition 1
				PraeceptaSimpleCondition n1condition1 = new PraeceptaSimpleCondition("n1Attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
				PraeceptaSimpleCondition n1condition2 = new PraeceptaSimpleCondition("n1Attr2", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 20));
				PraeceptaSimpleCondition n1condition3 = new PraeceptaSimpleCondition("n1Attr3", ConditionOperatorType.NOT_EMPTY, new ConditionValueHolder<String>("Rao", null));
				PraeceptaSimpleCondition n1condition4 = new PraeceptaSimpleCondition("n1Attr4", ConditionOperatorType.MATCHING_COLLECTION, new ConditionValueHolder<String>("[2, 3,4]", "[2, 3,4]"));
				
				
				PraeceptaMultiCondition n1condition4ToJoin = new PraeceptaMultiCondition(n1condition4);
				PraeceptaMultiCondition n1condition3ToJoin = new PraeceptaMultiCondition(n1condition3, JoinOperatorType.AND, n1condition4ToJoin);
				PraeceptaMultiCondition n1condition2ToJoin = new PraeceptaMultiCondition(n1condition2, JoinOperatorType.AND, n1condition3ToJoin);
				
				PraeceptaMultiCondition n1MultiCondition = new PraeceptaMultiCondition(n1condition1, JoinOperatorType.OR, n1condition2ToJoin);
				
				System.out.println(n1MultiCondition);
				
				// Multi Condition 2
				PraeceptaSimpleCondition n2condition1 = new PraeceptaSimpleCondition("n2Attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("10-MAR-2021", "09-MAY-2022"));
				n2condition1.setComparingFormat("dd-MMM-yyyy");
				
				PraeceptaSimpleCondition n2condition2 = new PraeceptaSimpleCondition("n2Attr2", ConditionOperatorType.NOT_MATCHING_COLLECTION, new ConditionValueHolder<String>("[2, 3,4]", "[11, 100]"));
				PraeceptaSimpleCondition n2condition3 = new PraeceptaSimpleCondition("n2Attr3", ConditionOperatorType.EMPTY, new ConditionValueHolder<String>(null, null));
				
				
				PraeceptaMultiCondition n2condition3ToJoin = new PraeceptaMultiCondition(n2condition3);
				PraeceptaMultiCondition n2condition2ToJoin = new PraeceptaMultiCondition(n2condition2, JoinOperatorType.AND, n2condition3ToJoin);
				
				PraeceptaMultiCondition n2MultiCondition = new PraeceptaMultiCondition(n2condition1, JoinOperatorType.OR, n2condition2ToJoin);
				
				System.out.println(n2MultiCondition);
				
				// Multi Condition 3
				PraeceptaSimpleCondition n3condition1 = new PraeceptaSimpleCondition("n3Attr1", ConditionOperatorType.EQUAL_DATE, new ConditionValueHolder<String>("10-17-2021", "06-23-2022"));
				n3condition1.setComparingFormat("MM-dd-yyyy");
				
				PraeceptaSimpleCondition n3condition2 = new PraeceptaSimpleCondition("n3Attr2", ConditionOperatorType.EQUAL_NUMBER, new ConditionValueHolder<Integer>(10, 10));
				PraeceptaSimpleCondition n3condition3 = new PraeceptaSimpleCondition("n3Attr3", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("XYZ", "XYZ"));
				PraeceptaSimpleCondition n3condition4 = new PraeceptaSimpleCondition("n3Attr4", ConditionOperatorType.NOT_EQUAL_NUMBER, new ConditionValueHolder<Integer>(5, 1));
				
				
				PraeceptaMultiCondition n3condition4ToJoin = new PraeceptaMultiCondition(n3condition4);
				PraeceptaMultiCondition n3condition3ToJoin = new PraeceptaMultiCondition(n3condition3, JoinOperatorType.OR, n3condition4ToJoin);
				PraeceptaMultiCondition n3condition2ToJoin = new PraeceptaMultiCondition(n3condition2, JoinOperatorType.OR, n3condition3ToJoin);
				
				PraeceptaMultiCondition n3MultiCondition = new PraeceptaMultiCondition(n3condition1, JoinOperatorType.AND, n3condition2ToJoin);
				
				System.out.println(n3MultiCondition);
				
				
				PraeceptaMultiNestedCondition n3NestedMultCondition = new PraeceptaMultiNestedCondition(n3MultiCondition);
				PraeceptaMultiNestedCondition n2NestedMultCondition = new PraeceptaMultiNestedCondition(n2MultiCondition, JoinOperatorType.OR, n3NestedMultCondition);
				PraeceptaMultiNestedCondition finalNestedMultCondition = new PraeceptaMultiNestedCondition(n1MultiCondition, JoinOperatorType.AND, n2NestedMultCondition);
				
				return finalNestedMultCondition;

	}

}
