package io.praecepta.rules.client;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MultiNestedRuleGroupUpdateRestClient {
    public static void main(String[] args) {
        String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/ruleGroupController/updateMultiNestedRuleGroup/{space}/{client}/{appName}/{version}");
        config.setRequestType("POST");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();


        String inputJson = "{\"ruleGroupName\":\"Multi Nested Condition\",\"ruleSpaceInfo\":{\"spaceName\":\"HDFC\",\"clientId\":\"001\",\"appName\":\"App1\",\"version\":\"V1\"},\"multiNestedConditionCriteriaInfos\":[{\"ruleName\":\"Rule1\",\"multiNestedConditionList\":[{\"multiConditionsList\":[{\"conditionInfo\":[{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"LESS_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"blood pressure\",\"valueToCompare\":\"130\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"}]}]},{\"joinOperatorType\":\"AND\",\"conditionInfo\":[{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"blood pressure\",\"valueToCompare\":\"140\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"}]}]}]},{\"multiConditionsList\":[{\"conditionInfo\":[{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"LESS_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"sugar levels\",\"valueToCompare\":\"110\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_NUMBER\"}]}]},{\"joinOperatorType\":\"OR\",\"conditionInfo\":[{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"sugar levels\",\"valueToCompare\":\"130\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_NUMBER\"}]}]}],\"joinOperatorType\":\"OR\"}],\"actionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"true\"}],\"failureActionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"true\"}]}]}";

        Map<String,String> pathParams = new HashMap<>();
        pathParams.put("space","PNB");
        pathParams.put("client","001");
        pathParams.put("appName","App1");
        pathParams.put("version", "V1");

        PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.POST,
                inputJson, null, pathParams, null, null);

        restClientWrapper.performOperation(wsReqResHolder, simpleRestBuilder.getWsClientConfig());

        System.out.println(wsReqResHolder.getOutput());
    }
}
