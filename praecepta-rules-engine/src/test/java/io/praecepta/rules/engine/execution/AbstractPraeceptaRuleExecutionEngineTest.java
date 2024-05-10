package io.praecepta.rules.engine.execution;

import org.junit.Assert;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class AbstractPraeceptaRuleExecutionEngineTest {

    @Test
    public void buildRuleSapceTest() throws Exception {
        AbstractPraeceptaRuleExecutionEngine abstractPraeceptaRuleExecutionEngine = new PraeceptaBasicRuleExecutionEngine();

        PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = new PraeceptaRuleSpaceCompositeKey();
        praeceptaRuleSpaceCompositeKey.setVersion("V1");
        praeceptaRuleSpaceCompositeKey.setSpaceName("SPACE1");
        praeceptaRuleSpaceCompositeKey.setAppName("APP1");
        praeceptaRuleSpaceCompositeKey.setClientId("001");


        PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey();
        key.setSpaceName("SPACE1");
        key.setAppName("APP1");
        key.setClientId("001");




        abstractPraeceptaRuleExecutionEngine.buildRuleSapce(praeceptaRuleSpaceCompositeKey);

    }

    @Test
    public void buildRuleSapceWithOutVersionTest(){
        AbstractPraeceptaRuleExecutionEngine abstractPraeceptaRuleExecutionEngine = new PraeceptaBasicRuleExecutionEngine();

        PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = new PraeceptaRuleSpaceCompositeKey();
        praeceptaRuleSpaceCompositeKey.setVersion("V1");
        praeceptaRuleSpaceCompositeKey.setSpaceName("SPACE1");
        praeceptaRuleSpaceCompositeKey.setAppName("APP1");
        praeceptaRuleSpaceCompositeKey.setClientId("001");
        PraeceptaRuleSpace ruleSpace =  abstractPraeceptaRuleExecutionEngine.buildRuleSapce(praeceptaRuleSpaceCompositeKey);
        Assert.assertNull(ruleSpace);
    }

    @Test
    public void buildRuleSapcesTest(){
        AbstractPraeceptaRuleExecutionEngine abstractPraeceptaRuleExecutionEngine = new PraeceptaBasicRuleExecutionEngine();
        abstractPraeceptaRuleExecutionEngine.buildRuleSapces();
    }

    @Test
    public void performRuleEngineExecutionTest(){
        AbstractPraeceptaRuleExecutionEngine abstractPraeceptaRuleExecutionEngine = new PraeceptaBasicRuleExecutionEngine();
        PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();
        abstractPraeceptaRuleExecutionEngine.performRuleEngineExecution(ruleStore);
    }
}
