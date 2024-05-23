package io.praecepta.rules;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.api.controller.IPraeceptaRuleExecutionController;
import io.praecepta.rest.config.PraeceptaRuleExecutorConfiguration;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PraeceptaRuleExecutorConfiguration.class)
public class MultiConditionNestedAttibuteRuleExecutionTest {


    private static final String PATH_PARAM_RULEGROUPNAME = ":rulegroupname";

    private static final String PATH_PARAM_SPACE_NAME = ":spacename";

    private static final String PATH_PARAM_CLIENT_ID = ":clientid";

    private static final String PATH_PARAM_APP_NAME = ":appname";

    private static final String PATH_PARAM_VERSION = ":version";

    private static final String PATH_PARAM_GROUP_NAME = ":groupname";

    @Test
    public void test(){


        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(PATH_PARAM_SPACE_NAME, "praecepta");
        pathParams.put(PATH_PARAM_CLIENT_ID, "icici");
        pathParams.put(PATH_PARAM_APP_NAME, "insurance");
        pathParams.put(PATH_PARAM_VERSION, "V3");
        pathParams.put(PATH_PARAM_GROUP_NAME, "dobvalidation");

        PraeceptaRequestStore store = new PraeceptaRequestStore();
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS, pathParams);
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION, "executeRule");


        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT, getInputMessage());


        System.setProperty("contextPath", "io.praecepta.rest.config.PraeceptaRuleExecutorConfiguration");

        PraeceptaRuleBuilder.buildWithPropsClasspath("praecepta-rule-load-test.properties").build(null, null, null, null);
        ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
        IPraeceptaRuleExecutionController praeceptaRuleExecutionController = (IPraeceptaRuleExecutionController) context.getBean("ruleExecutionController");
        praeceptaRuleExecutionController.execute(store);
        Assert.assertEquals("true",GsonHelper.toEntity((String)store.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT), Map.class).get("ageValidation"));
    }

    @Test
    public void test2(){


        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(PATH_PARAM_SPACE_NAME, "praecepta");
        pathParams.put(PATH_PARAM_CLIENT_ID, "icici");
        pathParams.put(PATH_PARAM_APP_NAME, "insurance");
        pathParams.put(PATH_PARAM_VERSION, "V3");
        pathParams.put(PATH_PARAM_GROUP_NAME, "dobornamevalidation");

        PraeceptaRequestStore store = new PraeceptaRequestStore();
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS, pathParams);
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION, "executeRule");


        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT, getInputMessage());


        System.setProperty("contextPath", "io.praecepta.rest.config.PraeceptaRuleExecutorConfiguration");

        PraeceptaRuleBuilder.buildWithPropsClasspath("praecepta-rule-load-test.properties").build(null, null, null, null);
        ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
        IPraeceptaRuleExecutionController praeceptaRuleExecutionController = (IPraeceptaRuleExecutionController) context.getBean("ruleExecutionController");
        praeceptaRuleExecutionController.execute(store);
        Assert.assertEquals("true",GsonHelper.toEntity((String)store.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT), Map.class).get("ageValidation"));

    }

    @Test
    public void test3(){


        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(PATH_PARAM_SPACE_NAME, "praecepta");
        pathParams.put(PATH_PARAM_CLIENT_ID, "icici");
        pathParams.put(PATH_PARAM_APP_NAME, "insurance");
        pathParams.put(PATH_PARAM_VERSION, "V3");
        pathParams.put(PATH_PARAM_GROUP_NAME, "dobandnamevalidation");

        PraeceptaRequestStore store = new PraeceptaRequestStore();
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS, pathParams);
        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION, "executeRule");


        store.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT, getInputMessage());


        System.setProperty("contextPath", "io.praecepta.rest.config.PraeceptaRuleExecutorConfiguration");

        PraeceptaRuleBuilder.buildWithPropsClasspath("praecepta-rule-load-test.properties").build(null, null, null, null);
        ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
        IPraeceptaRuleExecutionController praeceptaRuleExecutionController = (IPraeceptaRuleExecutionController) context.getBean("ruleExecutionController");
        praeceptaRuleExecutionController.execute(store);
        Assert.assertEquals("false",GsonHelper.toEntity((String)store.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT), Map.class).get("ageValidation"));

    }
    public String getInputMessage(){

        return "{\"emp\":{\"name\":\"vara\", \"dob\":\"1999-01-01\"}}";
    }

}
