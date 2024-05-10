package io.praecepta.rules.factor.actions.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;

public abstract class PraeceptaDateFactorAction implements PraeceptaUnderlyingFactorAction {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDateFactorAction.class); 
	
	private static final String DEFAULT_TIME_ZONE = "America/New_York";
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	
	protected final Date getDateObject(String date, Map<PraeceptaActionParametersType,Object> parameters) {
		
		String timezone = null;
		if(parameters.get(PraeceptaActionParametersType.FROM_TIME_ZONE) == null) {
			timezone = DEFAULT_TIME_ZONE;
		}else {
			timezone = parameters.get(PraeceptaActionParametersType.FROM_TIME_ZONE).toString();
		}
		String dateFormat = null;
		if(parameters.get(PraeceptaActionParametersType.FROM_DATE_FORMAT) == null) {
			dateFormat = DEFAULT_DATE_FORMAT;
		}else {
			dateFormat = parameters.get(PraeceptaActionParametersType.FROM_DATE_FORMAT).toString();
		}
		
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			LOG.error("Exception Occured while converting date  from % to % format", date, dateFormat);
			LOG.error("Date Format exceotion : " , e.getMessage());
			return null;
		}
	}
	
	
	protected final String convertDateToString(Date date, Map<PraeceptaActionParametersType,Object> parameters) {
		
		String timezone = null;
		if(parameters.get(PraeceptaActionParametersType.TO_TIME_ZONE) != null) {
			timezone = parameters.get(PraeceptaActionParametersType.TO_TIME_ZONE).toString();
			
		}else if(parameters.get(PraeceptaActionParametersType.FROM_TIME_ZONE) != null) {
			timezone = parameters.get(PraeceptaActionParametersType.FROM_TIME_ZONE).toString();
			
		}else {
			timezone = DEFAULT_TIME_ZONE;
		}
		
		String dateFormat = null;
		if(parameters.get(PraeceptaActionParametersType.TO_DATE_FORMAT) != null) {
			dateFormat = parameters.get(PraeceptaActionParametersType.TO_DATE_FORMAT).toString();
		}else if(parameters.get(PraeceptaActionParametersType.FROM_DATE_FORMAT) != null) {
			dateFormat = parameters.get(PraeceptaActionParametersType.FROM_DATE_FORMAT).toString();
		}else {
			dateFormat = DEFAULT_DATE_FORMAT;
		}
		
		DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return simpleDateFormat.format(date);
		
	}

	public final boolean validateDataType(Object value, Object factorValue) {
		return value instanceof String && factorValue instanceof Number;
	}

}
