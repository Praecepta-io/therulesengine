package io.praecepta.rules.executor.inspira.manager;

import io.praecepta.core.cocurrent.PraeceptaBatchProcessor;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.data.intf.IPraeceptaDataProcessor;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PraeceptaInspiraRulesExecutionManager extends PraeceptaRulesExecutionManager{
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaInspiraRulesExecutionManager.class);
	private IPraeceptaDataProcessor dataProcessor;

	private PraeceptaBatchProcessor batchProcessor;

	private static final String CONFIG_BATCH_PROCESSING_REQUIRED = "batchProcessingRequired";
	private static final int DEFAULT_MAX_RECORDS_TO_PROCESS = 500;
	private static final long DEFAULT_BATCH_THREAD_INTERVAL = 500L;


	public PraeceptaInspiraRulesExecutionManager(IPraeceptaDataCollector<PraeceptaDBInjestorConfig> dataCollector,
												 PraeceptaBasicRuleExecutionEngine rulesExecutionEngine, IPraeceptaDataProcessor dataProcessor) {
		super(dataCollector, rulesExecutionEngine);

		checkAndIniitializeBatchProcessor(dataProcessor);
	}

	private void checkAndIniitializeBatchProcessor(IPraeceptaDataProcessor dataProcessor) {
		if (!isBatchProcessingEnabled()) {
			logger.info("Batch processing not enabled");
			return;
		}

		if (PraeceptaObjectHelper.isObjectNull(dataProcessor)) {
			logger.warn("DataProcessor found null hence skipping batch processor setup.");
			return;
		}

		this.dataProcessor = dataProcessor;

		batchProcessor = new PraeceptaBatchProcessor<>(DEFAULT_MAX_RECORDS_TO_PROCESS, DEFAULT_BATCH_THREAD_INTERVAL);

		batchProcessor.setProcessor(dataProcessor);

		batchProcessor.doTheSetUp();

		logger.info("BatchProcessor successfully initialized.");
	}


	private boolean isBatchProcessingEnabled() {
		return Boolean.parseBoolean(System.getProperty(CONFIG_BATCH_PROCESSING_REQUIRED, "false"));
	}

	@Override
	public PraeceptaRequestStore createRuleStore(PraeceptaDataRecord.RecordEntry record) {

		PraeceptaRequestStore ruleRequestStore =super.createRuleStore(record);

		//attaching batchProcessor to RuleStore
		attachBatchProcessorToRuleStore(ruleRequestStore);

		return ruleRequestStore;
	}
	private void attachBatchProcessorToRuleStore(PraeceptaRequestStore ruleRequestStore) {

		if(!PraeceptaObjectHelper.isObjectEmpty(batchProcessor) && ruleRequestStore != null){

			ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_EXECUTION_BATCH_PROCESSOR,
					batchProcessor);
		}
	}

}
