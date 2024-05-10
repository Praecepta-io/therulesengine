package io.praecepta.rules.factor.actions.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;

public class PraeceptaAddFactorAction extends PraeceptaNumberFactorAction {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaAddFactorAction.class);

	@Override
	public PraeceptaActionHolder performAction(Object value, Map<PraeceptaActionParametersType, Object> parameters) {
		
		PraeceptaActionHolder actionHolder = null;
		
		Object factorValue = parameters.get(PraeceptaActionParametersType.FACTOR_VALUE);
		if (validateDataType(value, factorValue)) {
			
			Object actionedValue = null;

			try {
				actionedValue = new BigDecimal(value.toString()).add(new BigDecimal(factorValue.toString()));
				actionHolder = prepareActionHolder(actionedValue);

			} catch (Exception e) {
				PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder,
						ACTION_EXECUTION_STATUS.FAILURE,
						"Unable to Convert to Add Value : " + value + " and Substract Value "+factorValue);
			}
		} else {
			LOG.error(" Invalid format Value : {}, Factor Value:{}", value, factorValue);
			
			PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder,
					ACTION_EXECUTION_STATUS.UNABALE_TO_PERFORM_ACTION,
					" Data Type Validation failed. Value should be in Stringfy Number and Factor should be Stringfy Number");
		}
		
		return actionHolder;
	}

}
