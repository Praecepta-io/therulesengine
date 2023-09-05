package io.praecepta.rules.evaluators.impl;

import java.math.BigDecimal;

import io.praecepta.rules.evaluators.PraeceptaAbstractConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaNumberEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaAbstractConditionalEvaluator<INPUT> {


	@Override
	protected final boolean validateConditionOperatorType(ConditionOperatorType operatorTypeFromInput) {

		if(getOperatorType() == operatorTypeFromInput) {
			return true;
		}

		return false;
	}

	@Override
	protected boolean validateDataType(ConditionValueHolder<?> valueHolder) {
		
		if(valueHolder.getFromValue() instanceof Number && valueHolder.getToValue() instanceof Number) {
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				return new BigDecimal(input.getConditionValueHolder().getFromValue().toString()).compareTo(new BigDecimal(input.getConditionValueHolder().getToValue().toString())) == 0;
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.EQUAL_NUMBER;
	}
}
