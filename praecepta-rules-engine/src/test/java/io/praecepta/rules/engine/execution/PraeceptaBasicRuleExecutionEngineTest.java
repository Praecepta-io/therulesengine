package io.praecepta.rules.engine.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.enums.PraeceptaRuleGroupMetaData;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.ConditionOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

public class PraeceptaBasicRuleExecutionEngineTest {
    @Test
    public void test(){
        PraeceptaBasicRuleExecutionEngine praeceptaBasicRuleExecutionEngine = new PraeceptaBasicRuleExecutionEngine();
        PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();
        ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS, true);
        PraeceptaRuleGroup testGroup = new PraeceptaRuleGroup();
        testGroup.setActive(Boolean.TRUE);
        testGroup.setRuleGroupName("testGroup");
        testGroup.setRuleSpaceKey(new PraeceptaRuleSpaceCompositeKey("Space1","ClNT1", "APP1"));
        List<PraeceptaCriteria> praeceptaCriteriasList = new ArrayList<>();
        PraeceptaCriteria criteria1 = new PraeceptaCriteria();


        PraeceptaSimpleCondition condition = new PraeceptaSimpleCondition("name", ConditionOperatorType.EQUAL_CHARS, new PraeceptaSimpleCondition.ConditionValueHolder<String>("vara", ""));
        condition.setDefinedAttributeToCompare("emp.name");
        PraeceptaMultiNestedCondition praeceptaMultiNestedCondition = new PraeceptaMultiNestedCondition(new PraeceptaMultiCondition(condition));

        criteria1.setPredicates(praeceptaMultiNestedCondition);
        
        PraeceptaActionDetails actionToPerform = new PraeceptaActionDetails();
        actionToPerform.setValueToAssign("20");
        actionToPerform.setActionAttributeName("test");
        actionToPerform.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
        List<PraeceptaActionDetails> actions = new ArrayList<>();
        actions.add(actionToPerform);
        criteria1.setActionToPerform(actions);

        praeceptaCriteriasList.add(criteria1);

        testGroup.setPraeceptaCriterias(praeceptaCriteriasList);
        ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP, testGroup);
        ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST, "{\"name\":\"vara\"}");
        Map<String,Object> metaDataMap = new HashMap<>();
        metaDataMap.put(PraeceptaRuleGroupMetaData.NUMBER_OF_CRITERIAS.name(), 1);
        ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA,metaDataMap);


        praeceptaBasicRuleExecutionEngine.triggerRules(ruleStore);

        Assert.assertNotNull(ruleStore);
    }
}
