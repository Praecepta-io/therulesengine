package io.praecepta.rules.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class MultiRuleGroupCreationRestClient {
    public static void main(String[] args) {
        String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/ruleGroupController/multiRuleGroups/{space}/{client}/{appName}/{version}");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();


        String inputJson = "{\"ruleGroupName\":\"Multi Condition\",\"ruleSpaceInfo\":{\"spaceName\":\"HDFC\",\"clientId\":\"001\",\"appName\":\"App1\",\"version\":\"V1\"},\"multiConditionCriteriaInfos\":[{\"ruleName\":\"Rule1\",\"multiConditionList\":[{\"conditionInfo\":[{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"LESS_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"Blood Pressure\",\"valueToCompare\":\"130\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"}]},{\"conditionInfoList\":[{\"attributeName\":\"age\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"},{\"attributeName\":\"blood pressure\",\"valueToCompare\":\"140\",\"attributeToCompare\":\"\",\"joinOperatorType\":\"AND\",\"operatorType\":\"GREATER_THAN_EQUAL_NUMBER\"}],\"joinOperatorType\":\"OR\"}]}],\"actionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"true\"}],\"failureActionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"false\"}]}]}";

        Map<String,String> pathParams = new HashMap<>();
        pathParams.put("space","KOTAK");
        pathParams.put("client","001");
        pathParams.put("appName","App1");
        pathParams.put("version", "V1");

        PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.POST,
                inputJson, null, pathParams, null, null);

        restClientWrapper.performOperation(wsReqResHolder, simpleRestBuilder.getWsClientConfig());

        System.out.println(wsReqResHolder.getOutput());
    }
}
