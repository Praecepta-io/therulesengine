package io.praecepta.rules.sidecars;

import java.util.Map;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public interface IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector extends IPraeceptaInfoTrackerSideCarInjector{

	String getSubType();
	
	void setSubType(String subType);
	
	int getOrder();
	
	void setOrder(int order);
	
	Map<String, Object> getRuntimeConfigs();
	
	void setRuntimeConfigs(Map<String, Object> runtimeConfigs);
	
	public static final String RUN_TIME_CONFIG = "RUN_TIME_CONFIG";
	
	public static  void addParentHolderToCurrent(PraeceptaRequestStore ruleStore) {
		
		PraeceptaSideCarDataHolder<? , ?> parentSideCarHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);
		
		PraeceptaSideCarDataHolder<? , ?> currentSideCarHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore) && !PraeceptaObjectHelper.isObjectEmpty(parentSideCarHolder) && !PraeceptaObjectHelper.isObjectEmpty(currentSideCarHolder)) {
			currentSideCarHolder.setParentSideCarDataHolder(parentSideCarHolder);
			parentSideCarHolder.setNextSideCarDataHolder(currentSideCarHolder);
		}
	}
	
	public static  void addNextHolderToCurrent(PraeceptaRequestStore ruleStore) {
		
		PraeceptaSideCarDataHolder<? , ?> currentSideCarHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER);
		
		PraeceptaSideCarDataHolder<? , ?> nextSideCarHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.NEXT_SIDECAR_HOLDER);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore) && !PraeceptaObjectHelper.isObjectEmpty(currentSideCarHolder) && !PraeceptaObjectHelper.isObjectEmpty(nextSideCarHolder)) {
			currentSideCarHolder.setNextSideCarDataHolder(nextSideCarHolder);
			nextSideCarHolder.setParentSideCarDataHolder(currentSideCarHolder);
		}
	}
	
	public static void addSideCarHolder(PraeceptaRequestStore ruleStore, PraeceptaSideCarStoreType holderType, PraeceptaSideCarDataHolder<?, ?> holderValue) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore) && !PraeceptaObjectHelper.isObjectEmpty(holderType) && 
				(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER == holderType || PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER == holderType 
				|| PraeceptaSideCarStoreType.NEXT_SIDECAR_HOLDER == holderType) ) {
			
			ruleStore.upsertToPraeceptaStore(holderType, holderValue);
		}
	}
	
	public static void addCurrentSideCarHolder(PraeceptaRequestStore ruleStore, PraeceptaSideCarDataHolder<?, ?> holderValue) {
		
		addSideCarHolder(ruleStore, PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER, holderValue);
	}
	
	public static void moveCurrentToParentSideCarHolder(PraeceptaRequestStore ruleStore, PraeceptaSideCarDataHolder<?, ?> holderValue) {
		
		addSideCarHolder(ruleStore, PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, holderValue);
	}
	
	public static PraeceptaSideCarDataHolder<? , ?> fetchParentSideCarHolder(PraeceptaRequestStore ruleStore) {
		
		PraeceptaSideCarDataHolder<? , ?> holderToReturn = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)){
			holderToReturn = (PraeceptaSideCarDataHolder<?, ?>) 
					ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);
		}
		
		return holderToReturn;
	}
}
