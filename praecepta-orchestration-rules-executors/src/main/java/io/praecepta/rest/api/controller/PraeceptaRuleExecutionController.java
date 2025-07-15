package io.praecepta.rest.api.controller;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.praecepta.rules.engine.execution.IPraeceptaRuleExecutionEngine;
import io.praecepta.rules.engine.execution.PraeceptaPipesAndFiltersRuleExecutionEngine;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import org.springframework.beans.factory.annotation.Value;



public class PraeceptaRuleExecutionController implements IPraeceptaRuleExecutionController {

	@Value("${praecepta.ruleexecution.auditservice.url}")
	private String ruleExecutionAuditUrl;

	@Autowired
	IPraeceptaPivotalRulesHubManager pivotalRulesHubManager;

	private static final String PATH_PARAM_SPACE_NAME = ":spacename";

	private static final String PATH_PARAM_CLIENT_ID = ":clientid";

	private static final String PATH_PARAM_APP_NAME = ":appname";

	private static final String PATH_PARAM_VERSION = ":version";

	private static final String PATH_PARAM_GROUP_NAME = ":groupname";

	private boolean disableRuleExecution;

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleExecutionController.class);

	@Override
	public void execute(PraeceptaRequestStore requestStore) {
		String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);

		LOG.info("Operation Name: {}", operation);

		switch (operation) {
			case ServiceAndMethodNames.RULE_EXECUTOR_METHOD_NAME:
				if(!disableRuleExecution) {
					Map<String, Object> pathParams = (Map<String, Object>) requestStore
							.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);
					String spaceName = (String) pathParams.get(PATH_PARAM_SPACE_NAME);
					String clientId = (String) pathParams.get(PATH_PARAM_CLIENT_ID);
					String appName = (String) pathParams.get(PATH_PARAM_APP_NAME);
					String version = (String) pathParams.get(PATH_PARAM_VERSION);
					String groupName = (String) pathParams.get(PATH_PARAM_GROUP_NAME);
					LOG.info("Path Params: {}", pathParams);
					requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
							executeRule(spaceName, clientId, appName, version, groupName,
									requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT)));
				}else{
					requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,"Rule Execution is Stopped");
				}
				break;
			case ServiceAndMethodNames.RULE_EXECUTOR_DISABLE_METHOD_NAME:
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,disableRuleExecutor());
				break;
			case ServiceAndMethodNames.RULE_EXECUTOR_ENABLE_METHOD_NAME:
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,enableRuleExecutor());
				break;
		}
	}

	@Override
	public Object disableRuleExecutor(){
		disableRuleExecution = true;
		return "Disabled Rule Execution Successfully";
	}

	@Override
	public Object enableRuleExecutor(){
		disableRuleExecution = false;
		return "Enable Rule Execution Successfully";
	}
	@Override
	public Object executeRule(String spaceName, String clientId, String appName, String version,String groupName, Object inputMessage) {

		LOG.info("App Name: {} ", appName);
		//PraeceptaRequestStore requestStore = new PraeceptaRequestStore();
		Map<String,Object> request = new HashMap<>();
		Map<String, Object>  spaceKeyDetails = new HashMap<>();
		spaceKeyDetails.put("spaceName", spaceName);
		spaceKeyDetails.put("clientId", clientId);
		spaceKeyDetails.put("appName", appName);
		spaceKeyDetails.put("version", version);
		spaceKeyDetails.put("ruleGroupName",groupName);
		request.put("spaceName", spaceName);
		request.put("clientId", clientId);
		request.put("appName", appName);
		request.put("version", version);
		request.put("ruleGroupName",groupName);

		request.put("spaceKeyDetails", spaceKeyDetails);
		request.put("ruleInput", inputMessage);
		request.put("rulGrpDetails", spaceKeyDetails);
		PraeceptaRequestStore ruleRequestStore = PraeceptaRuleExecutionEngineHelper.createRuleStore(GsonHelper.toJson(request), new HashMap<>());

		// Convert Side Car Meta Data to Actual Side Cars Depending on the Type and SUb Type
		if(ruleRequestStore != null) {
			PraeceptaSideCarHelper.convertSideCarMetadataToSideCarsForAStore(ruleRequestStore);
		}

		IPraeceptaRuleExecutionEngine ruleExecutionEngine = new PraeceptaPipesAndFiltersRuleExecutionEngine();

		PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName);

		PraeceptaRuleSpace praeceptaRuleSpace = pivotalRulesHubManager.fetchRuleSpace(praeceptaRuleSpaceCompositeKey, version);

		ruleRequestStore
				.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE, praeceptaRuleSpace);
		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, GsonHelper.toJson(request));

		PraeceptaRuleGroup praeceptaRuleGroup = pivotalRulesHubManager.fetchRuleGrp(praeceptaRuleSpaceCompositeKey, version, groupName);
		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, praeceptaRuleGroup);

		Map<String, Object> metaData = new HashMap<>();
		metaData.put(PraeceptaRuleGroupMetaData.NUMBER_OF_CRITERIAS.name(), praeceptaRuleGroup.getPraeceptaCriterias().size());

		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA, metaData);
		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_AUDIT_URL, ruleExecutionAuditUrl);

		ruleExecutionEngine.performRuleEngineExecution(ruleRequestStore);
		if(ruleRequestStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT) != null) {
			return ruleRequestStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT);
		}
		return ruleRequestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_AS_KEY_VALUE_PAIR);
	}

}
