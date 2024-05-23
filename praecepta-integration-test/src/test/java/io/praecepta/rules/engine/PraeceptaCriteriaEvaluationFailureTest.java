package io.praecepta.rules.engine;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaCriteriaEvaluationFailureTest {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaEvaluationFailureTest.class);

	@Test
	public void testMultiCondition() {
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition(null, "abc", ConditionOperatorType.EQUAL_CHARS, null, "xxx");
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition(null, 25, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition(null, "def", ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
		System.out.println(PraeceptaCriteriaExecutor.evaluateAMultiCondition(firstSetMultiCondion, null));
	}
	
	@Test
	public void testNestedMultiCondition() {
		
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
