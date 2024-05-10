package io.praecepta.rules.engine.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;

public class PraeceptaRuleExecutionEngineHelperTest {

    @Test
    public void test(){
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("spaceName","Space1");
        requestMap.put("clientId", "test");
        requestMap.put("appName", "rulesEngine");
        requestMap.put("ruleGroupName", "group1");
        requestMap.put("version", "V1");
        requestMap.put("name","vara");
        // <PraeceptaRuleSpaceCompositeKey, <Version, Space>>
        Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesData = new HashMap<>();
        PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = new PraeceptaRuleSpaceCompositeKey("Space1","test","rulesEngine"  );
        Map<String, PraeceptaRuleSpace> ruleSpaceMap = new HashMap<>();
        PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
        PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey("Space1","test","rulesEngine"  );
        key.setVersion("V1");
        ruleSpace.setRuleSpaceKey(key);
        Collection<PraeceptaRuleGroup> ruleGroups = new ArrayList<>();
        PraeceptaRuleGroup ruleGroup = new PraeceptaRuleGroup();
        ruleGroup.setActive(Boolean.TRUE);
        ruleGroup.setRuleGroupName("group1");
        Collection<PraeceptaCriteria> criterias = new ArrayList<>();
        PraeceptaCriteria criteria = new PraeceptaCriteria();
        PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new PraeceptaSimpleCondition.ConditionValueHolder<String>("vara", ""));
        condition.setDefinedAttributeToCompare("emp.name");
        PraeceptaMultiNestedCondition praeceptaMultiNestedCondition = new PraeceptaMultiNestedCondition(new PraeceptaMultiCondition(condition));

        criteria.setPredicates(praeceptaMultiNestedCondition);

        criterias.add(criteria);
        ruleGroup.setPraeceptaCriterias(criterias);
        ruleGroups.add(ruleGroup);
        ruleSpace.setPraeceptaRuleGrps(ruleGroups);
        ruleSpace.setActive(Boolean.TRUE);
        ruleSpace.setPraeceptaRuleGrps(ruleGroups);
        ruleSpaceMap.put("V1", ruleSpace);

        ruleSpacesData.put(praeceptaRuleSpaceCompositeKey, ruleSpaceMap);

        // <PraeceptaRuleSpaceCompositeKey, <Version, Rule Grps>>
        Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpData = new HashMap<>();

        // <PraeceptaRuleSpaceCompositeKey, <Version, Rule Side Cars>>
        Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarData = new HashMap<>();

        PraeceptaPivotalRulesHubStore hubStore = new PraeceptaPivotalRulesHubStore(ruleSpacesData, ruleGrpData, ruleSideCarData);
        
        PraeceptaPivotalRulesHubContextHolder.addHubStore(hubStore);
        
        PraeceptaRuleExecutionEngineHelper.createRuleStore(GsonHelper.toJson(requestMap), new HashMap<>());
    }

}
