package io.praecepta.rules.executor.manager;

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
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaSidecarWindowsDbDao;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

public class PraeceptaRulesExecutionManagerTest {

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
    
    private static final String CLIENT_NAME = "GTO";
    
    private static final String APP_NAME = "ACATS";
    
    private static final String ACATS_TRANSFER_STOP_GRP = "TRANSFER_STOP";
	
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
			
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(5);
			
			
			
			dataRecord.addRecordEntry(getInputJson(), "Id 1", null);
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

	private PraeceptaRuleSpaceCompositeKey getAcatsSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(SPACE_NAME, CLIENT_NAME, APP_NAME);
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
	
	private static String getInputJson() {
		
		return "{\r\n" + 
				"   \"spaceName\":\"" + SPACE_NAME +"\",\r\n" + 
				"   \"clientId\":\"" + CLIENT_NAME +"\",\r\n" + 
				"   \"appName\":\"" + APP_NAME + "\",\r\n" + 
				"   \"ruleGroupName\":\"" + ACATS_TRANSFER_STOP_GRP + "\",\r\n" + 
				"   \"version\":\"" + ACTIVE_SPACE_VERSION+ "\",\r\n" + 
				"   \"emp\":{\r\n" + 
				"      \"dob\":\"1999-01-01\",\r\n" + 
				"      \"name\":\"Vara\"\r\n" + 
				"   }\r\n" + 
				"}";
	}
}
