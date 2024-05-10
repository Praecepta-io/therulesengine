package io.praecepta.rest.client.builder;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;

/**
 * 
 * Map<String, Object> restEnricherConfigs=new HashMap<>();
		restEnricherConfigs.put("clientType", "REST");
		
		restEnricherConfigs.put("requestType", "GET");
		
		restEnricherConfigs.put("endPointURL", "http://localhost:4567/myTestController/testGet/{userName}/{age}");
		
		restEnricherConfigs.put("requestType", "GET");
		
		List<Map<String,Object>> pathParamConfigs=new ArrayList<>();
		
		Map<String, Object> paramConfig1=new HashMap<>();
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_SOURCE.key, "test.userName");
		
		
		Map<String, Object> paramConfig2=new HashMap<>();
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_SOURCE.key, "test.age");
		
		pathParamConfigs.add(paramConfig1);
		pathParamConfigs.add(paramConfig2);
		
		restEnricherConfigs.put("pathParams", pathParamConfigs);
 *
 */

public class PraeceptaRestClientBuilderTest {

	@Test
	public void testSimpleWrapperClientWithBuilderConfig() {
		
		PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();
		
		config.setEndpointUrl("http://localhost:4567/myTestController/testGet/{userName}/{age}");
		config.setRequestType("GET");
		config.setConnectionTimeOut(10000L);
		config.setReadTimeOut(7500L);
		
		PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);
		
		PraeceptaRestClientWrapper<RestTemplate> restClientWrapper = 
				(PraeceptaRestClientWrapper<RestTemplate>) simpleRestBuilder.buildClient();
		
		assertNotNull(restClientWrapper);
	}

}
