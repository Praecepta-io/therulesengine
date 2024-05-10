package io.praecepta.rules.sidecars.formatter.attribute.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.formatter.attribute.IPraeceptaAttributeFormatter;

public class PraeceptaDateFormatter implements IPraeceptaAttributeFormatter {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDateFormatter.class);

	public static final String FROM_FORMAT = "fromDateFormat";
	
	private static final String DEFAULT_IN_DATE_FORMAT = "yyyy-MM-dd";

	private static final String DEFAULT_OUT_DATE_FORMAT = "yy-MMM-dd";

	@Override
	public Object format(String toDateFormat, Object objectToFormat, Map<String, Object> additionalInfoMap) {

		if (!PraeceptaObjectHelper.isObjectEmpty(objectToFormat) ) {
			
			LOG.debug("Object needed to format {} ", objectToFormat);
			
			if(PraeceptaObjectHelper.isObjectEmpty(toDateFormat)) {
				toDateFormat = DEFAULT_OUT_DATE_FORMAT;
			}
			
			LOG.debug("To Date format to be used {} ", toDateFormat);
			
			String fromDateFormat = DEFAULT_IN_DATE_FORMAT;
			try {
				
				if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfoMap) && !PraeceptaObjectHelper.isObjectEmpty(additionalInfoMap.get(FROM_FORMAT))) {
					fromDateFormat = (String) additionalInfoMap.get(FROM_FORMAT);
				}
				
				LOG.debug("From Date format to be used {} ", fromDateFormat);
				
				DateFormat df = new SimpleDateFormat(fromDateFormat);
				Date fromDate = df.parse((String) objectToFormat);

				DateFormat simpleDateFormat = new SimpleDateFormat(toDateFormat);
				
				return simpleDateFormat.format(fromDate);
				
			} catch (Exception e) {
				LOG.error("Exception Occured while converting date with input format {} to output format {} for Value "+ objectToFormat, fromDateFormat,
						toDateFormat);
				LOG.error("Date Format exceotion : ", e);
			}
		}
		
		// Return whatever input received incase the above Format Steps doesn't go fine
		return objectToFormat;
	}
}
