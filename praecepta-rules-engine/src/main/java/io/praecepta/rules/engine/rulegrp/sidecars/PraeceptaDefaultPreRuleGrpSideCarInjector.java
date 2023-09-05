package io.praecepta.rules.engine.rulegrp.sidecars;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaCriteria;

public class PraeceptaDefaultPreRuleGrpSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPreRuleGrpSideCarInjector.class);
			
	public final void captureInInfo(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside captureInInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
		
		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);
		
		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);
		
		if (ruleSpace != null && !ObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps()) && !ObjectHelper.isStringEmpty(ruleGroupToExecute)) {
			Collection<PraeceptaRuleGroup> praeceptaRuleGrps = ruleSpace.getPraeceptaRuleGrps();
			
			Optional<PraeceptaRuleGroup> ruleGrpToUse = praeceptaRuleGrps.stream().filter( eachRuleGrp -> !ObjectHelper.isStringEmpty(eachRuleGrp.getRuleGroupName()) && eachRuleGrp.getRuleGroupName().equals(ruleGroupToExecute)).findFirst();
			
			if(ruleGrpToUse.isPresent()) {
				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, ruleGrpToUse.get());
			}
		}
		
		logger.debug("Exiting captureInInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
	}
	
	public final void captureExitInfo(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside captureExitInfo of PraeceptaDefaultPreRuleGrpSideCarInjector");
		
		PraeceptaRuleGroup ruleGroup = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);
		
		if (!ObjectHelper.isObjectEmpty(ruleGroup) && !ObjectHelper.isObjectEmpty(ruleGroup.getPraeceptaCriterias())) {
			
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
