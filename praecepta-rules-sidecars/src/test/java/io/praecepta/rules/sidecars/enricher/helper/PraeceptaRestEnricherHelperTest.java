package io.praecepta.rules.sidecars.enricher.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.builder.PraeceptaSecureRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaSecureWebServiceClientConfig;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;
import io.praecepta.rest.client.enums.REST_CLIENT_CONFIG_KEYS;
import io.praecepta.rest.client.enums.REST_CLIENT_TYPE;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaRestEnricherHelperTest{
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRestEnricherHelperTest.class);

	@Test
	public void testRestTypeBuilder() {
		
		Map<String, Object> sideCarConfigs = new HashMap<String, Object>();
		
		sideCarConfigs.put("Type", "Enricher");
		
		PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> defaultRestClientBuilder = 
				PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs);
		
		assertEquals( PraeceptaRestClientBuilder.class.getName(), defaultRestClientBuilder.getClass().getName());
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.REST.name());
		
		PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> generatedRestClientBuilder = 
				PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs);
		
		assertEquals( PraeceptaRestClientBuilder.class.getName(), generatedRestClientBuilder.getClass().getName());
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.SECURE_REST.name());
		
		PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> generatedSecureRestClientBuilder = 
				PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs);
		
		assertEquals( PraeceptaSecureRestClientBuilder.class.getName(), generatedSecureRestClientBuilder.getClass().getName());
	}
	
	@Test
	public void testGenerateRestBuilderConfig() {
		
		Map<String, Object> sideCarConfigs = new HashMap<String, Object>();
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.REST.name());
		
		String url = "http://test";
		String httpMethod = "GET";
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key, url);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key, httpMethod);
		
		PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> generatedRestClientBuilder = 
				PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs);
		
		PraeceptaWebServiceClientConfig wsConfig = generatedRestClientBuilder.getWsClientConfig();
		
		assertNotNull(wsConfig);
		
		assertTrue(wsConfig instanceof PraeceptaWebServiceClientConfig);
		assertEquals(PraeceptaWebServiceClientConfig.class.getName(), wsConfig.getClass().getName());
		
		logger.debug(" WS Config Object Details -> {}", wsConfig);
		
		assertEquals(url, wsConfig.getEndpointUrl());
		assertEquals(httpMethod, wsConfig.getRequestType());
		
	}
	
	@Test
	public void testGenerateSecureRestBuilderConfig() {
		
		Map<String, Object> sideCarConfigs = new HashMap<String, Object>();
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.SECURE_REST.name());
		
		String url = "http://test";
		String httpMethod = "GET";
		String certPath = "C:\\certpath.txt";
		String password = "testPassword";
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key, url);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key, httpMethod);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CERT_PATH.key, certPath);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CERT_PASSWORD.key, password);
		
		PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> generatedRestClientBuilder = 
				PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs);
		
		PraeceptaWebServiceClientConfig wsConfig = generatedRestClientBuilder.getWsClientConfig();
		
		assertNotNull(wsConfig);
		
		logger.debug(" Secure WS Config Object Details -> {}", wsConfig);
		
		assertEquals(PraeceptaSecureWebServiceClientConfig.class.getName(), wsConfig.getClass().getName());
		
		assertTrue(wsConfig instanceof PraeceptaSecureWebServiceClientConfig);
		
		assertEquals(url, wsConfig.getEndpointUrl());
		assertEquals(httpMethod, wsConfig.getRequestType());
		
	}
	
	@Test
	public void testBuildRequestResponseHolder() {
		
		PraeceptaWsOperationType operationType = PraeceptaWsOperationType.GET;
		
		PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> input = buildSideCarInfo();
		
		PraeceptaWsRequestResponseHolder wsReqResHolder = PraeceptaRestEnricherHelper.buildRequestResponseHolder(input, operationType);
		
		assertNotNull(wsReqResHolder);
		
		assertEquals(input.retriveInput(), wsReqResHolder.getInput());
	}
	
	@Test
	public void testCaptureApiResponse() {
		
		PraeceptaWsOperationType operationType = PraeceptaWsOperationType.GET;

		PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> sideCarHolderObj = buildSideCarInfo();
		
		PraeceptaWsRequestResponseHolder wsReqResHolder = PraeceptaRestEnricherHelper.buildRequestResponseHolder(sideCarHolderObj, operationType);
		
		wsReqResHolder.setHttpStatus(HttpStatus.OK);
		wsReqResHolder.getOutHeaderParams().put("testOutHeader", "testOutHeaderValue1");
		
		wsReqResHolder.setOutput("Test Output");
		
		PraeceptaRestEnricherHelper.captureApiResponse(wsReqResHolder, sideCarHolderObj);
		
		assertEquals(sideCarHolderObj.retriveOutput(), wsReqResHolder.getOutput());
		
		assertNotNull(sideCarHolderObj.getAdditionalHolderInfo().get(REST_CLIENT_CONFIG_KEYS.OUT_HEADER_PARAMS.key));
		
	}
	
	@Test
	public void testCaptureAdditionalInfoFromSidecarInfo() {
		
		Map<String, Object> sideCarInfo = new HashMap<>();//getAdditionalInfo();
		
		// Add path Params to Side Car Info
		List<Map<String,Object>> pathParamConfigs=new ArrayList<>();
		
		Map<String, Object> paramConfig1=new HashMap<>();
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.userName");
		
		
		Map<String, Object> paramConfig2=new HashMap<>();
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.age");
		
		pathParamConfigs.add(paramConfig1);
		pathParamConfigs.add(paramConfig2);
		
		sideCarInfo.put(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key, pathParamConfigs);
		
		// Add Query Params to Side Car Info
		List<Map<String,Object>> queryParamConfigs=new ArrayList<>();
		
		Map<String, Object> queryConfig1=new HashMap<>();
		queryConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
		queryConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.userName");
		
		
		Map<String, Object> queryConfig2=new HashMap<>();
		queryConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
		queryConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.age");
		
		queryParamConfigs.add(queryConfig1);
		queryParamConfigs.add(queryConfig2);
		
		sideCarInfo.put(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key, queryParamConfigs);
		
		// Add Header Params to Side Car Info
		List<Map<String,Object>> headerParamConfigs=new ArrayList<>();
		
		Map<String, Object> headerConfig1=new HashMap<>();
		headerConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
		headerConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.userName");
		
		
		Map<String, Object> headerConfig2=new HashMap<>();
		headerConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
		headerConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.age");
		
		headerParamConfigs.add(headerConfig1);
		headerParamConfigs.add(headerConfig2);
		
		sideCarInfo.put(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key, headerParamConfigs);
		
		Map<String, Object> sideCarInput = new HashMap<>();
		sideCarInput.put("inpur", "Test Input") ;
		
		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG, sideCarInfo);

		PraeceptaRestEnricherHelper.captureAdditionalInfo(additionalInfo, sideCarInput);
		
		assertNotNull(additionalInfo.get(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key));
		
	}

	private PraeceptaSideCarDataHolder buildSideCarInfo() {
		PraeceptaSideCarDataHolder sideCarHolderObj = new PraeceptaSideCarDataHolder();
		
		Map<String, Object> additionalInfo = getAdditionalInfo();
		
		sideCarHolderObj.setAdditionalHolderInfo(additionalInfo);

		String inputJson = "{\"userName\":\"testUser\",\"age\":999}";
		
		sideCarHolderObj.addInput(inputJson);
		return sideCarHolderObj;
	}

	private Map<String, Object> getAdditionalInfo() {
		Map<String, Object> additionalInfo = new HashMap<String, Object>();
		
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("appName", "TestApp");
		pathParams.put("grpName", "TestGrp");
		
		additionalInfo.put(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key, pathParams);
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryParam1", "Value1");
		queryParams.put("queryParam2", "Value2");
		
		additionalInfo.put(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key, queryParams);
		
		Map<String, String> inHeaderParams = new HashMap<String, String>();
		inHeaderParams.put("messageId", UUID.randomUUID().toString());
		
		Map<String, String> authParams = new HashMap<String, String>();
		
		String jwtToken = "{\r\n" + 
				"  \"sub\": \"1234567890\",\r\n" + 
				"  \"name\": \"John Doe\",\r\n" + 
				"  \"admin\": true\r\n" + 
				"}";
		
		additionalInfo.put(REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key, authParams);
		
//		authParams.put("Authorization", "Bearer "+jwtToken);
		additionalInfo.put(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key, inHeaderParams);
		
		return additionalInfo;
	}

}
