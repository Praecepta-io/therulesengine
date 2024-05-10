package io.praecepta.rules.sidecars.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaFixedLengthParser;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaJsonParser;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaXmlParser;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaYamlParser;

public class PraeceptaParserTypeRegistry {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaParserTypeRegistry.class);

	private static Map<String, IPraeceptaSideCarParser> parserTypeRegistry = captureParserTypes();
	
	private static final PraeceptaParserTypeRegistry parserRegistryInstance = new PraeceptaParserTypeRegistry();
	
	private static final List<String> parserTypeStrs = getAllParserTypesInStringList();

	public PraeceptaParserTypeRegistry() {
	}
	
	public static PraeceptaParserTypeRegistry getInstance() {
		
		parserTypeRegistry = captureParserTypes();
		
		return parserRegistryInstance;
	}
	
	private static Map<String, IPraeceptaSideCarParser> captureParserTypes() {
		
		Map<String, IPraeceptaSideCarParser> parserRegistry = new HashMap<>();
		
		parserRegistry.put(PraeceptaParserType.JSON.name(), new PraeceptaJsonParser());
		parserRegistry.put(PraeceptaParserType.XML.name(), new PraeceptaXmlParser());
		parserRegistry.put(PraeceptaParserType.FIXED_LENGTH.name(), new PraeceptaFixedLengthParser());
		parserRegistry.put(PraeceptaParserType.DELIMITER.name(), new PraeceptaYamlParser());
		parserRegistry.put(PraeceptaParserType.YAML.name(), new PraeceptaJsonParser());
		
		return parserRegistry;
	}
	
	private static List<String> getAllParserTypesInStringList() {

		List<String> parserTypeStrList = new ArrayList<>();
		
		for(PraeceptaParserType eachParserType : PraeceptaParserType.values()) {
			logger.info(" Adding Parser Available {} ", eachParserType.name());
			parserTypeStrList.add(eachParserType.name());
		}
		
		logger.info(" List Of Parsers Availabele --> ", parserTypeStrList);
		
		return parserTypeStrList;
	}

	public enum PraeceptaParserType{
		JSON, XML, FIXED_LENGTH, DELIMITER, YAML;
	}
	
	public IPraeceptaSideCarParser getParserByType(String parserType) {
		
		logger.info("Inside the Get Parser By Type to fetch for Type {} ", parserType);
		
		IPraeceptaSideCarParser parserSideCar = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(parserType) && parserTypeStrs.contains(parserType.toUpperCase())) {
			
			parserSideCar = parserTypeRegistry.get(parserType.toUpperCase());
		}
		
		logger.info(" Parser To Return for a Type ", parserSideCar);
		return parserSideCar;
	}
}
