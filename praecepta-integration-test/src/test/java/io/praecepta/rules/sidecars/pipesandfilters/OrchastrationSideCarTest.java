package io.praecepta.rules.sidecars.pipesandfilters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;
import io.praecepta.rest.client.enums.REST_CLIENT_CONFIG_KEYS;
import io.praecepta.rest.client.enums.REST_CLIENT_TYPE;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.PraeceptaEnricherTypeRegistry.PraeceptaEnricherType;
import io.praecepta.rules.sidecars.enums.PraeceptaDefaultSideCarClazzType;
import io.praecepta.rules.sidecars.formatter.PraeceptaFormatterTypeRegistry.PraeceptaFormatterType;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;
import io.praecepta.rules.sidecars.parser.PraeceptaParserTypeRegistry.PraeceptaParserType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class OrchastrationSideCarTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrchastrationSideCarTest.class);
	
	@Test
	public void test() {
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector parserSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(getParserSideCarMetaData());
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector enricherSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(getEnricherSideCarMetaData());
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector formatterSideCar = PraeceptaSideCarHelper
				.convertSideCarMetadataToSideCars(getFormatterSideCarMetaData());
		
		PraeceptaRequestStore ruleStore = PraeceptaRuleExecutionEngineHelper.createRuleStore(getInputJson(), null);
		
		List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars = new ArrayList<IPraeceptaInfoTrackerSideCarInjector>();
		
		executionSideCars.add(parserSideCar);
		executionSideCars.add(enricherSideCar);
		executionSideCars.add(formatterSideCar);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(executionSideCars)) {
			triggerSideCars(executionSideCars, ruleStore);
		}
		
	}
	
	void triggerSideCars(List<IPraeceptaInfoTrackerSideCarInjector> sideCarsToTrigger,
			PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside triggerSideCars of GenericPraeceptaInfoTrackerSideCarInjector");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarsToTrigger) && !PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			
			LOGGER.debug(" Number of Sidecars to Trigger -> {} ", sideCarsToTrigger.size());
//			ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_INPUT, getInputJson());
			
			
			PraeceptaSideCarDataHolder<String, String> startingHolder = new PraeceptaSideCarDataHolder<>();
			startingHolder.addOutput(getInputJson());
			
			IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.moveCurrentToParentSideCarHolder(ruleStore, startingHolder);
			
			sideCarsToTrigger.forEach(eachSideCarToExecute -> {
				
				eachSideCarToExecute.trackAndCaptureInitialInfo(ruleStore);
				eachSideCarToExecute.executeSideCar(ruleStore);
				eachSideCarToExecute.trackAndCaptureExitInfo(ruleStore);
			});
		
		}
		
		LOGGER.debug(" Exiting triggerSideCars of GenericPraeceptaInfoTrackerSideCarInjector");
	}

	private PraeceptaSideCarMetadata getParserSideCarMetaData() {
		
		PraeceptaSideCarMetadata parserSideCarMetaData = new PraeceptaSideCarMetadata();
		parserSideCarMetaData.setOrder(1);
		parserSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.PARSER.name());
		parserSideCarMetaData.setSideCarType(PraeceptaParserType.JSON.name());
		
		Map<String, Object> sideCarMetadataMap=new HashMap<>();
		parserSideCarMetaData.setSideCarConfigs(sideCarMetadataMap);
		
		return parserSideCarMetaData;
	}
	
	private PraeceptaSideCarMetadata getEnricherSideCarMetaData() {
		
		PraeceptaSideCarMetadata enricherSideCarMetaData = new PraeceptaSideCarMetadata();
		enricherSideCarMetaData.setOrder(1);
		enricherSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.ENRICHER.name());
		enricherSideCarMetaData.setSideCarType(PraeceptaEnricherType.SIMPLE_REST_API.name());
		
		Map<String,Object> enrSideCarMetadataMap = getSimpleApiConfig();
		
		
		enricherSideCarMetaData.setSideCarConfigs(enrSideCarMetadataMap);
		return enricherSideCarMetaData;
	}
	
	/**
	 * 
	 * {"ruleGrpName":"RuleGroup1","preRuleGrpSideCars":[{"order":1,"type":"ENRICHER","sideCarType":"SIMPLE_REST_API",
	 * "sideCarConfigs":{"clientType":"REST","requestType":"GET",
	 * "endPointURL":"http://localhost:4567/myTestController/testGet/{userName}/{age}",
	 * "pathParams":[{REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key:"userName","paramValue":"test.userName"},
	 * {"paramName":"age","paramValue":"test.age"}]}}]}
	 */
	
	private Map<String,Object> getSimpleApiConfig(){
		
		Map<String,Object> sideCarConfigs = new HashMap<>();
		
		String url = "http://localhost:8080/myTestController/testGet/{userName}/{age}";
//		String url = "http://localhost:8080/myTestController/testGet/abc/2382";
		String httpMethod = PraeceptaWsOperationType.GET.name();
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key, url);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key, httpMethod);
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.REST.name());
		
		// Add path Params to Side Car Info
		List<Map<String,Object>> pathParamConfigs=new ArrayList<>();
		
		Map<String, Object> paramConfig1=new HashMap<>();
//		paramConfig1.put("paramName", "userName");
		
