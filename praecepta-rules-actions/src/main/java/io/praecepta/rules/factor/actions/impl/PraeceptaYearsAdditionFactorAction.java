package io.praecepta.rules.factor.actions.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;

public class PraeceptaYearsAdditionFactorAction extends PraeceptaDateFactorAction {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaYearsAdditionFactorAction.class);

	@Override
	public PraeceptaActionHolder performAction(Object value, Map<PraeceptaActionParametersType,Object> parameters) {
		
		PraeceptaActionHolder actionHolder = null;
		
		Object factorValue = parameters.get(PraeceptaActionParametersType.FACTOR_VALUE);
		if (validateDataType(value, factorValue)) {

			Date date = getDateObject(value.toString(), parameters);

			if (date != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.YEAR, Integer.valueOf(factorValue.toString()));

				Object actionedValue = convertDateToString(c.getTime(), parameters);

				actionHolder = prepareActionHolder(actionedValue);

			} else {

				PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder,
						ACTION_EXECUTION_STATUS.FAILURE,
						"Unable to Convert to Date format for Input String value : " + value);
			}

		} else {
			LOG.error(" Invalid format Date : {}, Additional Value:{}", value, factorValue);

			PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder,
					ACTION_EXECUTION_STATUS.UNABALE_TO_PERFORM_ACTION,
					" Data Type Validation failed. Date should be in String and Factor should be Number");
		}

		
		return actionHolder;
	}

}
