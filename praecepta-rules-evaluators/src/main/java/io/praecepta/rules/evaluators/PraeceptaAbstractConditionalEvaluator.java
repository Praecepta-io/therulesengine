package io.praecepta.rules.evaluators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public abstract class PraeceptaAbstractConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> implements IPraeceptaConditionalEvaluator<INPUT> {

//	private boolean inputValidationDone;
	

	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaAbstractConditionalEvaluator.class);
	
	@Override
	public final boolean validateInput(INPUT input) {
		boolean isAValidInput = false;
		/*if(ConditionOperatorType.EQUAL_CHARS == input.getConditionOperator() && !PraeceptaObjectHelper.isObjectEmpty(input.getSubjectName()) &&
				(!PraeceptaObjectHelper.isObjectEmpty(input.getValueToCompare()) || !PraeceptaObjectHelper.isObjectEmpty(input.getDefinedAttributeToCompare()))
				) {
			isAValidInput = true;
		}*/
		
		if(!PraeceptaObjectHelper.isObjectEmpty(input.getConditionOperator()) && validateConditionOperatorType(input.getConditionOperator()) 
				&& !PraeceptaObjectHelper.isObjectEmpty(input.getConditionValueHolder()) && validateDataType(input.getConditionValueHolder())
		){
			isAValidInput = true;
		}
		
//		inputValidationDone = true;
		return isAValidInput;
	}

	@Override
	public final boolean evaluateTheCondition(INPUT input) {
		boolean result = false;
		if(checkValidationStatus(input)) {
			LOG.debug("Evaluating input {}", input);
			result =  evaluate(input);
			
		}
		LOG.debug("Condition Result {}", result);
		return result;
	}

	protected final boolean checkValidationStatus(INPUT input) {
//		return !inputValidationDone && validateInput(input) && customValidation(input);
		return validateInput(input) && customValidation(input);
	}
	
	protected boolean customValidation(INPUT input) {
		return true;
	}

	protected abstract boolean evaluate(INPUT input);

	protected abstract boolean  validateConditionOperatorType(ConditionOperatorType operatorTypeFromInput);
	
	protected abstract boolean  validateDataType(ConditionValueHolder<?> valueHolder);
	
	/*protected boolean inputValidationPerformedStatus() {
		return inputValidationDone;
	}*/
}
