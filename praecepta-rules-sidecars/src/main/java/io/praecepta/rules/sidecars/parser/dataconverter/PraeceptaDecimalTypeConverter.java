package io.praecepta.rules.sidecars.parser.dataconverter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaDecimalTypeConverter implements IpraeceptaDataTypeConverter {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDecimalTypeConverter.class);

	@Override
	public Object convert(String data, Map<String, Object> additionalInfoMap) {

		logger.info(" String to Decimal Conversion Requested for String - {}", data);

		if (!PraeceptaObjectHelper.isObjectEmpty(data)) {
			return Double.valueOf(data);
		}

		return null;
	}
}
