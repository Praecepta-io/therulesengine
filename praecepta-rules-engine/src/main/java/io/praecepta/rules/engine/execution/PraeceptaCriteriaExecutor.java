package io.praecepta.rules.engine.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.evaluators.IPraeceptaConditionalEvaluator;
import io.praecepta.rules.evaluators.enums.ConditionOperatorToEvaluatorType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.IPraeceptaAction;

public class PraeceptaCriteriaExecutor {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaExecutor.class);
	
	public static void executeCriteriasOfARuleGroup(PraeceptaRequestStore ruleStore, PraeceptaRuleGroup ruleGrpToUse) {
		
		logger.debug("Inside Execute Criterias Of A RuleGroup");
		
		logger.info(" Before Executing Pre Rule Group Side Car");
		
		Boolean ruleValidationStatus =  (Boolean) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS);
		
		String inputRequest = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
		
		GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCar = (GenericPraeceptaInfoTrackerSideCarInjector) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_SIDE_CAR);
		
		if (preRuleGrpSideCar != null) {
			preRuleGrpSideCar.trackAndCaptureInitialInfo(ruleStore);
			
			preRuleGrpSideCar.executeSideCar(ruleStore);

			preRuleGrpSideCar.trackAndCaptureExitInfo(ruleStore);
		}
				
		logger.info(" After Executing Pre Rule Group Side Car");
		
		List<PraeceptaCriteria> praeceptaOrderedCriterias = ruleGrpToUse.getPraeceptaOrderedCriterias();
		
		List<PraeceptaRequestStore> resultCriteriaStores = new ArrayList<>();
		
		praeceptaOrderedCriterias.stream().forEach( eachCriteria -> {
			logger.info(" Preparing the Rule Trigger for Criteria {} ", eachCriteria);
			
			PraeceptaRequestStore eachCriteriaStore = new PraeceptaRequestStore();
			
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_CRITERIA, eachCriteria);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.ORIGINAL_RULE_STORE, ruleStore);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS, ruleValidationStatus);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, ruleGrpToUse);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, inputRequest);
			
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR));
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR));
			
			executeCriteria(eachCriteriaStore, eachCriteria);
			
			resultCriteriaStores.add(eachCriteriaStore);
			
		});
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES, resultCriteriaStores);
		
		logger.info(" Before Executing Post Rule Group Side Car");
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCar = (GenericPraeceptaInfoTrackerSideCarInjector) 
				ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_SIDE_CAR);
		
		if (postRuleGrpSideCar != null) {
			postRuleGrpSideCar.trackAndCaptureInitialInfo(ruleStore);
			
			postRuleGrpSideCar.executeSideCar(ruleStore);

			postRuleGrpSideCar.trackAndCaptureExitInfo(ruleStore);
		}
		
		logger.info(" After Executing Post Rule Group Side Car");
		
		logger.debug(" Finishing Execute Criterias Of A RuleGroup");
	}

	public static void executeCriteria(PraeceptaRequestStore criteriaRuleStore, PraeceptaCriteria criteria) {
		
		logger.debug("Inside Execute Criteria");

		logger.debug(" Getting Predicates for the Rule ");
		
		String ruleName = criteria.getRuleName();
		
		criteriaRuleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_NAME, ruleName);
		
		PraeceptaMultiNestedCondition nestedMultiCondition = criteria.getPredicates();
		
		logger.info(" Predicates to Evaluate {} ", nestedMultiCondition);
		
		Collection<IPraeceptaAction<PraeceptaRequestStore>> actionsToPerform = criteria.getActionToPerform();
		
		logger.info(" Actions to Perform {} ", actionsToPerform);
		
		logger.info(" Before Executing Pre Rule Side Car");
		
		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleRunSideCar = (GenericPraeceptaRuleLevelInfoTrackerSideCarInjector)
				criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR);
		
		if (preRuleRunSideCar != null) {
			preRuleRunSideCar.trackAndCaptureInitialInfo(criteriaRuleStore);
			
			preRuleRunSideCar.executeSideCar(criteriaRuleStore);

			preRuleRunSideCar.trackAndCaptureExitInfo(criteriaRuleStore);
		}
		
		logger.info(" After Executing Pre Rule Side Car");
		
		evaluateARule(nestedMultiCondition, criteriaRuleStore);
		
		logger.info(" Ready to Perform Actions ");

		
		Collection<IPraeceptaAction<PraeceptaRequestStore>> actionsList = null;
		
		// performAction
		if(Boolean.valueOf(criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS).toString())){
			logger.info(" Rule Condition is Status is Success. Capturing List of Success Actions");

			actionsList = criteria.getActionToPerform();
			
		}else {
			logger.info(" Rule Condition is Status is Failure. Capturing List of Failure Actions");

			//actions on condition fail
			actionsList = criteria.getActionToPerformOnFailure();
		}
		logger.info(" Triggering Actions");

		if(!ObjectHelper.isObjectEmpty(actionsList)) {
			actionsList.forEach(action -> {
				action.performAction(criteriaRuleStore);
			});
		}
		
		logger.info(" Before Executing Post Rule Side Car");
		
		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector postRuleSideCar = (GenericPraeceptaRuleLevelInfoTrackerSideCarInjector) 
				criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR);
		
		if (postRuleSideCar != null) {
			postRuleSideCar.trackAndCaptureInitialInfo(criteriaRuleStore);
			
			postRuleSideCar.executeSideCar(criteriaRuleStore);

			postRuleSideCar.trackAndCaptureExitInfo(criteriaRuleStore);
		}
		
		logger.info(" After Executing Post Rule Side Car");
		
		logger.debug("Exiting from Execute Criteria");
	}

	public static void evaluateARule(PraeceptaMultiNestedCondition nestedMultiCondition, PraeceptaRequestStore ruleStore) {
		
		logger.debug("Inside Evaluating A Rule");
		
		if(nestedMultiCondition != null && ruleStore != null) {
			
			logger.info("Inside Evaluating A Nested Multi Condition");
			
			PraeceptaMultiNestedCondition nextMultiNestedCondition = nestedMultiCondition;
			
			boolean currentMultiRuleEvaluationStatus = true;
			
			Boolean multiRuleEvaluationStatus = null;
			
			JoinOperatorType joinOperatorWithMultiNextCondition = null; 
			
			while(nextMultiNestedCondition != null){
				
				logger.info("Running for Multi Nested Condition --> {} ",nextMultiNestedCondition);
				
				PraeceptaMultiCondition parentMultiCondition = nextMultiNestedCondition.getMultiCondition();
				
//				logger.info("Running for Multi Condition --> {} ", parentMultiCondition);
				
				if(parentMultiCondition != null) {
					
					logger.info("Running for Parent Multi Condition --> {} ",parentMultiCondition);
					
					PraeceptaMultiCondition nextMultiCondition = parentMultiCondition;
					
					boolean currentRuleEvaluationStatus = true;

					Boolean ruleEvaluationStatus = null;
					
					JoinOperatorType joinOperatorWithNextCondition = null;
					
					while(nextMultiCondition != null) {
						
						logger.info("Running for Next Multi Condition --> {} ",nextMultiCondition);
						
						PraeceptaSimpleCondition condition = nextMultiCondition.getCondition();
						
						if(condition != null) {
							logger.info("Running for Condition --> {} ",condition);
							
							ConditionOperatorType conditionOperator = condition.getConditionOperator();
							
							// Get the Evaluator Based on the ConditionOperatorType 
							IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> evaluator = ConditionOperatorToEvaluatorType.getEvaluatorByOperatorType(conditionOperator);
							
							if(evaluator != null) {
								logger.info("Evaluator to Run  --> {} ",evaluator);
							// Enrich Condition with Value Holder and Meta Data etc
								Map<String,Object> requestMap = GsonHelper.toEntity(ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST).toString(), Map.class);
								Object fromValue = null;
								Map<String,Object> localRequestMap = requestMap;
								
								if(condition.getSubjectName().contains(".")){
									fromValue = getValueForNestedAttribute(condition.getSubjectName(), localRequestMap, fromValue);
								}else {
									fromValue = requestMap.get(condition.getSubjectName());
								}
								Object toValue = null;
								localRequestMap = requestMap;
								if(condition.getDefinedAttributeToCompare() != null && condition.getDefinedAttributeToCompare().contains(".")){
									toValue = getValueForNestedAttribute(condition.getDefinedAttributeToCompare(), localRequestMap, toValue);
								}else {
									toValue = requestMap.get(condition.getSubjectName());
								}
								if(toValue == null && condition.getValueToCompare() != null){
									toValue = condition.getValueToCompare();
								}
								PraeceptaSimpleCondition.ConditionValueHolder conditionValueHolder = new PraeceptaSimpleCondition.ConditionValueHolder(fromValue, toValue);

								condition.setConditionValueHolder(conditionValueHolder);
								currentRuleEvaluationStatus = evaluator.evaluateTheCondition(condition);
							} else {
								logger.error("No Matching Evaluator Exist for Condition Operator --> {} ",conditionOperator);
//								TO-DO - Change it to false
								currentRuleEvaluationStatus = false;
//							//	currentRuleEvaluationStatus = true;
							}
							
							logger.info("Evaluation Status --> {} ",currentRuleEvaluationStatus);
						}

						if(nextMultiCondition.getNextConditionJoinOperator() != null)
							joinOperatorWithNextCondition = nextMultiCondition.getNextConditionJoinOperator();
						
						if(joinOperatorWithNextCondition != null && ruleEvaluationStatus != null) {
							
							currentRuleEvaluationStatus = ruleStatusWithJoinOperator(currentRuleEvaluationStatus,
									ruleEvaluationStatus, joinOperatorWithNextCondition);
						}
						
						ruleEvaluationStatus = currentRuleEvaluationStatus;
						
						nextMultiCondition = nextMultiCondition.getNextCondition();
						
					}
					
					logger.info("Evaluation Status For A Multi Condition --> {} ",currentRuleEvaluationStatus);
					currentMultiRuleEvaluationStatus = ruleEvaluationStatus;
				}
				
				joinOperatorWithMultiNextCondition = nestedMultiCondition.getNextConditionJoinOperator();
				
				if(joinOperatorWithMultiNextCondition != null && multiRuleEvaluationStatus != null) {
					
					currentMultiRuleEvaluationStatus = ruleStatusWithJoinOperator(currentMultiRuleEvaluationStatus,
							multiRuleEvaluationStatus, joinOperatorWithMultiNextCondition);
				}
				
				logger.info("Multi Rule Evaluation Status With Join Operator --> {} ",currentMultiRuleEvaluationStatus);
				multiRuleEvaluationStatus = currentMultiRuleEvaluationStatus;
				
				nextMultiNestedCondition = nextMultiNestedCondition.getNextMultiCondition();
			}
			logger.info("Final Rule Evaluation Status is --> {} ",multiRuleEvaluationStatus);
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS, multiRuleEvaluationStatus);
		}
		
		logger.debug("Exiting from Rule Evaluation");

	}

	public static Object getValueForNestedAttribute(String condition, Map<String, Object> localRequestMap, Object fromValue) {
		logger.debug(" localRequestMap {}" , localRequestMap);
		logger.debug(" Condition {} and fromValue {}" , condition, fromValue);
		
		String[] st = condition.split("\\.");
		for (int i = 0; i < st.length; i++) {
			if (i != st.length - 1)
				localRequestMap = (Map<String, Object>) localRequestMap.get(st[i]);
			else
				fromValue = localRequestMap.get(st[i]);
			if (localRequestMap == null)
				break;
		}
		
		logger.debug(" Derived Value {}", fromValue); 
		return fromValue;
	}

	private static boolean ruleStatusWithJoinOperator(boolean currentMultiRuleEvaluationStatus,
			Boolean multiRuleEvaluationStatus, JoinOperatorType joinOperatorWithMultiNextCondition) {
		if(JoinOperatorType.AND == joinOperatorWithMultiNextCondition) {
			currentMultiRuleEvaluationStatus = currentMultiRuleEvaluationStatus && multiRuleEvaluationStatus;
		} else if (JoinOperatorType.OR == joinOperatorWithMultiNextCondition) {
			currentMultiRuleEvaluationStatus = currentMultiRuleEvaluationStatus || multiRuleEvaluationStatus;
		}
		return currentMultiRuleEvaluationStatus;
	}

}
