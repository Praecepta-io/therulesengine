package io.praecepta.rules.storeandpivotalmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.jvmbased.dao.PraeceptaRuleGroupJvmDao;
import io.praecepta.rules.hub.jvmbased.dao.PraeceptaRuleSideCarsJvmDao;
import io.praecepta.rules.hub.jvmbased.dao.PraeceptaRuleSpaceJvmDao;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleLevelSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;

public class PraeceptaRuleExecutionEngineHelperTestWithJvmDao {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleExecutionEngineHelperTestWithJvmDao.class);
    
	
	@Test
    public void testCreateRuleStore(){

    	IPraeceptaRuleSpaceDao spaceDao = getSpaceDao();
    	
    	IPraeceptaRuleGroupDao ruleGroupDao = getRuleGroupDao();
    	
    	IPraeceptaRuleSideCarsDao ruleSideCarsDao = getRuleSideCarsDao();
    	
    	PraeceptaPivotalRulesHubManager pivotalRulesHubManager = new PraeceptaPivotalRulesHubManager(spaceDao, ruleGroupDao, ruleSideCarsDao);
    	
    	pivotalRulesHubManager.fetchActiveUniverse();
    	
    	PraeceptaRequestStore ruleStore = PraeceptaRuleExecutionEngineHelper.createRuleStore(getInputJson(), null);
    	
    	LOG.debug(" Space Name {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE));
    	
    	LOG.debug(" Rule Request {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST));
    	
    	LOG.debug(" Meta Data Name {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_METADATA));
    	
    	LOG.debug(" Rule Group {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP));
    	
    	LOG.debug(" Side Cars Info {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SIDE_CARS_INFO));
    	
    	LOG.debug(" Pre Rule Grp Side Cars MetaData {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA));
    	
    	LOG.debug(" Post Rule Grp Side Cars MetaData {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA));
    	
    	LOG.debug(" Pre Rule Side Cars MetaData {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS_META_DATA));
    	
    	LOG.debug(" Post Rule Side Cars MetaData {} ", ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS_META_DATA));
    	
        /*PraeceptaPivotalRulesHubStore hubStore = new PraeceptaPivotalRulesHubStore(ruleSpacesData, ruleGrpData, ruleSideCarData);
        PraeceptaPivotalRulesHubContextHolder.addHubStore(hubStore);
        PraeceptaRuleExecutionEngineHelper.createRuleStore(GsonHelper.toJson(requestMap), new HashMap<>());*/
    }
    
    private IPraeceptaRuleSpaceDao getSpaceDao() {
    	
    	IPraeceptaRuleSpaceDao spaceDao = new PraeceptaRuleSpaceJvmDao();
    	
    	PraeceptaRuleSpace acatsRuleSapce = new PraeceptaRuleSpace();
		
		PraeceptaRuleSpaceCompositeKey acatsSpaceKey = PraeceptaTestRuleConstants
				.getAcatsSpaceKeyWithInitialVersion();
		acatsRuleSapce.setRuleSpaceKey(acatsSpaceKey);
		
		spaceDao.insert(acatsRuleSapce);
		
		// FXL
		/*PraeceptaRuleSpace fxlsRuleSapce = new PraeceptaRuleSpace();

		PraeceptaRuleSpaceCompositeKey fxlSpaceKey = PraeceptaTestRuleConstants.getFxlSpaceKeyWithInitialVersion();
		fxlsRuleSapce.setRuleSpaceKey(fxlSpaceKey);
		
		spaceDao.insert(fxlsRuleSapce);*/
    	
    	return spaceDao;
    }
    
    private IPraeceptaRuleGroupDao getRuleGroupDao() {
    	
    	IPraeceptaRuleGroupDao ruleGroupDao = new PraeceptaRuleGroupJvmDao();
    	
    	PraeceptaRuleGroup acatRuleGroup = getAcatRuleGroup();
    	
    	ruleGroupDao.insert(acatRuleGroup);
    	
    	return ruleGroupDao;
    }
    
    private IPraeceptaRuleSideCarsDao getRuleSideCarsDao() {
    	
    	IPraeceptaRuleSideCarsDao ruleSideCarsDao = new PraeceptaRuleSideCarsJvmDao();
    	
    	PraeceptaSideCarsInfo sideCarsInfo = getAcatSideCarInfo();
    	
    	ruleSideCarsDao.insert(sideCarsInfo);
    	
    	return ruleSideCarsDao;
    }
    
    private PraeceptaRuleGroup getAcatRuleGroup() {
    	
    	PraeceptaRuleSpaceCompositeKey acatsSpaceKey = PraeceptaTestRuleConstants
				.getAcatsSpaceKeyWithInitialVersion(); 
    	
		PraeceptaRuleGroup acatsRuleGrp = new PraeceptaRuleGroup();
		acatsRuleGrp.setRuleSpaceKey(acatsSpaceKey);
		
		acatsRuleGrp.setRuleGroupName(PraeceptaTestRuleConstants.ACATS_TRANSFER_STOP_GRP);
		acatsRuleGrp.setActive(true);

		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		criteria1.setRuleName(PraeceptaTestRuleConstants.ACATS_RULE_NAME);
		
		String nameValidationAttributeName = "Namevalidation";

		// Rule Level success actions
		PraeceptaActionDetails action1 = new PraeceptaActionDetails();
		action1.setValueToAssign("true");
		action1.setActionAttributeName(nameValidationAttributeName);
		action1.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerform = new ArrayList<>();
		actionsToPerform.add(action1);
		criteria1.setActionToPerform(actionsToPerform);

		// Rule level failure actions
		PraeceptaActionDetails ruleFailAction = new PraeceptaActionDetails();
		ruleFailAction.setValueToAssign("false");
		ruleFailAction.setActionAttributeName(nameValidationAttributeName);
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
	//	logger.info("Nested Multi Condition --> " + GsonHelper.toJson(nestedMultCondition));
		criteria1.setPredicates(nestedMultCondition);

		Collection<PraeceptaCriteria> criterias = new ArrayList<>();
		criterias.add(criteria1);

		acatsRuleGrp.setPraeceptaCriterias(criterias);

		// Rule Group Level success action
		String actionAttributeNameForGrpEvaluate = "NameAndAgevalidation";
		
		PraeceptaActionDetails successAction = new PraeceptaActionDetails();
		successAction.setValueToAssign("success");
		successAction.setActionAttributeName(actionAttributeNameForGrpEvaluate);
		successAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerformOnSuccess = new ArrayList<>();
		actionsToPerformOnSuccess.add(successAction);
		acatsRuleGrp.setActionToPerform(actionsToPerformOnSuccess);

		// Rule group Level failure actions
		PraeceptaActionDetails failureAction = new PraeceptaActionDetails();
		failureAction.setValueToAssign("failed");
		failureAction.setActionAttributeName(actionAttributeNameForGrpEvaluate);
		failureAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Collection<PraeceptaActionDetails> actionsToPerformOnFailure = new ArrayList<>();
		actionsToPerformOnFailure.add(failureAction);
		acatsRuleGrp.setActionToPerformOnFailure(actionsToPerformOnFailure);

//		logger.info(GsonHelper.toJson(ruleGrp1));

		return acatsRuleGrp;
	}
    
    private PraeceptaSideCarsInfo getAcatSideCarInfo() {
    	
    	PraeceptaSideCarsInfo acatsSideCarInfo = new PraeceptaSideCarsInfo();
    	
    	PraeceptaRuleSpaceCompositeKey acatsSpaceKey = PraeceptaTestRuleConstants
				.getAcatsSpaceKeyWithInitialVersion(); 
    	
    	PraeceptaRuleGroupSideCarsInfo acatsRuleGrpSidecarInfo = getAcatRuleGroupSideCars();
    	
    	acatsSideCarInfo.setRuleSpaceKey(acatsSpaceKey);
    	acatsSideCarInfo.setRuleGroupSideCars(acatsRuleGrpSidecarInfo);
    	acatsSideCarInfo.setActive(true);
    	
    	return acatsSideCarInfo;
    }
    
    
    private PraeceptaRuleGroupSideCarsInfo getAcatRuleGroupSideCars() {
    	
		PraeceptaRuleGroupSideCarsInfo ruleGrpSidecrasInfo=new PraeceptaRuleGroupSideCarsInfo();
		ruleGrpSidecrasInfo.setRuleGrpName(PraeceptaTestRuleConstants.ACATS_TRANSFER_STOP_GRP);
		
		//Pre RuleGroup Side car
		PraeceptaSideCarMetadata preRuleGrpSideCarMetaData = getPreRuleGrpSideCarMetaData();
		
		ruleGrpSidecrasInfo.setPreRuleGrpSideCars(Arrays.asList(preRuleGrpSideCarMetaData));
		
		//Post rule grp side car
		PraeceptaSideCarMetadata postRuleGrpSideCarMetaData = getPostRuleGrpSideCarMetaData();
		
		ruleGrpSidecrasInfo.setPostRuleGrpSideCars(Arrays.asList(postRuleGrpSideCarMetaData));
		
		//Rule level Side cars
		PraeceptaRuleLevelSideCarsInfo ruleLevelSideCarsInfo = new PraeceptaRuleLevelSideCarsInfo();
		ruleLevelSideCarsInfo.setRuleName(PraeceptaTestRuleConstants.ACATS_RULE_NAME);
		
		//Pre rule side car
		PraeceptaSideCarMetadata preRuleSideCarMetaData = getPreRuleSideCarMetaData();
		
		ruleLevelSideCarsInfo.setPreRuleSideCars(Arrays.asList(preRuleSideCarMetaData));
		
		// Post rule side car
		PraeceptaSideCarMetadata postRuleSideCarMetaData = getPostRuleSideCarMetaData();
		
		ruleLevelSideCarsInfo.setPostRuleSideCars(Arrays.asList(postRuleSideCarMetaData));
		
		//Rule level Side cars Ends Here
		
		// Add Rule Level Side Cars Info to Rule Group Side cars Info
		ruleGrpSidecrasInfo.setRuleLevelSideCars(Arrays.asList(ruleLevelSideCarsInfo));
		
		return ruleGrpSidecrasInfo;
	}
    
    private PraeceptaSideCarMetadata getPostRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata postRuleGrpSideCar = new PraeceptaSideCarMetadata();
		postRuleGrpSideCar.setOrder(1);
		postRuleGrpSideCar.setType("ENRICHER");
		postRuleGrpSideCar.setSideCarType("SIMPLE_REST_API");
		
		Map<String,Object> enrSideCarMetadataMap=new HashMap<>();
		postRuleGrpSideCar.setSideCarConfigs(enrSideCarMetadataMap);
		return postRuleGrpSideCar;
	}


	private PraeceptaSideCarMetadata getPreRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata preRuleGrpSideCar = new PraeceptaSideCarMetadata();
		preRuleGrpSideCar.setOrder(1);
		preRuleGrpSideCar.setType("PARSER");
		preRuleGrpSideCar.setSideCarType("JSON");
		
		Map<String, Object> sideCarMetadataMap=new HashMap<>();
		preRuleGrpSideCar.setSideCarConfigs(sideCarMetadataMap);
		return preRuleGrpSideCar;
	}
	

	private PraeceptaSideCarMetadata getPreRuleSideCarMetaData() {
		
		PraeceptaSideCarMetadata preRuleSideCarMetaData = new PraeceptaSideCarMetadata();
		preRuleSideCarMetaData.setOrder(1);
		preRuleSideCarMetaData.setType("PARSER");
		preRuleSideCarMetaData.setSideCarType("YAML");
		
		Map<String,Object> preRuleSideCarInfo=new HashMap<>();
		preRuleSideCarMetaData.setSideCarConfigs(preRuleSideCarInfo);
		
		return preRuleSideCarMetaData;
	}
	
	private PraeceptaSideCarMetadata getPostRuleSideCarMetaData() {
		
		PraeceptaSideCarMetadata preRuleSideCarMetaData = new PraeceptaSideCarMetadata();
		preRuleSideCarMetaData.setOrder(1);
		preRuleSideCarMetaData.setType("FORMATTER");
		preRuleSideCarMetaData.setSideCarType("YAML");
		
		Map<String,Object> preRuleSideCarInfo=new HashMap<>();
		preRuleSideCarMetaData.setSideCarConfigs(preRuleSideCarInfo);
		
		return preRuleSideCarMetaData;
	}
	
	private static String getInputJson() {
		
		return "{\r\n" + 
				"   \"spaceName\":\"" + PraeceptaTestRuleConstants.ACATS_SPACE_NAME +"\",\r\n" + 
				"   \"clientId\":\"" + PraeceptaTestRuleConstants.ACATS_CLIENT_NAME +"\",\r\n" + 
				"   \"appName\":\"" + PraeceptaTestRuleConstants.ACATS_APP_NAME + "\",\r\n" + 
				"   \"ruleGroupName\":\"" + PraeceptaTestRuleConstants.ACATS_TRANSFER_STOP_GRP + "\",\r\n" + 
				"   \"version\":\"" + PraeceptaTestRuleConstants.ACATS_ACTIVE_SPACE_VERSION+ "\",\r\n" + 
				"   \"emp\":{\r\n" + 
				"      \"dob\":\"1999-01-01\",\r\n" + 
				"      \"name\":\"Vara\"\r\n" + 
				"   }\r\n" + 
				"}";
	}

}
