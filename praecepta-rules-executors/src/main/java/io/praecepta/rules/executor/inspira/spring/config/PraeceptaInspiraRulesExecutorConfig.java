package io.praecepta.rules.executor.inspira.spring.config;

import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.APP_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.CLIENT_ID;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.RULE_GRP_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.SPACE_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.VERSION;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.buildRuleGrpDetailsMap;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.buildRuleSpaceKeyDetailsMap;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.buildRuleStrForExecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import io.praecepta.core.cocurrent.PraeceptaBatchProcessor;
import io.praecepta.core.data.PraeceptaDictionaryData;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.data.intf.IPraeceptaDataProcessor;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.collector.PraeceptaDBDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.injestors.common.impl.PraeceptaDBDataInjestor;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_CONFIG_KEYS;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig;

@Configuration
@Import({ PraeceptaRuleBuilderConfig.class})
public class PraeceptaInspiraRulesExecutorConfig {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaInspiraRulesExecutorConfig.class);
	
	@Autowired
	private Environment env;
	
	private PraeceptaBatchProcessor<PraeceptaRequestStore> batchProcessor = new PraeceptaBatchProcessor<>(50, 100l);
	
	private AtomicInteger counter = new AtomicInteger(0);

	@Bean(name = "dataCollector")
	public IPraeceptaDataCollector<PraeceptaDBInjestorConfig> getDataCollector(){


		IPraeceptaDataCollector<PraeceptaDBInjestorConfig> dbCollector = new PraeceptaDBDataCollector();
		
		PraeceptaDBInjestorConfig dbConfig = getDbConfig();
		
		dbCollector.openCollectorConnection(dbConfig);
		
		return dbCollector;
	}
	
