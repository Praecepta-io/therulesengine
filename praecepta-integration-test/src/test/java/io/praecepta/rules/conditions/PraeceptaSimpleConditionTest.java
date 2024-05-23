package io.praecepta.rules.conditions;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.MetaDataAttributes;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaSimpleConditionTest {

	@Test
	public void test() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateASimpleCondition(condition, null));
		
		Map<String,Object> metaData = new HashMap<>();
		metaData.put(MetaDataAttributes.FROM_DATE_FORMAT.getDescription(), "yyyy-MM-dd");
		metaData.put(MetaDataAttributes.TO_DATE_FORMAT.getDescription(), "yyyy-MM-dd");
		metaData.put(MetaDataAttributes.MIDDLE_DATE.getDescription(), "2022-10-15");
		PraeceptaSimpleCondition dateCondition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-11","2022-10-20"), metaData);
		
		assertTrue(PraeceptaCriteriaExecutor.evaluateASimpleCondition(dateCondition, null));
	}

}
