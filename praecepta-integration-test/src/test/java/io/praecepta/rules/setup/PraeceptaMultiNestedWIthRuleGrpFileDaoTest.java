package io.praecepta.rules.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
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
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import io.praecepta.rules.sidecars.enums.PraeceptaDefaultSideCarClazzType;
import io.praecepta.rules.sidecars.formatter.PraeceptaFormatterTypeRegistry.PraeceptaFormatterType;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleLevelSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;
import io.praecepta.rules.sidecars.parser.PraeceptaParserTypeRegistry.PraeceptaParserType;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PraeceptaMultiNestedWIthRuleGrpFileDaoTest {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaMultiNestedWIthRuleGrpFileDaoTest.class);

	private IPraeceptaPivotalRulesHubManager hubManager;
	
	private static final String defaultWindowsFolder = "G:\\Praecepta\\IntegrationTest";
    private static final String defaultMetaDataFileNameSuffix = "praeceptaMetaData.json";
    
    private IPraeceptaRuleSpaceDao ruleSpaceDao;
    
    private IPraeceptaRuleGroupDao ruleGroupDao;
    
    private IPraeceptaRuleSideCarsDao ruleSideCarsDao;
    
    private static final String ACTIVE_SPACE_VERSION = "1.2";
    
    private static final String SPACE_NAME = "RBC";
    
    private static final String CLIENT_NAME = "TRADE_EXECUTION";
    
    private static final String APP_NAME = "RISK_CALCULATOR";
    
    private static final String STOP_TRADE_EXECUTION = "STOP_TRADE_EXECUTION";
    
    private static final String RULE_NAME_1 = "RULE_WITH_POSITIVE_ACTIONS";
	private static final String RULE_NAME_2 = "RULE_WITH_NEGATIVE_ACTIONS";
	
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
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKeyWithInitialVersion();
		
		hubManager.createRuleSpace(key);
		
	}

	private PraeceptaRuleSpaceCompositeKey getRbcSpaceKey() {
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
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKey();
//		key.setVersion("");
		
		PraeceptaRuleSpace ruleSpaceWithInvalidVersion = hubManager.fetchRuleSpace(key, "1.8");
		
		assertNull(ruleSpaceWithInvalidVersion);
		
		PraeceptaRuleSpace ruleSpaceWithValidVersion = hubManager.fetchRuleSpace(key, ACTIVE_SPACE_VERSION);
		
		assertNotNull(ruleSpaceWithValidVersion);
		
		assertEquals(key, ruleSpaceWithValidVersion.getRuleSpaceKey());
		
	}

	@Test
	public void testSpaceUpsert() {
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKeyWithInitialVersion();
		
		PraeceptaRuleSpace ruleSpaceWithValidVersion = hubManager.fetchRuleSpace(key, key.getVersion());
		
		List<PraeceptaRuleGroup> praeceptaRuleGrps = new ArrayList<>();
		
		PraeceptaRuleGroup grp1 = getExecutionStopRuleGroup();//new PraeceptaRuleGroup(key);
		grp1.setRuleGroupName(STOP_TRADE_EXECUTION);
		
		praeceptaRuleGrps.add(grp1);
		
		ruleSpaceWithValidVersion.setPraeceptaRuleGrps(praeceptaRuleGrps);

		hubManager.updateRuleSpace(ruleSpaceWithValidVersion);
		
	}

	@Test
	public void testSpaceWithFetchRuleGrp() {
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKeyWithInitialVersion();
		
		List<PraeceptaRuleGroup> grepsReturned = hubManager.fetchRuleGrps(key, key.getVersion());
		
		assertNotNull(grepsReturned);
		
		assertEquals(grepsReturned.size(), 1);
		
		PraeceptaRuleGroup grpReturned = hubManager.fetchRuleGrp(key, key.getVersion(), STOP_TRADE_EXECUTION);
		
		assertNotNull(grpReturned);
		
		assertEquals(grpReturned.getRuleGroupName(), STOP_TRADE_EXECUTION);
	}
	

	@Test
	public void testSpaceUpsertSidecar() {
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKeyWithInitialVersion();
		
		PraeceptaSideCarsInfo sideCars = new PraeceptaSideCarsInfo();
		sideCars.setRuleSpaceKey(key);
		
		PraeceptaRuleGroupSideCarsInfo ruleGroupSideCars = getRbcStopExecutionRuleGroupSideCars();
		
		sideCars.setRuleGroupSideCars(ruleGroupSideCars);

		hubManager.createRuleSideCars(sideCars);
		
	}
	
	@Test
	public void testSpaceWithFetchRuleSidecar() {
		
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKeyWithInitialVersion();
		
		List<PraeceptaSideCarsInfo> sideCarsReturnFromDb = hubManager.fetchRuleSideCars(key, key.getVersion());
		
		assertNotNull(sideCarsReturnFromDb);
		
		assertEquals(sideCarsReturnFromDb.size(), 1);
		
		PraeceptaSideCarsInfo sideCarsForARuleGrp = hubManager.fetchRuleSideCars(key, key.getVersion(), STOP_TRADE_EXECUTION);
		
		assertNotNull(sideCarsForARuleGrp);
		
		assertEquals(sideCarsForARuleGrp.getRuleGroupSideCars().getRuleGrpName(), STOP_TRADE_EXECUTION);
		
	}
	
	private PraeceptaRuleGroupSideCarsInfo getRbcStopExecutionRuleGroupSideCars() {
    	
		PraeceptaRuleGroupSideCarsInfo ruleGrpSidecrasInfo=new PraeceptaRuleGroupSideCarsInfo();
		ruleGrpSidecrasInfo.setRuleGrpName(STOP_TRADE_EXECUTION);
		
		//Pre RuleGroup Side car
		PraeceptaSideCarMetadata preRuleGrpSideCarMetaData = getPreRuleGrpSideCarMetaData();
		
		ruleGrpSidecrasInfo.setPreRuleGrpSideCars(Arrays.asList(preRuleGrpSideCarMetaData));
		
		//Post rule grp side car
		PraeceptaSideCarMetadata postRuleGrpSideCarMetaData = getPostRuleGrpSideCarMetaData();
		
		ruleGrpSidecrasInfo.setPostRuleGrpSideCars(Arrays.asList(postRuleGrpSideCarMetaData));
		
		//Rule level Side cars
		PraeceptaRuleLevelSideCarsInfo ruleLevelSideCarsInfo = new PraeceptaRuleLevelSideCarsInfo();
		ruleLevelSideCarsInfo.setRuleName(RULE_NAME_1);
		
		//Pre rule side car
/*		PraeceptaSideCarMetadata preRuleSideCarMetaData = getPreRuleSideCarMetaData();
		
		ruleLevelSideCarsInfo.setPreRuleSideCars(Arrays.asList(preRuleSideCarMetaData));*/
		
		// Post rule side car
		/*PraeceptaSideCarMetadata postRuleSideCarMetaData = getPostRuleSideCarMetaData();
		
		ruleLevelSideCarsInfo.setPostRuleSideCars(Arrays.asList(postRuleSideCarMetaData));*/
		
		//Rule level Side cars Ends Here
		
		// Add Rule Level Side Cars Info to Rule Group Side cars Info
		ruleGrpSidecrasInfo.setRuleLevelSideCars(Arrays.asList(ruleLevelSideCarsInfo));
		
		return ruleGrpSidecrasInfo;
	}
	
	private PraeceptaSideCarMetadata getPreRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata preRuleGrpSideCar = new PraeceptaSideCarMetadata();
		preRuleGrpSideCar.setOrder(1);
		preRuleGrpSideCar.setType(PraeceptaDefaultSideCarClazzType.PARSER.name());
		preRuleGrpSideCar.setSideCarType(PraeceptaParserType.JSON.name());
		
		Map<String, Object> sideCarMetadataMap=new HashMap<>();
		preRuleGrpSideCar.setSideCarConfigs(sideCarMetadataMap);
		
		
		
		return preRuleGrpSideCar;
	}
	
	private PraeceptaSideCarMetadata getPostRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata postRuleGrpSideCar = new PraeceptaSideCarMetadata();
		postRuleGrpSideCar.setOrder(1);
		postRuleGrpSideCar.setType(PraeceptaDefaultSideCarClazzType.FORMATTER.name());
		postRuleGrpSideCar.setSideCarType(PraeceptaFormatterType.XML.name());
		
		Map<String,Object> formatterSideCarInfo=new HashMap<>();
		
		formatterSideCarInfo.put("TEMPLATE_NAME", "XML_FORMATTER_TEST");
		formatterSideCarInfo.put("FORMATTER_TEMPLATE", "<name>${emp.name}</name><id>${emp.id}</id>"
				+ "<successActionAttribute>${successActionAttribute}</successActionAttribute><failedActionAttribute2>${failedActionAttribute2}</failedActionAttribute2>");
		
		postRuleGrpSideCar.setSideCarConfigs(formatterSideCarInfo);
		
		return postRuleGrpSideCar;
	}
	
	private PraeceptaRuleSpaceCompositeKey getRbcSpaceKeyWithInitialVersion() {
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKey();
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
	
	private PraeceptaRuleGroup getExecutionStopRuleGroup() {
		
		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		
		PraeceptaCriteria criteria1 = captureCriteria1();
		praeceptaCriterias.add(criteria1);
		
		PraeceptaCriteria criteria2 = captureCriteria2();
		praeceptaCriterias.add(criteria2);
		
		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup(SPACE_NAME, CLIENT_NAME, APP_NAME);
		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);

//		logger.info(GsonHelper.toJson(ruleGrp1));

		return ruleGrpToUse;
	}
	
	private PraeceptaCriteria captureCriteria1() {
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME_1);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME_1);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		PraeceptaActionDetails successActionDetails2 = new PraeceptaActionDetails();
		
		successActionDetails2.setValueToAssign("Positive 2 With "+RULE_NAME_1);
		successActionDetails2.setActionAttributeName("successActionAttribute2");
		successActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails2);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME_1);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		return criteria;
		
	}
	
	// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
		private static PraeceptaMultiNestedCondition getMultiNestedCondition() {
			// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
			
			// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
			
					PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "Raja");
					PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 65);
					PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "RSN");
					
					
					firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
					firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
					
					PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
					
			// Multi Condition 2 - (name = "xyz" or id = 90) ) 
					
					PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
					PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 90);
					
					secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

					PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
					
				// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
					
					firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
					
					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
		
			// Multi Condition 3 - (company = "abc" or zip = "12345")

					PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
					PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", null, ConditionOperatorType.EQUAL_CHARS, null, "08512");
					
					thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

					PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
					
			// Multi Condition 4 - (company = "efg" and location = "hyd")
					
					PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("outerCompany", null, ConditionOperatorType.EQUAL_CHARS, "emp.company", null);
					PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", null, ConditionOperatorType.EQUAL_CHARS, null, "Cranbury");
					
					fourthSetCondition1.setNextConditionInfo(JoinOperatorType.AND, fourthSetCondition2);

					PraeceptaMultiCondition fourthSetMultiCondion = new PraeceptaMultiCondition(fourthSetCondition1);
					
				// Prepare 2nd Set Multi Nested Condition - (company = "abc" or zip = "12345") or (company = "efg" and location = "hyd")	
					
					thirdSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, fourthSetMultiCondion);
					
					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion);
