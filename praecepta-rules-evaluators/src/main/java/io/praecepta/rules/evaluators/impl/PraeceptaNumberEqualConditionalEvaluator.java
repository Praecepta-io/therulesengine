package io.praecepta.rules.evaluators.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.evaluators.PraeceptaAbstractConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaNumberEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaAbstractConditionalEvaluator<INPUT> {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaNumberEqualConditionalEvaluator.class);

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
		} else {
			
			boolean validLHSType = true;
			boolean validRHSType = true;
			
			if(!(valueHolder.getFromValue() instanceof Number)  && valueHolder.getFromValue() instanceof String) {
				 validLHSType =isValidBigDecimalNumber(valueHolder.getFromValue().toString());
			}
			if(!(valueHolder.getToValue() instanceof Number)  && valueHolder.getToValue() instanceof String) {
				 validRHSType =isValidBigDecimalNumber(valueHolder.getToValue().toString());
			}
			
			if(validLHSType && validRHSType) return true;
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
	
	 public static boolean isValidBigDecimalNumber(String str) {
	        if (str == null || str.trim().isEmpty()) {
	            return false; // Handle null or empty strings
	        }
	        try {
	            new BigDecimal(str);
	            return true; // Successfully created BigDecimal, so it's a valid number
	        } catch (NumberFormatException e) {
	            return false; // Failed to create BigDecimal, so it's not a valid number
	        }
	    }
}
