package io.praecepta.data.collectors.common.collector;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.exception.PraeceptaDataCollectorException;
import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public abstract class PraeceptaAbstractDataCollector<COLLECTOR_CONFIG extends GenericAbstractPraeceptaDataConfig>
		implements IPraeceptaDataCollector<COLLECTOR_CONFIG> {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaAbstractDataCollector.class); 
	
	private CONNECTION_STATUS status;
	
	private Map<String, Object> dataCollectorInfo;
	
	private String uniqueDataCollectorName;
	
	public static final String ASSIGNED_COLLECTOR_NAME =  "assignedCollectorName";
	
	private boolean collectorOpened;
	
	private LinkedBlockingDeque<PraeceptaDataRecord> polledRecords = new LinkedBlockingDeque<>();
	
	boolean stopPolling = false;
	
	@Override
	public void openCollectorConnection(COLLECTOR_CONFIG collectorConfig) {
		logger.info("In Open Connection");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(collectorConfig)) {
			collectorConfig.postConfigSetup(); // To make sure we have all mandatory attributes defined
			status = CONNECTION_STATUS.INITIALIZED;

			if(PraeceptaObjectHelper.isObjectEmpty(dataCollectorInfo)) {
				dataCollectorInfo = new HashMap<String, Object>();
				
				uniqueDataCollectorName =  String.join("$$", this.getClass().getName(), 
						UUID.randomUUID().toString(), String.valueOf(System.nanoTime())) ;
						
				dataCollectorInfo.put(ASSIGNED_COLLECTOR_NAME, uniqueDataCollectorName);
			}
		
		} else {
			throw new PraeceptaDataCollectorException("Collector Configuraiton is Empty. Unable to Perform Open Connection");
		}
		
		collectorOpened = true;
	}

	@Override
	public CONNECTION_STATUS getCollectorStatus() {
		return status;
	}

	@Override
	public Map<String, Object> getDataCollectorInfo() { // We can use it for health Check/jvm info, status or any meta data about the Collector
		return dataCollectorInfo;
	}

	@Override
	public final void startDataCollector() {
		logger.info("In Start Data Collector ");

		if(!collectorOpened && getCollectorStatus() == null) {
			throw new PraeceptaDataCollectorException("Open Collector Connection needs to be performed first before calling Start Collecting Data");
		}
		status = CONNECTION_STATUS.COLLECTOR_STARTED;
		
		Runnable runnable = () -> {
		
			int totalRecordsPolled = 0;
			
			while(!stopPolling) {
				PraeceptaDataRecord dataRecord = performCollection();
				
				if(!PraeceptaObjectHelper.isObjectEmpty(dataRecord) && !PraeceptaObjectHelper.isObjectEmpty(dataRecord.getRecordEntries())) {
					totalRecordsPolled++;
					polledRecords.add(dataRecord);
				}
			}
			logger.info("Total Number of Records Polled By Data Collector --> {}", totalRecordsPolled);
		};
		
		Thread pollingThread = new Thread(runnable);
		pollingThread.start();
	}

	protected PraeceptaDataRecord performCollection() {
		logger.debug("In Perform Collection ");
		return null;
	}

	@Override
	public void stopCollectingData() {
		logger.info("In Stop Data Collection ");
		
		status = CONNECTION_STATUS.COLLECTOR_STOPPED;
		
		stopPolling = true;
		// Stop Polling From Queue or Topic
	}

	@Override
	public void terminateDataCollector() {
		logger.info("In Terminate Data Collection ");
		
		if(CONNECTION_STATUS.COLLECTOR_STOPPED != status) {
			
			stopCollectingData();
		}
		
		status = CONNECTION_STATUS.TERMINATED;
		
		// Close Kafka/AMQ etc Connections, Sessions etc
	}
	
	@Override
	public boolean isDataCollectable() {
		
		return !stopPolling;
	}

	@Override
	public Collection<PraeceptaDataRecord> getCollectedData() {
		
		int size = polledRecords.size();
		
		logger.debug(" Records Size --> {}", size);
		if(size > 0) {
			LinkedBlockingDeque<PraeceptaDataRecord> recordsToReturn = new LinkedBlockingDeque<>(size);
			
			polledRecords.drainTo(recordsToReturn, size);
			
			return recordsToReturn;
		}
		
		
		return Collections.emptyList();
	}

	@Override
	public boolean awaitForDataCollectionStop() {
		
		if(!stopPolling) {
			throw new PraeceptaDataCollectorException(" Await for Data Colleciton Stop Called before to Calling stopCollectingData or terminateDataCollector");
		}
		
		if(polledRecords.size() > 0) {
			return false;
		}
		
		return true;
	}
	
	
			
}
