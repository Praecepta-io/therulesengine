package io.praecepta.rest.client.wrapper;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;

public class PraeceptaSpringRestClientWrapperTest {

	@Test
	public void testSpringWrapperClient() {
		
		PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();
		
		config.setEndpointUrl("http://localhost:4567/myTestController/testGet/{userName}/{age}");
		config.setRequestType("GET");
		config.setConnectionTimeOut(10000L);
		config.setReadTimeOut(7500L);
		
		PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);
		
		PraeceptaSpringRestClientWrapper sprintgRestClientWrapper = 
				
				(PraeceptaSpringRestClientWrapper) simpleRestBuilder.buildClient();
		
		assertNotNull(sprintgRestClientWrapper);
	}

}
