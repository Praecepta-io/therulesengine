package io.praecepta.rest.client.wrapper;

import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;

public class PraeceptaSpringRestClientWrapper extends PraeceptaRestClientWrapper<RestTemplate> {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaSpringRestClientWrapper.class);
			
	@Override
	public void performOperation(PraeceptaWsRequestResponseHolder holder, PraeceptaWebServiceClientConfig config) {
		
		logger.info(" Triggering the Rest Client with WS Config as -> {} and Holder Info as -> {} ", config, holder);

		String endpointUrl = config.getEndpointUrl();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointUrl);

		if (holder != null && holder.getQueryParams() != null) {

			holder.getQueryParams().forEach((paramName, paramValue) -> {
				builder.queryParam(paramName, paramValue);
			});
		}
		
		UriComponents uiComp = builder.build();

		logger.debug(" Uri Component Builder and Query Param init is done ");

		String utlToTrigger = uiComp.toString();
		
		logger.info(" Triggering the request for URL -> {} ", utlToTrigger);

		HttpHeaders headers = new HttpHeaders();

		holder.getInHeaderParams().forEach((paramName, paramValue) -> {
			headers.put(paramName, Arrays.asList(paramValue));
		});
		
		logger.debug(" Http Header Addition is done to Trigger the Request ");

		HttpEntity<String> requestEntity = new HttpEntity<String>(holder.getInput(), headers);
		
		logger.debug(" Http Request Entity Object created with Payload and Headers");
		
		ResponseEntity<String> strResponse = null;
		
		try {

			strResponse = getClient().exchange(utlToTrigger, holder.getHttpMethod(), requestEntity, String.class,
					holder.getPathParams() != null ? holder.getPathParams() : new HashMap<>());

		} catch (Exception e) {
			
			logger.error(" Exception While Connecting, Triggering and Generating the Http response ", e); 
			
			holder.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			
			holder.setOutput(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			
			logger.debug(" Exception Status Code and Error Message is Done ");
		}
		
		logger.info(" Triggering the Rest request is done ");
		
		if (!PraeceptaObjectHelper.isObjectNull(strResponse)) {
			
			logger.info(" Here is the Http Response Received -> {} ", strResponse);
			
			holder.setHttpStatus(strResponse.getStatusCode());
			
			holder.setOutput(strResponse.getBody());
			
			holder.getOutHeaderParams().putAll(strResponse.getHeaders().toSingleValueMap());
		}
		
		logger.debug(" Adding Http Response info to the Holder is done, "
				+ "Here is the Updated Request Response Holder -> {} ", holder);
	}

}
