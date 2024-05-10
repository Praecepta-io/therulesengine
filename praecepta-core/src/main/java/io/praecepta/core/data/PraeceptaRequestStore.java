package io.praecepta.core.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.praecepta.core.data.enums.IPraeceptaRequestStoreType;

public class PraeceptaRequestStore {

	private final Map<IPraeceptaRequestStoreType, Object> praeceptaRequestStore = new ConcurrentHashMap<>();
	
	public void upsertToPraeceptaStore(IPraeceptaRequestStoreType type, Object value) {
		if(type != null && value != null)
			praeceptaRequestStore.put(type, value);
	}
	
	public void deleteFromPraeceptaStore(IPraeceptaRequestStoreType type) {
		praeceptaRequestStore.remove(type);
	}
	
	public Object fetchFromPraeceptaStore(IPraeceptaRequestStoreType type) {
		return praeceptaRequestStore.get(type);
	}
}
