package io.praecepta.rules.sidecars.formatter.attribute.impl;

import java.util.Map;

import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.formatter.attribute.IPraeceptaAttributeFormatter;

public class PraeceptaPhoneNumberFormatter implements IPraeceptaAttributeFormatter {

	private final static Logger LOG = LoggerFactory.getLogger(PraeceptaPhoneNumberFormatter.class);

	private static final String DEFAULT_PHONE_FORMAT = "(####)-###-###";

	@Override
	public Object format(String pattern, Object objectToFormat, Map<String, Object> additionalInfoMap) {

		LOG.info(" Phone Format Pattern {} and Value to Format {}", pattern, objectToFormat);

		if (!PraeceptaObjectHelper.isObjectEmpty(objectToFormat)) {

			if (PraeceptaObjectHelper.isObjectEmpty(pattern)) {
				pattern = DEFAULT_PHONE_FORMAT;
			}

			try {
				MaskFormatter maskFormatter = new MaskFormatter(pattern);
				maskFormatter.setValueContainsLiteralCharacters(false);
				return maskFormatter.valueToString(objectToFormat);

			} catch (Exception e) {
				LOG.error("Exception Occured while Performing the Phone format to {} for Input Value received  {}  "
						, pattern, objectToFormat);
				LOG.error("Phone Format Exception : ", e);
			}
		}

		// Return whatever input received incase the above Format Steps doesn't go fine
		return objectToFormat;
	}
}
