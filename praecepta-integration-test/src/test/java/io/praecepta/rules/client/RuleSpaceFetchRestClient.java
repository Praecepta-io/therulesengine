package io.praecepta.rules.client;
import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class RuleSpaceFetchRestClient {

    public static void main(String[] args) {
       String hostName = System.getProperty("server.hostName", "http://localhost:4567");

        PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

        config.setEndpointUrl(hostName+ "/ruleSpaceController/ruleSpaceList");
        config.setRequestType("GET");
        config.setConnectionTimeOut(10000L);
        config.setReadTimeOut(7500L);

        PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

        PraeceptaRestClientWrapper<RestTemplate> restClientWrapper =
                (PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();



        PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.GET,
                null, null, null, null, null);

        restClientWrapper.performOperation(wsReqResHolder, simpleRestBuilder.getWsClientConfig());
        System.out.println(wsReqResHolder.getOutput());
    }
}
