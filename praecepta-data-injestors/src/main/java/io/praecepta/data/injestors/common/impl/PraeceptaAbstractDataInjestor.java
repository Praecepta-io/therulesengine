package io.praecepta.data.injestors.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.injestor.common.exception.PraeceptaDataInjestorException;
import io.praecepta.data.injestors.common.intf.IPraeceptaDataInjestor;

public abstract class PraeceptaAbstractDataInjestor<INJESTOR_CONFIG extends GenericAbstractPraeceptaDataConfig>
		implements IPraeceptaDataInjestor<INJESTOR_CONFIG> {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaAbstractDataInjestor.class);

	private CONNECTION_STATUS status;
	
	private boolean injestorOpened;

	@Override
	public void openInjestorConnection(INJESTOR_CONFIG collectorConfig) {

		logger.info("In Open Connection");

		if (!PraeceptaObjectHelper.isObjectEmpty(collectorConfig)) {
			collectorConfig.postConfigSetup();
			status = CONNECTION_STATUS.INITIALIZED;

		} else {
			throw new PraeceptaDataInjestorException(
					"Injestor Configuraiton is Empty. Unable to Perform Open Connection");
		}
		injestorOpened=true;
	}

	@Override
	public CONNECTION_STATUS getInjestorStatus() {
		return status;
	}
	
	@Override
	public void terminateDataInjestor() {
		logger.info("In Terminate Data Collection ");
		
		status = CONNECTION_STATUS.TERMINATED;
		
	}
	
	@Override
	public final void initializeDataInjestor() {
		logger.info("In initialize Data Injestor ");

		if(!injestorOpened && getInjestorStatus() == null) {
			throw new PraeceptaDataInjestorException("Open INjestor Connection needs to be performed first before calling Start Collecting Data");
		}
		status = CONNECTION_STATUS.COLLECTOR_STARTED;
		
	}

}
