package io.praecepta.rules.evaluators.impl;

import java.util.Arrays;
import java.util.List;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaContainsInCollectionConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaStringEqualConditionalEvaluator<INPUT> {


	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(input.getConditionValueHolder().getToValue() != null && input.getConditionValueHolder().getFromValue() != null) {
				List<String> toArrayList = Arrays.asList(input.getConditionValueHolder().getToValue().toString().split(VALUE_SPLIT_STRING));
				if (input.getConditionValueHolder().getFromValue().toString().contains(VALUE_SPLIT_STRING)) {
					String[] fromValues = input.getConditionValueHolder().getFromValue().toString().split(VALUE_SPLIT_STRING);
					for (String s : fromValues) {
						if (!toArrayList.contains(s))
							return false;
					}
					return true;
				} else
					return toArrayList.contains(input.getConditionValueHolder().getFromValue().toString());
			}
			
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.CONTAINS_IN_COLLECTION;
	}
}
