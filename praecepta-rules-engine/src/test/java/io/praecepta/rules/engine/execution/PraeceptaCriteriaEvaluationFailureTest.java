package io.praecepta.rules.engine.execution;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.evaluators.enums.ConditionOperatorToEvaluatorType;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaCriteriaEvaluationFailureTest {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaEvaluationFailureTest.class);

	@Test
	public void testMultiCondition() {
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition(null, "abc", ConditionOperatorType.EQUAL_CHARS, null, "xxx");
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition(null, 25, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition(null, "def", ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
		System.out.println(evaluateAMultiCondition(firstSetMultiCondion, null));
	}
	
	@Test
	public void testNestedMultiCondition() {
		
		PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "xxx");
		PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
		PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
		
		
		firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
		firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
		
		PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
		
// Multi Condition 2 - (name = "xyz" or id = 90) ) 
		
		PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
		PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 6);
		
		secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

		PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
		
	// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
		
		firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
		
		PraeceptaMultiNestedCondition multNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
		
		System.out.println(evaluateAMultiNestedCondition(multNestedCondition, null));
	}
	
	@Test
	public void testACriteria() {
		
		PraeceptaMultiNestedCondition criteria = getMultiNestedCondition();
		
		System.out.println(stimulateARule(criteria, null));
	}
	
// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
	private static PraeceptaMultiNestedCondition getMultiNestedCondition() {
			// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
			
			// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
			
					PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "xxx");
					PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
					PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
					
					
					firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
					firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
					
					PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
					
			// Multi Condition 2 - (name = "xyz" or id = 90) ) 
					
					PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
					PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 6);
					
					secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

					PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
					
				// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
					
					firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
					
					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
		
			// Multi Condition 3 - (company = "abc" or zip = "12345")

					PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
					PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", null, ConditionOperatorType.EQUAL_CHARS, null, "08910");
					
					thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

					PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
					
			// Multi Condition 4 - (company = "efg" and location = "hyd")
					
					PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("outerCompany", null, ConditionOperatorType.EQUAL_CHARS, "emp.zip", null);
					PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", null, ConditionOperatorType.EQUAL_CHARS, null, "Woodbridge");
					
					fourthSetCondition1.setNextConditionInfo(JoinOperatorType.AND, fourthSetCondition2);

					PraeceptaMultiCondition fourthSetMultiCondion = new PraeceptaMultiCondition(fourthSetCondition1);
					
				// Prepare 2nd Set Multi Nested Condition - (company = "abc" or zip = "12345") or (company = "efg" and location = "hyd")	
					
					thirdSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, fourthSetMultiCondion);
					
					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion);
