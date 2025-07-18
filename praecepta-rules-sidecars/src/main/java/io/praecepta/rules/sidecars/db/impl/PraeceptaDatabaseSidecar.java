package io.praecepta.rules.sidecars.db.impl;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.collector.PraeceptaDBDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.sidecars.db.IPraeceptaDatabaseSideCar;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class PraeceptaDatabaseSidecar implements IPraeceptaDatabaseSideCar {


	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDatabaseSidecar.class);
	PraeceptaDBDataCollector dbCollector = null;
	@Override
	public void initializeDBConnection(PraeceptaDBInjestorConfig dbConfig) {
		dbCollector = new PraeceptaDBDataCollector();
		dbCollector.openCollectorConnection(dbConfig);
		dbCollector.startDataCollector();
	}

	@Override
	public void processDbRecords(PraeceptaRequestStore ruleStore) {

		if (!PraeceptaObjectHelper.isObjectNull(dbCollector)) {
			while (dbCollector.isDataCollectable()) {
				Collection<PraeceptaDataRecord> recordsPolled = dbCollector.getCollectedData();

				logger.debug("Number of Records Polled --> {} ", recordsPolled.size());

				if (!PraeceptaObjectHelper.isObjectEmpty(recordsPolled)) {
					recordsPolled.forEach(dataRecord -> {
						LinkedBlockingDeque<PraeceptaDataRecord.RecordEntry> recordEntries = dataRecord.getRecordEntries();

						logger.debug("Record Entries in One Polled Record  --> {} ", recordEntries.size());

						while (recordEntries != null&&!PraeceptaObjectHelper.isObjectEmpty(recordEntries)) {

							logger.debug("Triggering rules execution");

							processData(recordEntries.poll(),ruleStore);
							logger.debug("Done rules execution");
						}
					});
				}
			}
		}
	}

	void processData(PraeceptaDataRecord.RecordEntry record,PraeceptaRequestStore ruleStore) {
		if (!PraeceptaObjectHelper.isObjectNull(record)) {

			logger.debug("Performing the Rules Engine Execution  ");


			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, record.getMessageText());


			performRuleExecution(ruleStore);

			logger.debug("rules execution done");
		}
	}

	public void performRuleExecution(PraeceptaRequestStore ruleStore){
		Object ruleRequestAsAMap = ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);

		String inputRequest = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);

		Boolean ruleValidationStatus =  (Boolean) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS);

		String ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);

		logger.info(" After Executing Pre Rule Group Side Car");

		logger.info(" About to Rule Trigger for Rule Group {} ", ruleGroupToExecute);

		PraeceptaRuleGroup ruleGrpToUse = (PraeceptaRuleGroup) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_TO_USE);

		List<PraeceptaCriteria> praeceptaOrderedCriterias = ruleGrpToUse.getPraeceptaOrderedCriterias();

		List<PraeceptaRequestStore> resultCriteriaStores = new ArrayList<>();

		praeceptaOrderedCriterias.stream().forEach( eachCriteria -> {
			logger.info(" Preparing the Rule Trigger for Criteria {} ", eachCriteria);

			PraeceptaRequestStore eachCriteriaStore = new PraeceptaRequestStore();

			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_CRITERIA, eachCriteria);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.ORIGINAL_RULE_STORE, ruleStore);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS, ruleValidationStatus);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, ruleGrpToUse);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, inputRequest);
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR, ruleRequestAsAMap);

			// Adding a new Data Holder with Input received for every Rule to make sure We have Thread Safe and One rule change of Data Holder will not impact Other rule
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, getInputDataHolder(inputRequest));

			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR));
			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR));

			eachCriteriaStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE, ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE));

			PraeceptaCriteriaExecutor.executeCriteria(eachCriteriaStore, eachCriteria);

			resultCriteriaStores.add(eachCriteriaStore);

		});

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES, resultCriteriaStores);

		logger.info(" Before Executing Post Rule Group Side Car");
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCar = (GenericPraeceptaInfoTrackerSideCarInjector)
				ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_SIDE_CAR);

		if (postRuleGrpSideCar != null) {
			ruleGroupToExecute = (String) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME);

			logger.info(" About to Perform Post Rule Grp Side car for Rule Group {} ", ruleGroupToExecute);
			PraeceptaCriteriaExecutor.performSideCarActivity(ruleStore, postRuleGrpSideCar);
		}

		logger.info(" After Executing Post Rule Group Side Car");

		logger.debug(" Finishing Execute Criterias Of A RuleGroup");
	}

	public static PraeceptaSideCarDataHolder<String, String> getInputDataHolder(String inputRequest) {
		// Adding this Input Data Holder to Use it in the Sidecars
		PraeceptaSideCarDataHolder<String, String> inputDataHolder = new PraeceptaSideCarDataHolder<>();

		// Adding both Input and Output with the same value But, in the Sidecars Pre proces, better to use the Out Param to make the sequence of Side cars run
		inputDataHolder.addInput(inputRequest);
		inputDataHolder.addOutput(inputRequest);
		return inputDataHolder;
	}
}
