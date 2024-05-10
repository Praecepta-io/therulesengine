package io.praecepta.rules.evaluators.impl;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaDateLessThanEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaDateEqualConditionalEvaluator<INPUT> {



	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(validateMetaData(input.getParameters())) {
				return (getFromDate(input)).compareTo(getToDate(input)) <= 0;
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.LESS_THAN_EQUAL_DATE;
	}
}