//					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
		
				// Final Nested Multi Condition 
					firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
					
					return firstSetMultNestedCondition;


	}
	
	private static boolean evaluateASimpleCondition(PraeceptaSimpleCondition simpleCondition, PraeceptaRequestStore ruleStore) {
		
		boolean simpleConditionResult = false;
		
		logger.info(" Triggering a Simple Condition ");
		
		if (simpleCondition != null) {
			
			ConditionOperatorType conditionOperator = simpleCondition.getConditionOperator();

			// Get the Evaluator Based on the ConditionOperatorType
			IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> evaluator = ConditionOperatorToEvaluatorType
					.getEvaluatorByOperatorType(conditionOperator);

			if (evaluator != null) {
				
				// Build the Value Holder Here 
				buildConditionValueHolder(simpleCondition, ruleStore);

				simpleConditionResult = evaluator.evaluateTheCondition(simpleCondition);
			}
			
		}
		
		logger.info(" Simple Rule Result --> {} ", simpleConditionResult);
		
		return simpleConditionResult;
	}
	
	private static boolean evaluateAMultiCondition(PraeceptaMultiCondition multiCondition, PraeceptaRequestStore ruleStore) {
		
		boolean multiConditionResult = false;
		
		logger.info(" Triggering a Multi Condition ");
		
		if (multiCondition != null) {
			
			PraeceptaSimpleCondition conditionToEvaluate = multiCondition.getCondition();
			
			boolean previousSimpleConditionResult = evaluateASimpleCondition(conditionToEvaluate, ruleStore);

			while(conditionToEvaluate.getNextCondition() != null && conditionToEvaluate.getNextConditionJoinOperator() != null) {
				
				JoinOperatorType joinOperatorToUse = conditionToEvaluate.getNextConditionJoinOperator();
				conditionToEvaluate = conditionToEvaluate.getNextCondition();
				
				boolean currentSimpleConditionResult = evaluateASimpleCondition(conditionToEvaluate, ruleStore);
				
				previousSimpleConditionResult = statusWithJoinOperator(currentSimpleConditionResult, previousSimpleConditionResult, 
						joinOperatorToUse);
			}
			
			multiConditionResult = previousSimpleConditionResult;
			
		}
		logger.info(" Multi Condition Result --> {} ", multiConditionResult);
		
		return multiConditionResult;
	}
	
	private static boolean evaluateAMultiNestedCondition(PraeceptaMultiNestedCondition multiNestedCondition, PraeceptaRequestStore ruleStore) {
		
		logger.info(" Triggering a Multi Nested Condition ");
		
		boolean multiNestedConditionResult = false;
		
		if (multiNestedCondition != null) {
			
			PraeceptaMultiCondition multiConditionToEvaluate = multiNestedCondition.getMultiCondition();
			
			boolean previousMultiConditionResult = evaluateAMultiCondition(multiConditionToEvaluate, ruleStore);
			
			while(multiConditionToEvaluate.getNextMultiCondition() != null && multiConditionToEvaluate.getNextConditionJoinOperator() != null) {
				
				JoinOperatorType joinOperatorToUse = multiConditionToEvaluate.getNextConditionJoinOperator();
				multiConditionToEvaluate = multiConditionToEvaluate.getNextMultiCondition();
				
				boolean currentMultiConditionResult = evaluateAMultiCondition(multiConditionToEvaluate, ruleStore);
				
				previousMultiConditionResult = statusWithJoinOperator(currentMultiConditionResult, previousMultiConditionResult, 
						joinOperatorToUse);
			}
			
			multiNestedConditionResult = previousMultiConditionResult;
			
		}
		logger.info(" Nested Multi Condition Result --> {} ", multiNestedConditionResult);
		
		return multiNestedConditionResult;
	}
	
	private static boolean stimulateARule(PraeceptaMultiNestedCondition multiNestedCondition, PraeceptaRequestStore ruleStore) {
		
		logger.info(" Triggering a Criteria ");
		
		boolean criteriaResult = false;
		
		if (multiNestedCondition != null) {
			
			PraeceptaMultiNestedCondition multiNestedConditionToEvaluate = multiNestedCondition;
			
			boolean previousMultiNestedConditionResult = evaluateAMultiNestedCondition(multiNestedConditionToEvaluate, ruleStore);
			
			while(multiNestedConditionToEvaluate.getNextMultiNestedCondition() != null && multiNestedConditionToEvaluate.getNextConditionJoinOperator() != null) {
				
				JoinOperatorType joinOperatorToUse = multiNestedConditionToEvaluate.getNextConditionJoinOperator();
				multiNestedConditionToEvaluate = multiNestedConditionToEvaluate.getNextMultiNestedCondition();
				
				boolean currentMultiNestedConditionResult = evaluateAMultiNestedCondition(multiNestedConditionToEvaluate, ruleStore);
				
				previousMultiNestedConditionResult = statusWithJoinOperator(currentMultiNestedConditionResult, previousMultiNestedConditionResult, 
						joinOperatorToUse);
			}
			
			criteriaResult = previousMultiNestedConditionResult;
			
		}
		logger.info(" Criteria Result --> {} ", criteriaResult);
		
		return criteriaResult;
		
	}
	
	private static boolean statusWithJoinOperator(boolean currentConditionResult,
			boolean previousConditionResult, JoinOperatorType joinOperatorForACondition) {
		
		logger.info(" Join Operator - {} ", joinOperatorForACondition);
		
		logger.info(" Performing a Condition Evaluation for currentConditionResult - {} and previousConditionResult {} "
				, currentConditionResult, previousConditionResult);
		
		boolean statusAfterJoinOperator = true; 
		
		if(JoinOperatorType.AND == joinOperatorForACondition) {
			statusAfterJoinOperator = previousConditionResult && currentConditionResult;
		} else if (JoinOperatorType.OR == joinOperatorForACondition) {
			statusAfterJoinOperator = previousConditionResult || currentConditionResult;
		}
		
		return statusAfterJoinOperator;
	}
	
	@SuppressWarnings("unused")
	public static void buildConditionValueHolder(PraeceptaSimpleCondition condition, PraeceptaRequestStore ruleStore){
		
		logger.info(" Building Condition value Holder ");
		
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			Object ruleRequest = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleRequest)) {
				
				Map<String,Object> requestMap = 
						GsonHelper.toMapEntityPreserveNumber(ruleRequest.toString());
				
				// LHS value will comes from the Subject or Attribute name that we need to Compare  
				logger.info(" Getting LHS Value ");
				
				Object fromValue = deriveLhsValue(condition, requestMap);
				
				logger.info(" LHS Value - {}", fromValue);
				
				// RHS value will comes from the Attribute name provided in the nested loop or a static value assigned in the Condition 
				logger.info(" Getting RHS Value ");
				
				Object toValue = deriveRhsValue(condition, requestMap);
				
				logger.info(" RHS Value - {}", toValue);
				
				ConditionValueHolder conditionValueHolder = new ConditionValueHolder(fromValue, toValue);
				
				condition.setConditionValueHolder(conditionValueHolder);
			
			}
		}
		
