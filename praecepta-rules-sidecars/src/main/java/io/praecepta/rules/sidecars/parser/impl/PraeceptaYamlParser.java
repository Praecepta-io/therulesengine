package io.praecepta.rules.sidecars.parser.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.parser.IPraeceptaSideCarParser;
import io.praecepta.rules.sidecars.parser.exception.PraeceptaParserException;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaYamlParser implements IPraeceptaSideCarParser {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaYamlParser.class);

	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

	@Override
	public void initializeParser() {
		
	}

	/**
	 *
	 * @param sideCarDataHolder
	 */
	@Override
	public void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder) {
		
		logger.debug(" Inside the Yaml Parser");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {
			
		logger.debug(" Side Car Holder Present in Yaml Parser");
			
			String inputMessage = sideCarDataHolder.retriveInput();
			
			if (!PraeceptaObjectHelper.isObjectEmpty(inputMessage)) {

				logger.debug(" Input Yaml received in the Parser - {} ", inputMessage);

				if (validate(inputMessage)) {

					Map<String,Object> parsedData = null;
					try {
						parsedData = mapper.readValue(inputMessage, Map.class);
					} catch (JsonProcessingException e) {
						logger.error("failed to parse data");
					}

					logger.debug(" Parsed Data from Yaml parser - {} ", parsedData);

					if (!PraeceptaObjectHelper.isObjectEmpty(parsedData)) {

						sideCarDataHolder.addOutput(parsedData);
					}
				} else {
					throw new PraeceptaParserException(" Input received is not a Valid Yaml to Parse. Check the Input passed -  "+ inputMessage);
				}
			}
			
		}
	}


	
	

}
