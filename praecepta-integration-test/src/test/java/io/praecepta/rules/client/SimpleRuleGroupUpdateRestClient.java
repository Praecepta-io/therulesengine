package io.praecepta.rules.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class SimpleRuleGroupUpdateRestClient {
    public static void main(String[] args) {
        String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/ruleGroupController/updateRuleGroup/{space}/{client}/{appName}/{version}");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();


        String inputJson = "{\"ruleGroupName\":\"Simple Condition Group\",\"ruleSpaceInfo\":{\"spaceName\":\"KOTAK\",\"clientId\":\"001\",\"appName\":\"App1\",\"version\":\"V1\"},\"simpleConditionCriteriaInfos\":[{\"ruleName\":\"Age Validation2\",\"conditionInfo\":{\"conditionInfoList\":[{\"attributeName\":\"age\",\"operatorType\":\"GREATER_THAN_NUMBER\",\"valueToCompare\":\"40\",\"attributeToCompare\":\"\"}]},\"actionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"true\"}],\"failureActionList\":[{\"sourceValueAttributeName\":\"\",\"actionAttributeName\":\"Medical Test Required\",\"valueToAssign\":\"false\"}]}]}";

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
