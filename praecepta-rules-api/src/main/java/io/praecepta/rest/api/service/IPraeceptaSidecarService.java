package io.praecepta.rest.api.service;

import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public interface IPraeceptaSidecarService {

    public PraeceptaRuleGroupSideCarsInfo getSideCarsByKey(String spaceName, String client, String appId, String version, String groupName);

    public void saveSideCars(String spaceName, String client,String appId,  String version, String groupName, PraeceptaRuleGroupSideCarsInfo request);

    public void updateSideCars(String spaceName, String client,String appId,  String version, String groupName, PraeceptaRuleGroupSideCarsInfo request);


    public void deleteSideCars(String spaceName, String client, String appId, String version, String groupName);
}
