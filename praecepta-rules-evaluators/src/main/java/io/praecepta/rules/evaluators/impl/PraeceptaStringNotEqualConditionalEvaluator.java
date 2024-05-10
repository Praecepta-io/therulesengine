package io.praecepta.rules.evaluators.impl;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public final class PraeceptaStringNotEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {

	@Override
	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.NOT_EQUAL_CHARS;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return !PraeceptaObjectHelper.stringEquals(input.getConditionValueHolder().getFromValue().toString(), input.getConditionValueHolder().getToValue().toString());
			}
		}
		return false;
	}

}
