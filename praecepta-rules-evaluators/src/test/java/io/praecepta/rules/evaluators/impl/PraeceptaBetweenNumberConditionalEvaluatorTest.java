package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaBetweenNumberConditionalEvaluatorTest {

	@Test
	public void testPositive() {
		Map<String,Object> metaData = new HashMap<>();
		metaData.put("endValue", 100.05);

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN, new ConditionValueHolder<Number>(100.03,100.02),metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithDIfferentValues() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("endValue", 100);
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN, new ConditionValueHolder<Number>(100.001, 99.001), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN, new ConditionValueHolder<String>("10", "20"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}

}
