package io.praecepta.rules.sidecars.parser;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaParserSideCarInjector  extends AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaParserSideCarInjector.class);
			
	@Override
	protected final void performAdditionalPreOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureInitialInfo");
		
		String parserType = this.getSubType();
		
		logger.info(" Received Parser Type as --> {}", parserType);
		if(!PraeceptaObjectHelper.isObjectEmpty(parserType)) {
			
			IPraeceptaSideCarParser parserToTrigger = PraeceptaParserTypeRegistry.getInstance().getParserByType(parserType);
			
			if(parserToTrigger != null) {
				logger.info(" Parser Type is Present for Parser Type --> {}", parserType);
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, parserToTrigger);
			}
		}
		
	}

	@Override
	public final void executeSideCar(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside executeSideCar");
		
		IPraeceptaSideCarParser parserToTrigger = (IPraeceptaSideCarParser)ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER);
	
		PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();
		
		if(parserToTrigger != null) { 
			logger.info(" Parser To Trigger is Available");
			
			String sideCarInput = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT);
			
			Map<String, Object> additionalInfo = new HashMap<>();
			
			additionalInfo.put(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG, getRuntimeConfigs());
			
			if(!PraeceptaObjectHelper.isObjectEmpty(sideCarInput)) {
				
				logger.info(" Parser's Side Car Input is Available and About to Trigger");
			
				sideCarDataHolder.addInput(sideCarInput);
				sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);
				
				try {
					logger.debug(" Before Parser Trigger ");
					parserToTrigger.parse(sideCarDataHolder);
					logger.debug(" After Parser Trigger ");
				}catch(Exception e) {
					logger.error(" Exception While Processing the Parser Side Car ", e);
				}
				
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT, sideCarDataHolder);
			}
		}
		
	}
	
	@Override
	public final PraeceptaSideCarDataHolder<?, ?> performAdditionalPostOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureExitInfo");
		
		return (PraeceptaSideCarDataHolder<?, ?>) ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT);
	}

	@Override
	protected boolean enrichRulesRequestWithSidecarHolderOutput() {
		return true;
	}
}
