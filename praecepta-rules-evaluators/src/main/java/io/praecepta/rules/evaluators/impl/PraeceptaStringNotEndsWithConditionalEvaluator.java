package io.praecepta.rules.evaluators.impl;

import org.springframework.util.StringUtils;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public final class PraeceptaStringNotEndsWithConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {

	@Override
	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.NOT_ENDS_WITH;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return !StringUtils.endsWithIgnoreCase(input.getConditionValueHolder().getFromValue().toString(), input.getConditionValueHolder().getToValue().toString());
			}
		}
		return false;
	}

}
