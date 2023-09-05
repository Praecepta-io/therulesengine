package io.praecepta.rules.model.filter;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaMultiConditionTest {

	@Test
	public void test() {

		PraeceptaSimpleCondition condition1 = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Srikar"));
		PraeceptaSimpleCondition condition2 = new PraeceptaSimpleCondition("attr2", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 20));
		PraeceptaSimpleCondition condition3 = new PraeceptaSimpleCondition("attr3", ConditionOperatorType.NOT_EMPTY, new ConditionValueHolder<String>("Rao", null));
		PraeceptaSimpleCondition condition4 = new PraeceptaSimpleCondition("attr4", ConditionOperatorType.MATCHING_COLLECTION, new ConditionValueHolder<String>("[2, 3,4]", "[1, 2, 3,4]"));
		
		
		PraeceptaMultiCondition condition4ToJoin = new PraeceptaMultiCondition(condition4);
		PraeceptaMultiCondition condition3ToJoin = new PraeceptaMultiCondition(condition3, JoinOperatorType.AND, condition4ToJoin);
		PraeceptaMultiCondition condition2ToJoin = new PraeceptaMultiCondition(condition2, JoinOperatorType.AND, condition3ToJoin);
		
		PraeceptaMultiCondition finalMultiCondition = new PraeceptaMultiCondition(condition1, JoinOperatorType.OR, condition2ToJoin);
		
		System.out.println(finalMultiCondition);
		
		System.out.println(" In JSON --> "+GsonHelper.toJson(finalMultiCondition));
	}

}
