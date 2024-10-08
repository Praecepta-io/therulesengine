package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaGreaterThanDateConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-MM-dd");
		metaData.put("toDateFormat", "yyyy-MM-dd");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022-10-11","2022-10-10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}
	
	@Test
	public void testPositive_DateWithTime() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("toDateFormat", "yyyy-mm-dd hh:mm:ss");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022-10-10 20:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}
	
	@Test
	public void testPositive_WIthDifferent_Formats() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy/mm/dd");
		metaData.put("toDateFormat", "yyyy-mm-dd");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022/10/11","2022-10-10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	@Test
	public void testPositive_DateWithOutToDateFormat() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022-10-10 10:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	@Test
	public void testPositive_DateTimeZone() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("toDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("fromTimeZone", "IST");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022-10-10 10:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-MM-dd");
		metaData.put("toDateFormat", "yyyy-MM-dd");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("2022-10-10","2022-10-11"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<Number>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateGreaterThanConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}

}
