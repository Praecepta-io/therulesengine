package io.praecepta.rules.model.filter;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaSimpleConditionTest {

	@Test
	public void test() {
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Srikar"));
		
		System.out.println(condition);
		
		System.out.println(" In JSON --> "+GsonHelper.toJson(condition));
	}

}
