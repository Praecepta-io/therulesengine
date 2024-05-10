package io.praecepta.rest.api.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.praecepta.rules.dto.MultiConditionGroupInfo;
import io.praecepta.rules.dto.MultiNestedConditionGroupInfo;
import io.praecepta.rules.dto.SimpleConditionGroupInfo;


public class PraeceptaRuleGroupController implements IPraeceptaRuleGroupController {
	

	@Autowired
	IPraeceptaRulesGroupService rulesGroupService;

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleGroupController.class);
	
	@Override
	public void execute(PraeceptaRequestStore requestStore) {
		String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);
		
		LOG.info("Operation Name: {}", operation);
		
		Map<String, Object> pathParams = (Map<String, Object>) requestStore
				.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);
		String spaceName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_SPACE_NAME);
		String clientId = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_CLIENT_ID);
		String appName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_APP_NAME);
		String version = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_VERSION);
		LOG.info("Path Params: {}", pathParams);
		
		switch (operation) {
			case ServiceAndMethodNames.RULE_GROUP_GET_METHOD_NAME:
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(getRuleGroups(spaceName, clientId, appName, version)));
				break;
			
			case ServiceAndMethodNames.RULE_GROUP_BY_NAME_GET_METHOD_NAME:
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(getRuleGroupByName(
								spaceName, clientId, appName, version,
								(String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_RULEGROUPNAME))));
					break;
			
			case ServiceAndMethodNames.ADD_RULE_GROUP_METHOD_NAME:
				SimpleConditionGroupInfo ruleGroup =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), SimpleConditionGroupInfo.class);


				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(addRuleGroup(spaceName,clientId,appName,version ,ruleGroup)));
					break;
			
			case ServiceAndMethodNames.UPDATE_RULE_GROUP_METHOD_NAME:
				SimpleConditionGroupInfo updatedRuleGroup =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), SimpleConditionGroupInfo.class);
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(updateRuleGroup(spaceName,clientId,appName,version ,updatedRuleGroup)));
					break;
			case ServiceAndMethodNames.ADD_MULTI_RULE_GROUP_METHOD_NAME:
				MultiConditionGroupInfo multiRuleGroup =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), MultiConditionGroupInfo.class);
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(addMultiRuleGroup(spaceName,clientId,appName,version ,multiRuleGroup)));
				break;

			case ServiceAndMethodNames.UPDATE_MULTI_RULE_GROUP_METHOD_NAME:
				MultiConditionGroupInfo multiConditionGroupInfo =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), MultiConditionGroupInfo.class);
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(updateMultiRuleGroup(spaceName,clientId,appName,version ,multiConditionGroupInfo)));
				break;
			case ServiceAndMethodNames.ADD_MULTI_NESTED_RULE_GROUP_METHOD_NAME:
				MultiNestedConditionGroupInfo multiNestedConditionGroupInfo =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), MultiNestedConditionGroupInfo.class);
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(addMultiNestedRuleGroup(spaceName,clientId,appName,version ,multiNestedConditionGroupInfo)));
				break;

			case ServiceAndMethodNames.UPDATE_MULTI_NESTED_RULE_GROUP_METHOD_NAME:
				multiNestedConditionGroupInfo =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), MultiNestedConditionGroupInfo.class);
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						GsonHelper.toJson(updateMultiNestedRuleGroup(spaceName,clientId,appName,version ,multiNestedConditionGroupInfo)));
				break;
			
			case ServiceAndMethodNames.DELETE_RULE_GROUP_METHOD_NAME:
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						deleteRuleGroup(spaceName,clientId,appName,version, (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_RULEGROUPNAME)));
					break;
		}
	}

	@Override
	public Object getRuleGroups(String spaceName, String clientId, String appName, String version) {
		LOG.info("App Name: {} ", appName);
		return rulesGroupService.getRuleGroups(spaceName, clientId, appName, version);
	}
	
	@Override
	public Object getRuleGroupByName(String spaceName, String clientId, String appName, String version, String ruleGroupName) {
		LOG.info("App Name: {} , Rule Group Name: {}", appName, ruleGroupName);
		return rulesGroupService.getRuleGroupByName(spaceName, clientId, appName, version, ruleGroupName);
	}

	@Override
	public Object addRuleGroup(String spaceName, String clientId, String appName, String version, SimpleConditionGroupInfo ruleGroup) {
		LOG.info("App Name: {} ", appName);
		LOG.info("Request : {} ", ruleGroup);

		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, ruleGroup);
	}
	@Override
	public Object updateRuleGroup(String spaceName, String clientId, String appName, String version, SimpleConditionGroupInfo ruleGroup) {
		LOG.info("App Name: {} ", appName);
		LOG.info("Request Body: {} ", ruleGroup);
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, ruleGroup);
	}

	@Override
	public Object addMultiRuleGroup(String spaceName, String clientId, String appName, String version, MultiConditionGroupInfo request) {
		LOG.info("App Name: {} ", appName);
		LOG.info("Request Body: {} ", request);
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}

	@Override
	public Object updateMultiRuleGroup(String spaceName, String clientId, String appName, String version, MultiConditionGroupInfo request) {
		LOG.info("App Name: {} ", appName);
		LOG.info("Request Body: {} ", request);
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}

	@Override
	public Object addMultiNestedRuleGroup(String spaceName, String clientId, String appName, String version, MultiNestedConditionGroupInfo request) {
		LOG.info("App Name: {} ", appName);

		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}


	@Override
	public Object updateMultiNestedRuleGroup(String spaceName, String clientId, String appName, String version, MultiNestedConditionGroupInfo request) {
		LOG.info("App Name: {} ", appName);
		LOG.info("Request Body: {} ", request);
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}

	@Override
	public Object deleteRuleGroup(String spaceName, String clientId, String appName, String version, String ruleGroupName) {
		LOG.info("App Name: {} , Rule Group Name: {}", appName, ruleGroupName);
		return rulesGroupService.deleteRuleGroup(spaceName, clientId, appName, version, ruleGroupName);
	}
	


}