//	@Bean(name = "dataInjestor")
	public PraeceptaDBDataInjestor getDataInjestor() {
		
		PraeceptaDBDataInjestor dbInjector = new PraeceptaDBDataInjestor();
		
		PraeceptaDBInjestorConfig dbConfig = getDbConfig();
		
		dbInjector.openInjestorConnection(dbConfig);
		
		return dbInjector;
	}
	
	private IPraeceptaDataProcessor<PraeceptaRequestStore> getTestDataProcessor() {
		
		IPraeceptaDataProcessor<PraeceptaRequestStore> processor = (requestStoresToProcess) -> {
			
			if(!PraeceptaObjectHelper.isObjectEmpty(requestStoresToProcess)) {
				
				counter.addAndGet(requestStoresToProcess.size());
				
//				System.out.println("Here is the Count -->"+counter);
			}
		};
		
		return processor;
		
	}
	
	private IPraeceptaDataProcessor<PraeceptaRequestStore> getDataProcessor() {
		
		PraeceptaDBDataInjestor dbInjestor = getDataInjestor();
		
		IPraeceptaDataProcessor<PraeceptaRequestStore> processor = (requestStoreToProcess) -> {
			
			List<PraeceptaDictionaryData> dataRecordsToInsert = new ArrayList<>();
			
			requestStoreToProcess.stream().forEach(requestStore -> {
				 
				Map<String, Object> ruleResponseMap = (Map<String, Object>) requestStore
						 .fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);
				 
				if(ruleResponseMap != null) {
					PraeceptaDictionaryData dataRecord = new PraeceptaDictionaryData();
					dataRecord.setDictionaryDetails(ruleResponseMap);
					
	//				System.out.println("Data Record to Insert --> "+dataRecord);
					
					dataRecordsToInsert.add(dataRecord);
				}
			});
			
			
//			dbInjestor.injestData(null);
			if(dataRecordsToInsert.size() > 0) {
				dbInjestor.injestData(dataRecordsToInsert);
			}
			
		};
	
		return processor;
		
	}
	

	private PraeceptaDBInjestorConfig getDbConfig() {
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
//				"com.mysql.jdbc.Driver"
				getConfigPropName(PraeceptaDBDataConfigType.DB_DRIVER.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
//				"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true&useSSL=false"
				getConfigPropName(PraeceptaDBDataConfigType.DB_URL.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
//				""
				getConfigPropName(PraeceptaDBDataConfigType.USERNAME.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
//				""
				getConfigPropName(PraeceptaDBDataConfigType.PASSWORD.getElementName())
				);
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
//				"SELECT * FROM praecepta.rule_space_info;"
				getConfigPropName(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName())
				);
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, 
//				"INSERT INTO  RULE_RESULT(CIFID,ACID,FIRST_NAME,EMAIL,MMF,RSA) VALUES (?,?,?,?,?,?) ");
				getConfigPropName(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName())
				);
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_ATTRIBUTE_NAMES.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, 
//				"CIFID|ACID|FIRST_NAME|EMAIL|MMF|RSA");
				getConfigPropName(PraeceptaDBDataConfigType.INSERT_ATTRIBUTE_NAMES.getElementName())
				);
				
		
		return dbConfig;
	}
	
	private String getConfigPropName(String configPropertyKey) {
		return env.getProperty(getConnectionPrefix() + configPropertyKey);
	}

	private String getConnectionPrefix() {
		return System.getProperty(DATA_COLLECTOR_CONFIG_KEYS.CONN_PROPS_PREFIX.value) + ".";
	}

	@Bean(name = "rulesExecutionEngine")
	@DependsOn("pivotalRuleHubManager")
	public PraeceptaBasicRuleExecutionEngine getRuleExecutionEngine() {
		
		PraeceptaBasicRuleExecutionEngine ruleExecutionEngine  = new PraeceptaBasicRuleExecutionEngine() {
			
			@Override
			protected void triggerRuleGroupActions(PraeceptaRequestStore ruleStore) {
				super.triggerRuleGroupActions(ruleStore);
				
//				System.out.println("Finished Execution. before Adding to Batch Processor");
				
				batchProcessor.addData(ruleStore);
			}
			
		};
		
		batchProcessor.setProcessor(getDataProcessor());
//		batchProcessor.setProcessor(getTestDataProcessor());
		
		return ruleExecutionEngine;
	}
	
	@Bean(name = "rulesExecutionManager")
	public PraeceptaRulesExecutionManager getRulesExecutionManager(IPraeceptaDataCollector<PraeceptaDBInjestorConfig> dataCollector, PraeceptaBasicRuleExecutionEngine rulesExecutionEngine) {
		PraeceptaRulesExecutionManager manager = new PraeceptaRulesExecutionManager(dataCollector, rulesExecutionEngine);
		
		// Capture Rule Space Details thru a System Param
		PraeceptaDictionaryData inputEnricher = new PraeceptaDictionaryData();
		
		HashMap<String, Object> ruleSpaceDetails = new HashMap<>();
		
		ruleSpaceDetails.put(SPACE_NAME, System.getProperty(SPACE_NAME));
		ruleSpaceDetails.put(CLIENT_ID, System.getProperty(CLIENT_ID));
		ruleSpaceDetails.put(APP_NAME, System.getProperty(APP_NAME));
		ruleSpaceDetails.put(VERSION, System.getProperty(VERSION));
		ruleSpaceDetails.put(RULE_GRP_NAME, System.getProperty(RULE_GRP_NAME));
		
		inputEnricher.setDictionaryDetails(ruleSpaceDetails);
		
		// Build a Input Data Magnifier to Enrich the Input JSON with Rule Space Details and Stamp it to Record Entry
		BiFunction<RecordEntry, PraeceptaDictionaryData, RecordEntry> inputMagnifier = 
				(RecordEntry inputRecord, PraeceptaDictionaryData mganifyingLense) -> {
					
					Map<String, Object> ruleSpaceDetailsFromDictionary = mganifyingLense.getDictionaryDetails();
					
					String inputJson = inputRecord.getMessageText();
					
					logger.debug("Input Message Text to Magnify --> "+inputJson);
					
					Map<String, Object> spaceDetailsMap = buildRuleSpaceKeyDetailsMap(
							ruleSpaceDetailsFromDictionary.get(SPACE_NAME).toString(), ruleSpaceDetailsFromDictionary.get(CLIENT_ID).toString(), ruleSpaceDetailsFromDictionary.get(APP_NAME).toString()   
							);
					
					Map<String, Object> grpDetailsMap = buildRuleGrpDetailsMap(
							ruleSpaceDetailsFromDictionary.get(RULE_GRP_NAME).toString(), ruleSpaceDetailsFromDictionary.get(VERSION).toString()   
							);
					
					String ruleStrForExecution = buildRuleStrForExecution(spaceDetailsMap, grpDetailsMap, inputJson, "JSON");
					
					inputRecord.setMessageText(ruleStrForExecution);
					
					return inputRecord;
				};
		
		// Inject Space Details and Magnifier Function to Rule Execution Manager to Magnify rule Input which doesn't have space details in it
		manager.magnifySource(inputEnricher, inputMagnifier);
		
		
		return manager;
	}
}
