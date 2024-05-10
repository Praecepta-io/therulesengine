package io.praecepta.rules.sidecars.enricher.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.builder.PraeceptaSecureRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaSecureWebServiceClientConfig;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;
import io.praecepta.rest.client.enums.REST_CLIENT_CONFIG_KEYS;
import io.praecepta.rest.client.enums.REST_CLIENT_TYPE;
import io.praecepta.rest.client.enums.VALUE_STRATEGY_TYPE;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaRestEnricherHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRestEnricherHelper.class);
	
	public static final String HEADERS_FROM_INPUT = REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key;
	
	public static final String RUN_TIME_CONFIG = IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG;

	public static final String MULTI_API_INDEX_SPLIT = "_";
	
	public static final String MULTI_API_INDEX_REGEX = "index";
	
	public static PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig> getClientBuilder(Map<String, Object> sideCarConfigs) {
		logger.debug("Inside getClientBuilder");

		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigs)) {
			
			String clientType =(String) sideCarConfigs.getOrDefault(REST_CLIENT_CONFIG_KEYS.CLIENT_TYPE.key, REST_CLIENT_TYPE.REST.name());
			
			logger.debug("Rest Client type :{}",clientType);
			
			REST_CLIENT_TYPE restClientType = REST_CLIENT_TYPE.valueOf(clientType);
			
			switch (restClientType) {
			
			case REST:
				return new PraeceptaRestClientBuilder<>(buildWSClientConfig(sideCarConfigs));
				
			case SECURE_REST:
				return new PraeceptaSecureRestClientBuilder(buildSecureWSClientConfig(sideCarConfigs));
				
			default:
				return null;
			}
		}
		
		logger.debug("Rest Client Configs not found");
		
		return null;

	}

	private static PraeceptaWebServiceClientConfig buildWSClientConfig(Map<String, Object> sideCarConfigs) {
		logger.debug("Inside getClientBuilder");
		
		if (!PraeceptaObjectHelper.isObjectNull(sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key))) {
			
			PraeceptaWebServiceClientConfig clientConfig = new PraeceptaWebServiceClientConfig();
			
			clientConfig.setEndpointUrl((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key));
			
			clientConfig.setRequestType((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key));
			
			logger.info(" Simple WS Service COnfig has Created {} ", clientConfig);
			
			return clientConfig;
		}
		logger.debug("Rest Client endPoint URL not found");
		
		return null;
	}

	private static PraeceptaSecureWebServiceClientConfig buildSecureWSClientConfig(Map<String, Object> sideCarConfigs) {
		logger.debug("Inside buildSecureWSClientConfig");

		if (!PraeceptaObjectHelper.isObjectNull(sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key))) {

			PraeceptaSecureWebServiceClientConfig secureRestClientConfig = new PraeceptaSecureWebServiceClientConfig();

			secureRestClientConfig.setEndpointUrl((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key));
			
			secureRestClientConfig.setRequestType((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key));

			secureRestClientConfig.setCertPath((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.CERT_PATH.key));

			secureRestClientConfig.setCertPassword((String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.CERT_PASSWORD.key));
			
			logger.info(" Simple Secure WS Service COnfig has Created {} ", secureRestClientConfig);

			return secureRestClientConfig;
		}

		logger.debug("Secure Rest Client endPoint URL not found");

		return null;
	}

	public static PraeceptaWsRequestResponseHolder buildRequestResponseHolder(PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> input, PraeceptaWsOperationType operationType) {
		logger.debug("Inside buildRequestResponseHolder");

		Map<String, String> queryParams = null;
		Map<String, String> pathParams = null;
		Map<String, String> inHeaderParams = null;
		Map<String, String> authParams = null;

		Map<String, Object> additionalInfo = input.getAdditionalHolderInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo)) {

			if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo.get(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key))) {
				queryParams = (Map<String, String>) additionalInfo.get(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key);
			}
			if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo.get(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key))) {
				pathParams = (Map<String, String>) additionalInfo.get(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key);
			}
			if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo.get(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key))) {
				inHeaderParams = (Map<String, String>) additionalInfo.get(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key);
			}
			if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo.get(REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key))) {
				authParams = (Map<String, String>) additionalInfo.get(REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key);
			}
		}

		PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(operationType,
				GsonHelper.toJson(input.retriveInput()), 
				queryParams, pathParams, inHeaderParams, authParams);
		
		logger.debug("Exiting buildRequestResponseHolder");

		return wsReqResHolder;
	}
	
	public static void captureApiResponse(PraeceptaWsRequestResponseHolder requestResponseHolderObj, PraeceptaSideCarDataHolder input) {
		logger.debug("Inside captureApiResponse");
		
		Map<String,Object> enrichedMap=new HashMap<>();
		//Adding incoming input to Map
		if (!PraeceptaObjectHelper.isObjectNull(requestResponseHolderObj.getInput())) {
			enrichedMap.putAll(GsonHelper.toMapEntityPreserveNumber(requestResponseHolderObj.getInput()));
		}
		
		if (!PraeceptaObjectHelper.isObjectNull(requestResponseHolderObj.getHttpStatus())
				&& requestResponseHolderObj.getHttpStatus().value() == 200) {
            //Combining input and enrich response to map
			if (!PraeceptaObjectHelper.isObjectNull(requestResponseHolderObj.getOutput())) {
				enrichedMap.putAll(GsonHelper.toMapEntityPreserveNumber(requestResponseHolderObj.getOutput()));
				input.addOutput(enrichedMap);
			}
			
			if (!PraeceptaObjectHelper.isObjectNull(requestResponseHolderObj.getOutHeaderParams())) {
				if(input.getAdditionalHolderInfo() == null){
					input.setAdditionalHolderInfo(new HashMap<>());
				}
				
				input.getAdditionalHolderInfo().put(REST_CLIENT_CONFIG_KEYS.OUT_HEADER_PARAMS.key,
						requestResponseHolderObj.getOutHeaderParams());
			}
		}
		
		logger.debug("Exiting captureApiResponse");
	}
	
	public static void captureAdditionalInfo(Map<String, Object> additionalInfo, Map<String, Object> sideCarInput) {
		logger.debug("Inside captureAdditionalInfo");
		
		if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfo.get(RUN_TIME_CONFIG))) {
			
			Map<String,Object> sideCarConfigs=(Map<String, Object>) additionalInfo.get(RUN_TIME_CONFIG);
			
			additionalInfo.put(REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key, captureInputParams(sideCarInput, sideCarConfigs,
					REST_CLIENT_CONFIG_KEYS.QUERY_PARAMS.key));

			additionalInfo.put(REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key,
					captureInputParams(sideCarInput, sideCarConfigs, REST_CLIENT_CONFIG_KEYS.PATH_PARAMS.key));

			additionalInfo.put(REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key,
					captureInputParams(sideCarInput, sideCarConfigs, REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.key));

			Map<String, String> headerParams = captureInputParams(sideCarInput, sideCarConfigs,
					REST_CLIENT_CONFIG_KEYS.AUTH_PARAMS.IN_HEADER_PARAMS.key);

			Map<String, String> headersFrmInput = (Map<String, String>) additionalInfo.get(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key);

			if (PraeceptaObjectHelper.isObjectEmpty(headersFrmInput)) {
				headersFrmInput = new HashMap<>();
			}

			headersFrmInput.putAll(headerParams);

			additionalInfo.put(REST_CLIENT_CONFIG_KEYS.IN_HEADER_PARAMS.key, headersFrmInput);
		}
		logger.debug("Exiting captureAdditionalInfo");
	}
	
	public static Map<String, String> captureInputParams(Map<String, Object> input, Map<String, Object> sideCarConfigs, String requestParamType) {
		Map<String, String> params = new HashMap<>(); 
		
		logger.debug("Getting AdditionalInfo for Param Type - {} ", requestParamType);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigs.get(requestParamType))) {
			
			List<Map<String, String>> paramConfigs = (List<Map<String, String>>) sideCarConfigs.get(requestParamType);
			
			if (!PraeceptaObjectHelper.isObjectEmpty(paramConfigs)) {
				paramConfigs.stream().forEach(paramConfig -> {
					
					Object sourceToUseIntheEndpoint = paramConfig.get(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_IN_SOURCE.key);
					
					logger.debug("Source To Use in the endpoint - {} ", sourceToUseIntheEndpoint);
					
					if (!PraeceptaObjectHelper.isObjectEmpty(sourceToUseIntheEndpoint)) {
						
						// Check the Value Strategy. Either to Pull the Value from Payload or using a Constant value passed
						String valueStrategy = paramConfig.get(REST_CLIENT_CONFIG_KEYS.PARAM_VALUE_STRATEGY.key);

						VALUE_STRATEGY_TYPE strategyType = VALUE_STRATEGY_TYPE.FROM_PAY_LOAD;

						if (!PraeceptaObjectHelper.isObjectEmpty(valueStrategy)) {
							strategyType = VALUE_STRATEGY_TYPE.getType(valueStrategy.toUpperCase());
						}
						
						logger.debug("Value Strategy to be used - {} ", strategyType);

						if (VALUE_STRATEGY_TYPE.FROM_PAY_LOAD == strategyType) {
							
							String paramValueFromInput = getParamValueFromInput(input, String.valueOf(sourceToUseIntheEndpoint).split(REST_CLIENT_CONFIG_KEYS.PARAM_VAL_SPLIT_REGEX.key));

							logger.debug("Parameter Value Retrived from Nesed Map - {} ", paramValueFromInput);
							
							if (!PraeceptaObjectHelper.isObjectNull(paramValueFromInput)) {
								params.put(paramConfig.get(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key),
										paramValueFromInput);

							}
						} else if (VALUE_STRATEGY_TYPE.CONSTANT == strategyType) {
							
							logger.debug("Constant Value Getting Added to the Url Parameter - {} ", sourceToUseIntheEndpoint);
							
							params.put(paramConfig.get(REST_CLIENT_CONFIG_KEYS.PARAM_NAME_IN_THE_ENDPOINT.key),
									String.valueOf(sourceToUseIntheEndpoint));
						}
					}
				});
			}
		}
		return params;
	}

	public static Map<Integer, Map<String, Object>> getMultiApiSideCarConfigs(Map<String, Object> input) {

		Map<Integer, Map<String, Object>> multiApiConfigMap = new HashMap<>();

		input.entrySet().stream().forEach(e -> {

			String[] multiApiIndexKeyArr = e.getKey().split(MULTI_API_INDEX_SPLIT);

			if (!PraeceptaObjectHelper.isObjectEmpty(multiApiIndexKeyArr) && multiApiIndexKeyArr.length > 1) {

				Integer indexId = getIndexId(multiApiIndexKeyArr[0]);

				if (!PraeceptaObjectHelper.isObjectNull(indexId)) {

					multiApiConfigMap.putIfAbsent(indexId, new HashMap<String, Object>());

					multiApiConfigMap.get(indexId).putIfAbsent(multiApiIndexKeyArr[1], e.getValue());

				}else {
					logger.debug("index Id found null");
				}
				
			}

		});
		return multiApiConfigMap;
	}
	
	public static String getParamValueFromInput(Map<String, Object> inputMap, String[] keys) {
		Object paramValue = null;
		Map<String, Object> valueMap = inputMap;
		for (String key : keys) {
			if (valueMap.get(key) != null) {
				paramValue = valueMap.get(key);
				if (paramValue instanceof Map) {
					valueMap = (Map<String, Object>) paramValue;
				} else {
					break;
				}
			}
		}
		if (!PraeceptaObjectHelper.isObjectNull(paramValue)) {
			return paramValue.toString();
		}
		return null;
	}
	public static Integer getIndexId(String indexStr) {
		try {
			if(!PraeceptaObjectHelper.isObjectEmpty(indexStr)) {
				return Integer.parseInt(indexStr.substring(MULTI_API_INDEX_REGEX.length()));
			}
		} catch (NumberFormatException e) {
			logger.error("error while getting api index Id from indexConfig {}",indexStr,e);
		}
		return null;
	}
}
