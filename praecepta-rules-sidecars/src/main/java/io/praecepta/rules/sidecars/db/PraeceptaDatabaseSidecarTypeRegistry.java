package io.praecepta.rules.sidecars.db;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.db.impl.PraeceptaDatabaseSidecar;
import io.praecepta.rules.sidecars.enricher.IPraeceptaSideCarEnricher;
import io.praecepta.rules.sidecars.enricher.impl.PraeceptaMultiRestApiEnricher;
import io.praecepta.rules.sidecars.enricher.impl.PraeceptaSimpleRestApiEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PraeceptaDatabaseSidecarTypeRegistry {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaDatabaseSidecarTypeRegistry.class);

	private static Map<String, IPraeceptaDatabaseSideCar> dbTypeRegistry = null;

	private static final List<String> dbTypeStrs = getAllEnricherTypesInStringList();

	private static final PraeceptaDatabaseSidecarTypeRegistry dbRegistryInstance = new PraeceptaDatabaseSidecarTypeRegistry();

	public PraeceptaDatabaseSidecarTypeRegistry() {
	}
	
	public static PraeceptaDatabaseSidecarTypeRegistry getInstance() {
		
		dbTypeRegistry = captureDBTypes();
		
		return dbRegistryInstance;
	}
	
	private static Map<String, IPraeceptaDatabaseSideCar> captureDBTypes() {
		
		Map<String, IPraeceptaDatabaseSideCar> dbSidecarRegistry = new HashMap<>();
		
		dbSidecarRegistry.put(PraeceptaDBSidecarType.DATABASE.name(), new PraeceptaDatabaseSidecar());

		
		return dbSidecarRegistry;
	}
	
	public enum PraeceptaDBSidecarType{
		DATABASE;
	}
	
	private static List<String> getAllEnricherTypesInStringList() {

		List<String> dbTypeStrList = new ArrayList<>();
		
		for(PraeceptaDBSidecarType dbType : PraeceptaDBSidecarType.values()) {
			logger.info(" Adding DB Sidecar Available {} ", dbType.name());
			dbTypeStrList.add(dbType.name());
		}
		
		logger.info(" List Of DBSidecars Availabele --> ", dbTypeStrList);
		
		return dbTypeStrList;
	}

	public IPraeceptaDatabaseSideCar getDBSidecarByType(String dbType) {
		
		logger.info("Inside the Get Enricher By Type to fetch for Type {} ", dbType);

		IPraeceptaDatabaseSideCar dbSideCar = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dbType) && dbTypeStrs.contains(dbType.toUpperCase())
				&& dbTypeRegistry.containsKey(dbType.toUpperCase())) {

			dbSideCar = dbTypeRegistry.get(dbType);
		}
		
		logger.info(" DBSideCar To Return for a Type ", dbSideCar);
		return dbSideCar;
	}
}
