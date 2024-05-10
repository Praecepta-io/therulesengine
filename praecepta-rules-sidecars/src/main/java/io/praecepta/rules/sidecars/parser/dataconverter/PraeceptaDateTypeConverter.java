package io.praecepta.rules.sidecars.parser.dataconverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaDateTypeConverter implements IpraeceptaDataTypeConverter {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDateTypeConverter.class);

	public static final String FROM_DATE_FORMAT = "fromDateFormat";
	public static final String TO_DATE_FORMAT = "toDateFormat";

	@Override
	public Object convert(String data, Map<String, Object> additionalInfoMap) {

		logger.info(" String to Date Conversion Requested for String - {}", data);

		if (!PraeceptaObjectHelper.isObjectEmpty(data) && !PraeceptaObjectHelper.isObjectEmpty(additionalInfoMap)) {

			String fromFormat = (String) additionalInfoMap.get(FROM_DATE_FORMAT);
			String toFormat = (String) additionalInfoMap.get(TO_DATE_FORMAT);

			if (fromFormat != null && toFormat != null) {

				logger.info(" Date Conversion Requested With From Format - {} to To Format - {}", fromFormat, toFormat);

				DateFormat simpleDateFormat = new SimpleDateFormat(fromFormat);
				try {
					Date fromDate = simpleDateFormat.parse(data);
					
					// Formating to the specified To Format After Preparing the Date above
					DateFormat simpleToDateFormat = new SimpleDateFormat(toFormat);
					
					return simpleToDateFormat.format(fromDate);
				} catch (ParseException e) {
					return data;
				}
			} else {
				return data;
			}
		}

		return data;
	}
}
