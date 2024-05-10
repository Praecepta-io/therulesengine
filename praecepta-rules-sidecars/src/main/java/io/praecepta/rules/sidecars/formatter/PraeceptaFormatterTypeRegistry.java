package io.praecepta.rules.sidecars.formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.formatter.impl.PraeceptaGenericFormatter;

public class PraeceptaFormatterTypeRegistry {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaFormatterTypeRegistry.class);
	
	private static  Map<String, IPraeceptaSideCarFormatter> formatterTypeRegistry = null;
	
	private static final List<String> enricherTypeStrs = getAllFormatterTypesInStringList();
	
	private static final PraeceptaFormatterTypeRegistry formatterRegistryInstance = new PraeceptaFormatterTypeRegistry();
	
	public enum PraeceptaFormatterType{
		JSON, XML, FIXED_LENGTH, DELIMITER, YAML;
	}
	
	private PraeceptaFormatterTypeRegistry() {
	}
	
	public static PraeceptaFormatterTypeRegistry getInstance() {
		
		formatterTypeRegistry = captureFormaterTypes();
		
		return formatterRegistryInstance;
	}

	private static Map<String, IPraeceptaSideCarFormatter> captureFormaterTypes() {

		Map<String, IPraeceptaSideCarFormatter> formatterRegistry = new HashMap<>();
		
		formatterRegistry.put(PraeceptaFormatterType.JSON.name(), new PraeceptaGenericFormatter());
		formatterRegistry.put(PraeceptaFormatterType.XML.name(), new PraeceptaGenericFormatter());
		formatterRegistry.put(PraeceptaFormatterType.FIXED_LENGTH.name(), null);
		formatterRegistry.put(PraeceptaFormatterType.DELIMITER.name(), null);
		formatterRegistry.put(PraeceptaFormatterType.YAML.name(), new PraeceptaGenericFormatter());
		
		return formatterRegistry;
	}
	
	private static List<String> getAllFormatterTypesInStringList() {
		
		List<String> formatterTypeStrList = new ArrayList<>();
		
		for(PraeceptaFormatterType formatterType : PraeceptaFormatterType.values()) {
			logger.info(" Adding Formatter Available {} ", formatterType.name());
			formatterTypeStrList.add(formatterType.name());
		}
		
		logger.info(" List Of Enrichers Availabele --> ", formatterTypeStrList);
		
		return formatterTypeStrList;
		
	}
	
	public  IPraeceptaSideCarFormatter getFormatterType(String formatterType){
		
		logger.info("Inside the Get Formatter By Type to fetch for Type {} ", formatterType);
		
		IPraeceptaSideCarFormatter formatter = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(formatterType) && enricherTypeStrs.contains(formatterType.toUpperCase()) && formatterTypeRegistry.containsKey(formatterType.toUpperCase())) {
			
			formatter = formatterTypeRegistry.get(formatterType.toUpperCase());
		}
		
		logger.info(" Formatter To Return for a Type ", formatter);
		return formatter;
	}
}
