package io.praecepta.rules.sidecars.parser.dataconverter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaNumberTypeConverter implements IpraeceptaDataTypeConverter {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaNumberTypeConverter.class);

	@Override
	public Object convert(String data, Map<String, Object> additionaInfoMap) {
		
		logger.info(" String to Number Conversion Requested for String - {}", data);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(data)) {
			
			return Long.valueOf(data);
		}

		return null;
	}
}
