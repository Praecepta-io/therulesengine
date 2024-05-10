package io.praecepta.rest.client.wrapper;

import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;

public abstract class PraeceptaRestClientWrapper<T> {
	
	private T client = null;
	
	public  void wrap(T client, Class<T> type) {
		
		this.client = client;
	}
	
	public T getClient() {
		
		return client;
	}
	
	public abstract void performOperation(PraeceptaWsRequestResponseHolder holder, PraeceptaWebServiceClientConfig config);

}
