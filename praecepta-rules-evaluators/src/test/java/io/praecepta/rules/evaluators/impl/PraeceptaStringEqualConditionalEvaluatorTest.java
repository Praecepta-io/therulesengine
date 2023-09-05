package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaStringEqualConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Raja"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Srikar"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithNullValue() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>(null, "Srikar"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithWrongConditionalOperator() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_DATE, new ConditionValueHolder<String>("Raja", "Srikar"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongConditionalOperator "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<Integer>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEqualsEval = new PraeceptaStringEqualConditionalEvaluator<>();
		
		strEqualsEval.validateInput(condition);
		
		boolean conditionEvalResult = strEqualsEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}

}
