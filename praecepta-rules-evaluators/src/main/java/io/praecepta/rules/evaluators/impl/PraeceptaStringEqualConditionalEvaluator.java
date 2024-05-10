package io.praecepta.rules.evaluators.impl;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.evaluators.PraeceptaAbstractConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaStringEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaAbstractConditionalEvaluator<INPUT> {


	@Override
	protected final boolean validateConditionOperatorType(ConditionOperatorType operatorTypeFromInput) {

		if(getOperatorType() == operatorTypeFromInput) {
			return true;
		}

		return false;
	}

	@Override
	protected final boolean validateDataType(ConditionValueHolder<?> valueHolder) {
		
		if(valueHolder.getFromValue() instanceof String && valueHolder.getToValue() instanceof String) {
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return PraeceptaObjectHelper.stringEquals(input.getConditionValueHolder().getFromValue().toString(), input.getConditionValueHolder().getToValue().toString());
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.EQUAL_CHARS;
	}
}
