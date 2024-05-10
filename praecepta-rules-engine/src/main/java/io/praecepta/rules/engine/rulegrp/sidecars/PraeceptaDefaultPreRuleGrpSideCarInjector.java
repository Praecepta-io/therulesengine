package io.praecepta.rules.engine.rulegrp.sidecars;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaDefaultPreRuleGrpSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPreRuleGrpSideCarInjector.class);
			
	public final void captureInInfo(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside captureInInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
		
		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);
		
		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);
		
		if (ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps()) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupToExecute)) {
			Collection<PraeceptaRuleGroup> praeceptaRuleGrps = ruleSpace.getPraeceptaRuleGrps();
			
			Optional<PraeceptaRuleGroup> ruleGrpToUse = praeceptaRuleGrps.stream().filter( eachRuleGrp -> !PraeceptaObjectHelper.isStringEmpty(eachRuleGrp.getRuleGroupName()) && eachRuleGrp.getRuleGroupName().equals(ruleGroupToExecute)).findFirst();
			
			if(ruleGrpToUse.isPresent()) {
				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, ruleGrpToUse.get());
			}
		}
		
		logger.debug("Exiting captureInInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
	}
	
	public final void captureExitInfo(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside captureExitInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
		
		List<IPraeceptaInfoTrackerSideCarInjector> sideCarsExecutedInPreRuleGrp = getExecutionSideCars();
		
		Map<String,Object> ruleRequestAsAMap = (Map<String, Object>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);
		
		PraeceptaSideCarDataHolder<?, ?> dataHolder = (PraeceptaSideCarDataHolder<?, ?>) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);
		
		Object dataHolderOutput = dataHolder.retriveOutput();
		
		// This is when there is no Request As Map OR Data Holder doesn't have the Map as the output From Sidecar
		if(PraeceptaObjectHelper.isObjectEmpty(ruleRequestAsAMap) && !PraeceptaObjectHelper.isObjectEmpty(dataHolderOutput) && dataHolderOutput instanceof Map) {
			
			ruleRequestAsAMap = (Map) dataHolderOutput;
			
		} else if(PraeceptaObjectHelper.isObjectEmpty(ruleRequestAsAMap) || (!PraeceptaObjectHelper.isObjectEmpty(dataHolderOutput) && !(dataHolderOutput instanceof Map))) {
			
			logger.info("Rule Request as a Map or ");
			
			Object ruleRequest = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
				
			PraeceptaSideCarDataHolder<String, Map<String, Object>> dataHolderToUse = new PraeceptaSideCarDataHolder<>();
			
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleRequest) && PraeceptaObjectHelper.isObjectEmpty(ruleRequestAsAMap)
					&& GsonHelper.isValidJson(ruleRequest.toString())) {
				
				logger.info("Preparing The Rule Request as a Map From a Valid JSON ");

				ruleRequestAsAMap = GsonHelper.toMapEntityPreserveNumber(ruleRequest.toString());

			} else if (dataHolderOutput instanceof Map) {
				
				logger.info("Using The Rule Request as a Map From Sidecar Data Holder Output Map ");
				
				ruleRequestAsAMap = (Map) dataHolderOutput;
			}
			
			dataHolderToUse.addInput(dataHolder.retriveInput().toString());
			
			dataHolderToUse.addOutput(ruleRequestAsAMap);
			
			ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, dataHolderToUse);
		
		}  
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR, ruleRequestAsAMap);
		
		PraeceptaRuleGroup ruleGroup = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroup) && !PraeceptaObjectHelper.isObjectEmpty(ruleGroup.getPraeceptaCriterias())) {
			
			Collection<PraeceptaCriteria> praeceptaCriterias = ruleGroup.getPraeceptaCriterias();
			
			Map<String, Object> ruleGroupMetaData = new HashMap<>();
			
			ruleGroupMetaData.put(PraeceptaRuleGroupMetaData.NUMBER_OF_CRITERIAS.name(), praeceptaCriterias.size());
			ruleGroupMetaData.put(PraeceptaRuleGroupMetaData.CLIENT_NAME.name(), ruleGroup.getClientName());
			ruleGroupMetaData.put(PraeceptaRuleGroupMetaData.APP_NAME.name(), ruleGroup.getAppName());
			ruleGroupMetaData.put(PraeceptaRuleGroupMetaData.RULE_GROUP_ID.name(), ruleGroup.getUniqueId());
			ruleGroupMetaData.put(PraeceptaRuleGroupMetaData.RULE_GROUP_NAME.name(), ruleGroup.getRuleGroupName());
			
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA, ruleGroupMetaData);
		}
				
		
		logger.debug("Exiting captureExitInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
	}
}
