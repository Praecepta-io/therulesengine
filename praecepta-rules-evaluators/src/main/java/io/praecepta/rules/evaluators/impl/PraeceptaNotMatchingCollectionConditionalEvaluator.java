package io.praecepta.rules.evaluators.impl;

import java.util.Arrays;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaNotMatchingCollectionConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {


	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				String fromValues = input.getConditionValueHolder().getFromValue().toString();
				String toValues = input.getConditionValueHolder().getToValue().toString();

				return !Arrays.asList(fromValues.split(VALUE_SPLIT_STRING)).containsAll(Arrays.asList(toValues.split(VALUE_SPLIT_STRING)));
			}
		}
		
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.NOT_MATCHING_COLLECTION;
	}
}
