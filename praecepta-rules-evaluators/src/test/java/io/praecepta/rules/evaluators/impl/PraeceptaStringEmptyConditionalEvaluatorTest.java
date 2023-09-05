package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaStringEmptyConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EMPTY, new ConditionValueHolder<String>("",""));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEmptyEval = new PraeceptaStringEmptyConditionalEvaluator<>();
		
		strEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EMPTY, new ConditionValueHolder<String>("Raja", "Raja"));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEmptyEval = new PraeceptaStringEmptyConditionalEvaluator<>();
		
		strEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.EMPTY, new ConditionValueHolder<Integer>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strEmptyEval = new PraeceptaStringEmptyConditionalEvaluator<>();
		
		strEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertNotEquals(conditionEvalResult, true);
	}

}
