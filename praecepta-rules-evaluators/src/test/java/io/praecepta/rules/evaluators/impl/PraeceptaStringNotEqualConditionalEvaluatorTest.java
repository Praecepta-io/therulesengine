package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaStringNotEqualConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Srikar"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringNotEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithSameValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.NOT_EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringNotEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithSameValues "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithWrongConditionalOperator() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_DATE, new ConditionValueHolder<String>("Raja", "Srikar"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringNotEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongConditionalOperator "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<Integer>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringNotEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}

}
