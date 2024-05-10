package io.praecepta.rules.hub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaSidecarWindowsDbDao;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PraeceptaPivotalRulesHubManagerWithFileDaoTest {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPivotalRulesHubManagerWithFileDaoTest.class);

	private IPraeceptaPivotalRulesHubManager hubManager;
	
	private static final String defaultWindowsFolder = "G:\\Praecepta\\IntegrationTest";
    private static final String defaultMetaDataFileNameSuffix = "praeceptaMetaData.json";
    
    private IPraeceptaRuleSpaceDao ruleSpaceDao;
    
    private IPraeceptaRuleGroupDao ruleGroupDao;
    
    private IPraeceptaRuleSideCarsDao ruleSideCarsDao;
    
    private static final String ACTIVE_SPACE_VERSION = "1.1";
    
    private static final String SPACE_NAME = "BRD";
    
    private static final String CLIENT_NAME = "GTO";
    
    private static final String APP_NAME = "ACATS";
    
    private static final String ACATS_TRANSFER_STOP_GRP = "TRANSFER_STOP";
    
    private static final String ACATS_RULE_NAME = "NameAndAgeRule";
	
	@Before
	public void setup() {	
		
		 String folderName = getFolderName();
	     String metaDataFileNameSuffix = getMetaDataFileNameSuffix();
	    
	    ruleSpaceDao = new PraeceptaRuleSpaceWindowsDbDao(folderName, metaDataFileNameSuffix);
	    
	    ruleGroupDao = new PraeceptaRuleGroupWindowsDbDao(folderName, metaDataFileNameSuffix);
	    
	    ruleSideCarsDao = new PraeceptaSidecarWindowsDbDao(folderName, metaDataFileNameSuffix);
		
		hubManager = new PraeceptaPivotalRulesHubManager(ruleSpaceDao, ruleGroupDao, ruleSideCarsDao);
	    
	}
	
	@Test
	public void testSpaceInsert() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKeyWithInitialVersion();
		
		hubManager.createRuleSpace(key);
		
	}

	private PraeceptaRuleSpaceCompositeKey getAcatsSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(SPACE_NAME, CLIENT_NAME, APP_NAME);
	}
	
	@Test
	public void testSpaceRetrieveAllActive() {
		
		hubManager.fetchActiveUniverse();
		
		PraeceptaPivotalRulesHubContextHolder.addHubManager(hubManager);
		
		PraeceptaPivotalRulesHubStore hubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
		
		Collection<PraeceptaRuleSpace> ruleSpaces = hubStore.getAllActiveRuleSpaces();
		
		System.out.println("Active Rule Space Size --> "+ruleSpaces.size());
		
	}
	
	@Test
	public void testSpaceRetrieveForKeyAndVersion() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKey();
//		key.setVersion("");
		
		PraeceptaRuleSpace ruleSpaceWithInvalidVersion = hubManager.fetchRuleSpace(key, "1.8");
		
		assertNull(ruleSpaceWithInvalidVersion);
		
		PraeceptaRuleSpace ruleSpaceWithValidVersion = hubManager.fetchRuleSpace(key, ACTIVE_SPACE_VERSION);
		
		assertNotNull(ruleSpaceWithValidVersion);
		
		assertEquals(key, ruleSpaceWithValidVersion.getRuleSpaceKey());
		
	}

	@Test
	public void testSpaceUpsert() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKeyWithInitialVersion();
		
		PraeceptaRuleSpace ruleSpaceWithValidVersion = hubManager.fetchRuleSpace(key, key.getVersion());
		
		List<PraeceptaRuleGroup> praeceptaRuleGrps = new ArrayList<>();
		
		PraeceptaRuleGroup grp1 = getAcatRuleGroup();//new PraeceptaRuleGroup(key);
		grp1.setRuleGroupName(ACATS_TRANSFER_STOP_GRP);
		
		praeceptaRuleGrps.add(grp1);
		
		ruleSpaceWithValidVersion.setPraeceptaRuleGrps(praeceptaRuleGrps);

		hubManager.updateRuleSpace(ruleSpaceWithValidVersion);
		
	}

	@Test
	public void testSpaceWithFetchRuleGrp() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKeyWithInitialVersion();
		
		List<PraeceptaRuleGroup> grepsReturned = hubManager.fetchRuleGrps(key, key.getVersion());
		
		assertNotNull(grepsReturned);
		
		assertEquals(grepsReturned.size(), 1);
		
		PraeceptaRuleGroup grpReturned = hubManager.fetchRuleGrp(key, key.getVersion(), ACATS_TRANSFER_STOP_GRP);
		
		assertNotNull(grpReturned);
		
		assertEquals(grpReturned.getRuleGroupName(), ACATS_TRANSFER_STOP_GRP);
	}
	

	@Test
	public void testSpaceUpsertSidecar() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKeyWithInitialVersion();
		
		PraeceptaSideCarsInfo sideCars = new PraeceptaSideCarsInfo();
		sideCars.setRuleSpaceKey(key);
		
		PraeceptaRuleGroupSideCarsInfo ruleGroupSideCars = new PraeceptaRuleGroupSideCarsInfo();
		ruleGroupSideCars.setRuleGrpName(ACATS_TRANSFER_STOP_GRP);
		
		sideCars.setRuleGroupSideCars(ruleGroupSideCars);

		hubManager.createRuleSideCars(sideCars);
		
	}
	
	@Test
	public void testSpaceWithFetchRuleSidecar() {
		
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKeyWithInitialVersion();
		
		List<PraeceptaSideCarsInfo> sideCarsReturnFromDb = hubManager.fetchRuleSideCars(key, key.getVersion());
		
		assertNotNull(sideCarsReturnFromDb);
		
		assertEquals(sideCarsReturnFromDb.size(), 1);
		
		PraeceptaSideCarsInfo sideCarsForARuleGrp = hubManager.fetchRuleSideCars(key, key.getVersion(), ACATS_TRANSFER_STOP_GRP);
		
		assertNotNull(sideCarsForARuleGrp);
		
		assertEquals(sideCarsForARuleGrp.getRuleGroupSideCars().getRuleGrpName(), ACATS_TRANSFER_STOP_GRP);
		
	}
	
	private PraeceptaRuleSpaceCompositeKey getAcatsSpaceKeyWithInitialVersion() {
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKey();
		key.setVersion(ACTIVE_SPACE_VERSION);
		return key;
	}

	private String getConnectionPrefix() {
		return System.getProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value)+".";
	}
	
	private String getMetaDataFileNameSuffix() {
		String metaDataFileNameSuffix =   System.getProperty(getConnectionPrefix()+"file_name",defaultMetaDataFileNameSuffix);
		return metaDataFileNameSuffix;
	}

	private String getFolderName() {
		String folderName =  System.getProperty(getConnectionPrefix()+"folder_name",defaultWindowsFolder);
		return folderName;
	}
	
	private PraeceptaRuleGroup getAcatRuleGroup() {
    	
    	PraeceptaRuleSpaceCompositeKey acatsSpaceKey = getAcatsSpaceKeyWithInitialVersion(); 
    	
		PraeceptaRuleGroup acatsRuleGrp = new PraeceptaRuleGroup();
		acatsRuleGrp.setRuleSpaceKey(acatsSpaceKey);
		
		acatsRuleGrp.setRuleGroupName(ACATS_TRANSFER_STOP_GRP);
		acatsRuleGrp.setActive(true);

		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		criteria1.setRuleName(ACATS_RULE_NAME);
		
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
}
