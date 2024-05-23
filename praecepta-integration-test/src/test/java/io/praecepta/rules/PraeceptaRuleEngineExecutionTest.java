package io.praecepta.rules;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.config.PraeceptaRuleExecutorConfiguration;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PraeceptaRuleExecutorConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PraeceptaRuleEngineExecutionTest {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleEngineExecutionTest.class);

	private static ApplicationContext context = null;

	private static IPraeceptaPivotalRulesHubManager hubManager = null;

	private static PraeceptaBasicRuleExecutionEngine ruleEngine;

	@BeforeClass
	public static void init() {
		PraeceptaRuleBuilder.buildWithPropsClasspath("praecepta-rule-load-test.properties").build(null, null, null,
				null);

		context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
		
		hubManager = (IPraeceptaPivotalRulesHubManager) context.getBean("pivotalRuleHubManager");

		ruleEngine = new PraeceptaBasicRuleExecutionEngine();

		logger.info("Done initialization of Rule Builder Context");
	}

   @Test
	public void testInsert() {

		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		ruleSpace.setActive(true);
		PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey("praecepta", "icici", "praecepta");
		ruleSpace.setRuleSpaceKey(key);

		ruleSpace.setPraeceptaRuleGrps(addRuleGroups(key));

		hubManager.createRuleSpace(ruleSpace);

		assertNotNull(ruleSpace);
		
	}

	@Test
	public void testRuleGroupExecution() {
		hubManager.fetchActiveUniverse();
		PraeceptaRequestStore requestStore = createRuleStore();
		ruleEngine.performRuleEngineExecution(requestStore);

		logger.info(GsonHelper.toJson(requestStore));
	}

	private List<PraeceptaRuleGroup> addRuleGroups(PraeceptaRuleSpaceCompositeKey key) {
		PraeceptaRuleGroup ruleGrp1 = new PraeceptaRuleGroup();
		ruleGrp1.setRuleSpaceKey(key);
		ruleGrp1.setRuleGroupName("NameAndAgevalidation");
		ruleGrp1.setActive(true);

		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		criteria1.setRuleName("NameAndAgeRule");

		// Rule Level success actions
		PraeceptaActionDetails action1 = new PraeceptaActionDetails();
		action1.setValueToAssign("true");
		action1.setActionAttributeName("Namevalidation");
		action1.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerform = new ArrayList<>();
		actionsToPerform.add(action1);
		criteria1.setActionToPerform(actionsToPerform);

		// Rule level failure actions
		PraeceptaActionDetails ruleFailAction = new PraeceptaActionDetails();
		ruleFailAction.setValueToAssign("false");
		ruleFailAction.setActionAttributeName("Namevalidation");
		ruleFailAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerformOnRuleFailure = new ArrayList<>();
		actionsToPerformOnRuleFailure.add(ruleFailAction);
		criteria1.setActionToPerformOnFailure(actionsToPerformOnRuleFailure);

		// Rule predicates
		PraeceptaSimpleCondition nameCondition = new PraeceptaSimpleCondition("emp.name",
				ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Alex", "Tom"));
		PraeceptaSimpleCondition ageCondition = new PraeceptaSimpleCondition("emp.age",
				ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(5, 15));

		PraeceptaMultiCondition ageConditionToJoin = new PraeceptaMultiCondition(ageCondition);
		PraeceptaMultiCondition multiCondition = new PraeceptaMultiCondition(nameCondition, JoinOperatorType.AND,
				ageConditionToJoin);

		PraeceptaMultiNestedCondition nestedMultCondition = new PraeceptaMultiNestedCondition(multiCondition);
		logger.info("Nested Multi Condition --> " + GsonHelper.toJson(nestedMultCondition));
		criteria1.setPredicates(nestedMultCondition);

		Collection<PraeceptaCriteria> criterias = new ArrayList<>();
		criterias.add(criteria1);

		ruleGrp1.setPraeceptaCriterias(criterias);

		// Rule Group Level success action
		PraeceptaActionDetails successAction = new PraeceptaActionDetails();
		successAction.setValueToAssign("success");
		successAction.setActionAttributeName("NameAndAgevalidation");
		successAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerformOnSuccess = new ArrayList<>();
		actionsToPerformOnSuccess.add(successAction);
		ruleGrp1.setActionToPerform(actionsToPerformOnSuccess);

		// Rule group Level failure actions
		PraeceptaActionDetails failureAction = new PraeceptaActionDetails();
		failureAction.setValueToAssign("failed");
		failureAction.setActionAttributeName("NameAndAgevalidation");
		failureAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerformOnFailure = new ArrayList<>();
		actionsToPerformOnFailure.add(failureAction);
		ruleGrp1.setActionToPerformOnFailure(actionsToPerformOnFailure);

		logger.info(GsonHelper.toJson(ruleGrp1));

		List<PraeceptaRuleGroup> ruleGroups = new ArrayList<>();
		ruleGroups.add(ruleGrp1);
		return ruleGroups;
	}

	private PraeceptaRequestStore createRuleStore() {

		PraeceptaRequestStore ruleRequestStore = null;

		Map<String, Object> metaDataMap = new HashMap<>();

		ruleRequestStore = PraeceptaRuleExecutionEngineHelper.createRuleStore(getInputMessage(), metaDataMap);

		// Pre Rule Grp
		List<PraeceptaSideCarMetadata> preRuleGrpSideCarMetaData = (List<PraeceptaSideCarMetadata>) ruleRequestStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA);

		List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> preRuleGrpSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(preRuleGrpSideCarMetaData);

		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS,
				preRuleGrpSideCar);

		// Pre Rule
		Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPreRuleSideCarsMetaData = (Map<String, List<PraeceptaSideCarMetadata>>) ruleRequestStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS_META_DATA);

		Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> preRuleSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(ruleNameToPreRuleSideCarsMetaData);

		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS,
				preRuleSideCar);

		// Post Rule
		Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPostRuleSideCarsMetaData = (Map<String, List<PraeceptaSideCarMetadata>>) ruleRequestStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS_META_DATA);

		Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> postRuleSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(ruleNameToPostRuleSideCarsMetaData);

		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS,
				preRuleSideCar);

		// Post Rule Grp
		List<PraeceptaSideCarMetadata> postRuleGrpSideCarMetaData = (List<PraeceptaSideCarMetadata>) ruleRequestStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA);

		List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> postRuleGrpSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(postRuleGrpSideCarMetaData);

		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS,
				postRuleGrpSideCar);

		return ruleRequestStore;
	}

	public String getInputMessage() {

		return "{\"spaceName\": \"praecepta\",\"clientId\": \"icici\",\"appName\": \"praecepta\", \"ruleGroupName\" :\"NameAndAgevalidation\", \"emp.name\": \"Alex\",\"emp.age\": \"15\",\"mobile\": \"0987654321\",\"version\":\"V1\",\"test\":{\"userName\":\"alex1234\",\"age\":\"15\"}}";
	}
}
