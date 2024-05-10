package io.praecepta.rules.evaluators.impl;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.evaluators.PraeceptaAbstractConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public final class PraeceptaEmptyConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaAbstractConditionalEvaluator<INPUT> {

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			return PraeceptaObjectHelper.isObjectEmpty(input.getConditionValueHolder().getFromValue());
		}
		return false;
	}

	@Override
	protected boolean validateConditionOperatorType(ConditionOperatorType operatorTypeFromInput) {
		return  ConditionOperatorType.EMPTY == operatorTypeFromInput ? true : false ;
	}

	@Override
	protected boolean validateDataType(ConditionValueHolder<?> valueHolder) {
		return true;
	}

}
