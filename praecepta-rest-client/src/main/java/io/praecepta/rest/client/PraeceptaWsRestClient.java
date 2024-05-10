package io.praecepta.rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rest.client.wrapper.PraeceptaRestClientWrapper;
/**
 * 
 * @author rajasrikar
 *
 * @param <BUILDER>
 * 
 *  String uri = http://my-rest-url.org/rest/account/{account};

    Map<String, String> uriParam = new HashMap<>();
    uriParam.put("account", "my_account");

    UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("pageSize","2")
                        .queryParam("page","0")
                        .queryParam("name","my_name").build();

    HttpEntity<String> requestEntity = new HttpEntity<>(null, getHeaders());

    ResponseEntity<String> strResponse = restTemplate.exchange(builder.toUriString(),HttpMethod.GET, requestEntity,
                        String.class,uriParam);
 */

public class PraeceptaWsRestClient<BUILDER extends PraeceptaRestClientBuilder<? extends PraeceptaWebServiceClientConfig>> {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaWsRestClient.class);
	
	private BUILDER builder;
	
	private PraeceptaRestClientWrapper<?> restWrapperClient;
	
	private boolean initialized;
	
	public PraeceptaWsRestClient(BUILDER builder) {
		this.builder = builder;
	}
	
	public void initialize() {
		
		logger.debug("Inside initialize");
		
		if(builder != null) {
			
			logger.info(" Building the Rest Wrapper Client ");
			restWrapperClient=	builder.buildClient();
			initialized = true;
		}
		
		logger.debug("Exiting initialize");
	}
	
	public void triggerCall(PraeceptaWsRequestResponseHolder holder) {
		
		if(!initialized) {
			initialize();
		}
		
		if(builder != null) {
			if(restWrapperClient != null) {
				restWrapperClient.performOperation(holder, builder.getWsClientConfig());
			}
		}
	}
}
