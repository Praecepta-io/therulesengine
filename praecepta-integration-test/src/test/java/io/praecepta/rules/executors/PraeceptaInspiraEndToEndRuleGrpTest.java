package io.praecepta.rules.executors;

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
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaSidecarWindowsDbDao;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

public class PraeceptaInspiraEndToEndRuleGrpTest {

	private PraeceptaBasicRuleExecutionEngine rulesExecutionEngine;
	private IPraeceptaDataCollector<PraeceptaKafkaCollectorConfig> dataCollector;
	private PraeceptaRulesExecutionManager executionManager;
	
	
	private IPraeceptaPivotalRulesHubManager hubManager;
	
	private static final String defaultWindowsFolder = "G:\\Praecepta\\IntegrationTest";
    private static final String defaultMetaDataFileNameSuffix = "praeceptaMetaData.json";
    
    private IPraeceptaRuleSpaceDao ruleSpaceDao;
    
    private IPraeceptaRuleGroupDao ruleGroupDao;
    
    private IPraeceptaRuleSideCarsDao ruleSideCarsDao;
    
    private static final String ACTIVE_SPACE_VERSION = "V1";
    
    private static final String SPACE_NAME = "INSPIRA";
    
    private static final String CLIENT_NAME = "ABC_BANK";
    
    private static final String APP_NAME = "RETAIL_DIVISION";
    
    private static final String RULE_GRP_CREDIT_CHECK = "CREDIT_CHECK";
	
	@Before
	public void setup() {
		System.setProperty("LOG_LEVEL", "DEBUG");
		
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
	
	private PraeceptaRuleSpaceCompositeKey getRbcSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(SPACE_NAME, CLIENT_NAME, APP_NAME);
	}
	
	@Test
	public void test() {
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		ruleSpace.setActive(true);
		PraeceptaRuleSpaceCompositeKey key = getRbcSpaceKey();
		key.setVersion(ACTIVE_SPACE_VERSION);
		ruleSpace.setRuleSpaceKey(key);
//		ruleSpace.set

		hubManager.fetchActiveUniverse();
		
		PraeceptaPivotalRulesHubContextHolder.addHubManager(hubManager);
		
		PraeceptaPivotalRulesHubStore hubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
		
		collectData(dataCollector);
		
		executionManager.execute();
		
//		fail("Not yet implemented");
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
			
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(1);
			
			dataRecord.addRecordEntry(getInputMessage(), "Id 1", null);
			
			numberOfMessages++;
			
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(numberOfMessages == 1) {
				terminateDataCollector();
			}
			
			return dataRecord;
		}
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
		
		/*return "{\r\n" + 
				"   \"spaceKeyDetails\":{\r\n" + 
				"   \"spaceName\":\"" + SPACE_NAME +"\",\r\n" + 
				"   \"clientId\":\"" + CLIENT_NAME +"\",\r\n" + 
				"   \"appName\":\"" + APP_NAME + "\"\r\n" +  
				"   },\r\n" + 
				"   \"rulGrpDetails\":{\r\n" + 
				"   \"ruleGroupName\":\"" + RULE_GRP_CREDIT_CHECK + "\",\r\n" +
				"   \"version\":\"" + ACTIVE_SPACE_VERSION+ "\"\r\n" +  
				"   },\r\n" + 
				"	\"ruleInputType\" : \"JSON\"," + 
				"   \"ruleInput\":\"Rajasrikar Punugoti Xploretech    3221008512Investor Communication System Newark, New Jersey, 08512 \"\r\n" + 
				"}";*/
		
		return  "{\r\n" + 
//				"  \"ruleInput\": \"{\\r\\n\\t\\\"age\\\": 26,\\r\\n\\t\\\"hasSalaryAccount\\\" : \\\"Y\\\",\\r\\n\\t\\\"eligibleForQuickCredit\\\" : \\\"Y\\\",\\r\\n\\t\\\"activeFixedDepositAcc\\\" : \\\"Y\\\",\\r\\n\\t\\\"average12MonthsBal\\\" : 2700000,\\r\\n\\t\\\"turnOver12Mon\\\" : 6000000\\t\\t\\r\\n}\",\r\n" + 
				"  \"ruleInput\": \"{\\r\\n\\t\\\"AGE\\\": 26,\\r\\n\\t\\\"HASSALARYACCOUNT\\\" : \\\"Y\\\",\\r\\n\\t\\\"ELIGIBLEFORQUICKCREDIT\\\" : \\\"Y\\\",\\r\\n\\t\\\"ACTIVEFIXEDDEPOSITACC\\\" : \\\"Y\\\",\\r\\n\\t\\\"AVERAGE12MONTHSBAL\\\" : 2700000,\\r\\n\\t\\\"TURNOVER12MON\\\" : 6000000\\t\\t\\r\\n}\",\r\n" + 
				"  \"spaceKeyDetails\": {\r\n" + 
				"    \"spaceName\": \"INSPIRA\",\r\n" + 
				"    \"clientId\": \"ABC_BANK\",\r\n" + 
				"    \"appName\": \"RETAIL_DIVISION\",\r\n" + 
				"    \"spaceKeyDetails\": {}\r\n" + 
				"  },\r\n" + 
				"  \"rulGrpDetails\": {\r\n" + 
				"    \"ruleGroupName\": \"CREDIT_CHECK\",\r\n" + 
				"    \"rulGrpDetails\": {},\r\n" + 
				"    \"version\": \"V1\"\r\n" + 
				"  },\r\n" + 
				"  \"ruleInputType\": \"JSON\"\r\n" + 
				"}";


//			return "{\r\n" + 
//					"   \"spaceName\":\"" + SPACE_NAME +"\",\r\n" + 
//					"   \"clientId\":\"" + CLIENT_NAME +"\",\r\n" + 
//					"   \"appName\":\"" + APP_NAME + "\",\r\n" + 
//					"   \"ruleGroupName\":\"" + STOP_TRADE_EXECUTION + "\",\r\n" + 
//					"   \"version\":\"" + ACTIVE_SPACE_VERSION+ "\",\r\n" + 
//				"	\"emp\":\r\n" + 
//				"	{\r\n" + 
//				"		\"name\":\"Raja\",\r\n" + 
//				"		\"company\":\"Xploretech\",\r\n" + 
//				"		\"id\":25\r\n" + 
//				"    },\r\n" + 
//				"	\"zip\":\"08512\",\r\n" + 
//				"	\"outerCompany\":\"Xploretech\",\r\n" + 
//				"	\"location\":\"Cranbury\"\r\n" + 
//				"} ";
	}
}