//		return conditionValueHolder;
	}

	public static Object deriveLhsValue(PraeceptaSimpleCondition condition, Map<String, Object> requestMap) {
		logger.info(" Capturing LHS Value ");
		
		Object fromValue = null;
		
		if(condition.getSubjectValue() != null){
			
			fromValue = condition.getSubjectValue();
			
		} else {
		
			fromValue = getValueUsingAttributeName(condition.getSubjectName(), requestMap);
			
		}
		
		logger.info(" LHS Value Captured - {} ", fromValue);
		return fromValue;
	}
	
	public static Object getValueUsingAttributeName(String attributeName, Map<String, Object> requestMap) {
		
		logger.info(" Getting Value for Attribute Name - {} ", attributeName);
		Object valueToReturn = null; 
		
		if(!PraeceptaObjectHelper.isObjectEmpty(attributeName)) {
			
			if(attributeName.contains(".")){
				valueToReturn = getValueForNestedAttribute(attributeName, requestMap);
			}else {
				valueToReturn = requestMap.get(attributeName);
			}
		}
		
		return valueToReturn;
	}

	private static Object deriveRhsValue(PraeceptaSimpleCondition condition, Map<String, Object> requestMap) {
		logger.info(" Capturing RHS Value ");
		
		Object toValue = null;
		
		if(condition.getValueToCompare() != null){
			
			toValue = condition.getValueToCompare();
			
		} else {
			
			toValue = getValueUsingAttributeName(condition.getDefinedAttributeToCompare(), requestMap);
			
		}
		
		logger.info(" RHS Value Captured - {} ", toValue);
		return toValue;
	}
	
	public static Object getValueForNestedAttribute(String condition, Map<String, Object> requestMap) {
		
		Object valueToReturn = null; 
		
		logger.debug(" RequestMap {}" , requestMap);
		logger.debug(" Condition {}" , condition);
		
		String[] st = condition.split("\\.");
		for (int i = 0; i < st.length; i++) {
			if (i != st.length - 1)
				requestMap = (Map<String, Object>) requestMap.get(st[i]);
			else
				valueToReturn = requestMap.get(st[i]);
			if (requestMap == null)
				break;
		}
		
		logger.debug(" Derived Value {}", valueToReturn); 
		
		return valueToReturn;
	}

}
