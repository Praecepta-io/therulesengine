package io.praecepta.rest.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.rest.api.service.IPraeceptaSidecarService;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class PraeceptaSidecarService implements IPraeceptaSidecarService {

    @Autowired
    IPraeceptaPivotalRulesHubManager pivotalRuleHubManager;

    public PraeceptaRuleGroupSideCarsInfo getSideCarsByKey(String spaceName, String client, String appId, String version, String groupName){
        PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName,client, appId );
        praeceptaRuleSpaceCompositeKey.setVersion(version);

        PraeceptaSideCarsInfo praeceptaRuleSideCars =  pivotalRuleHubManager.fetchRuleSideCars(praeceptaRuleSpaceCompositeKey, version, groupName);
        if(praeceptaRuleSideCars != null){
            return praeceptaRuleSideCars.getRuleGroupSideCars();
        }

        return null;
    }

    @Override
    public void saveSideCars(String spaceName, String client, String appId, String version, String groupName, PraeceptaRuleGroupSideCarsInfo request) {
    	PraeceptaSideCarsInfo praeceptaRuleSideCars = new PraeceptaSideCarsInfo();
        PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey(spaceName, client, appId);
        key.setVersion(version);
        praeceptaRuleSideCars.setRuleSpaceKey(key);
        praeceptaRuleSideCars.setActive(Boolean.TRUE);
        praeceptaRuleSideCars.setRuleGroupSideCars(request);
        if(pivotalRuleHubManager.fetchRuleSideCars(praeceptaRuleSideCars.getRuleSpaceKey(), praeceptaRuleSideCars.getRuleSpaceKey().getVersion(), praeceptaRuleSideCars.getRuleGroupSideCars().getRuleGrpName()) != null){
            pivotalRuleHubManager.deleteSideCars(praeceptaRuleSideCars.getRuleSpaceKey(), praeceptaRuleSideCars.getRuleSpaceKey().getVersion(), praeceptaRuleSideCars.getRuleGroupSideCars().getRuleGrpName());
        }
            pivotalRuleHubManager.createRuleSideCars(praeceptaRuleSideCars);
    }

    @Override
    public void updateSideCars(String spaceName, String client, String appId, String version, String groupName, PraeceptaRuleGroupSideCarsInfo request) {
        saveSideCars(spaceName, client, appId, version, groupName, request);
    }

    @Override
    public void deleteSideCars(String spaceName, String client, String appId, String version, String groupName) {
        PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey(spaceName, client, appId);
        pivotalRuleHubManager.deleteSideCars(key, version, groupName);
    }
}
