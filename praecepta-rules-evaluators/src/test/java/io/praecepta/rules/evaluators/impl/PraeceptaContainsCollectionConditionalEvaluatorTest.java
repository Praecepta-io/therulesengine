package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaContainsCollectionConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.CONTAINS_IN_COLLECTION, new ConditionValueHolder<String>("Test4","Test3!#@#Test!#@#Test4"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaContainsInCollectionConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositive_multipleValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.CONTAINS_IN_COLLECTION, new ConditionValueHolder<String>("Test4!#@#Test3","Test3!#@#Test!#@#Test4"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaContainsInCollectionConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegative() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.CONTAINS_IN_COLLECTION, new ConditionValueHolder<String>("Test7","Test3!#@#Test!#@#Test4"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaContainsInCollectionConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.CONTAINS_IN_COLLECTION, new ConditionValueHolder<String>("Test!#@#Test7!#@#Test4","Test3!#@#Test!#@#Test4"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaContainsInCollectionConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.CONTAINS_IN_COLLECTION, new ConditionValueHolder<Integer>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaContainsInCollectionConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}

}
