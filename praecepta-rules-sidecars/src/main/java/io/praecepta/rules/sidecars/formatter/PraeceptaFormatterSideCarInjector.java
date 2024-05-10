package io.praecepta.rules.sidecars.formatter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.sidecars.AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaFormatterSideCarInjector  extends AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaFormatterSideCarInjector.class);

	@Override
	public final void performAdditionalPreOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureInitialInfo");
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, null);

		String formatterType = this.getSubType();
		logger.info(" Received Formatter Type as --> {}", formatterType);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(formatterType)) {
			
			IPraeceptaSideCarFormatter formatterToRun = PraeceptaFormatterTypeRegistry.getInstance().getFormatterType(formatterType);
			
			if(formatterToRun != null) {
				logger.info(" Formatter Type is Present for Formatter Type --> {}", formatterType);
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, formatterToRun);
			}
		}
	}

	@Override
	public final void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside executeSideCar");
		
		IPraeceptaSideCarFormatter formatterToRun = (IPraeceptaSideCarFormatter) ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER);
	
		PraeceptaSideCarDataHolder<Map<String, Object>, String> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();
		
		if(formatterToRun != null) { 
			logger.info(" Formatter To Trigger is Available");
			
			Map<String, Object> sideCarInput = (Map<String, Object>) ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(sideCarInput)) {
				
				logger.info(" Formatter Side Car Input is Available and About to Trigger {} - ", sideCarInput);
				logger.info(" Formatter Runtime Config in executeSideCar --> {}", getRuntimeConfigs());
			
				sideCarDataHolder.addInput(sideCarInput);
				
				Map<String, Object> additionalInfo = new HashMap<>();
				
				additionalInfo.put(PraeceptaRestEnricherHelper.HEADERS_FROM_INPUT, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_METADATA));
				
				additionalInfo.put(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG, getRuntimeConfigs());
				
				sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);
				
				try {
					logger.debug(" Before Formatter Trigger ");
					formatterToRun.format(sideCarDataHolder);
					logger.debug(" After Formatter Trigger ");
				}catch(Exception e) {
					logger.error(" Exception While Processing the Formatter Side Car ", e);
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
		return false;
	}
}
