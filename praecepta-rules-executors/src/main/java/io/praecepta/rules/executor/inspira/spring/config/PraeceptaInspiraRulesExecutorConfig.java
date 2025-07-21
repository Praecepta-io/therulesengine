package io.praecepta.rules.executor.inspira.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import io.praecepta.core.data.PraeceptaDictionaryData;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.collector.PraeceptaDBDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_CONFIG_KEYS;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig;

import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.*;

@Configuration
@Import({ PraeceptaRuleBuilderConfig.class})
public class PraeceptaInspiraRulesExecutorConfig {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaInspiraRulesExecutorConfig.class);
	
	@Autowired
	private Environment env;

	@Bean(name = "dataCollector")
	public IPraeceptaDataCollector<PraeceptaDBInjestorConfig> getDataCollector(){


		IPraeceptaDataCollector<PraeceptaDBInjestorConfig> dbCollector = new PraeceptaDBDataCollector();
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.DB_DRIVER.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.DB_URL.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.USERNAME.getElementName())
				);
		
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.PASSWORD.getElementName())
				);
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName())
				);
		
		dbCollector.openCollectorConnection(dbConfig);
		
		return dbCollector;
	}
	
	private String getConfigPropName(String configPropertyKey) {
		return env.getProperty(getConnectionPrefix() + configPropertyKey);
	}

	private String getConnectionPrefix() {
		return System.getProperty(DATA_COLLECTOR_CONFIG_KEYS.CONN_PROPS_PREFIX.value) + ".";
	}

	@Bean(name = "rulesExecutionEngine")
	public PraeceptaBasicRuleExecutionEngine getRuleExecutionEngine() {
		return new PraeceptaBasicRuleExecutionEngine();
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
					
					logger.info("Input Message Text to Magnify --> "+inputJson);
					
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
