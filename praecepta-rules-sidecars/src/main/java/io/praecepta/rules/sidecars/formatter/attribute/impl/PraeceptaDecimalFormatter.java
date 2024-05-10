package io.praecepta.rules.sidecars.formatter.attribute.impl;

import java.text.DecimalFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.formatter.attribute.IPraeceptaAttributeFormatter;

public class PraeceptaDecimalFormatter implements IPraeceptaAttributeFormatter {

	private final static Logger LOG = LoggerFactory.getLogger(PraeceptaDecimalFormatter.class);

	@Override
	public Object format(String pattern, Object objectToFormat, Map<String, Object> additionalInfoMap) {
		
		LOG.info(" Decimal Format Pattern {} and Value to Format {}", pattern, objectToFormat);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(objectToFormat) && !PraeceptaObjectHelper.isObjectEmpty(pattern)) {

			try {
				if(! (objectToFormat instanceof Number) ) {
					throw new RuntimeException(" Input Received is not a Valid Number");
				}
				
				// <b> NOTE : </b>Conversion From Float to Double is needed as Flow will support only 7 Chars.
				// Anything more than 7 chars in Float will generate a wrong format
				// Double will support 15 chars. 
				if(objectToFormat instanceof Float) {
					objectToFormat = Double.valueOf(String.valueOf(objectToFormat));
				}
				
				DecimalFormat decimalFormat = new DecimalFormat(pattern);

				return decimalFormat.format(objectToFormat);

			} catch (Exception e) {
				LOG.error("Exception Occured while Performing the Decimal format to {} for Input Value received  {}  "
						, pattern, objectToFormat);
				LOG.error("Decimal Format Exception : ", e);
			}
		}
		
		// Return whatever input received incase the above Format Steps doesn't go fine
		return objectToFormat;
		
	}
}
