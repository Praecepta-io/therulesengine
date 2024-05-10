package io.praecepta.rules.evaluators.enums;

import java.util.EnumMap;
import java.util.Map;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaContainsInCollectionConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateBetweenConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateGreaterThanConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateGreaterThanEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateLessThanConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaDateLessThanEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaEmptyConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaMatchingCollectionConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNotContainsInCollectionConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNotEmptyConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNotMatchingCollectionConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberBetweenConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberGreaterThanConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberGreaterThanEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberLessThanConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberLessThanEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberNotBetweenConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaNumberNotEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringEndsWithConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringLikeConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringNotEndsWithConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringNotEqualConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringNotLikeConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringNotStartsWithConditionalEvaluator;
import io.praecepta.rules.evaluators.impl.PraeceptaStringStartsWithConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
/**
 * 
 * @author rajasrikar
 *
 */
/**
 * 
 * 
	 CONTAINS_IN_COLLECTION, 
	NOT_CONTAINS_IN_COLLECTION, 
	MATCHING_COLLECTION, NOT_MATCHING_COLLECTION,
	 *
 */
public enum ConditionOperatorToEvaluatorType {

	//Simple Checks
	EMPTY_EVLUATOR(ConditionOperatorType.EMPTY , new PraeceptaEmptyConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_EMPTY_EVLUATOR(ConditionOperatorType.NOT_EMPTY, new PraeceptaNotEmptyConditionalEvaluator<PraeceptaSimpleCondition>()),
	// Strings
	EQUAL_CHARS_EVLUATOR(ConditionOperatorType.EQUAL_CHARS, new PraeceptaStringEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_EQUAL_CHARS_EVLUATOR(ConditionOperatorType.NOT_EQUAL_CHARS, new PraeceptaStringNotEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	STARTS_WITH_EVLUATOR(ConditionOperatorType.STARTS_WITH, new PraeceptaStringStartsWithConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_STARTS_WITH_EVLUATOR(ConditionOperatorType.NOT_STARTS_WITH, new PraeceptaStringNotStartsWithConditionalEvaluator<PraeceptaSimpleCondition>()),
	ENDS_WITH_EVLUATOR(ConditionOperatorType.ENDS_WITH, new PraeceptaStringEndsWithConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_ENDS_WITH_EVLUATOR(ConditionOperatorType.NOT_ENDS_WITH, new PraeceptaStringNotEndsWithConditionalEvaluator<PraeceptaSimpleCondition>()),
	LIKE_EVLUATOR(ConditionOperatorType.LIKE, new PraeceptaStringLikeConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_LIKE_EVLUATOR(ConditionOperatorType.NOT_LIKE, new PraeceptaStringNotLikeConditionalEvaluator<PraeceptaSimpleCondition>()),
	// Numbers
	EQUAL_NUMBER_EVLUATOR(ConditionOperatorType.EQUAL_NUMBER, new PraeceptaNumberEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_EQUAL_NUMBER_EVLUATOR(ConditionOperatorType.NOT_EQUAL_NUMBER, new PraeceptaNumberNotEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	LESS_THAN_NUMBER_EVLUATOR(ConditionOperatorType.LESS_THAN_NUMBER, new PraeceptaNumberLessThanConditionalEvaluator<PraeceptaSimpleCondition>()),
	LESS_THAN_EQUAL_NUMBER_EVLUATOR(ConditionOperatorType.LESS_THAN_EQUAL_NUMBER, new PraeceptaNumberLessThanEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	GREATER_THAN_NUMBER_EVLUATOR(ConditionOperatorType.GREATER_THAN_NUMBER, new PraeceptaNumberGreaterThanConditionalEvaluator<PraeceptaSimpleCondition>()),
	GREATER_THAN_EQUAL_NUMBER_EVLUATOR(ConditionOperatorType.GREATER_THAN_EQUAL_NUMBER, new PraeceptaNumberGreaterThanEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	BETWEEN_NUMBER_EVLUATOR(ConditionOperatorType.BETWEEN, new PraeceptaNumberBetweenConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_BETWEEN_NUMBER_EVLUATOR(ConditionOperatorType.NOT_BETWEEN, new PraeceptaNumberNotBetweenConditionalEvaluator<PraeceptaSimpleCondition>()),
	//Dates
	EQUAL_DATE_EVLUATOR(ConditionOperatorType.EQUAL_DATE, new PraeceptaDateEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	BETWEEN_DATE_EVLUATOR(ConditionOperatorType.BETWEEN_DATE, new PraeceptaDateBetweenConditionalEvaluator<PraeceptaSimpleCondition>()),
	GREATER_THAN_DATE_EVLUATOR(ConditionOperatorType.GREATER_THAN_DATE, new PraeceptaDateGreaterThanConditionalEvaluator<PraeceptaSimpleCondition>()),
	GREATER_THAN_EQUAL_DATE_EVLUATOR(ConditionOperatorType.GREATER_THAN_EQUAL_DATE, new PraeceptaDateGreaterThanEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	LESS_THAN_DATE_EVLUATOR(ConditionOperatorType.LESS_THAN_DATE, new PraeceptaDateLessThanConditionalEvaluator<PraeceptaSimpleCondition>()),
	LESS_THAN_EQUAL_DATE_EVLUATOR(ConditionOperatorType.LESS_THAN_EQUAL_DATE, new PraeceptaDateLessThanEqualConditionalEvaluator<PraeceptaSimpleCondition>()),
	// Collections
	CONTAINS_IN_COLLECTION_EVLUATOR(ConditionOperatorType.CONTAINS_IN_COLLECTION, new PraeceptaContainsInCollectionConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_CONTAINS_IN_COLLECTION_EVLUATOR(ConditionOperatorType.NOT_CONTAINS_IN_COLLECTION, new PraeceptaNotContainsInCollectionConditionalEvaluator<PraeceptaSimpleCondition>()),
	MATCHING_COLLECTION_EVLUATOR(ConditionOperatorType.MATCHING_COLLECTION, new PraeceptaMatchingCollectionConditionalEvaluator<PraeceptaSimpleCondition>()),
	NOT_MATCHING_COLLECTION_EVLUATOR(ConditionOperatorType.NOT_MATCHING_COLLECTION, new PraeceptaNotMatchingCollectionConditionalEvaluator<PraeceptaSimpleCondition>()),
	;
	
	private static Map<ConditionOperatorType, IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition>> conditionOperatorToEvaluatorMap = new EnumMap<>(ConditionOperatorType.class);
	
	static {
		 
		for(ConditionOperatorToEvaluatorType eachConditionOperatorToEvaluatorType : ConditionOperatorToEvaluatorType.values()) {
			conditionOperatorToEvaluatorMap.put(eachConditionOperatorToEvaluatorType.conditionOperator, eachConditionOperatorToEvaluatorType.evaluator);
		}
	}
	
	private ConditionOperatorToEvaluatorType(ConditionOperatorType conditionOperator, IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> evaluator) {
		this.conditionOperator =conditionOperator;
		this.evaluator = evaluator;
	}
	
	private ConditionOperatorType conditionOperator;
	
	private IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> evaluator;
	
	public static IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> getEvaluatorByOperatorType(ConditionOperatorType operatorType){
		return  PraeceptaObjectHelper.isObjectEmpty(operatorType) ? null : conditionOperatorToEvaluatorMap.get(operatorType);
	}

	public static IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> getEvaluatorByOperatorType(String operatorType){
		return PraeceptaObjectHelper.isStringEmpty(operatorType) ? null : getEvaluatorByOperatorType(ConditionOperatorType.valueOf(operatorType));
	}
}
