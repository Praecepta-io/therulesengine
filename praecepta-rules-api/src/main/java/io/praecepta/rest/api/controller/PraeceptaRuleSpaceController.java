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
import io.praecepta.rules.dto.RuleSpaceInfo;

public class PraeceptaRuleSpaceController implements IPraeceptaRuleSpaceController{



    private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleSpaceController.class);

    @Autowired
    IPraeceptaRulesGroupService rulesGroupService;
    @Override
    public Object addRuleSpace(RuleSpaceInfo request) {
        return rulesGroupService.addRuleSpace(request);
    }

    @Override
    public Object deleteRuleSpace(String spaceName, String clientId, String appName, String version) {

        return rulesGroupService.deleteRuleSpace(spaceName, clientId, appName, version);
    }

    @Override
    public Object getRuleSpace(String spaceName, String clientId, String appName, String version) {

        return rulesGroupService.getRuleSpace(spaceName, clientId, appName, version);
    }

    @Override
    public Object getRuleSpaceList(){
        return rulesGroupService.getRuleSpaceList();
    }

    @Override
    public void execute(PraeceptaRequestStore requestStore) {
        String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);

        LOG.info("Operation Name: {}", operation);

        Map<String, Object> pathParams = (Map<String, Object>) requestStore
                .fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);

        LOG.info("Path Params: {}", pathParams);

        switch (operation) {
            case ServiceAndMethodNames.ADD_RULE_SPACE_METHOD_NAME:
                RuleSpaceInfo ruleSpaceInfo =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), RuleSpaceInfo.class);
                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(addRuleSpace( ruleSpaceInfo)));
                break;
            case ServiceAndMethodNames.DELETE_RULE_SPACE_METHOD_NAME:
                String spaceName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_SPACE_NAME);
                String clientId = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_CLIENT_ID);
                String appName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_APP_NAME);
                String version = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_VERSION);

                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(deleteRuleSpace(spaceName, clientId, appName, version)));
                break;
            case ServiceAndMethodNames.GET_RULE_SPACE_METHOD_NAME:
                spaceName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_SPACE_NAME);
                clientId = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_CLIENT_ID);
                appName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_APP_NAME);
                version = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_VERSION);

                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(getRuleSpace( spaceName, clientId, appName, version)));
                break;
            case ServiceAndMethodNames.GET_RULE_SPACE_LIST_METHOD_NAME:

                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(getRuleSpaceList()));
                break;
        }
    }
}
