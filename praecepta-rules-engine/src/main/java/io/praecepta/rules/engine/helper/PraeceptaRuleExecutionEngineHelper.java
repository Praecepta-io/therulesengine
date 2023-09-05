package io.praecepta.rules.engine.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSideCars;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.sidecars.model.PraeceptaRuleLevelSideCars;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;

public class PraeceptaRuleExecutionEngineHelper {

	public static PraeceptaRequestStore createRuleStore(String inputText, Map<String, Object> metaData) {

		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		Map<String, Object> dataMap = GsonHelper.toEntity(inputText, Map.class);
		
		PraeceptaRuleSpaceCompositeKey spaceCompositeKey= getRuleSpaceKey(dataMap);
		
		String version = (String) dataMap.get("version");
		String ruleGroupName = (String) dataMap.get("ruleGroupName");
		
		PraeceptaPivotalRulesHubStore praeceptaPivotalHubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
		
		PraeceptaRuleSpace ruleSpace = praeceptaPivotalHubStore.getActiveRuleSpacesWithVersion(spaceCompositeKey, version);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE, ruleSpace);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST,inputText);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_METADATA, metaData);
		
		PraeceptaRuleGroup ruleGrp = praeceptaPivotalHubStore.getRuleGrpForAndSpaceVersionAndGroupName(spaceCompositeKey, version, ruleGroupName);
		
		if(ruleGrp !=  null) {
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, ruleGrp);
		}
		
		captureRuleGroupDetails(ruleStore, ruleSpace, ruleGrp, ruleGroupName);
		
		captureRuleGroupActions(ruleStore, ruleGrp);
		
		PraeceptaRuleSideCars ruleSideCars = praeceptaPivotalHubStore.getSideCarsForSpaceAndVersionAndGroupName(spaceCompositeKey, version, ruleGroupName);
		
		if(!ObjectHelper.isObjectEmpty(ruleSideCars)) {
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SIDE_CARS_INFO, ruleSideCars);
		}
		
		captureRuleSideCarDetails(ruleStore, ruleSideCars, ruleGroupName);
		
		return ruleStore;
	}
	
	private static void captureRuleSideCarDetails(PraeceptaRequestStore ruleStore, 
			PraeceptaRuleSideCars ruleSideCarsInfo, String ruleGroupName) {
		
		if(!ObjectHelper.isObjectEmpty(ruleSideCarsInfo) && !ObjectHelper.isObjectEmpty(ruleSideCarsInfo.getRuleGroupSideCars()) && 
				!ObjectHelper.isObjectEmpty(ruleGroupName)) {
			
			// Pre Rule Group
			ruleStore
			.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA, 
					ruleSideCarsInfo.getRuleGroupSideCars().getPreRuleGrpSideCars());
			
			// Rule Level
			List<PraeceptaRuleLevelSideCars> ruleLevelSideCars = ruleSideCarsInfo.getRuleGroupSideCars().getRuleLevelSideCars();
			
			if(!ObjectHelper.isObjectEmpty(ruleLevelSideCars)){
				
				// Pre Rule with Rule Name
				Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPreRuleSideCars = ruleLevelSideCars.stream().collect(Collectors.toMap(PraeceptaRuleLevelSideCars::getRuleName, 
						PraeceptaRuleLevelSideCars::getPreRuleSideCars));
				
				ruleStore
				.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS_META_DATA, ruleNameToPreRuleSideCars);

				// Post Rule with Rule Name
				Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPostRuleSideCars = ruleLevelSideCars.stream().collect(Collectors.toMap(PraeceptaRuleLevelSideCars::getRuleName, 
						PraeceptaRuleLevelSideCars::getPostRuleSideCars));
				
				ruleStore
				.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS_META_DATA, ruleNameToPostRuleSideCars);
				
			}
			
			// Post Rule Group
			ruleStore
			.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA, 
					ruleSideCarsInfo.getRuleGroupSideCars().getPostRuleGrpSideCars());
			
		}
		
	}


	private static PraeceptaRuleSpaceCompositeKey getRuleSpaceKey(Map<String, Object> dataMap) {
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(
				(String) dataMap.get("spaceName"), (String) dataMap.get("clientId"), (String) dataMap.get("appName"));
		return compositeKey;
	}
	
	private static void captureRuleGroupDetails(PraeceptaRequestStore ruleStore, PraeceptaRuleSpace ruleSpace, PraeceptaRuleGroup ruleGrp,
			String ruleGroupName) {
		
		if (!ObjectHelper.isObjectNull(ruleSpace) && !ObjectHelper.isObjectNull(ruleGrp)) {

			Map<String, Object> ruleGrpMetaData =null;
            if(!ObjectHelper.isObjectEmpty(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA))) {
            	ruleGrpMetaData =(Map<String, Object>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA);
            }
            if(ObjectHelper.isObjectEmpty(ruleGrpMetaData)) {
            	ruleGrpMetaData=new HashMap<>();
            }
			ruleGrpMetaData.put(PraeceptaRuleGroupMetaData.NUMBER_OF_CRITERIAS.name(),
					ruleGrp.getPraeceptaCriterias().size());
			
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA, ruleGrpMetaData);
			//To be used as hashKey for RuleActionMap
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY, captureRuleNameWithSpaceKeyInfo(ruleSpace,ruleGrp));
		}
	}
	
	private static RuleGroupInfo captureRuleNameWithSpaceKeyInfo(PraeceptaRuleSpace ruleSpace,
			PraeceptaRuleGroup ruleGrp) {

		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		ruleSpaceInfo.setVersion(ruleSpace.getRuleSpaceKey().getVersion());
		ruleSpaceInfo.setSpaceName(ruleSpace.getRuleSpaceKey().getSpaceName());
		ruleSpaceInfo.setClientId(ruleSpace.getRuleSpaceKey().getClientId());
		ruleSpaceInfo.setAppName(ruleSpace.getRuleSpaceKey().getAppName());

		ruleGroupInfo.setRuleGroupName(ruleGrp.getRuleGroupName());
		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;

	}
	
	private static void captureRuleGroupActions(PraeceptaRequestStore ruleStore, PraeceptaRuleGroup ruleGrp) {

		if (!ObjectHelper.isObjectNull(ruleGrp)) {

			if (!ObjectHelper.isObjectEmpty(ruleGrp.getActionToPerform())) {
				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_ACTIONS,
						ruleGrp.getActionToPerform());
			}
			
			if (!ObjectHelper.isObjectEmpty(ruleGrp.getActionToPerformOnFailure())) {
				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_ACTIONS_ON_FAILURE,
						ruleGrp.getActionToPerformOnFailure());
			}
		}
	}
}
