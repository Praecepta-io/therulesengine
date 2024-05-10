package io.praecepta.rules.sidecars.enricher.impl;

import static io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper.buildRequestResponseHolder;
import static io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper.captureAdditionalInfo;
import static io.praecepta.rules.sidecars.enricher.helper.PraeceptaRestEnricherHelper.getMultiApiSideCarConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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

public class PraeceptaMultiRestApiEnricher implements IPraeceptaSideCarEnricher {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaMultiRestApiEnricher.class);

	Map<UrlWithOperationType, PraeceptaWsRestClient> urlWithOperationTypeToRestClientMap;

	Map<UrlWithOperationType, PraeceptaRestClientBuilder> urlWithOperationTypeToRestClientBuilderMap;
	
	ExecutorService executor = Executors.newFixedThreadPool(1);

	private boolean initialized = false;

	@Override
	public void initializeEnricher() {

		if (!initialized) {
			urlWithOperationTypeToRestClientMap = new HashMap<>();

			urlWithOperationTypeToRestClientBuilderMap = new HashMap<>();
		}
	}

	@Override
	public void enrich(PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> input) {

		Map<String, Object> sideCarConfigs = (Map<String, Object>) input.getAdditionalHolderInfo()
				.get(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG);
		
        /*Getting multi rest api configs with index as key 
		  For Ex:{"index1_key1":"val1","index1_key2":"val2","index2_key1":"val1","index2_key2":"val2"} becomes
		  {1:{"key1":"val1","key2":"val2"},2:{"key1":"val1","key2":"val2"}} 
		 */
		Map<Integer, Map<String, Object>> sideCarConfigsForMultiApi = getMultiApiSideCarConfigs(sideCarConfigs);

		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigsForMultiApi)) {
		

			Map<Integer, Future<PraeceptaWsRequestResponseHolder>> requestResponseHolderMap = executeAndGetResponseHolderMap(
					sideCarConfigsForMultiApi, input,executor);

			// reading all futures and preparing response map by index
			Map<Integer, PraeceptaWsRequestResponseHolder> responseMap = readFeatures(requestResponseHolderMap);

			if (!PraeceptaObjectHelper.isObjectEmpty(responseMap)) {
				AtomicInteger indexCounter=new AtomicInteger(0);
				// iterating over the map and
				responseMap.entrySet().stream()
						.sorted(Map.Entry.<Integer, PraeceptaWsRequestResponseHolder>comparingByKey()).forEach(e -> {
                            if(indexCounter.get()==0) {
                            	
								PraeceptaRestEnricherHelper.captureApiResponse(e.getValue(), input);

							} else {
								PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> nextSideCarDataHolder = new PraeceptaSideCarDataHolder<>();
								
								PraeceptaRestEnricherHelper.captureApiResponse(e.getValue(), nextSideCarDataHolder);

								addNextSideCarDataHolder(input, nextSideCarDataHolder);

							}
                            indexCounter.getAndIncrement();
						});
			}

			logger.debug("Api response captured from Data Holder {} - ", input.retriveOutput());
			
		}

	}
    
	/*method to execute enricher for given apiConfig and input*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PraeceptaWsRequestResponseHolder executeEnricher(Map<String, Object> enricherApiConfigs,
			PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> input) {
		
		//creating temp sideCarDataHolder to update additionalHoledrInfo with index based api configs
		PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> tempInput=input;
		
		tempInput.getAdditionalHolderInfo().put(PraeceptaRestEnricherHelper.RUN_TIME_CONFIG, enricherApiConfigs);
		
		captureAdditionalInfo(tempInput.getAdditionalHolderInfo(), tempInput.retriveInput());

		String url = (String) enricherApiConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key);

		String operationType = (String) enricherApiConfigs.get(REST_CLIENT_CONFIG_KEYS.REQUEST_TYPE.key);

		PraeceptaWsOperationType httpRequestType = PraeceptaWsOperationType.valueOf(operationType);

		UrlWithOperationType hashKey = new UrlWithOperationType(url, httpRequestType, this);

		urlWithOperationTypeToRestClientBuilderMap.putIfAbsent(hashKey,
				PraeceptaRestEnricherHelper.getClientBuilder(enricherApiConfigs));

		PraeceptaRestClientBuilder restClientBuilder = urlWithOperationTypeToRestClientBuilderMap.get(hashKey);

		urlWithOperationTypeToRestClientMap.putIfAbsent(hashKey, new PraeceptaWsRestClient(restClientBuilder));

		PraeceptaWsRestClient wsClient = urlWithOperationTypeToRestClientMap.get(hashKey);

		logger.debug("Before triggering call");

		PraeceptaWsRequestResponseHolder requestResponseHolderObj = buildRequestResponseHolder(tempInput, httpRequestType);

		wsClient.triggerCall(requestResponseHolderObj);

		return requestResponseHolderObj;

	}

	private Map<Integer, PraeceptaWsRequestResponseHolder> readFeatures(
			Map<Integer, Future<PraeceptaWsRequestResponseHolder>> requestResponseHolderMap) {

		if (!PraeceptaObjectHelper.isObjectEmpty(requestResponseHolderMap)) {
			Map<Integer, PraeceptaWsRequestResponseHolder> responseMap = new HashMap<>();
			for (Map.Entry<Integer, Future<PraeceptaWsRequestResponseHolder>> responseEntry : requestResponseHolderMap
					.entrySet()) {

				try {

					PraeceptaWsRequestResponseHolder reqResponseHolder = responseEntry.getValue().get(15,
							TimeUnit.SECONDS);
					responseMap.put(responseEntry.getKey(), reqResponseHolder);
				} catch (InterruptedException | ExecutionException | TimeoutException e) {

					logger.error("error while reading response feature for key {}", responseEntry.getKey(), e);
				}
			}
			return responseMap;
		} else {
			logger.debug("request response holders map object is empty");
		}
		return null;
	}
    
	/*method to execute api call for each api sidecar config */
	private Map<Integer, Future<PraeceptaWsRequestResponseHolder>> executeAndGetResponseHolderMap(
			Map<Integer, Map<String, Object>> sideCarConfigsForMultiApi,
			PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> input,ExecutorService executor) {
		
		Map<Integer, Future<PraeceptaWsRequestResponseHolder>> requestResponseHolderMap = new HashMap<>();

		for (Map.Entry<Integer, Map<String, Object>> apiConfig : sideCarConfigsForMultiApi.entrySet()) {

			Map<String, Object> enricherApiConfigs = apiConfig.getValue();

			if (!PraeceptaObjectHelper.isObjectNull(enricherApiConfigs.get(REST_CLIENT_CONFIG_KEYS.ENDPOINT_URL.key))) {

				Future<PraeceptaWsRequestResponseHolder> reqResposneHolderObj = executor
						.submit(() -> executeEnricher(enricherApiConfigs, input));

				requestResponseHolderMap.put(apiConfig.getKey(), reqResposneHolderObj);
			}
		}
				
		return requestResponseHolderMap;
	}

	@SuppressWarnings("unchecked")
	private void addNextSideCarDataHolder(
			PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> parentSideCarDataHolder,
			PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>> nextSideCarDataHolderToAdd) {

		if (!PraeceptaObjectHelper.isObjectNull(parentSideCarDataHolder)) {

			if (PraeceptaObjectHelper.isObjectEmpty(parentSideCarDataHolder.getNextSideCarDataHolder())) {

				parentSideCarDataHolder.setNextSideCarDataHolder(nextSideCarDataHolderToAdd);

			} else {
				addNextSideCarDataHolder(
						(PraeceptaSideCarDataHolder<Map<String, Object>, Map<String, Object>>) parentSideCarDataHolder
								.getNextSideCarDataHolder(),
						nextSideCarDataHolderToAdd);
			}
		}
	}
}
