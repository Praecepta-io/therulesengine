package io.praecepta.rules.evaluators.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaBetweenDateConditionalEvaluatorTest {

	@Test
	public void testPositive() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-MM-dd");
		metaData.put("toDateFormat", "yyyy-MM-dd");
		metaData.put("endDate", "2022-10-15");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-11","2022-10-10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositive_DateWithTime() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("toDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("endDate", "2022-10-10 10:10:12");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-10 10:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositive_WIthDifferent_Formats() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy/mm/dd");
		metaData.put("toDateFormat", "yyyy-mm-dd");
		metaData.put("endDate", "2022-10-12");
		metaData.put("endDateFromat", "yyyy-mm-dd");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022/10/11","2022-10-10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositive_DateWithOutToDateFormat() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("endDate", "2022-10-10 10:10:15");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-10 10:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testPositive_DateTimeZone() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("toDateFormat", "yyyy-mm-dd hh:mm:ss");
		metaData.put("fromTimeZone", "IST");
		metaData.put("endDate", "2022-10-11 10:10:10");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-10 10:10:11","2022-10-10 10:10:10"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testPositive "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, true);
	}
	
	@Test
	public void testNegativeWithDifferentValues() {

		Map<String,Object> metaData = new HashMap<>();
		metaData.put("fromDateFormat", "yyyy-MM-dd");
		metaData.put("toDateFormat", "yyyy-MM-dd");
		metaData.put("endDate", "2022-10-10");
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<String>("2022-10-10","2022-10-09"), metaData);
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithDifferentValues "+conditionEvalResult);
		
		assertEquals(conditionEvalResult, false);
	}
	
	
	@Test
	public void testNegativeWithWrongDataType() {
		
		PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("attr1", ConditionOperatorType.BETWEEN_DATE, new ConditionValueHolder<Number>(10, 20));
		
		IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> strNotEmptyEval = new PraeceptaDateBetweenConditionalEvaluator<>();
		
		strNotEmptyEval.validateInput(condition);
		
		boolean conditionEvalResult = strNotEmptyEval.evaluateTheCondition(condition);
		
		System.out.println("condition Eval Result for testNegativeWithWrongDataType "+conditionEvalResult);
		
		assertEquals(false,conditionEvalResult);
	}

}
