package io.praecepta.rules.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleGroupMongoDbDao;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleSpaceMongoDbDao;
import io.praecepta.rules.hub.nosqlbased.service.IPraeceptaMongoDbService;
import io.praecepta.rules.hub.nosqlbased.service.PraeceptaMongoDbServiceImpl;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.projection.IPraeceptaAction;

public class PraeceptaMongoDbServiceTest {

    //@Test
    public void fetchAllTest(){
        Properties properties = new Properties();
        properties.put("QL_DB.connection_props.mongo.ip", "localhost");
        properties.put("QL_DB.connection_props.mongo.port", 27017);

        PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao = new PraeceptaRuleSpaceMongoDbDao(properties);
        PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao = new PraeceptaRuleGroupMongoDbDao(properties);
        IPraeceptaMongoDbService praeceptaMongoDbService = new PraeceptaMongoDbServiceImpl(praeceptaRuleGroupMongoDbDao, praeceptaRuleSpaceMongoDbDao);

     //  Collection<PraeceptaRuleSpace> praeceptaRuleSpaces = praeceptaMongoDbService.fetchAll();
       // Assert.assertNotNull(praeceptaRuleSpaces);
    }

    //@Test
    public void fetchByIdsTest(){
        Properties properties = new Properties();
        properties.put("QL_DB.connection_props.mongo.ip", "localhost");
        properties.put("QL_DB.connection_props.mongo.port", 27017);

        PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao = new PraeceptaRuleSpaceMongoDbDao(properties);
        PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao = new PraeceptaRuleGroupMongoDbDao(properties);
        IPraeceptaMongoDbService praeceptaMongoDbService = new PraeceptaMongoDbServiceImpl(praeceptaRuleGroupMongoDbDao, praeceptaRuleSpaceMongoDbDao);

        PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("BRB","ICICI","App1");
        Collection<PraeceptaRuleSpaceCompositeKey> compositeKeys = new ArrayList<>();
        compositeKeys.add(compositeKey);
        //Collection<PraeceptaRuleSpace> praeceptaRuleSpaces = praeceptaMongoDbService.fetchByIds(compositeKeys);
        //Assert.assertNotNull(praeceptaRuleSpaces);
    }

    //@Test
    public void fetchByIdsWithOutVersionTest(){
        Properties properties = new Properties();
        properties.put("QL_DB.connection_props.mongo.ip", "localhost");
        properties.put("QL_DB.connection_props.mongo.port", 27017);

        PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao = new PraeceptaRuleSpaceMongoDbDao(properties);
        PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao = new PraeceptaRuleGroupMongoDbDao(properties);
        IPraeceptaMongoDbService praeceptaMongoDbService = new PraeceptaMongoDbServiceImpl(praeceptaRuleGroupMongoDbDao, praeceptaRuleSpaceMongoDbDao);

        PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("BRB","ICICI","App1");
        compositeKey.setVersion(null);
        Collection<PraeceptaRuleSpaceCompositeKey> compositeKeys = new ArrayList<>();
        compositeKeys.add(compositeKey);
        //Collection<PraeceptaRuleSpace> praeceptaRuleSpaces = praeceptaMongoDbService.fetchByIds(compositeKeys);
        //Assert.assertNotNull(praeceptaRuleSpaces);
    }


    //@Test
    public void insertTest(){
        Properties properties = new Properties();
        properties.put("QL_DB.connection_props.mongo.ip", "localhost");
        properties.put("QL_DB.connection_props.mongo.port", 27017);

        PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao = new PraeceptaRuleSpaceMongoDbDao(properties);
        PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao = new PraeceptaRuleGroupMongoDbDao(properties);
        IPraeceptaMongoDbService praeceptaMongoDbService = new PraeceptaMongoDbServiceImpl(praeceptaRuleGroupMongoDbDao, praeceptaRuleSpaceMongoDbDao);

        PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("BRB","ICICI","App5");
        PraeceptaRuleSpace praeceptaRuleSpace = new PraeceptaRuleSpace();
        praeceptaRuleSpace.setRuleSpaceKey(compositeKey);
        Collection<PraeceptaRuleGroup> praeceptaRuleGroups = new ArrayList<>();
        PraeceptaRuleGroup praeceptaRuleGroup = new PraeceptaRuleGroup("BRB","ICICI","App5");
        Collection<PraeceptaCriteria> praeceptaCriteriaCollection = new ArrayList<>();
        PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();
        Collection<IPraeceptaAction> actions = new ArrayList<>();

        PraeceptaValueAssignAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
        praeceptaValueAssignAction.setValueToAssign("100");
        praeceptaValueAssignAction.setActionAttributeName("attribute1");
        praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
        actions.add(praeceptaValueAssignAction);

     //   praeceptaCriteria.setActionToPerform(actions);

        //praeceptaCriteria.setPredicates();
        praeceptaCriteria.setOrderNumber(1);
        praeceptaCriteria.setRuleName("Rule1");
        praeceptaCriteriaCollection.add(praeceptaCriteria);
        praeceptaRuleGroup.setPraeceptaCriterias(praeceptaCriteriaCollection);
        praeceptaRuleGroup.setRuleSpaceKey(compositeKey);
        praeceptaRuleGroup.setRuleGroupName("RuleGroup1");
        praeceptaRuleGroups.add(praeceptaRuleGroup);
        praeceptaRuleSpace.setPraeceptaRuleGrps(praeceptaRuleGroups);
        //praeceptaMongoDbService.insert(praeceptaRuleSpace);
    }


}
