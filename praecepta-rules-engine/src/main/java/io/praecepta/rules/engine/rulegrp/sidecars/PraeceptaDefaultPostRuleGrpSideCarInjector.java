package io.praecepta.rules.engine.rulegrp.sidecars;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;

public class PraeceptaDefaultPostRuleGrpSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPostRuleGrpSideCarInjector.class);
			
	public final void captureInInfo(PraeceptaRequestStore ruleStore) {
		
		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);
		
		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);
		
		if (ruleSpace != null && !ObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps()) && !ObjectHelper.isStringEmpty(ruleGroupToExecute)) {
			
			List<PraeceptaRequestStore> resultCriteriaStores = (List<PraeceptaRequestStore>) 
					ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES);
			
			if(!ObjectHelper.isObjectEmpty(resultCriteriaStores)) {
				
			}
		}
	}
	
	public final void captureExitInfo(PraeceptaRequestStore ruleStore) {
		
	}
}
