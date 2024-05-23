package io.praecepta.rules.conditions;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaMultiConditionTest {

	@Test
	public void testOnelevel() {

		// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 65));
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("company", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("zzz", "RSN"));
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
		System.out.println(" In JSON --> "+GsonHelper.toJsonPreserveNumber(firstSetMultiCondion));
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateAMultiCondition(firstSetMultiCondion, null));
	}
	
	@Test
	public void testTwoLevels() {
		
		// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) )
		
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
		
		System.out.println(" In JSON --> "+GsonHelper.toJsonPreserveNumber(firstSetMultiCondion));
		assertTrue(PraeceptaCriteriaExecutor.evaluateAMultiCondition(firstSetMultiCondion, null));
	}
	
	@Test
	public void testMultiConditionNegative() {
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition(null, "abc", ConditionOperatorType.EQUAL_CHARS, null, "xxx");
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition(null, 25, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition(null, "def", ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
		assertFalse(PraeceptaCriteriaExecutor.evaluateAMultiCondition(firstSetMultiCondion, null));
	}

}
