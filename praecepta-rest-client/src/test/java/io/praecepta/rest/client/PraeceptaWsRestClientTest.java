package io.praecepta.rest.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;

public class PraeceptaWsRestClientTest {

	@Test
	public void test() {
		
		PraeceptaWsOperationType httpOperationType = PraeceptaWsOperationType.POST;
		
		PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();
		
//		config.setEndpointUrl("http://localhost:4567/myTestController/testPost/{appName}/{grpName}?query1={value1PlaceHolder}");
		config.setEndpointUrl("http://localhost:4567/myTestController/testPost/{appName}/{grpName}");
		config.setRequestType(httpOperationType.name());
		config.setConnectionTimeOut(10000L);
		config.setReadTimeOut(7500L);
		
		PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);
		
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("appName", "TestApp");
		pathParams.put("grpName", "TestGrp");
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryParam1", "Value1");
		queryParams.put("queryParam2", "Value2");
		
		Map<String, String> inHeaderParams = new HashMap<String, String>();
		inHeaderParams.put("messageId", UUID.randomUUID().toString());
		
		Map<String, String> authParams = new HashMap<String, String>();
		
		String jwtToken = "{\r\n" + 
				"  \"sub\": \"1234567890\",\r\n" + 
				"  \"name\": \"John Doe\",\r\n" + 
				"  \"admin\": true\r\n" + 
				"}";
		
//		authParams.put("Authorization", "Bearer "+jwtToken);

		String inputJson = "{\"userName\":\"testUser\",\"age\":999}";

		PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(httpOperationType,
				inputJson, queryParams, pathParams, inHeaderParams, authParams);
		
		PraeceptaWsRestClient<PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig>> restClient =
				new PraeceptaWsRestClient<>(simpleRestBuilder);
		
		restClient.initialize();
		
		restClient.triggerCall(wsReqResHolder);
	}

}
