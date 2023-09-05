package io.praecepta.rules.evaluators.impl;

import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public final class PraeceptaStringEmptyConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {

	@Override
	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.EMPTY;
	}

	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			return ObjectHelper.isObjectEmpty(input.getConditionValueHolder().getFromValue());
		}
		return false;
	}

}
