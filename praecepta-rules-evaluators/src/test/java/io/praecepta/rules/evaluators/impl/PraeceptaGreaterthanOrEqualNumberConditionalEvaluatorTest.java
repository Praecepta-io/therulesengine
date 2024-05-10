package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaGreaterthanOrEqualNumberConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_EQUAL_NUMBER, new ConditionValueHolder<Number>(1000,100));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberGreaterThanEqualConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositiveWithSameValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_EQUAL_NUMBER, new ConditionValueHolder<Number>(100, 100));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberGreaterThanEqualConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_EQUAL_NUMBER, new ConditionValueHolder<Number>(99, 100));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberGreaterThanEqualConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_EQUAL_NUMBER, new ConditionValueHolder<String>("10", "20"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaNumberGreaterThanEqualConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}

}
