package io.praecepta.rules.executor.manager;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;

public class PraeceptaRulesExecutionManager {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRulesExecutionManager.class);

	private IPraeceptaDataCollector dataCollector;
	private PraeceptaBasicRuleExecutionEngine rulesExecutionEngine;
	
	public PraeceptaRulesExecutionManager(IPraeceptaDataCollector dataCollector,PraeceptaBasicRuleExecutionEngine rulesExecutionEngine) {
		this.dataCollector=dataCollector;
		this.rulesExecutionEngine=rulesExecutionEngine;
	}

	public void execute() {
		if (!PraeceptaObjectHelper.isObjectNull(dataCollector)) {
			while (dataCollector.isDataCollectable()) {
				Collection<PraeceptaDataRecord> recordsPolled = dataCollector.getCollectedData();
				
				logger.debug("Number of Records Polled --> {} ", recordsPolled.size());
				
				if (!PraeceptaObjectHelper.isObjectEmpty(recordsPolled)) {
					recordsPolled.forEach(dataRecord -> {
						LinkedBlockingDeque<RecordEntry> recordEntries = dataRecord.getRecordEntries();
						
						logger.debug("Record Entries in One Polled Record  --> {} ", recordEntries.size());
						
						while (recordEntries != null&&!PraeceptaObjectHelper.isObjectEmpty(recordEntries)) {
							
							logger.debug("Triggering rules execution");
							
							triggerRulesEngine(recordEntries.poll());
							logger.debug("Done rules execution");
						}
					});
				}
			}
		}
	}

	void triggerRulesEngine(RecordEntry record) {
		if (!PraeceptaObjectHelper.isObjectNull(record)) {
			
			logger.debug("Before Rule Store Creation  ");
			
			PraeceptaRequestStore ruleStore = createRuleStore(record);
			
			logger.debug("Performing the Rules Engine Execution  ");
			
			rulesExecutionEngine.performRuleEngineExecution(ruleStore);
			
			logger.debug("rules execution done");
		}
	}

	private PraeceptaRequestStore createRuleStore(RecordEntry record) {
		
		logger.info(" Creating Rule Request Store for Record Entry --> {}  ", record);
		
		PraeceptaRequestStore ruleRequestStore = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(record) && !PraeceptaObjectHelper.isObjectEmpty(record.getMessageText())) {
			
			ruleRequestStore = PraeceptaRuleExecutionEngineHelper.createRuleStore(record.getMessageText(), record.getMetaData());
			
			// Convert Side Car Meta Data to Actual Side Cars Depending on the Type and SUb Type
			if(ruleRequestStore != null) {
				PraeceptaSideCarHelper.convertSideCarMetadataToSideCarsForAStore(ruleRequestStore);
			}
			
		}
		
		return ruleRequestStore;
	}
	
	
}
