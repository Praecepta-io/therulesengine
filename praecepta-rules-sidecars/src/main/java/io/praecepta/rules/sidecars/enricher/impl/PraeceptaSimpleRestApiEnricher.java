package io.praecepta.rules.sidecars.enricher.impl;

import static io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper.buildRequestResponseHolder;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.client.PraeceptaWsRestClient;
import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType;
import io.praecepta.rest.client.enums.REST_CLIENT_CONFIG_KEYS;
import io.praecepta.rules.sidecars.enricher.IPraeceptaSideCarEnricher;
import io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaSimpleRestApiEnricher implements IPraeceptaSideCarEnricher {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaSimpleRestApiEnricher.class);
	
	Map<UrlWithOperationType, PraeceptaWsRestClient> urlWithOperationTypeToRestClientMap; 

	Map<UrlWithOperationType, PraeceptaRestClientBuilder> urlWithOperationTypeToRestClientBuilderMap; 

	private boolean initialized = false;

	@Override
	public void initializeEnricher() {

		if(!initialized) {
			urlWithOperationTypeToRestClientMap = new HashMap<>();
		
			urlWithOperationTypeToRestClientBuilderMap = new HashMap<>();
		}
	}

	@Override
	public void enrich(PraeceptaSideCarDataHolder<Map<String, Object> , Map<String, Object>> input) {
		
		Map<String, Object> sideCarConfigs = (Map<String, Object>) input.getAdditionalHolderInfo().get(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG);
		
		if(!PraeceptaObjectHelper.isObjectNull(sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key))){
			String url = (String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key);
			String operationType = (String) sideCarConfigs.get(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key);
			
			PraeceptaWsOperationType httpRequestType = PraeceptaWsOperationType.valueOf(operationType);
			
			UrlWithOperationType hashKey = new UrlWithOperationType(url, httpRequestType,this);
			
			urlWithOperationTypeToRestClientBuilderMap.putIfAbsent(hashKey, 
					PraeceptaRestEnricherHelper.getClientBuilder(sideCarConfigs));

			PraeceptaRestClientBuilder restClientBuilder = urlWithOperationTypeToRestClientBuilderMap.get(hashKey);
			
			urlWithOperationTypeToRestClientMap.putIfAbsent(hashKey, new PraeceptaWsRestClient(restClientBuilder));
			
			PraeceptaWsRestClient wsClient = urlWithOperationTypeToRestClientMap.get(hashKey);
			
			logger.debug("Before triggering call");
			
			
//			PraeceptaWsOperationType httpRequestType = PraeceptaWsOperationType.valueOf(restClientBuilder.getWsClientConfig().getRequestType());
			
			PraeceptaWsRequestResponseHolder requestResponseHolderObj=buildRequestResponseHolder(input,httpRequestType);
			
			wsClient.triggerCall(requestResponseHolderObj);
			
			PraeceptaRestEnricherHelper.captureApiResponse(requestResponseHolderObj, input);
			
			logger.debug("Api response captured from Data Holder {} - ", input.retriveOutput());
			
		}
	}

	
}
