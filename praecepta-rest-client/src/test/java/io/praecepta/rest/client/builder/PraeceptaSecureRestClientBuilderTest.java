package io.praecepta.rest.client.builder;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.config.PraeceptaSecureWebServiceClientConfig;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

public class PraeceptaSecureRestClientBuilderTest {

	@Test
	public void test() {
		
		PraeceptaSecureWebServiceClientConfig config = new PraeceptaSecureWebServiceClientConfig();
		
		config.setEndpointUrl("http://localhost:4567/myTestController/testGet/{userName}/{age}");
		config.setRequestType("GET");
		config.setConnectionTimeOut(10000L);
		config.setReadTimeOut(7500L);
		
		config.setCertPath("test Path");
		config.setCertPassword("TestPassword");
		
		PraeceptaSecureRestClientBuilder secureRestBuilder = new PraeceptaSecureRestClientBuilder(config);
		
		PraeceptaRestClientWrapper<RestTemplate> restClientWrapper = 
				(PraeceptaRestClientWrapper<RestTemplate>) secureRestBuilder.buildClient();
		
		assertNotNull(restClientWrapper);
	}

}