//					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
		
				// Final Nested Multi Condition 
					firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
					
					return firstSetMultNestedCondition;

		}
	
	private PraeceptaCriteria captureCriteria2() {
		
		PraeceptaCriteria criteria = new PraeceptaCriteria();
		
		criteria.setRuleName(RULE_NAME_2);
		
		// Add Predicate to Evaluate
		PraeceptaMultiNestedCondition predicate = getMultiNestedCondition2();
		
		criteria.setPredicates(predicate);
		
		// Add Success Action to Perform
		List<PraeceptaActionDetails> successActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails successActionDetails = new PraeceptaActionDetails();
		
		successActionDetails.setValueToAssign("Positive With "+RULE_NAME_2);
		successActionDetails.setActionAttributeName("successActionAttribute");
		successActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		successActionsToPerform.add(successActionDetails);
		
		criteria.setActionToPerform(successActionsToPerform);
		
		// Add Fail Action to Perform
		List<PraeceptaActionDetails> failActionsToPerform = new ArrayList<>();
		
		PraeceptaActionDetails failActionDetails = new PraeceptaActionDetails();
		
		failActionDetails.setValueToAssign("Negative With "+RULE_NAME_2);
		failActionDetails.setActionAttributeName("failedActionAttribute");
		failActionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails);
		
		PraeceptaActionDetails failActionDetails2 = new PraeceptaActionDetails();
		
		failActionDetails2.setValueToAssign("Negative 2 With "+RULE_NAME_2);
		failActionDetails2.setActionAttributeName("failedActionAttribute2");
		failActionDetails2.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		
		failActionsToPerform.add(failActionDetails2);
		
		criteria.setActionToPerformOnFailure(failActionsToPerform);
		
		return criteria;
		
	}
	
	// ((name = "Raja" and id = 42) or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))
		private static PraeceptaMultiNestedCondition getMultiNestedCondition2() {
			// ((name = "Raja" and id < 65 and company != "RSN") or (name = "xyz" or id = 90) ) or ((company = "abc" or zip = "12345") or (company = "efg" and location = "hyd"))		
			
			// Multi Condition 1 - (name = "Raja" and id < 65 and company != "RSN")
			
					PraeceptaSimpleCondition firstSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "xxx");
					PraeceptaSimpleCondition firstSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 10);
					PraeceptaSimpleCondition firstSetCondition3 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.NOT_EQUAL_CHARS, null, "def");
					
					
					firstSetCondition1.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition2);
					firstSetCondition2.setNextConditionInfo(JoinOperatorType.AND, firstSetCondition3);
					
					PraeceptaMultiCondition firstSetMultiCondion = new PraeceptaMultiCondition(firstSetCondition1);
					
			// Multi Condition 2 - (name = "xyz" or id = 90) ) 
					
					PraeceptaSimpleCondition secondSetCondition1 = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "zyx");
					PraeceptaSimpleCondition secondSetCondition2 = new PraeceptaSimpleCondition("emp.id", null, ConditionOperatorType.LESS_THAN_NUMBER, null, 6);
					
					secondSetCondition1.setNextConditionInfo(JoinOperatorType.OR, secondSetCondition2);

					PraeceptaMultiCondition secondSetMultiCondion = new PraeceptaMultiCondition(secondSetCondition1);
					
				// Prepare 1st Set Multi Nested Condition - (name = "Raja" and id < 42) or (name = "xyz" or id = 90)
					
					firstSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, secondSetMultiCondion);
					
					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion);
