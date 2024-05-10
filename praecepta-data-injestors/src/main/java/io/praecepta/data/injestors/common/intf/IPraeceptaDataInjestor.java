package io.praecepta.data.injestors.common.intf;

import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public interface IPraeceptaDataInjestor<INJESTER_CONFIG extends IPraeceptaDataConfig> {

	void openInjestorConnection(INJESTER_CONFIG collectorConfig);
	
	CONNECTION_STATUS getInjestorStatus();
	
	void initializeDataInjestor();

	void injestData(PraeceptaDataRecord dataRecord);
	
	public void terminateDataInjestor();
}
