package io.praecepta.rules.sidecars.parser.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.parser.IPraeceptaSideCarParser;
import io.praecepta.rules.sidecars.parser.exception.PraeceptaParserException;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaJsonParser implements IPraeceptaSideCarParser {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaJsonParser.class);

	@Override
	public void initializeParser() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see io.praecepta.rules.sidecars.parser.IPraeceptaSideCarParser#parse(io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder)
	 * PraeceptaSideCarDataHolder - String is an input in JSON format and Map<String, Object> is an output in Map of Maps
	 */
	@Override
	public void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder) {
		
		logger.debug(" Inside the Json Parser");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {
			
		logger.debug(" Side Car Holder Present in Json Parser");
			
			String inputJson = sideCarDataHolder.retriveInput();
			
			if (!PraeceptaObjectHelper.isObjectEmpty(inputJson)) {

				logger.debug(" Input Json received in the Parser - {} ", inputJson);

				if (validate(inputJson)) {

					Map<String, Object> parsedData = GsonHelper.toMapEntityPreserveNumber(inputJson);

					logger.debug(" Parsed Data from Json parser - {} ", parsedData);

					if (!PraeceptaObjectHelper.isObjectEmpty(parsedData)) {

						sideCarDataHolder.addOutput(parsedData);
					}
				} else {
					throw new PraeceptaParserException(" Input received is not a Valid Json to Parse. Check the Input passed -  "+ inputJson);
				}
			}
			
		}
	}

	@Override
	public boolean validate(String input) {
		return GsonHelper.isValidJson(input);
	}
	
	

}
