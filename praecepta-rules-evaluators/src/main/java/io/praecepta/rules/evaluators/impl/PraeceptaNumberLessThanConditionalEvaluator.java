package io.praecepta.rules.evaluators.impl;

import java.math.BigDecimal;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

/**
 * 
 * @author vara
 *
 * @param <INPUT>
 */
public class PraeceptaNumberLessThanConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaNumberEqualConditionalEvaluator<INPUT> {



	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return new BigDecimal(input.getConditionValueHolder().getFromValue().toString()).compareTo(new BigDecimal(input.getConditionValueHolder().getToValue().toString())) == -1;
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.LESS_THAN_NUMBER;
	}
}
