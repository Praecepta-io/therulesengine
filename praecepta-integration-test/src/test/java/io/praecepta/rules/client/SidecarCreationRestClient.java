package io.praecepta.rules.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class SidecarCreationRestClient {
    public static void main(String[] args) {
        String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/sidecarController/saveSidecars/{space}/{client}/{appName}/{version}/{groupName}");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();


        String inputJson = "{\"ruleGrpName\":\"Simple Condition Group\",\"preRuleGrpSideCars\":[{\"order\":\"0\",\"sideCarType\":\"PARSER\",\"type\":\"JSON\",\"sideCarConfigs\":{\"metadat1\":\"matadatavalue1\"}}],\"postRuleGrpSideCars\":[{\"order\":\"0\",\"sideCarType\":\"FORMATTER\",\"type\":\"XML\"}],\"ruleLevelSideCarsInfo\":[{\"preRuleSideCars\":[{\"order\":\"0\",\"sideCarType\":\"PARSER\",\"type\":\"DELIMITER\"}],\"ruleName\":\"Rule1\",\"postRuleSideCars\":[{\"order\":\"0\",\"sideCarType\":\"FORMATTER\",\"type\":\"XML\"}]}]}";

        Map<String,String> pathParams = new HashMap<>();
        pathParams.put("space","KOTAK");
        pathParams.put("client","001");
        pathParams.put("appName","App1");
        pathParams.put("version", "V1");
        pathParams.put("groupName","Simple Condition Group");

        PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.PUT,
                inputJson, null, pathParams, null, null);

        restClientWrapper.performOperation(wsReqResHolder, simpleRestBuilder.getWsClientConfig());

        System.out.println(wsReqResHolder.getOutput());
    }
}
