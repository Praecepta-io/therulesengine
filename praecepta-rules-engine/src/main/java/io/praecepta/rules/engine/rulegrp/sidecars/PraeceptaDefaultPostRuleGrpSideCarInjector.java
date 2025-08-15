package io.praecepta.rules.engine.rulegrp.sidecars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaActionResultDetails;
import io.praecepta.rules.model.PraeceptaConditionResult;
import io.praecepta.rules.model.PraeceptaConditionResult.CONDITION_RESULT;
import io.praecepta.rules.model.PraeceptaRuleResult;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaDefaultPostRuleGrpSideCarInjector extends GenericPraeceptaInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDefaultPostRuleGrpSideCarInjector.class);
	
	private static final String FailedActionTypeExecutedMessage = "Executed Failure Actions";
	private static final String SuccessActionTypeExecutedMessage = "Executed Success Actions";
			
	public final void captureInInfo(PraeceptaRequestStore ruleStore) {
		
		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);
		
		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);
		
		logger.debug("Capturing the Rule Engine Results for Space {} and Rule Grp Name {} ",ruleSpace.getRuleSpaceKey(), ruleGroupToExecute);
		
		if (ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps()) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupToExecute)) {
			
			List<PraeceptaRequestStore> resultCriteriaStores = (List<PraeceptaRequestStore>) 
					ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(resultCriteriaStores)) {
				
				logger.debug("Result Rule Criteria Stores are not empty and the size is {}", resultCriteriaStores.size());
				
				List<PraeceptaRuleResult> resultInfoForEveryRule = new ArrayList<>();
				
				resultCriteriaStores.forEach( eachStoreForARule -> {
					
					String ruleName = (String) eachStoreForARule.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME);
					
					logger.debug("Capturing the Rule Execution Result for Rule Name  - {}", ruleName);
					
					// Capturing the Criteria Satisfied or Not details
					
					PraeceptaConditionResult conditionResult = captureConditionResultInfo(eachStoreForARule);
					
					// Capturing the Action Details for each action executed for a Rule
					List<PraeceptaActionResultDetails> allActionResultDetails = captureActionResultInfo(eachStoreForARule);
					
					// Build a Result for a Rule
					PraeceptaRuleResult resultInfoForARule = new PraeceptaRuleResult(ruleName, conditionResult, allActionResultDetails);
					
					resultInfoForEveryRule.add(resultInfoForARule);
					
					logger.debug("Finished Capturing the Result for Rule Name  - {}", ruleName);
					
				});
				
				logger.debug(" Rules Response After the Full Rule Group Execution is --> {} ", GsonHelper.toJsonPreserveNumber(resultInfoForEveryRule));
				
				// Assign/Stamp the Action Name and Action Values to Side Car Output Holder
				assignResultToRequestForSidcarHolder(ruleStore, resultInfoForEveryRule);
				
				logger.debug(" Rules Response After the Full Rule Group Execution And Applying The Action Result is --> {} ", GsonHelper.toJsonPreserveNumber(resultInfoForEveryRule));

				ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, resultInfoForEveryRule);
				
				logger.debug("Exiting after Capturing the Result for Every Rule");
			}		
		}
	}

	private void assignResultToRequestForSidcarHolder(PraeceptaRequestStore ruleStore,
			List<PraeceptaRuleResult> resultInfoForEveryRule) {
		
		logger.debug(" Inside assignResultToRequestForSidcarHolder ");

		if (!PraeceptaObjectHelper.isObjectEmpty(resultInfoForEveryRule)) {
			
			logger.debug(" Result Info is Available to Assign Values of Action "); 

//			PraeceptaSideCarDataHolder<?, Map<String, Object>> dataHolder = (PraeceptaSideCarDataHolder<?, Map<String, Object>>) ruleStore
			PraeceptaSideCarDataHolder<?, ?> dataHolder = (PraeceptaSideCarDataHolder<?, Map<String, Object>>) ruleStore
					.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER);

			Object dataToHolderOutPutFromSidecars = dataHolder.retriveOutput();
			
			// Side Car Holder Must be a Map to Enrich with Action Data. It has the be either a Parser or an Enricher Output 
			if(!PraeceptaObjectHelper.isObjectEmpty(dataToHolderOutPutFromSidecars) && dataToHolderOutPutFromSidecars instanceof Map ) {
				
				Map<String, Object> dataToEnrichWithActionResults = (Map<String, Object>)dataToHolderOutPutFromSidecars;

				resultInfoForEveryRule.forEach(resultInfoForARule -> {
					
					logger.debug("  Assign Values of Action to Result for Rule name {} ", resultInfoForARule.getRuleName()); 
	
					List<PraeceptaActionResultDetails> allActionResultDetailsForARule = resultInfoForARule
							.getAllActionResultDetails();
	
					if (!PraeceptaObjectHelper.isObjectEmpty(allActionResultDetailsForARule)) {
	
						allActionResultDetailsForARule.forEach(eachActionDetail -> {
							
							logger.debug("  Capturing Assign Values of Action for Action name {} ", eachActionDetail.getActionName()); 
							logger.debug("  Action Attribute - {} and Value - {} ", eachActionDetail.getActionAttributeName(), eachActionDetail.getActionedValue()); 
	
							dataToEnrichWithActionResults.put(eachActionDetail.getActionAttributeName(),
									eachActionDetail.getActionedValue());
						});
	
					}
	
				});
			}
		}
		
		logger.debug(" Exiting assignResultToRequestForSidcarHolder ");

	}

	private List<PraeceptaActionResultDetails> captureActionResultInfo(PraeceptaRequestStore eachStoreForARule) {
		
		logger.debug("Capturing the Action Result ");
		
		List<PraeceptaActionResultDetails> allActionResultDetails = new ArrayList<>();
		
		Map<String, PraeceptaActionHolder> actionHolders = (Map<String, PraeceptaActionHolder>) 
				eachStoreForARule.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(actionHolders)) {
			
			actionHolders.forEach( (actionName, ruleActionHolder) -> {
				
				PraeceptaActionResultDetails actionResultDetails = 
						new PraeceptaActionResultDetails(actionName, ruleActionHolder.getActionResult(), ruleActionHolder.getActionAttributeName() , ruleActionHolder.getActionedValue());
				
				allActionResultDetails.add(actionResultDetails);
			});
			
		}
		
		logger.debug("Exiting from Capturing the Action Result ");
		
		return allActionResultDetails;
	}

	private PraeceptaConditionResult captureConditionResultInfo(PraeceptaRequestStore eachStoreForARule) {
		
		logger.debug("Capturing the Condition Result ");
		
		CONDITION_RESULT conditionResultStatus = CONDITION_RESULT.NOT_SATISFIED;
		
		String actionTypeExecutedMsg = FailedActionTypeExecutedMessage;
		
		Boolean ruleConditionStatus = (Boolean) eachStoreForARule.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleConditionStatus) && ruleConditionStatus) {
			conditionResultStatus = CONDITION_RESULT.SATISFIED;
			actionTypeExecutedMsg = SuccessActionTypeExecutedMessage;
		}
		
		PraeceptaConditionResult conditionResult = new PraeceptaConditionResult();
		
		conditionResult.setResult(conditionResultStatus);
		conditionResult.setResultMessage(actionTypeExecutedMsg);
		
		logger.debug("Exiting from Capturing the Condition Result ");
		
		return conditionResult;
	}
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		List<PraeceptaRequestStore> resultCriteriaStores = (List<PraeceptaRequestStore>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(resultCriteriaStores)) {
			
		}
		
		super.executeSideCar(ruleStore);
		
	}
	
	public final void captureExitInfo(PraeceptaRequestStore ruleStore) {
		
		
		
	}
}
