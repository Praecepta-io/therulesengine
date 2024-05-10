package io.praecepta.rules.model.filter;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaMultiConditionTest {

	@Test
	public void test() {

		// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("id", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 65));
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("company", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("zzz", "RSN"));
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
		System.out.println(" In JSON --> "+GsonHelper.toJsonPreserveNumber(firstSetMultiCondion));
	}

}
