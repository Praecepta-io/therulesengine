package io.praecepta.data.collectors.common;

import java.util.Collection;
import java.util.Map;

import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public interface IPraeceptaDataCollector<COLLECTOR_CONFIG extends IPraeceptaDataConfig> {

	void openCollectorConnection(COLLECTOR_CONFIG collectorConfig);
	
	CONNECTION_STATUS getCollectorStatus();
	
	Map<String, Object> getDataCollectorInfo();

	void startDataCollector();
	
	Collection<PraeceptaDataRecord> getCollectedData();
	
	void stopCollectingData();
	
	void terminateDataCollector();// We can use it for Clean Up
	
	boolean isDataCollectable();
	
	boolean awaitForDataCollectionStop();
}
