package io.praecepta.rules.sidecars.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enums.PraeceptaDefaultSideCarClazzType;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;

public class PraeceptaSideCarHelper {

	public static void convertSideCarMetadataToSideCarsForAStore(PraeceptaRequestStore ruleRequestStore) {

		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleRequestStore)){
			
			// Pre Rule Grp
			List<PraeceptaSideCarMetadata> preRuleGrpSideCarMetaData = (List<PraeceptaSideCarMetadata>) ruleRequestStore
					.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA);
	
			
			if(!PraeceptaObjectHelper.isObjectEmpty(preRuleGrpSideCarMetaData)){
				
				List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> preRuleGrpSideCar = PraeceptaSideCarHelper
						.convertSideCarMetadataToSideCars(preRuleGrpSideCarMetaData);
				
				ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS,
						preRuleGrpSideCar);
			}
	
			// Rule Level Conversion Starts Here 
					// Pre Rule
					Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPreRuleSideCarsMetaData = (Map<String, List<PraeceptaSideCarMetadata>>) ruleRequestStore
							.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS_META_DATA);
			
					if(!PraeceptaObjectHelper.isObjectEmpty(ruleNameToPreRuleSideCarsMetaData)){
						
						Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> preRuleSideCar = PraeceptaSideCarHelper
								.convertSideCarMetadataToSideCars(ruleNameToPreRuleSideCarsMetaData);
				
						ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS,
								preRuleSideCar);
					
					}
			
					// Post Rule
					Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPostRuleSideCarsMetaData = (Map<String, List<PraeceptaSideCarMetadata>>) ruleRequestStore
							.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS_META_DATA);
					
					if(!PraeceptaObjectHelper.isObjectEmpty(ruleNameToPostRuleSideCarsMetaData)){
			
						Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> postRuleSideCar = PraeceptaSideCarHelper
								.convertSideCarMetadataToSideCars(ruleNameToPostRuleSideCarsMetaData);
				
						ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS,
								postRuleSideCar);
					}
	
			// Post Rule Grp
			List<PraeceptaSideCarMetadata> postRuleGrpSideCarMetaData = (List<PraeceptaSideCarMetadata>) ruleRequestStore
					.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(postRuleGrpSideCarMetaData)){
	
				List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> postRuleGrpSideCar = PraeceptaSideCarHelper
						.convertSideCarMetadataToSideCars(postRuleGrpSideCarMetaData);
		
				ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS,
						postRuleGrpSideCar);
			}
		
		}

	}
	
	public static List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> convertSideCarMetadataToSideCars( 
			List<PraeceptaSideCarMetadata> preRuleGrpSideCarMetaData) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(preRuleGrpSideCarMetaData)) {
			
			List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> generatedSideCarsToUse = new ArrayList<>(preRuleGrpSideCarMetaData.size());
			
			preRuleGrpSideCarMetaData.forEach( eachSideCarMetaDataObj -> {
				
				IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector sideCar = convertSideCarMetadataToSideCars(eachSideCarMetaDataObj);
						
				if(sideCar != null) {
					generatedSideCarsToUse.add(sideCar);
				}
				
			});
			return generatedSideCarsToUse;
		}
		
		return Collections.emptyList();
	}
	
	@SuppressWarnings("unused")
	public static IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector convertSideCarMetadataToSideCars(PraeceptaSideCarMetadata sideCarMetaData) {
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector sideCar = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarMetaData) && !PraeceptaObjectHelper.isObjectEmpty(sideCarMetaData.getType())) {
			
			sideCar = PraeceptaDefaultSideCarClazzType.fetchSideCarFromRegistry(sideCarMetaData.getType());
			
			if(sideCar != null) {
				sideCar.setOrder(sideCarMetaData.getOrder());
				sideCar.setSubType(sideCarMetaData.getSideCarType());
				sideCar.setRuntimeConfigs(sideCarMetaData.getSideCarConfigs());
			}
		}
		
		return sideCar;
		
	}

	public static Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> convertSideCarMetadataToSideCars(
			Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPreRuleSideCarsMetaData) {
		
		Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> nameToSideCarsMap = new HashMap<>();
		
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleNameToPreRuleSideCarsMetaData)) {
			ruleNameToPreRuleSideCarsMetaData.forEach((name, sideCarsMetadataList) -> {

					nameToSideCarsMap.put(name, convertSideCarMetadataToSideCars(sideCarsMetadataList));
				}

			);
		}
		
		return nameToSideCarsMap;
	}
}
