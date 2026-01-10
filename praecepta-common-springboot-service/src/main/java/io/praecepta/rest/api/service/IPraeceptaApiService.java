package io.praecepta.rest.api.service;

import io.praecepta.core.data.PraeceptaRequestStore;

public interface IPraeceptaApiService {
	
	void execute(PraeceptaRequestStore requestStore);

}
