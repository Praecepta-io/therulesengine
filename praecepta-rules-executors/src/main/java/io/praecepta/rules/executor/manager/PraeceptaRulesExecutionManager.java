package io.praecepta.rules.executor.manager;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.cocurrent.PraeceptaExecutor;
import io.praecepta.core.data.PraeceptaDictionaryData;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;

public class PraeceptaRulesExecutionManager {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRulesExecutionManager.class);

	private IPraeceptaDataCollector<?> dataCollector;
	private PraeceptaBasicRuleExecutionEngine rulesExecutionEngine;
	
	private PraeceptaDictionaryData magnifyingLense;
	private BiFunction<RecordEntry, PraeceptaDictionaryData, RecordEntry> inputMagnifier;

	private PraeceptaExecutor executore = new PraeceptaExecutor(3);

	public PraeceptaRulesExecutionManager(IPraeceptaDataCollector<?> dataCollector,PraeceptaBasicRuleExecutionEngine rulesExecutionEngine) {
		this.dataCollector=dataCollector;
		this.rulesExecutionEngine=rulesExecutionEngine;
	}

	public void execute() {
		if (!PraeceptaObjectHelper.isObjectNull(dataCollector)) {
			while (dataCollector.isDataCollectable()) {
				
				if(CONNECTION_STATUS.COLLECTOR_STOPPED != dataCollector.getCollectorStatus()) {
					logger.info("Data Collector Has Not Yet Started. Starting it now ");
					
					dataCollector.startDataCollector();
				}
				
				Collection<PraeceptaDataRecord> recordsPolled = dataCollector.getCollectedData();
				
				logger.debug("Number of Records Polled --> {} ", recordsPolled.size());
				
				if (!PraeceptaObjectHelper.isObjectEmpty(recordsPolled)) {
					recordsPolled.forEach(dataRecord -> {
						LinkedBlockingDeque<RecordEntry> recordEntries = dataRecord.getRecordEntries();
						
						logger.debug("Record Entries in One Polled Record  --> {} ", recordEntries.size());
						
						while (recordEntries != null && !PraeceptaObjectHelper.isObjectEmpty(recordEntries)) {
							
							logger.debug("Triggering rules execution");
							
							try {
								
								executore.submitTask(() -> triggerRulesEngine(recordEntries.poll()));
								
							}catch(Exception ex) {
								
								logger.error("Exception in the Rule Trigger. So terminating the Data Collector", ex);
								
								dataCollector.terminateDataCollector();
								
								logger.info("Data Collector Termination is Triggered. So terminating the Data Collector");
								
							}
							logger.debug("Done rules execution");
							
						}
					});
				} else {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						logger.debug("Sleeping for Second Since there are Zero Records Polled  ");
					}
					
				}
			}
		}
	}

	void triggerRulesEngine(RecordEntry record) {
		if (!PraeceptaObjectHelper.isObjectNull(record)) {
			
			logger.debug("Before Rule Store Creation  ");
			
			record = magnifyInput(record);
			
			PraeceptaRequestStore ruleStore = createRuleStore(record);
			
			logger.debug("Performing the Rules Engine Execution  ");
			
			rulesExecutionEngine.performRuleEngineExecution(ruleStore);
			
			logger.debug("rules execution done");
		}
	}

	public PraeceptaRequestStore createRuleStore(RecordEntry record) {
		
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
	
	public void magnifySource(PraeceptaDictionaryData magnifyingLense, BiFunction<RecordEntry, PraeceptaDictionaryData, RecordEntry> inputMagnifier) {
		
			this.magnifyingLense = magnifyingLense;
			this.inputMagnifier = inputMagnifier;
	}

	public RecordEntry magnifyInput(RecordEntry record) {
		
		if(magnifyingLense != null && inputMagnifier != null) {
			
			record = inputMagnifier.apply(record, magnifyingLense);
		} 
		
		return record;
	}
	
	
}
