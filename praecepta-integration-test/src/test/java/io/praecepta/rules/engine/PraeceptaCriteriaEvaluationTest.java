package io.praecepta.rules.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaCriteriaEvaluationTest {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaEvaluationTest.class);

	@Test
	public void testSimpleCondition() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		
		System.out.println(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, null));
	}
	
	@Test
	public void testSimpleConditionWithDerivedValues() {
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\r\n" + 
				"	\"emp\":\r\n" + 
				"	{\r\n" + 
				"		\"name\":\"Raja\",\r\n" + 
				"		\"company\":\"Raja\",\r\n" + 
				"		\"id\":25\r\n" + 
				"    }\r\n" + 
				"} ");
		
		PraeceptaSimpleCondition condition = null;
		
		// Get LHS Value from Attribute in the Json and RHS Value from Attribute in the Json that can Match
		condition = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, "emp.company", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));
		
		// Get LHS Value from Attribute in the Json and RHS as a Constant Value that doesn't Match
		condition = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "Srikar");
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertFalse(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));
		
		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that can Match
		condition = new PraeceptaSimpleCondition(null, "Raja", ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));
		
		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that doesn't Match
		condition = new PraeceptaSimpleCondition(null, "Srikar", ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertFalse(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));

		// Get LHS Value from Constant and RHS Value from Constant that can Match
		condition = new PraeceptaSimpleCondition(null, "XYZ", ConditionOperatorType.EQUAL_CHARS, null, "XYZ");
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));
		

		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that doesn't Match
		condition = new PraeceptaSimpleCondition(null, 10, ConditionOperatorType.EQUAL_NUMBER, "emp.id", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		assertFalse(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, ruleStore));
	}
	
	@Test
	public void testMultiCondition() {
		
		PraeceptaSimpleCondition condition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		PraeceptaSimpleCondition condition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 65));
		PraeceptaSimpleCondition condition3 = new PraeceptaSimpleCondition("company", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("zzz", "RSN"));
		
		
		condition1.setNextConditionInfo(JoinOperatorType.AND, condition2);
		condition2.setNextConditionInfo(JoinOperatorType.AND, condition3);
		
		PraeceptaMultiCondition multiCondion = new PraeceptaMultiCondition(condition1);
		
		System.out.println(PraeceptaCriteriaExecutor.evaluateAMultiCondition(multiCondion, null));
	}
	
	@Test
	public void testNestedMultiCondition() {
		
		// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 65));
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("company", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("zzz", "RSN"));
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
// Multi Condition 2 - (name = "xyz" or id = 90) ) 
		
		PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("xyz", "zyx"));
		PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.EQUAL_NUMBER, new ConditionValueHolder<Integer>(10, 90));
		
		secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

		PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
		
	// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90)
		
		firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
		
		PraeceptaMultiNestedCondition multNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
		
		System.out.println(PraeceptaCriteriaExecutor.evaluateAMultiNestedCondition(multNestedCondition, null));
	}
	
	@Test
	public void testACriteria() {
		
		PraeceptaMultiNestedCondition criteria = getMultiNestedCondition();
		
		System.out.println(PraeceptaCriteriaExecutor.stimulateARule(criteria, null));
	}
	
// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
	private static PraeceptaMultiNestedCondition getMultiNestedCondition() {
		// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
		
		// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
		
				PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
				PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 65));
				PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("company", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("zzz", "RSN"));
				
				
				firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
				firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
				
				PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
				
		// Multi Condition 2 - (name = "xyz" or id = 90) ) 
				
				PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("xyz", "zyx"));
				PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.EQUAL_NUMBER, new ConditionValueHolder<Integer>(10, 90));
				
				secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

				PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
				
			// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
				
				firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
				
				PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//				PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
	
		// Multi Condition 3 - (company = "abc" or zip = "12345")

				PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("company", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("abc", "cba"));
				PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("12345", "54321"));
				
				thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

				PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
				
		// Multi Condition 4 - (company = "efg" and location = "hyd")
				
				PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("company", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("efg", "efg"));
				PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("hyd", "hyd"));
				
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
