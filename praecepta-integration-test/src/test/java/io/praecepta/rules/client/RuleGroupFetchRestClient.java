package io.praecepta.rules.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class RuleGroupFetchRestClient {
    public static void main(String[] args) {
        String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/ruleGroupController/ruleGroups/{space}/{client}/{appName}/{version}/{groupName}");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();



        Map<String,String> pathParams = new HashMap<>();
        pathParams.put("space","KOTAK");
        pathParams.put("client","001");
        pathParams.put("appName","App1");
        pathParams.put("version", "V1");
        pathParams.put("groupName", "Simple Condition Group");

        PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.GET,
                null, null, pathParams, null, null);

        restClientWrapper.performOperation(wsReqResHolder, simpleRestBuilder.getWsClientConfig());

        System.out.println(wsReqResHolder.getOutput());
    }
}
