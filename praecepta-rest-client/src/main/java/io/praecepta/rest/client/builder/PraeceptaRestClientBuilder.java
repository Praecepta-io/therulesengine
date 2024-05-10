package io.praecepta.rest.client.builder;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;
import io.praecepta.rest.client.wrapper.PraeceptaSpringRestClientWrapper;

public class PraeceptaRestClientBuilder<CONFIG extends PraeceptaWebServiceClientConfig> {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRestClientBuilder.class);

	private CONFIG wsClientConfig;
	
	private PraeceptaRestClientWrapper<?> restClient;
	
	public PraeceptaRestClientBuilder(CONFIG config) {
		this.wsClientConfig = config;
	}
	
	public PraeceptaRestClientWrapper<?> buildClient() {
		logger.debug("Inside buildCLient");
		
		return buildWrapperClient();
	}

	private PraeceptaRestClientWrapper<?> buildWrapperClient() {
		logger.info("Preparing to build Spring Rest Client Wrapper");
		
		PraeceptaRestClientWrapper<RestTemplate> springRestClient = new PraeceptaSpringRestClientWrapper();
		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(getHttpClient());
		
		logger.info("Connection Request Time Out Speccified {} ",wsClientConfig.getConnectionTimeOut().intValue());
		
		requestFactory.setConnectionRequestTimeout(wsClientConfig.getConnectionTimeOut().intValue());
		
		logger.info("Read Time Out Speccified {} ",wsClientConfig.getReadTimeOut().intValue());
		
		requestFactory.setReadTimeout(wsClientConfig.getReadTimeOut().intValue());
		
		logger.info(" Preparing the Rest Template with the given Configuration");
		
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		
		logger.info(" Rest Template Preparing is done");
		springRestClient.wrap(restTemplate, RestTemplate.class);
		
		restClient = springRestClient;
		return restClient;
	}
	
	protected HttpClient getHttpClient() {
		logger.info("Returning Default Http Client for Wrapper");
		
		return HttpClients.createDefault();
	}

	public CONFIG getWsClientConfig() {
		return wsClientConfig;
	}

	public PraeceptaRestClientWrapper<?> getWrappedRestClient() {
		return restClient;
	}
	
}
