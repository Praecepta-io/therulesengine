package io.praecepta.rules.sidecars.enums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.PraeceptaEnricherSideCarInjector;
import io.praecepta.rules.sidecars.formatter.PraeceptaFormatterSideCarInjector;
import io.praecepta.rules.sidecars.parser.PraeceptaParserSideCarInjector;

public enum PraeceptaDefaultSideCarClazzType {
	
	
	PARSER (PraeceptaParserSideCarInjector.class), ENRICHER (PraeceptaEnricherSideCarInjector.class), 
	FORMATTER (PraeceptaFormatterSideCarInjector.class);	

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultSideCarClazzType.class);
	
	private static Map<String, Class<? extends IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> defaultSideCarTypeToSideCarMap = new HashMap<>();
	
	static {
		
		for(PraeceptaDefaultSideCarClazzType eachSideCarType : PraeceptaDefaultSideCarClazzType.values()) {
			defaultSideCarTypeToSideCarMap.put(eachSideCarType.name(), eachSideCarType.sideCarClazz);
		}
	}
	
	private PraeceptaDefaultSideCarClazzType(Class<? extends IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> sideCarClazz) {
		this.sideCarClazz = sideCarClazz;
	}
	
	private Class<? extends IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> sideCarClazz;
	
	public static void registerCustomSideCar(String customSideCarName, IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector customSideCar) {
		defaultSideCarTypeToSideCarMap.put(customSideCarName, (Class<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>) customSideCar.getClass());
	}
	
	public static IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector fetchSideCarFromRegistry(String customSideCarName) {
		Class<? extends IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> sideCarClazz = defaultSideCarTypeToSideCarMap.get(customSideCarName);
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector toReturn = null;
		
		if(sideCarClazz != null) {
			try {
				toReturn =  (IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector) Class.forName(sideCarClazz.getCanonicalName()).newInstance();
			} catch (InstantiationException e) {
				logger.error( " InstantiationException for Side Car Type - "+ customSideCarName + " ", e );
			} catch (IllegalAccessException e) {
				logger.error( " IllegalAccessException for Side Car Type - "+ customSideCarName + " ", e );
			} catch (ClassNotFoundException e) {
				logger.error( " ClassNotFoundException for Side Car Type - "+ customSideCarName + " ", e );
			}
		} else {
			logger.warn(" Side Car Type - {} - Passed doesn't have any match ", customSideCarName);
		}
		
		return toReturn;
	}
}
