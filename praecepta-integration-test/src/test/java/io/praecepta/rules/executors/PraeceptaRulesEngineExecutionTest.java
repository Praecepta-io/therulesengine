package io.praecepta.rules.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.collector.PraeceptaAbstractDataCollector;
import io.praecepta.data.collectors.common.configs.kafka.PraeceptaKafkaCollectorConfig;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.exception.PraeceptaDataCollectorException;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
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

public class PraeceptaRulesEngineExecutionTest {

	private PraeceptaBasicRuleExecutionEngine rulesExecutionEngine;
	private IPraeceptaDataCollector<PraeceptaKafkaCollectorConfig> dataCollector;
	private PraeceptaRulesExecutionManager executionManager;
	
	
	private IPraeceptaPivotalRulesHubManager hubManager;
	
	private static final String defaultWindowsFolder = "G:\\Praecepta\\IntegrationTest";
    private static final String defaultMetaDataFileNameSuffix = "praeceptaMetaData.json";
    
    private IPraeceptaRuleSpaceDao ruleSpaceDao;
    
    private IPraeceptaRuleGroupDao ruleGroupDao;
    
    private IPraeceptaRuleSideCarsDao ruleSideCarsDao;
    
    private static final String ACTIVE_SPACE_VERSION = "1.0";
    
    private static final String SPACE_NAME = "BRD";
    
    private static final String CLIENT_NAME = "ICICI";
    
    private static final String APP_NAME = "FXL";
    
    private static final String NAME_AGE_VALIDATION = "NameAndAgevalidation";
	
	@Before
	public void setup() {
		
		String folderName = getFolderName();
	     String metaDataFileNameSuffix = getMetaDataFileNameSuffix();
	    
	    ruleSpaceDao = new PraeceptaRuleSpaceWindowsDbDao(folderName, metaDataFileNameSuffix);
	    
	    ruleGroupDao = new PraeceptaRuleGroupWindowsDbDao(folderName, metaDataFileNameSuffix);
	    
	    ruleSideCarsDao = new PraeceptaSidecarWindowsDbDao(folderName, metaDataFileNameSuffix);
		
		hubManager = new PraeceptaPivotalRulesHubManager(ruleSpaceDao, ruleGroupDao, ruleSideCarsDao);
		
		rulesExecutionEngine = new PraeceptaBasicRuleExecutionEngine();
		
		dataCollector = new TestDataCollector();
		
		executionManager = new PraeceptaRulesExecutionManager(dataCollector, rulesExecutionEngine);
	}
	
	@Test
	public void test() {
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		ruleSpace.setActive(true);
		PraeceptaRuleSpaceCompositeKey key = getFxlSpaceKey();
		key.setVersion(ACTIVE_SPACE_VERSION);
		ruleSpace.setRuleSpaceKey(key);
//		ruleSpace.set

		ruleSpace.setPraeceptaRuleGrps(addRuleGroups(key));

		hubManager.createRuleSpace(ruleSpace);
		
		hubManager.fetchActiveUniverse();
		
		PraeceptaPivotalRulesHubContextHolder.addHubManager(hubManager);
		
		PraeceptaPivotalRulesHubStore hubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
		
		collectData(dataCollector);
		
		executionManager.execute();
		
//		fail("Not yet implemented");
	}
	
	private List<PraeceptaRuleGroup> addRuleGroups(PraeceptaRuleSpaceCompositeKey key) {
		PraeceptaRuleGroup ruleGrp1 = new PraeceptaRuleGroup();
		ruleGrp1.setRuleSpaceKey(key);
		ruleGrp1.setRuleGroupName(NAME_AGE_VALIDATION);
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
	//	logger.info("Nested Multi Condition --> " + GsonHelper.toJson(nestedMultCondition));
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

//		logger.info(GsonHelper.toJson(ruleGrp1));

		List<PraeceptaRuleGroup> ruleGroups = new ArrayList<>();
		ruleGroups.add(ruleGrp1);
		return ruleGroups;
	}

	
	private static void collectData(IPraeceptaDataCollector<PraeceptaKafkaCollectorConfig> testDataCollector) {
		PraeceptaKafkaCollectorConfig kafkaConfig = new PraeceptaKafkaCollectorConfig();

		System.out.println(kafkaConfig.getMandatoryElementNameAndDataTypeMap());
		
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Broker1");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER, 500);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, "Test Pass");
		
		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");
		
		
		testDataCollector.openCollectorConnection(kafkaConfig);
		
		testDataCollector.startDataCollector();
		
	}


	private static class TestDataCollector extends PraeceptaAbstractDataCollector<PraeceptaKafkaCollectorConfig>{
		
		private int numberOfMessages = 0;
		@Override
		protected PraeceptaDataRecord performCollection() {
//			System.out.println("In Perform Collection");

			// This method cannot be called directly. Must call startDataCollector(). Underlying will call this method to poll individual Collection 
			
			if(getCollectorStatus() == null || getCollectorStatus() == CONNECTION_STATUS.INITIALIZED) {
				throw new PraeceptaDataCollectorException("Perform Collector should be called only after Starting the Data Collector");
			}
			
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(5);
			
			
			
			dataRecord.addRecordEntry(getInputMessage(), "Id 1", null);
			/*dataRecord.addRecordEntry("Message 2", "Id 2", null);
			dataRecord.addRecordEntry("Message 3", "Id 3", null);
			dataRecord.addRecordEntry("Message 4", "Id 4", null);
			dataRecord.addRecordEntry("Message 5", "Id 5", null);*/
			
			if(numberOfMessages == 3) {
				terminateDataCollector();
			}
			numberOfMessages++;
			
			
			return dataRecord;
		}
	}

	private PraeceptaRuleSpaceCompositeKey getFxlSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(SPACE_NAME, CLIENT_NAME, APP_NAME);
	}

	private PraeceptaRuleSpaceCompositeKey getFxlSpaceKeyWithInitialVersion() {
		PraeceptaRuleSpaceCompositeKey key = getFxlSpaceKey();
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
	
	public static String getInputMessage() {


			return "{\"spaceName\": \"BRD\",\"clientId\": \"ICICI\",\"appName\": \"FXL\", \"ruleGroupName\" :\"NameAndAgevalidation\", \"emp.name\": \"Alex\",\"emp.age\": \"15\",\"mobile\": \"0987654321\",\"version\":\"1.0\",\"test\":{\"userName\":\"alex1234\",\"age\":\"15\"}}";
	}
}
