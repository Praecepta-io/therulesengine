package io.praecepta.rest.api.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.api.service.IPraeceptaSidecarService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class PraeceptaSidecarController implements IPraeceptaSidecarController{

    private static final Logger LOG = LoggerFactory.getLogger(PraeceptaSidecarController.class);

    @Autowired
    IPraeceptaSidecarService sidecarService;
    @Override
    public void execute(PraeceptaRequestStore requestStore) {
        String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);
        LOG.info("Operation Name: {}", operation);

        Map<String, Object> pathParams = (Map<String, Object>) requestStore
                .fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);

        LOG.info("Path Params: {}", pathParams);

        String spaceName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_SPACE_NAME);
        String clientId = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_CLIENT_ID);
        String appName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_APP_NAME);
        String version = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_VERSION);
        String groupName = (String) pathParams.get(ServiceAndMethodNames.PATH_PARAM_RULEGROUPNAME);

        switch (operation) {
            case ServiceAndMethodNames.SIDE_CARS_GET_METHOD_NAME:
                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(getSidecars(spaceName, clientId, appName, version, groupName)));
                break;
            case ServiceAndMethodNames.SIDE_CARS_SAVE_METHOD_NAME:
            	PraeceptaRuleGroupSideCarsInfo request =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), PraeceptaRuleGroupSideCarsInfo.class);

                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(saveSidecars(spaceName, clientId, appName, version, groupName, request)));
                break;
            case ServiceAndMethodNames.SIDE_CARS_UPDATE_METHOD_NAME:
                request =  GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), PraeceptaRuleGroupSideCarsInfo.class);
                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(updateSidecars(spaceName, clientId, appName, version, groupName, request)));
                break;
            case ServiceAndMethodNames.SIDE_CARS_DELETE_METHOD_NAME:
                requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
                        GsonHelper.toJson(deleteSidecars(spaceName, clientId, appName, version, groupName)));
                break;
        }
    }

    @Override
    public Object getSidecars(String spaceName, String clientId, String appName, String version, String groupName) {
        return sidecarService.getSideCarsByKey(spaceName, clientId, appName, version, groupName);
    }

    @Override
    public Object saveSidecars(String spaceName, String clientId, String appName, String version, String groupName, PraeceptaRuleGroupSideCarsInfo request) {

         sidecarService.saveSideCars(spaceName, clientId, appName, version, groupName, request);

        return "Save successfully";
    }

    @Override
    public Object updateSidecars(String spaceName, String clientId, String appName, String version, String groupName, PraeceptaRuleGroupSideCarsInfo request) {
        sidecarService.updateSideCars(spaceName, clientId, appName, version, groupName, request);
        return "Update successfully";
    }

    @Override
    public Object deleteSidecars(String spaceName, String clientId, String appName, String version, String groupName) {
        sidecarService.deleteSideCars(spaceName, clientId, appName, version, groupName);
        return "Delete successfully";
    }


}