//					PraeceptaMultiNestedCondition firstSetMultNestedCondition = new PraeceptaMultiNestedCondition(firstSetMultiCondion, JoinOperatorType.OR, firstSetMultNestedCondition1);
		
			// Multi Condition 3 - (company = "abc" or zip = "12345")

					PraeceptaSimpleCondition thirdSetCondition1 = new PraeceptaSimpleCondition("emp.company", null, ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
					PraeceptaSimpleCondition thirdSetCondition2 = new PraeceptaSimpleCondition("zip", null, ConditionOperatorType.EQUAL_CHARS, null, "08910");
					
					thirdSetCondition1.setNextConditionInfo(JoinOperatorType.OR, thirdSetCondition2);

					PraeceptaMultiCondition thirdSetMultiCondion = new PraeceptaMultiCondition(thirdSetCondition1);
					
			// Multi Condition 4 - (company = "efg" and location = "hyd")
					
					PraeceptaSimpleCondition fourthSetCondition1 = new PraeceptaSimpleCondition("outerCompany", null, ConditionOperatorType.EQUAL_CHARS, "emp.zip", null);
					PraeceptaSimpleCondition fourthSetCondition2 = new PraeceptaSimpleCondition("location", null, ConditionOperatorType.EQUAL_CHARS, null, "Woodbridge");
					
					fourthSetCondition1.setNextConditionInfo(JoinOperatorType.AND, fourthSetCondition2);

					PraeceptaMultiCondition fourthSetMultiCondion = new PraeceptaMultiCondition(fourthSetCondition1);
					
				// Prepare 2nd Set Multi Nested Condition - (company = "abc" or zip = "12345") or (company = "efg" and location = "hyd")	
					
					thirdSetMultiCondion.setNextMultiConditionInfo(JoinOperatorType.OR, fourthSetMultiCondion);
					
					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion);
//					PraeceptaMultiNestedCondition secondSetMultNestedCondition = new PraeceptaMultiNestedCondition(thirdSetMultiCondion, JoinOperatorType.OR, secondSetMultNestedCondition1);
		
				// Final Nested Multi Condition 
					firstSetMultNestedCondition.setNextMultiNestedConditionInfo(JoinOperatorType.OR, secondSetMultNestedCondition);
					
					return firstSetMultNestedCondition;

		}
}
