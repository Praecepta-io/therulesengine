package io.praecepta.rules.engine.execution;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import org.springframework.beans.factory.annotation.Value;

public class PraeceptaBasicRuleExecutionEngine extends AbstractPraeceptaRuleExecutionEngine{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaBasicRuleExecutionEngine.class);

	@Override
	protected void triggerRules(PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside triggerRules");
		
		Boolean ruleValidationStatus =  (Boolean) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS);
		
		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);

		String inputRequest = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
		
		Map<String, Object> ruleGroupMetaData = (Map<String, Object>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA);
		
		if(ruleValidationStatus && ruleGrpToUse != null  && ruleGroupMetaData != null && !PraeceptaObjectHelper.isStringEmpty(inputRequest)) {
			
			logger.info("Triggering Rules for Rule Group {} ", ruleGrpToUse.getRuleGroupName());
			
			Integer numberOfCriterias = (Integer) ruleGroupMetaData.get(PraeceptaRuleGroupMetaData.NUMBER_OF_CRITERIAS.name());
			
			logger.info(" Number of Criterias to run is - {}", numberOfCriterias);
			if(numberOfCriterias != null && numberOfCriterias > 0) {

				conductRuleGroupExecution(ruleStore, ruleGrpToUse);
			}
			
		}
	}

	protected void conductRuleGroupExecution(PraeceptaRequestStore ruleStore, PraeceptaRuleGroup ruleGrpToUse) {
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE, this);
		
		PraeceptaCriteriaExecutor.executeCriteriasOfARuleGroup(ruleStore, ruleGrpToUse);
		
	}
	
	@Override
	protected void enrichRuleStoreForExecution(PraeceptaRequestStore ruleStore) {
	}

	@Override
	protected void postExecutionChecks(PraeceptaRequestStore ruleStore) {
	}

	@Override
	protected boolean performAdditionalRuleSpaceChecks(PraeceptaRuleSpace ruleSpace) {
		return super.performAdditionalRuleSpaceChecks(ruleSpace);
	}

}
