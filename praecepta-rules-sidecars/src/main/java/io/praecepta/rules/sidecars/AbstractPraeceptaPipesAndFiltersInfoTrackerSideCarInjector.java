package io.praecepta.rules.sidecars;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public abstract class AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector implements IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector{
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.class);

	private String subType;
	
	private int order;
	
	private Map<String, Object> runtimeConfigs;
	
	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Map<String, Object> getRuntimeConfigs() {
		return runtimeConfigs;
	}

	public void setRuntimeConfigs(Map<String, Object> runtimeConfigs) {
		this.runtimeConfigs = runtimeConfigs;
	}
	
	protected final void captureChainingInfo(PraeceptaRequestStore ruleStore, PraeceptaSideCarDataHolder<?, ?> sideCarDataHolder) {
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addCurrentSideCarHolder(ruleStore, sideCarDataHolder);
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addParentHolderToCurrent(ruleStore);
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.moveCurrentToParentSideCarHolder(ruleStore, sideCarDataHolder);
	}
	
	@Override
	public final void trackAndCaptureInitialInfo(PraeceptaRequestStore ruleStore) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, null);
			
			// Get the Parent Side Car Holder. With this, we can get Last Holders Output and use it for next Holders Input
			PraeceptaSideCarDataHolder<? , ?> parentRuleHolder = 
					IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.fetchParentSideCarHolder(ruleStore);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(parentRuleHolder)) {
				
				Object outputFromLastSideCar = parentRuleHolder.retriveOutput();
				
				if(!PraeceptaObjectHelper.isObjectEmpty(outputFromLastSideCar)) {
					
					// Assign As an Input to the Next Sidecar
					
					ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT, outputFromLastSideCar);
				}
			}
			
			performAdditionalPreOperations(ruleStore);
		}
	}

	protected abstract void performAdditionalPreOperations(PraeceptaRequestStore ruleStore);
	
	@Override
	public final void trackAndCaptureExitInfo(PraeceptaRequestStore ruleStore) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			
			PraeceptaSideCarDataHolder<? , ?> sideCarDataHolder = performAdditionalPostOperations(ruleStore);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {
				
				// Add this line to Chain the Side Car Data Holders
				captureChainingInfo(ruleStore, sideCarDataHolder);
				
				// This is True only incase of Parser and Enricher as the Output for both of them are forced to use Map<String, Object>
				// This is False for Formatter as it is a String
				if(enrichRulesRequestWithSidecarHolderOutput()) {
					
					enrichInputWithSideCarHoldersOutput(ruleStore);
				}
			}
			
			cleanUpRequestStore(ruleStore);
			
		}
		
	}

	private void cleanUpRequestStore(PraeceptaRequestStore ruleStore) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			
			ruleStore.deleteFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT);
			ruleStore.deleteFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT);
		}
		
	}

	protected abstract PraeceptaSideCarDataHolder<? , ?> performAdditionalPostOperations(PraeceptaRequestStore ruleStore);
	
	protected abstract boolean enrichRulesRequestWithSidecarHolderOutput();
	
	// Merging The SideCar Output to Rules Input 
	private static void enrichInputWithSideCarHoldersOutput(PraeceptaRequestStore ruleStore) {
		
		PraeceptaSideCarDataHolder<?, ?> inputDataHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);
		
		String rulesInput = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(rulesInput) && !PraeceptaObjectHelper.isObjectEmpty(inputDataHolder)) {
			
			Map<String, Object> sideCarOutput = (Map<String, Object>) inputDataHolder.retriveOutput();
			
//			if (!PraeceptaObjectHelper.isObjectEmpty(sideCarOutput)) {
//
//				Map<String, Object> requestMap = GsonHelper.toMapEntityPreserveNumber(rulesInput);
//
//				sideCarOutput.keySet().forEach(key -> {
//					requestMap.put(key, sideCarOutput.get(key));
//				});
//
//				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, GsonHelper.toJsonPreserveNumber(requestMap));
//				
//			}
			
		}
	}
}
