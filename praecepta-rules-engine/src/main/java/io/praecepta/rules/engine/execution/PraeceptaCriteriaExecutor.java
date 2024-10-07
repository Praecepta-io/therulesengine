package io.praecepta.rules.engine.execution;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;
import io.praecepta.rest.client.PraeceptaWsRestClient;
import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.model.PraeceptaActionResultDetails;
import io.praecepta.rules.model.PraeceptaConditionResult;
import io.praecepta.rules.model.PraeceptaRuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleGroupExecutionAuditPoint;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
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
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

import static io.praecepta.rules.enums.PraeceptaRuleRequestStoreType.*;

public class PraeceptaCriteriaExecutor {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaCriteriaExecutor.class);

	static ExecutorService executor = Executors.newFixedThreadPool(1);

	static String PATH_PARAM_SPACE_NAME = "spacename";

	static String PATH_PARAM_CLIENT_ID = "clientid";

	static String PATH_PARAM_APP_NAME = "appname";

	static String PATH_PARAM_VERSION = "version";

	static String PATH_PARAM_GROUP_NAME = "groupname";


	private static final String SuccessActionTypeExecutedMessage = "Executed Success Actions";
	private static final String FailedActionTypeExecutedMessage = "Executed Failure Actions";

	public static void executeCriteriasOfARuleGroup(PraeceptaRequestStore ruleStore, PraeceptaRuleGroup ruleGrpToUse) {

		logger.debug("Inside Execute Criterias Of A RuleGroup");

		logger.info(" Before Executing Pre Rule Group Side Car");

		Boolean ruleValidationStatus =  (Boolean) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS);

		String inputRequest = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);

		PraeceptaSideCarDataHolder<String, String> inputDataHolder = getInputDataHolder(inputRequest);

		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, inputDataHolder);

		GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCar = (GenericPraeceptaInfoTrackerSideCarInjector)
				ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_SIDE_CAR);

		String traceId = UUID.randomUUID().toString();

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_TRACE_ID, traceId);

		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);

		// Start Pre Rule GROUP Execution Audit Point
		executor.submit(() -> publishRuleExecutionAudit( EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE, buildPreRuleGrpExecutionPoint(ruleStore), (String)ruleStore.fetchFromPraeceptaStore(RULE_EXECUTION_AUDIT_URL)));

		if (preRuleGrpSideCar != null) {

			performSideCarActivity(ruleStore, preRuleGrpSideCar);
		}

		Object ruleRequestAsAMap = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);

		logger.info(" After Executing Pre Rule Group Side Car");

		logger.info(" About to Rule Trigger for Rule Group {} ", ruleGroupToExecute);

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
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR, ruleRequestAsAMap);

			// Adding a new Data Holder with Input received for every Rule to make sure We have Thread Safe and One rule change of Data Holder will not impact Other rule
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, getInputDataHolder(inputRequest));

			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR));
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR));

			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE));
			//TODO AUDIT BEFORE RULE
			String spanId = UUID.randomUUID().toString();
			executor.submit(() -> publishRuleExecutionAudit( EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE,buildPreRuleExecutionPoint(ruleStore, spanId, eachCriteria),  (String)ruleStore.fetchFromPraeceptaStore(RULE_EXECUTION_AUDIT_URL)));
			executeCriteria(eachCriteriaStore, eachCriteria);

			executor.submit(() -> publishRuleExecutionAudit( EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE, buildPostRuleExecutionPoint(eachCriteriaStore, spanId, eachCriteria, ruleStore),  (String)ruleStore.fetchFromPraeceptaStore(RULE_EXECUTION_AUDIT_URL)));

			resultCriteriaStores.add(eachCriteriaStore);

		});

		ruleStore.upsertToPraeceptaStore(CRITERIA_RULE_STORES, resultCriteriaStores);

		logger.info(" Before Executing Post Rule Group Side Car");
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCar = (GenericPraeceptaInfoTrackerSideCarInjector)
				ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_SIDE_CAR);

		if (postRuleGrpSideCar != null) {
			 ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);

			logger.info(" About to Perform Post Rule Grp Side car for Rule Group {} ", ruleGroupToExecute);
			performSideCarActivity(ruleStore, postRuleGrpSideCar);
		}
		executor.submit(() -> publishRuleExecutionAudit( EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE,buildPostRuleGrpExecutionPoint(ruleStore), (String)ruleStore.fetchFromPraeceptaStore(RULE_EXECUTION_AUDIT_URL)));
		logger.info(" After Executing Post Rule Group Side Car");

		logger.debug(" Finishing Execute Criterias Of A RuleGroup");
	}

	private static PraeceptaExecutionAuditPoint buildPreRuleGrpExecutionPoint(PraeceptaRequestStore ruleStore) {

		String traceId = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_TRACE_ID);

		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);

		PraeceptaExecutionAuditPoint executionTraceDtoWithPreRuleGrpDetails = new PraeceptaExecutionAuditPoint();

		LocalDateTime ldt = LocalDateTime.now();
		Date ruleGrpStartTime = addMinutesToDate(ldt, 0);

		populatePreRuleGroupAuditDetails(traceId, ruleGrpToUse, executionTraceDtoWithPreRuleGrpDetails, ruleGrpStartTime);

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_GROUP_START_TIME, ruleGrpStartTime);

		return executionTraceDtoWithPreRuleGrpDetails;
	}

	private static PraeceptaExecutionAuditPoint buildPostRuleGrpExecutionPoint(PraeceptaRequestStore ruleStore) {

		String traceId = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_TRACE_ID);

		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);

		PraeceptaExecutionAuditPoint executionTraceDtoWithPostRuleGroupDetails = new PraeceptaExecutionAuditPoint();

		PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetails = executionTraceDtoWithPostRuleGroupDetails
				.getPostRuleGroupExecutionAuditDetails();

		postRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo(ruleGrpToUse));

		postRuleGroupExecutionAuditDetails.setTraceId(traceId);

		Date ruleGrpStartTime = (Date) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_GROUP_START_TIME);

		postRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);

		populatePreRuleGroupAuditDetails(traceId, ruleGrpToUse, executionTraceDtoWithPostRuleGroupDetails, ruleGrpStartTime);

		LocalDateTime ldt = LocalDateTime.now();

		Date ruleGrpEndTime = addMinutesToDate(ldt, 0);

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_GROUP_END_TIME, ruleGrpEndTime);

		List<PraeceptaRequestStore> resultCriteriaStores = (List<PraeceptaRequestStore>) ruleStore.fetchFromPraeceptaStore(CRITERIA_RULE_STORES);

		List<PraeceptaRuleResult> ruleExecutionAuditPointList = new ArrayList<>();
		AtomicInteger successRuleCount = new AtomicInteger(0);
		AtomicInteger failureRuleCount = new AtomicInteger(0);
		resultCriteriaStores.stream().forEach(praeceptaRequestStore->{
			PraeceptaConditionResult result = captureConditionResultInfo(praeceptaRequestStore);
			if(result.getResult().compareTo(PraeceptaConditionResult.CONDITION_RESULT.SATISFIED) == 0){
				successRuleCount.incrementAndGet();
			}else{
				failureRuleCount.incrementAndGet();
			}
			List<PraeceptaActionResultDetails> allActionResultDetails = captureActionResultInfo(praeceptaRequestStore);
			PraeceptaRuleResult praeceptaRuleResult = new PraeceptaRuleResult((String) praeceptaRequestStore.fetchFromPraeceptaStore(RULE_NAME), result, allActionResultDetails);
			ruleExecutionAuditPointList.add(praeceptaRuleResult);
		});
		postRuleGroupExecutionAuditDetails.setFailureRulesCount(String.valueOf(failureRuleCount.get()));
		postRuleGroupExecutionAuditDetails.setSuccessRulesCount(String.valueOf(successRuleCount.get()));
		postRuleGroupExecutionAuditDetails.setRuleExecutionAuditPoints(ruleExecutionAuditPointList);

		postRuleGroupExecutionAuditDetails.setEndTime(ruleGrpEndTime);

		return executionTraceDtoWithPostRuleGroupDetails;
	}

	private static void populatePreRuleGroupAuditDetails(String traceId, PraeceptaRuleGroup ruleGrpToUse, PraeceptaExecutionAuditPoint executionTraceDtoWithPostRuleGroupDetails, Date ruleGrpStartTime) {
		PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetails = executionTraceDtoWithPostRuleGroupDetails
				.getPreRuleGroupExecutionAuditDetails();

		preRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo(ruleGrpToUse));

		preRuleGroupExecutionAuditDetails.setTraceId(traceId);

		preRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);
	}

	private static PraeceptaExecutionAuditPoint buildPreRuleExecutionPoint(PraeceptaRequestStore ruleStore, String spanId, PraeceptaCriteria criteriaStore) {

		String traceId = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_TRACE_ID);

		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);

		PraeceptaExecutionAuditPoint executionTraceDtoWithPreRuleDetails = new PraeceptaExecutionAuditPoint();

		PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetails = executionTraceDtoWithPreRuleDetails
				.getPreRuleExecutionAuditDetails();

		preRuleExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo(ruleGrpToUse));

		preRuleExecutionAuditDetails.setTraceId(traceId);

		preRuleExecutionAuditDetails.setSpanId(spanId);

		LocalDateTime ldt = LocalDateTime.now();
		Date ruleStartTime = addMinutesToDate(ldt, 0);

		PraeceptaRuleResult praeceptaRuleResult = new PraeceptaRuleResult(criteriaStore.getRuleName(), null, null);
		preRuleExecutionAuditDetails.setRuleExecutionAuditPoint(praeceptaRuleResult);

		preRuleExecutionAuditDetails.setStartTime(ruleStartTime);

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_START_TIME, ruleStartTime);

		Date ruleGrpStartTime = (Date) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_GROUP_START_TIME);

		populatePreRuleGroupAuditDetails(traceId, ruleGrpToUse, executionTraceDtoWithPreRuleDetails, ruleGrpStartTime);

		return executionTraceDtoWithPreRuleDetails;
	}

	private static PraeceptaExecutionAuditPoint buildPostRuleExecutionPoint(PraeceptaRequestStore eachCriteriaStore, String spanId, PraeceptaCriteria criteriaStore, PraeceptaRequestStore ruleStore) {

		String traceId = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_TRACE_ID);

		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP);

		PraeceptaExecutionAuditPoint executionTraceDtoWithPostRuleDetails = new PraeceptaExecutionAuditPoint();

		PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetails = executionTraceDtoWithPostRuleDetails
				.getPostRuleExecutionAuditDetails();

		PraeceptaConditionResult result = captureConditionResultInfo(eachCriteriaStore);
		List<PraeceptaActionResultDetails> allActionResultDetails = captureActionResultInfo(eachCriteriaStore);
		PraeceptaRuleResult praeceptaRuleResult = new PraeceptaRuleResult(criteriaStore.getRuleName(), result, allActionResultDetails);

		postRuleExecutionAuditDetails.setRuleExecutionAuditPoint(praeceptaRuleResult);

		postRuleExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo(ruleGrpToUse));

		postRuleExecutionAuditDetails.setTraceId(traceId);

		postRuleExecutionAuditDetails.setSpanId(spanId);

		Date ruleStartTime = (Date) eachCriteriaStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_START_TIME);

		Date ruleGrpStartTime = (Date) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_GROUP_START_TIME);

		populatePreRuleGroupAuditDetails(traceId, ruleGrpToUse, executionTraceDtoWithPostRuleDetails, ruleGrpStartTime);

		postRuleExecutionAuditDetails.setStartTime(ruleStartTime);

		LocalDateTime ldt = LocalDateTime.now();
		Date ruleEndTime = addMinutesToDate(ldt, 0);

		postRuleExecutionAuditDetails.setEndTime(ruleEndTime);

		eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_END_TIME, ruleEndTime);

		return executionTraceDtoWithPostRuleDetails;
	}

	private static RuleGroupInfo getRuleGroupInfo(PraeceptaRuleGroup ruleGrpToUse) {

		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();

		ruleGroupInfo.setRuleGroupName(ruleGrpToUse.getRuleGroupName());

		RuleSpaceInfo ruleSpaceInfo = getRuleSpaceInfo(ruleGrpToUse);

		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;
	}

	private static RuleSpaceInfo getRuleSpaceInfo(PraeceptaRuleGroup ruleGrpToUse) {

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		ruleSpaceInfo.setSpaceName(ruleGrpToUse.getRuleSpaceKey().getSpaceName());
		ruleSpaceInfo.setClientId(ruleGrpToUse.getRuleSpaceKey().getClientId());
		ruleSpaceInfo.setAppName(ruleGrpToUse.getRuleSpaceKey().getAppName());
		ruleSpaceInfo.setVersion(ruleGrpToUse.getRuleSpaceKey().getVersion());

		return ruleSpaceInfo;
	}

	private static Date addMinutesToDate(LocalDateTime ldt, long minutes) {

		LocalDateTime ldtAfterAdd = ldt.plus(minutes, ChronoUnit.MINUTES);

		ZoneId zoneId = ZoneId.systemDefault();
		ZoneOffset zoneOffsetAfterAdd = zoneId.getRules().getOffset(ldtAfterAdd);

		Instant instantAfterAdd = ldtAfterAdd.toInstant(zoneOffsetAfterAdd);

		Date dateToReturn = Date.from(instantAfterAdd);

		System.out.println(dateToReturn);

		return dateToReturn;
	}

	private static PraeceptaSideCarDataHolder<String, String> getInputDataHolder(String inputRequest) {
		// Adding this Input Data Holder to Use it in the Sidecars
		PraeceptaSideCarDataHolder<String, String> inputDataHolder = new PraeceptaSideCarDataHolder<>();

		// Adding both Input and Output with the same value But, in the Sidecars Pre proces, better to use the Out Param to make the sequence of Side cars run
		inputDataHolder.addInput(inputRequest);
		inputDataHolder.addOutput(inputRequest);
		return inputDataHolder;
	}

	public static void executeCriteria(PraeceptaRequestStore criteriaRuleStore, PraeceptaCriteria criteria) {

		logger.debug("Inside Execute Criteria");

		logger.debug(" Getting Predicates for the Rule ");

		String ruleName = criteria.getRuleName();

		criteriaRuleStore.upsertToPraeceptaStore(RULE_NAME, ruleName);

		PraeceptaMultiNestedCondition nestedMultiCondition = criteria.getPredicates();

		logger.info(" Predicates to Evaluate {} ", nestedMultiCondition);
		
		/*Collection<PraeceptaActionDetails> actionsToPerform = criteria.getActionToPerform();
		
		logger.info(" Actions to Perform {} ", actionsToPerform);*/

		logger.info(" Before Executing Pre Rule Side Car");

		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleRunSideCar = (GenericPraeceptaRuleLevelInfoTrackerSideCarInjector)
				criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR);

		if (preRuleRunSideCar != null) {
			performSideCarActivity(criteriaRuleStore, preRuleRunSideCar);
		}

		logger.info(" After Executing Pre Rule Side Car");

		evaluateARule(nestedMultiCondition, criteriaRuleStore);

		logger.info(" Ready to Perform Actions ");


		Collection<PraeceptaActionDetails> simpleActionsList = null;

		// performAction
		if(Boolean.valueOf(criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS).toString())){
			logger.info(" Rule Condition is Status is Success. Capturing List of Success Actions");

			simpleActionsList = criteria.getActionToPerform();

		}else {
			logger.info(" Rule Condition is Status is Failure. Capturing List of Failure Actions");

			//actions on condition fail
			simpleActionsList = criteria.getActionToPerformOnFailure();
		}
		logger.info(" Triggering Actions");

		AbstractPraeceptaRuleExecutionEngine executionEngine = (AbstractPraeceptaRuleExecutionEngine) criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE);

		if(!PraeceptaObjectHelper.isObjectEmpty(simpleActionsList)) {
			simpleActionsList.forEach(simpleAction -> {

				PraeceptaAbstractAction actionToPerform = null;

				if(!PraeceptaObjectHelper.isObjectEmpty(executionEngine)) {
					actionToPerform = executionEngine.getPraeceptaAction(simpleAction);
				} else {
					actionToPerform = getPraeceptaAbstractAction(simpleAction);
				}
//				PraeceptaAbstractAction actionToPerform = executionEngine.getPraeceptaAction(simpleAction);//getPraeceptaAbstractAction(simpleAction);

				actionToPerform.performAction(criteriaRuleStore);
			});
		}

		logger.info(" Before Executing Post Rule Side Car");

		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector postRuleSideCar = (GenericPraeceptaRuleLevelInfoTrackerSideCarInjector)
				criteriaRuleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR);

		if (postRuleSideCar != null) {
			performSideCarActivity(criteriaRuleStore, postRuleSideCar);
		}

		logger.info(" After Executing Post Rule Side Car");

		logger.debug("Exiting from Execute Criteria");
	}

	private static PraeceptaAbstractAction getPraeceptaAbstractAction(PraeceptaActionDetails actionInfo) {
		PraeceptaAbstractAction valueAssignAction = new PraeceptaValueAssignAction();
		valueAssignAction.setActionStrategy(actionInfo.getActionStrategy());
		valueAssignAction.setActionAttributeName(actionInfo.getActionAttributeName());
		valueAssignAction.setValueToAssign(actionInfo.getValueToAssign());
		valueAssignAction.setSourceValueAttributeName(actionInfo.getSourceValueAttributeName());
		valueAssignAction.setParameters(actionInfo.getAdditionalParameters());
		return valueAssignAction;
	}

	private static void performSideCarActivity(PraeceptaRequestStore criteriaRuleStore,
			IPraeceptaInfoTrackerSideCarInjector sideCarToRun) {

		sideCarToRun.trackAndCaptureInitialInfo(criteriaRuleStore);

		sideCarToRun.executeSideCar(criteriaRuleStore);

		sideCarToRun.trackAndCaptureExitInfo(criteriaRuleStore);

	}

	public static void evaluateARule(PraeceptaMultiNestedCondition nestedMultiCondition, PraeceptaRequestStore ruleStore) {

		logger.debug("Inside Evaluating A Rule");

		boolean multiRuleEvaluationStatus = stimulateARule(nestedMultiCondition, ruleStore);

		logger.info("Final Rule Evaluation Status is --> {} ",multiRuleEvaluationStatus);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS, multiRuleEvaluationStatus);

		logger.debug("Exiting from Rule Evaluation");

	}

	public static boolean evaluateASimpleCondition(PraeceptaSimpleCondition simpleCondition, PraeceptaRequestStore ruleStore) {

		boolean simpleConditionResult = false;

		logger.info(" Triggering a Simple Condition ");

		if (simpleCondition != null) {

			ConditionOperatorType conditionOperator = simpleCondition.getConditionOperator();

			// Get the Evaluator Based on the ConditionOperatorType
			IPraeceptaConditionalEvaluator<PraeceptaSimpleCondition> evaluator = ConditionOperatorToEvaluatorType
					.getEvaluatorByOperatorType(conditionOperator);

			if (evaluator != null) {

				// Build the Value Holder Here 
				buildConditionValueHolder(simpleCondition, ruleStore);

				simpleConditionResult = evaluator.evaluateTheCondition(simpleCondition);
			}

		}

		logger.info(" Simple Rule Result --> {} ", simpleConditionResult);

		return simpleConditionResult;
	}

	public static boolean evaluateAMultiCondition(PraeceptaMultiCondition multiCondition, PraeceptaRequestStore ruleStore) {

		boolean multiConditionResult = false;

		logger.info(" Triggering a Multi Condition ");

		if (multiCondition != null) {

			PraeceptaSimpleCondition conditionToEvaluate = multiCondition.getCondition();

			boolean previousSimpleConditionResult = evaluateASimpleCondition(conditionToEvaluate, ruleStore);

			while(conditionToEvaluate.getNextCondition() != null && conditionToEvaluate.getNextConditionJoinOperator() != null) {

				JoinOperatorType joinOperatorToUse = conditionToEvaluate.getNextConditionJoinOperator();
				conditionToEvaluate = conditionToEvaluate.getNextCondition();

				boolean currentSimpleConditionResult = evaluateASimpleCondition(conditionToEvaluate, ruleStore);

				previousSimpleConditionResult = statusWithJoinOperator(currentSimpleConditionResult, previousSimpleConditionResult,
						joinOperatorToUse);
			}

			multiConditionResult = previousSimpleConditionResult;

		}
		logger.info(" Multi Condition Result --> {} ", multiConditionResult);

		return multiConditionResult;
	}

	public static boolean evaluateAMultiNestedCondition(PraeceptaMultiNestedCondition multiNestedCondition, PraeceptaRequestStore ruleStore) {

		logger.info(" Triggering a Multi Nested Condition ");

		boolean multiNestedConditionResult = false;

		if (multiNestedCondition != null) {

			PraeceptaMultiCondition multiConditionToEvaluate = multiNestedCondition.getMultiCondition();

			boolean previousMultiConditionResult = evaluateAMultiCondition(multiConditionToEvaluate, ruleStore);

			while(multiConditionToEvaluate.getNextMultiCondition() != null && multiConditionToEvaluate.getNextConditionJoinOperator() != null) {

				JoinOperatorType joinOperatorToUse = multiConditionToEvaluate.getNextConditionJoinOperator();
				multiConditionToEvaluate = multiConditionToEvaluate.getNextMultiCondition();

				boolean currentMultiConditionResult = evaluateAMultiCondition(multiConditionToEvaluate, ruleStore);

				previousMultiConditionResult = statusWithJoinOperator(currentMultiConditionResult, previousMultiConditionResult,
						joinOperatorToUse);
			}

			multiNestedConditionResult = previousMultiConditionResult;

		}
		logger.info(" Nested Multi Condition Result --> {} ", multiNestedConditionResult);

		return multiNestedConditionResult;
	}

	public static boolean stimulateARule(PraeceptaMultiNestedCondition multiNestedCondition, PraeceptaRequestStore ruleStore) {

		logger.info(" Triggering a Criteria ");

		boolean criteriaResult = false;

		if (multiNestedCondition != null) {

			PraeceptaMultiNestedCondition multiNestedConditionToEvaluate = multiNestedCondition;

			boolean previousMultiNestedConditionResult = evaluateAMultiNestedCondition(multiNestedConditionToEvaluate, ruleStore);

			while(multiNestedConditionToEvaluate.getNextMultiNestedCondition() != null && multiNestedConditionToEvaluate.getNextConditionJoinOperator() != null) {

				JoinOperatorType joinOperatorToUse = multiNestedConditionToEvaluate.getNextConditionJoinOperator();
				multiNestedConditionToEvaluate = multiNestedConditionToEvaluate.getNextMultiNestedCondition();

				boolean currentMultiNestedConditionResult = evaluateAMultiNestedCondition(multiNestedConditionToEvaluate, ruleStore);

				previousMultiNestedConditionResult = statusWithJoinOperator(currentMultiNestedConditionResult, previousMultiNestedConditionResult,
						joinOperatorToUse);
			}

			criteriaResult = previousMultiNestedConditionResult;

		}
		logger.info(" Criteria Result --> {} ", criteriaResult);

		return criteriaResult;

	}

	private static boolean statusWithJoinOperator(boolean currentConditionResult,
			boolean previousConditionResult, JoinOperatorType joinOperatorForACondition) {

		logger.debug(" Join Operator - {} ", joinOperatorForACondition);

		logger.debug(" Performing a Condition Evaluation for currentConditionResult - {} and previousConditionResult {} "
				, currentConditionResult, previousConditionResult);
		boolean statusAfterJoinOperator = true;

		if(JoinOperatorType.AND == joinOperatorForACondition) {
			statusAfterJoinOperator = previousConditionResult && currentConditionResult;
		} else if (JoinOperatorType.OR == joinOperatorForACondition) {
			statusAfterJoinOperator = previousConditionResult || currentConditionResult;
		}

		return statusAfterJoinOperator;
	}

	@SuppressWarnings("unused")
	public static void buildConditionValueHolder(PraeceptaSimpleCondition condition, PraeceptaRequestStore ruleStore){

		logger.info(" Building Condition value Holder ");


		if(!PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			Object ruleRequest = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
			Object ruleRequestAsAMap = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);

			Map<String,Object> requestMap = null;
			boolean reQuestAvailableAsAMap = false;

			if(!PraeceptaObjectHelper.isObjectEmpty(ruleRequestAsAMap) && ruleRequestAsAMap instanceof Map) {
				requestMap = (Map<String, Object>)ruleRequestAsAMap;
				reQuestAvailableAsAMap = true;

				logger.info(" Rules Request is Available as a Key Value Pair");
			}

			if(!reQuestAvailableAsAMap && !PraeceptaObjectHelper.isObjectEmpty(ruleRequest)) {

				logger.info(" Rules Request is Available as a String");

				if(GsonHelper.isValidJson(ruleRequest.toString())) {
					requestMap =
							GsonHelper.toMapEntityPreserveNumber(ruleRequest.toString());

					ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR, requestMap);
				} else {
					logger.error(" Rules Request has to be either a Valid JSON or Provided as a Key Value Pair");
					logger.error(" Unable to Perform the Condition as the Input is Invalid");
				}
			}

			if(!PraeceptaObjectHelper.isObjectEmpty(requestMap)) {

				// LHS value will comes from the Subject or Attribute name that we need to Compare  
				logger.info(" Getting LHS Value ");

				Object fromValue = deriveLhsValue(condition, requestMap);

				logger.info(" LHS Value - {}", fromValue);

				// RHS value will comes from the Attribute name provided in the nested loop or a static value assigned in the Condition 
				logger.info(" Getting RHS Value ");

				Object toValue = deriveRhsValue(condition, requestMap);

				logger.info(" RHS Value - {}", toValue);

				ConditionValueHolder conditionValueHolder = new ConditionValueHolder(fromValue, toValue);

				condition.setConditionValueHolder(conditionValueHolder);

			}
		}

