package io.praecepta.rules.sidecars.db;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.configs.common.IPraeceptaDataConfig;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.rules.sidecars.AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class PraeceptaDatabaseSideCarInjector extends AbstractPraeceptaPipesAndFiltersInfoTrackerSideCarInjector{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDatabaseSideCarInjector.class);

	@Override
	public final void performAdditionalPreOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureInitialInfo");
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, null);
		
		String dbType=this.getSubType();

		logger.info(" Received db Type as --> {}", dbType);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dbType)) {
			
			IPraeceptaDatabaseSideCar dbSideCarToTrigger = PraeceptaDatabaseSidecarTypeRegistry.getInstance().getDBSidecarByType(dbType);
			
			if(dbSideCarToTrigger != null) {
				dbSideCarToTrigger.initializeDBConnection(populateDBConfigs());
				
				ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER, dbSideCarToTrigger);
			}
		}
		
	}

	private PraeceptaDBInjestorConfig populateDBConfigs(){
		PraeceptaDBInjestorConfig dbCollectorConfig=new PraeceptaDBInjestorConfig();
Map<String,Object> dbConfigs=getRuntimeConfigs();
		dbCollectorConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER,
				IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, dbConfigs.get(PraeceptaDBDataConfigType.DB_DRIVER.getElementName()));
		dbCollectorConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL,
				IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, dbConfigs.get(PraeceptaDBDataConfigType.DB_URL.getElementName()));
		dbCollectorConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME,
				IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, dbConfigs.get(PraeceptaDBDataConfigType.USERNAME.getElementName()));
		dbCollectorConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD,
				IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, dbConfigs.get(PraeceptaDBDataConfigType.PASSWORD.getElementName()));

		dbCollectorConfig.addNonMandatoryConfigElements("select.query", (String)dbConfigs.get("select.query"));
	   return dbCollectorConfig;
	}
	
	@Override
	public final void executeSideCar(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside executeSideCar");
		
		IPraeceptaDatabaseSideCar dbSideCarToTrigger = (IPraeceptaDatabaseSideCar)ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_TO_TRIGGER);

		if(dbSideCarToTrigger != null) {
			logger.info(" DBSideCar To Trigger is Available");

				try {
					logger.debug(" Before DB SideCar Trigger ");
					dbSideCarToTrigger.processDbRecords(ruleStore);
					logger.debug(" After DB SideCar Trigger ");
				}catch(Exception e) {
					logger.error(" Exception While Processing the Database Side Car ", e);
				}

		}
	}

	@Override
	public final PraeceptaSideCarDataHolder<?, ?> performAdditionalPostOperations(PraeceptaRequestStore ruleStore) {
		logger.debug("Inside trackAndCaptureExitInfo");
		
		return (PraeceptaSideCarDataHolder<?, ?>) ruleStore.fetchFromPraeceptaStore(PraeceptaSideCarStoreType.SIDCAR_OUTPUT);
	}

	@Override
	protected boolean enrichRulesRequestWithSidecarHolderOutput() {
		return true;
	}
	
}
