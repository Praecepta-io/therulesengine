package io.praecepta.rules.evaluators;

import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

/**
 * 
 * @author rajasrikar
 *
 * @param <INPUT>
 */
public interface IPraeceptaConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> {
	
	String VALUE_SPLIT_STRING = "!#@#";

	boolean validateInput(INPUT input);
	
	boolean evaluateTheCondition(INPUT input);
}
