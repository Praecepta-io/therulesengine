package io.praecepta.rules.evaluators.impl;

import java.util.Date;
import java.util.Map;

import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.MetaDataAttributes;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaDateBetweenConditionalEvaluator<INPUT extends PraeceptaSimpleCondition>
		extends PraeceptaDateEqualConditionalEvaluator<INPUT> {

	private String getEndTimeZone(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.END_TIME_ZONE.getDescription()) != null )
			return parameters.get(MetaDataAttributes.END_TIME_ZONE.getDescription()).toString();
		
		return getToTimeZone(parameters);
	}

	private String getEndDateFormat(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.END_DATE_FORMAT.getDescription()) != null)
			return parameters.get(MetaDataAttributes.END_DATE_FORMAT.getDescription()).toString();
		
		return toDateFromat(parameters);
	}
	
	@Override
	protected boolean evaluate(INPUT input) {

		if (checkValidationStatus(input)) {
			if (validateMetaData(input.getParameters())) {

				Date fromDate = getFromDate(input);
				Date toDate = getToDate(input);
				
				Date endDate = getDateObject(input.getParameters().get(MetaDataAttributes.MIDDLE_DATE.getDescription()).toString(), 
						getEndDateFormat(input.getParameters()), getEndTimeZone(input.getParameters()));
				
				return fromDate.before(endDate) && toDate.after(endDate);
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.BETWEEN_DATE;
	}
}
