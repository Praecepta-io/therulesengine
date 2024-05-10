package io.praecepta.rules.sidecars.enricher;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.client.enums.REST_CLIENT_CONFIG_KEYS;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper;
import io.praecepta.rules.sidecars.enricher.impl.PraeceptaSimpleRestApiEnricher;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaSimpleRestApiEnricherTest {

	@Before
	public void init() {
		
	}

	@Test
	public void test() {
		PraeceptaSimpleRestApiEnricher apiEnricherTest = new PraeceptaSimpleRestApiEnricher();

		PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("userName", "test123");
		pathParams.put("age", "65");
		
		sideCarDataHolder.addInput(
				GsonHelper.toEntity(
				"{\"spaceName\": \"test1\",\"clientId\": \"CLNT1\",\"appName\": \"praecepta\", \"ruleGroupName\" :\"RuleGroup1\", \"email\": \"test@gmail.com\",\"email2\": \"test@gmail.com\",\"mobile\": \"1234567890\",\"version\":\"V1\",\"test\":{\"userName\":\"app1234\",\"age\":\"23\"}}"
						, Map.class));

		Map<String, Object> additionalInfo = new HashMap<>();
	    
		/*SideCar Metadata configuration structure
		 * 
		 {"ruleGrpName":"RuleGroup1","preRuleGrpSideCars":[{"order":1,"type":"ENRICHER","sideCarType":"SIMPLE_REST_API","sideCarConfigs":{"clientType":"REST","requestType":"GET","endPointURL":"http://localhost:4567/myTestController/testGet/{userName}/{age}","pathParams":[{"paramName":"userName","paramValue":"test.userName"},{"paramName":"age","paramValue":"test.age"}]}}]}
		 * 
		 */
		additionalInfo.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, getRestClientConfig());
		
		PraeceptaRestEnricherHelper.captureAdditionalInfo(additionalInfo, sideCarDataHolder.retriveInput());

		sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);


		apiEnricherTest.initializeEnricher();

		apiEnricherTest.enrich(sideCarDataHolder);
        
		assertNotNull(sideCarDataHolder.retriveOutput());
		
		assertNotNull(sideCarDataHolder.getAdditionalHolderInfo().get(REST_CLIENT_CONFIG_KEYS.OUT_HEADER_PARAMS.key));
	}
	
	public static Map<String, Object> getRestClientConfig(){
		Map<String, Object> restEnricherConfigs=new HashMap<>();
		restEnricherConfigs.put("clientType", "REST");
		
		restEnricherConfigs.put("requestType", "GET");
		
		restEnricherConfigs.put("endPointURL", "http://localhost:8080/myTestController/testGet/{userName}/{age}");
		
		restEnricherConfigs.put("requestType", "GET");
		
		List<Map<String,Object>> pathParamConfigs=new ArrayList<>();
		
		Map<String, Object> paramConfig1=new HashMap<>();
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.userName");
		
		
		Map<String, Object> paramConfig2=new HashMap<>();
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "test.age");
		
		pathParamConfigs.add(paramConfig1);
		pathParamConfigs.add(paramConfig2);
		
		restEnricherConfigs.put("pathParams", pathParamConfigs);
		
		
		return restEnricherConfigs;
	}
}
