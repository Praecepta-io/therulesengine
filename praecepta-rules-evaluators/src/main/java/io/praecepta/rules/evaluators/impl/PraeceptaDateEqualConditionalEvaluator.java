package io.praecepta.rules.evaluators.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.evaluators.PraeceptaAbstractConditionalEvaluator;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.MetaDataAttributes;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaDateEqualConditionalEvaluator<INPUT extends PraeceptaSimpleCondition> extends PraeceptaAbstractConditionalEvaluator<INPUT> {

	private static final String DEFAULT_TIME_ZONE = "America/New_York";
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDateEqualConditionalEvaluator.class); 

	@Override
	protected final boolean validateConditionOperatorType(ConditionOperatorType operatorTypeFromInput) {

		if(getOperatorType() == operatorTypeFromInput) {
			return true;
		}

		return false;
	}

	@Override
	protected final boolean validateDataType(ConditionValueHolder<?> valueHolder) {
		
		if(valueHolder.getFromValue() instanceof String && valueHolder.getToValue() instanceof String) {
			return true;
		}
		
		return false;
	}
	
	protected final boolean validateMetaData(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.FROM_DATE_FORMAT.getDescription()) != null) {
			return true;
		}
		return false;
	}

	protected final Date getDateObject(String date, String format) {
		return getDateObject(date, format, DEFAULT_TIME_ZONE);
	}
	protected final Date getDateObject(String date, String format, String timezone) {
		DateFormat simpleDateFormat = new SimpleDateFormat(format);
		if(timezone == null) {
			timezone = DEFAULT_TIME_ZONE;
		}
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			LOG.error("Exception Occured while converting date  from % to % format", date, format);
			LOG.error("Date Format exceotion : " , e.getMessage());
			return null;
		}
	}
	
	protected final String getFromTimeZone(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.FROM_TIME_ZONE.getDescription()) != null )
			return parameters.get(MetaDataAttributes.FROM_TIME_ZONE.getDescription()).toString();
		return null;
	}
	
	protected final String getToTimeZone(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.TO_TIME_ZONE.getDescription()) != null )
			return parameters.get(MetaDataAttributes.TO_TIME_ZONE.getDescription()).toString();
		
		return getFromTimeZone(parameters);
	}
	
	protected final Date getFromDate(INPUT input) {
		return getDateObject(input.getConditionValueHolder().getFromValue().toString(),
				input.getParameters().get(MetaDataAttributes.FROM_DATE_FORMAT.getDescription()).toString(), getFromTimeZone(input.getParameters()));
	}
	
	protected final String toDateFromat(Map<String,Object> parameters) {
		if(parameters.get(MetaDataAttributes.TO_DATE_FORMAT.getDescription()) != null)
			return parameters.get(MetaDataAttributes.TO_DATE_FORMAT.getDescription()).toString();
		
		return parameters.get(MetaDataAttributes.FROM_DATE_FORMAT.getDescription()).toString();
	}
	
	protected final Date getToDate(INPUT input) {
			return getDateObject(input.getConditionValueHolder().getToValue().toString(),
					toDateFromat(input.getParameters()), getToTimeZone(input.getParameters()));
	}
	
	@Override
	protected boolean evaluate(INPUT input) {
		
		if(checkValidationStatus(input)) {
			if(validateMetaData(input.getParameters())) {
				return (getFromDate(input)).compareTo(getToDate(input)) == 0;
			}
		}
		return false;
	}

	protected ConditionOperatorType getOperatorType() {
		return ConditionOperatorType.EQUAL_DATE;
	}
}
