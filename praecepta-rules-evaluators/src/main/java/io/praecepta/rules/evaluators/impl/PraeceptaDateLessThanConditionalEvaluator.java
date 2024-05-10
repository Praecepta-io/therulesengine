package io.praecepta.rules.evaluators.impl;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaDateLessThanConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaDateEqualConditionalEvaluator<INPUT> {



	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(validateMetaData(input.getParameters())) {
				return getFromDate(input).after(getToDate(input));
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.LESS_THAN_DATE;
	}
}
