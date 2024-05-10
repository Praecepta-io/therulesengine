package io.praecepta.rules.sidecars.enricher;

import static io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper.captureAdditionalInfo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.sidecars.AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

//public class PraeceptaEnricherSideCarInjector implements IPraeceptaInfoTrackerSideCarInjector{
public class PraeceptaEnricherSideCarInjector extends AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaEnricherSideCarInjector.class);

	@Override
	public final void performAdditionalPreOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureInitialInfo");
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, null);
		
		String enricherType=this.getSubType();
		
		logger.info(" Received Enricher Type as --> {}", enricherType);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(enricherType)) {
			
			IPraeceptaSideCarEnricher enricherToTrigger = PraeceptaEnricherTypeRegistry.getInstance().getEnricherByType(enricherType);
			
			if(enricherToTrigger != null) {
				/*enricherToTrigger.initialize(getRuntimeConfigs());*/
				logger.info(" Enricher Type is Present for Enricher Type --> {}", enricherType);
				
				enricherToTrigger.initializeEnricher();
				
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, enricherToTrigger);
			}
		}
		
	}
	
	@Override
	public final void executeSideCar(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside executeSideCar");
		
		IPraeceptaSideCarEnricher enricherToTrigger = (IPraeceptaSideCarEnricher)ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER);
		
		PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();
		
		if(enricherToTrigger != null) { 
			logger.info(" Enricher To Trigger is Available");
			
			Map<String, Object> sideCarInput =null;
			
			Object sideCarInputObj=ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT);
			
			if(!PraeceptaObjectHelper.isObjectNull(sideCarInputObj)) {
				if(sideCarInputObj instanceof String) {
					sideCarInput = GsonHelper.toMapEntityPreserveNumber((String)sideCarInputObj);
				}else if(sideCarInputObj instanceof Map) {
					sideCarInput = (Map<String, Object>) sideCarInputObj;
				}
			}
			
			if(!PraeceptaObjectHelper.isObjectEmpty(sideCarInput)) {
				
				logger.info(" Enricher's Side Car Input is Available and About to Trigger with input - {} ", sideCarInput);
			
				sideCarDataHolder.addInput(sideCarInput);
				
				Map<String, Object> additionalInfo = new HashMap<>();
				
				additionalInfo.put(PraeceptaRestEnricherHelper.HEADERS_FROM_INPUT, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_METADATA));
				
				additionalInfo.put(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG, getRuntimeConfigs());
				
				captureAdditionalInfo(additionalInfo, sideCarDataHolder.retriveInput());
				
				sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);
				
				try {
					logger.debug(" Before Enricher Trigger ");
					enricherToTrigger.enrich(sideCarDataHolder);
					logger.debug(" After Enricher Trigger ");
				}catch(Exception e) {
					logger.error(" Exception While Processing the Enricher Side Car ", e);
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