//		NOTE : This will be used for Path Param / Query Param and Header Param
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "userName");
//		paramConfig1.put("paramValue", "test.userName");
//		paramConfig1.put("paramValue", "name"); // Subject/Attribute Name from Input
		paramConfig1.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "name"); // Subject/Attribute Name from Input
//		paramConfig1.put("strategy", "constant");
		
		
		Map<String, Object> paramConfig2=new HashMap<>();
//		paramConfig2.put("paramName", "age");
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key, "age");
//		paramConfig2.put("paramValue", "test.age");
		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, "ageNum");// Subject/Attribute Name from Input
		
		/**
		 * Use Below Strategy and Constant Value incase we don't want to pull the Value from Input json Payload
		 */
//		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key, 50);// Subject/Attribute Name from Input
//		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_STRATEGY.key, VALUE_STRATEGY_TYPE.CONSTANT.name());

		//		paramConfig2.put(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_STRATEGY.key, "test");// Subject/Attribute Name from Input
		
		pathParamConfigs.add(paramConfig1);
		pathParamConfigs.add(paramConfig2);
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key, pathParamConfigs);
		
		// Add Query Params to Side Car Info
		/*List<Map<String,Object>> queryParamConfigs=new ArrayList<>();
		
		Map<String, Object> queryConfig1=new HashMap<>();
		queryConfig1.put("paramName", "userName");
		queryConfig1.put("paramValue", "test.userName");
		
		
		Map<String, Object> queryConfig2=new HashMap<>();
		queryConfig2.put("paramName", "age");
		queryConfig2.put("paramValue", "test.age");
		
		queryParamConfigs.add(queryConfig1);
		queryParamConfigs.add(queryConfig2);
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key, queryParamConfigs);
		
		// Add Header Params to Side Car Info
		List<Map<String,Object>> headerParamConfigs=new ArrayList<>();
		
		Map<String, Object> headerConfig1=new HashMap<>();
		headerConfig1.put("paramName", "userName");
		headerConfig1.put("paramValue", "test.userName");
		
		
		Map<String, Object> headerConfig2=new HashMap<>();
		headerConfig2.put("paramName", "age");
		headerConfig2.put("paramValue", "test.age");
		
		headerParamConfigs.add(headerConfig1);
		headerParamConfigs.add(headerConfig2);
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key, headerParamConfigs);*/
		
		
		/*Map<String, String> inHeaderParams = new HashMap<String, String>();
		inHeaderParams.put("messageId", UUID.randomUUID().toString());*/
		
		Map<String, String> authParams = new HashMap<String, String>();
		
		String jwtToken = "{\r\n" + 
				"  \"sub\": \"1234567890\",\r\n" + 
				"  \"name\": \"John Doe\",\r\n" + 
				"  \"admin\": true\r\n" + 
				"}";
		
		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key, authParams);
		
//		authParams.put("Authorization", "Bearer "+jwtToken);
//		sideCarConfigs.put(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key, inHeaderParams);
		
		return sideCarConfigs;
		
	}
	
	private PraeceptaSideCarMetadata getFormatterSideCarMetaData() {
		
		PraeceptaSideCarMetadata formatterSideCarMetaData = new PraeceptaSideCarMetadata();
		formatterSideCarMetaData.setOrder(1);
		formatterSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.FORMATTER.name());
		formatterSideCarMetaData.setSideCarType(PraeceptaFormatterType.XML.name());
		
		Map<String,Object> formatterSideCarInfo=new HashMap<>();
		
		formatterSideCarInfo.put("TEMPLATE_NAME", "XML_FORMATTER_TEST");
		formatterSideCarInfo.put("FORMATTER_TEMPLATE", "<name>${userName}</name><age>${age}</age>");
		
		formatterSideCarMetaData.setSideCarConfigs(formatterSideCarInfo);
		
		return formatterSideCarMetaData;
	}
	
	private static String getInputJson() {
		
//		return "{\"userName\":\"testUser\",\"age\":999}";
		return "{\"name\":\"testUser\",\"ageNum\":999}";
//		return "{\"name\":\"testUser\"}";
		
		/*return "{\r\n" + 
				"   \"spaceName\":\"" + ACATS_SPACE_NAME +"\",\r\n" + 
				"   \"clientId\":\"" + ACATS_CLIENT_NAME +"\",\r\n" + 
				"   \"appName\":\"" + ACATS_APP_NAME + "\",\r\n" + 
				"   \"ruleGroupName\":\"" + ACATS_TRANSFER_STOP_GRP + "\",\r\n" + 
				"   \"version\":\"" + ACATS_ACTIVE_SPACE_VERSION+ "\",\r\n" + 
				"   \"emp\":{\r\n" + 
				"      \"dob\":\"1999-01-01\",\r\n" + 
				"      \"name\":\"Vara\"\r\n" + 
				"   }\r\n" + 
				"}";*/
	}
	
	/*public static final String ACATS_ACTIVE_SPACE_VERSION = "1.0";

	public static final String ACATS_SPACE_NAME = "BRD";

	public static final String ACATS_CLIENT_NAME = "GTO";

	public static final String ACATS_APP_NAME = "ACATS";

	public static final String ACATS_TRANSFER_STOP_GRP = "TRANSFER_STOP";

	public static final String ACATS_RULE_NAME = "NameAndAgeRule";*/
}
