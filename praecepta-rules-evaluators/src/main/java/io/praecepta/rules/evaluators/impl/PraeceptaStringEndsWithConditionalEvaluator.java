package io.praecepta.rules.evaluators.impl;

import org.springframework.util.StringUtils;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public final class PraeceptaStringEndsWithConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {

	@Override
	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.ENDS_WITH;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return StringUtils.endsWithIgnoreCase(input.getConditionValueHolder().getToValue().toString(), input.getConditionValueHolder().getFromValue().toString());
			}
		}
		return false;
	}

}