//		return conditionValueHolder;
	}

	public static Object deriveLhsValue(PraeceptaSimpleCondition condition, Map<String, Object> requestMap) {
		logger.info(" Capturing LHS Value ");

		Object fromValue = null;

		if(condition.getSubjectValue() != null){

			fromValue = condition.getSubjectValue();

		} else {

			fromValue = getValueUsingAttributeName(condition.getSubjectName(), requestMap);

		}

		logger.info(" LHS Value Captured - {} ", fromValue);
		return fromValue;
	}

	public static Object getValueUsingAttributeName(String attributeName, Map<String, Object> requestMap) {

		logger.info(" Getting Value for Attribute Name - {} ", attributeName);
		Object valueToReturn = null;

		if(!PraeceptaObjectHelper.isObjectEmpty(attributeName)) {

			if(attributeName.contains(".")){
				valueToReturn = getValueForNestedAttribute(attributeName, requestMap);
			}else {
				valueToReturn = requestMap.get(attributeName);
			}
		}

		return valueToReturn;
	}

	private static Object deriveRhsValue(PraeceptaSimpleCondition condition, Map<String, Object> requestMap) {
		logger.info(" Capturing RHS Value ");

		Object toValue = null;

		if(condition.getValueToCompare() != null){

			toValue = condition.getValueToCompare();

		} else {

			toValue = getValueUsingAttributeName(condition.getDefinedAttributeToCompare(), requestMap);

		}

		logger.info(" RHS Value Captured - {} ", toValue);
		return toValue;
	}

	public static Object getValueForNestedAttribute(String condition, Map<String, Object> requestMap) {

		Object valueToReturn = null;

		logger.debug(" RequestMap {}" , requestMap);
		logger.debug(" Condition {}" , condition);

		String[] st = condition.split("\\.");
		for (int i = 0; i < st.length; i++) {
			if (i != st.length - 1)
				requestMap = (Map<String, Object>) requestMap.get(st[i]);
			else
				valueToReturn = requestMap.get(st[i]);
			if (requestMap == null)
				break;
		}

		logger.debug(" Derived Value {}", valueToReturn);

		return valueToReturn;
	}

	public static void publishRuleExecutionAudit(EXECUTION_AUDIT_OPERATION_TYPE auditOperationType, PraeceptaExecutionAuditPoint executionTraceDto, String auditUrl){
		RuleSpaceInfo spaceInfo = new RuleSpaceInfo();
		Map<String, String> pathParams = new HashMap<>();
		switch (auditOperationType){
			case PRE_RULE_GROUP_HAWK_EYE:
				spaceInfo = executionTraceDto.getPreRuleGroupExecutionAuditDetails().getRuleGroupInfo().getRuleSpaceInfo();
				pathParams.put(PATH_PARAM_GROUP_NAME, executionTraceDto.getPreRuleGroupExecutionAuditDetails().getRuleGroupInfo().getRuleGroupName());
				break;
			case PRE_RULE_HAWK_EYE:
					spaceInfo = executionTraceDto.getPreRuleExecutionAuditDetails().getRuleGroupInfo().getRuleSpaceInfo();
					pathParams.put(PATH_PARAM_GROUP_NAME, executionTraceDto.getPreRuleExecutionAuditDetails().getRuleGroupInfo().getRuleGroupName());
				break;
			case POST_RULE_HAWK_EYE:
				spaceInfo = executionTraceDto.getPostRuleExecutionAuditDetails().getRuleGroupInfo().getRuleSpaceInfo();
				pathParams.put(PATH_PARAM_GROUP_NAME, executionTraceDto.getPostRuleExecutionAuditDetails().getRuleGroupInfo().getRuleGroupName());
				break;
			case POST_RULE_GROUP_HAWK_EYE:
				spaceInfo = executionTraceDto.getPostRuleGroupExecutionAuditDetails().getRuleGroupInfo().getRuleSpaceInfo();
				pathParams.put(PATH_PARAM_GROUP_NAME, executionTraceDto.getPostRuleGroupExecutionAuditDetails().getRuleGroupInfo().getRuleGroupName());
				break;
		}


		pathParams.put(PATH_PARAM_SPACE_NAME, spaceInfo.getSpaceName());
		pathParams.put(PATH_PARAM_CLIENT_ID, spaceInfo.getClientId());
		pathParams.put(PATH_PARAM_APP_NAME, spaceInfo.getAppName());
		pathParams.put(PATH_PARAM_VERSION, spaceInfo.getVersion());


		PraeceptaExecutionAuditPoints auditPoints = new PraeceptaExecutionAuditPoints();
		Collection<PraeceptaExecutionAuditPoint> auditPointsList = new ArrayList<>();
		auditPointsList.add(executionTraceDto);
		 auditPoints.setRuleExecutionAuditPoints(auditPointsList);

		PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.PUT,
				GsonHelper.toJson(auditPoints), new HashMap<>(), pathParams, new HashMap<>(), new HashMap<>());

		PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

		config.setEndpointUrl(auditUrl);
		config.setConnectionTimeOut(10000L);
		config.setReadTimeOut(7500L);

		PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

		PraeceptaWsRestClient<PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig>> restClient =
				new PraeceptaWsRestClient<>(simpleRestBuilder);

		restClient.initialize();

		restClient.triggerCall(wsReqResHolder);
	}


	private static List<PraeceptaActionResultDetails> captureActionResultInfo(PraeceptaRequestStore eachStoreForARule) {

		logger.debug("Capturing the Action Result ");

		List<PraeceptaActionResultDetails> allActionResultDetails = new ArrayList<>();

		Map<String, PraeceptaActionHolder> actionHolders = (Map<String, PraeceptaActionHolder>)
				eachStoreForARule.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);

		if (!PraeceptaObjectHelper.isObjectEmpty(actionHolders)) {

			actionHolders.forEach((actionName, ruleActionHolder) -> {

				PraeceptaActionResultDetails actionResultDetails =
						new PraeceptaActionResultDetails(actionName, ruleActionHolder.getActionResult(), ruleActionHolder.getActionAttributeName(), ruleActionHolder.getActionedValue());

				allActionResultDetails.add(actionResultDetails);
			});

		}
		logger.debug("Exiting from Capturing the Action Result ");

		return allActionResultDetails;

	}

	private static PraeceptaConditionResult captureConditionResultInfo(PraeceptaRequestStore eachStoreForARule) {

		logger.debug("Capturing the Condition Result ");

		PraeceptaConditionResult.CONDITION_RESULT conditionResultStatus = PraeceptaConditionResult.CONDITION_RESULT.NOT_SATISFIED;

		String actionTypeExecutedMsg = FailedActionTypeExecutedMessage;

		Boolean ruleConditionStatus = (Boolean) eachStoreForARule.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS);

		if(!PraeceptaObjectHelper.isObjectEmpty(ruleConditionStatus) && ruleConditionStatus) {
			conditionResultStatus = PraeceptaConditionResult.CONDITION_RESULT.SATISFIED;
			actionTypeExecutedMsg = SuccessActionTypeExecutedMessage;
		}

		PraeceptaConditionResult conditionResult = new PraeceptaConditionResult();

		conditionResult.setResult(conditionResultStatus);
		conditionResult.setResultMessage(actionTypeExecutedMsg);

		logger.debug("Exiting from Capturing the Condition Result ");

		return conditionResult;
	}

}
