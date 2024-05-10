package io.praecepta.rules.sidecars.enricher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.enricher.impl.PraeceptaMultiRestApiEnricher;
import io.praecepta.rules.sidecars.enricher.impl.PraeceptaSimpleRestApiEnricher;

public class PraeceptaEnricherTypeRegistry {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaEnricherTypeRegistry.class);
	
	private static Map<String, IPraeceptaSideCarEnricher> enricherTypeRegistry = null;
	
	private static final List<String> enricherTypeStrs = getAllEnricherTypesInStringList();
	
	private static final PraeceptaEnricherTypeRegistry enricherRegistryInstance = new PraeceptaEnricherTypeRegistry();
	
	public PraeceptaEnricherTypeRegistry() {
	}
	
	public static PraeceptaEnricherTypeRegistry getInstance() {
		
		enricherTypeRegistry = captureenricherTypes();
		
		return enricherRegistryInstance;
	}
	
	private static Map<String, IPraeceptaSideCarEnricher> captureenricherTypes() {
		
		Map<String, IPraeceptaSideCarEnricher> enricherRegistry = new HashMap<>();
		
		enricherRegistry.put(PraeceptaEnricherType.SIMPLE_REST_API.name(), new PraeceptaSimpleRestApiEnricher());
		enricherRegistry.put(PraeceptaEnricherType.SIMPLE_SOAP_API.name(), null);
		enricherRegistry.put(PraeceptaEnricherType.MULTI_REST_API.name(), new PraeceptaMultiRestApiEnricher());
		enricherRegistry.put(PraeceptaEnricherType.MULTI_SOAP_API.name(), null);
		enricherRegistry.put(PraeceptaEnricherType.MIXED_API.name(), null);
		enricherRegistry.put(PraeceptaEnricherType.MIXED_MULTI_API.name(), null);
		
		return enricherRegistry;
	}
	
	public enum PraeceptaEnricherType{
		SIMPLE_REST_API, SIMPLE_SOAP_API, MULTI_REST_API, MULTI_SOAP_API, MIXED_API, MIXED_MULTI_API;
	}
	
	private static List<String> getAllEnricherTypesInStringList() {

		List<String> enricherTypeStrList = new ArrayList<>();
		
		for(PraeceptaEnricherType enricherType : PraeceptaEnricherType.values()) {
			logger.info(" Adding Enricher Available {} ", enricherType.name());
			enricherTypeStrList.add(enricherType.name());
		}
		
		logger.info(" List Of Enrichers Availabele --> ", enricherTypeStrList);
		
		return enricherTypeStrList;
	}

	public IPraeceptaSideCarEnricher getEnricherByType(String enricherType) {
		
		logger.info("Inside the Get Enricher By Type to fetch for Type {} ", enricherType);
		
		IPraeceptaSideCarEnricher enricherSideCar = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(enricherType) && enricherTypeStrs.contains(enricherType.toUpperCase()) 
				&& enricherTypeRegistry.containsKey(enricherType.toUpperCase())) {
			
			enricherSideCar = enricherTypeRegistry.get(enricherType);
		}
		
		logger.info(" Enricher To Return for a Type ", enricherSideCar);
		return enricherSideCar;
	}
}
