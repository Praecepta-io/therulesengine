package io.praecepta.rules.hub.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.hub.dbbased.dao.PraeceptaRuleGroupMySQLDao;
import io.praecepta.rules.hub.dbbased.dao.PraeceptaRuleSpaceMySQLDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;

public class PraeceptaRuleDaoRegister {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleDaoRegister.class);
			
	private static  Map<RULE_SET_UP_PERSISTANT_TYPE, Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider>> praeceptaRuleDaoProviders = createDaoProviders();
	
	private static Map<RULE_SET_UP_PERSISTANT_TYPE, Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider>> createDaoProviders() {
		
		Map<RULE_SET_UP_PERSISTANT_TYPE, Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider>> typeMapper = new HashMap<>();
		
			// SQL Sub Type Starts Here 
			Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider> sqlSubTypeMap = new HashMap<>();
				
				sqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.MySQL, new PraeceptaRuleDaoProvider(new PraeceptaRuleSpaceMySQLDao(), new PraeceptaRuleGroupMySQLDao()));
				sqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Postgres, new PraeceptaRuleDaoProvider(null, null));
				sqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Oracle, new PraeceptaRuleDaoProvider(null, null));
				sqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.SQLServer, new PraeceptaRuleDaoProvider(null, null));
				sqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.H2, new PraeceptaRuleDaoProvider(null, null));
			
			// Adding SQL DB related Providers  
			typeMapper.put(RULE_SET_UP_PERSISTANT_TYPE.SQL_DB, sqlSubTypeMap);
			
			// No SQL Sub Type Starts Here 
			Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider> noSqlSubTypeMap = new HashMap<>();
			
				noSqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.MongoDb, new PraeceptaRuleDaoProvider(new PraeceptaRuleSpaceWindowsDbDao(), new PraeceptaRuleGroupWindowsDbDao()));
				noSqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Cassandra, new PraeceptaRuleDaoProvider(null, null));
				noSqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.ElasticSearch, new PraeceptaRuleDaoProvider(null, null));
				noSqlSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.DynamoDb, new PraeceptaRuleDaoProvider(null, null));
			
			typeMapper.put(RULE_SET_UP_PERSISTANT_TYPE.NO_SQL_DB, noSqlSubTypeMap);
			
			// File Sub Type Starts Here 
			Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider> fileSubTypeMap = new HashMap<>();
			
				fileSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Windows, new PraeceptaRuleDaoProvider(null, null));
				fileSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Unix, new PraeceptaRuleDaoProvider(null, null));
			
			typeMapper.put(RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM, fileSubTypeMap);
			
			// Cache Sub Type Starts Here 
			Map<RULE_SET_UP_PERSISTANT_SUB_TYPE, PraeceptaRuleDaoProvider> cacheSubTypeMap = new HashMap<>();
			
				cacheSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Redis, new PraeceptaRuleDaoProvider(null, null));
				cacheSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Memcache, new PraeceptaRuleDaoProvider(null, null));
				cacheSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.Hazelcast, new PraeceptaRuleDaoProvider(null, null));
				cacheSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.OracleCoherance, new PraeceptaRuleDaoProvider(null, null));
				cacheSubTypeMap.put(RULE_SET_UP_PERSISTANT_SUB_TYPE.EhCache, new PraeceptaRuleDaoProvider(null, null));
			
			typeMapper.put(RULE_SET_UP_PERSISTANT_TYPE.CACHE, cacheSubTypeMap);
		
		return typeMapper;
	}
	
	private static  Map<String, Map<String, PraeceptaRuleDaoProvider>> daoProvidersMapAtRunTime = buildDaoProviders();
	
	private static Map<String, Map<String, PraeceptaRuleDaoProvider>> buildDaoProviders() {
		
		Map<String, Map<String, PraeceptaRuleDaoProvider>>  daoProviders = new HashMap<>();
		
		praeceptaRuleDaoProviders.forEach( (type, subTypeWithProvider) -> {
			
			Map<String, PraeceptaRuleDaoProvider> subTypeProvider = new HashMap<>();
			
			subTypeWithProvider.forEach( (subType, provider) -> {
				subTypeProvider.put(subType.name(), provider);
			});
			
			daoProviders.put(type.name(), subTypeProvider);
		});
		
		return daoProviders;
	}
	
	private static class PraeceptaRuleDaoProvider {
		
		private IPraeceptaRuleSpaceDao ruleSpaceDao;
		
		private IPraeceptaRuleGroupDao ruleGroupDao;
		
		public PraeceptaRuleDaoProvider(IPraeceptaRuleSpaceDao ruleSpaceDao, IPraeceptaRuleGroupDao ruleGroupDao) {
			this.ruleSpaceDao = ruleSpaceDao;
			this.ruleGroupDao = ruleGroupDao;
		}

		public IPraeceptaRuleGroupDao getRuleGroupDao() {
			return ruleGroupDao;
		}

		public IPraeceptaRuleSpaceDao getRuleSpaceDao() {
			return ruleSpaceDao;
		}
		
	}

	/**
	 * 
	 * @param type - Enum - RULE_SET_UP_PERSISTANT_TYPE
	 * @param subType - Enum - RULE_SET_UP_PERSISTANT_SUB_TYPE
	 * @return
	 */
	public static IPraeceptaRuleSpaceDao getRuleSpaceDaoForTypeAndSubType(String type, String subType) {
		logger.info("Getting Space Dao for Type - {} and Sub Type - {}", type, subType);
		
		Map<String, PraeceptaRuleDaoProvider> subTypeProvider =  daoProvidersMapAtRunTime.get(type);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(subTypeProvider)) {
			logger.info("Type - {} exist for Space Dao", type);
			
			PraeceptaRuleDaoProvider provider = subTypeProvider.get(subType);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(provider)) {
				logger.info("Provider Exist for Type - {} and Sub Type - {}. Returning Space Dao", type, subType);
				return provider.getRuleSpaceDao();
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param type - Enum - RULE_SET_UP_PERSISTANT_TYPE
	 * @param subType - Enum - RULE_SET_UP_PERSISTANT_SUB_TYPE
	 * @return
	 */
	public static IPraeceptaRuleGroupDao getRuleGroupDaoForTypeAndSubType(String type, String subType) {
		logger.info("Getting Group Dao for Type - {} and Sub Type - {}", type, subType);
		
		Map<String, PraeceptaRuleDaoProvider> subTypeProvider =  daoProvidersMapAtRunTime.get(type);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(subTypeProvider)) {
			logger.info("Type - {} exist for Group Dao", type);
			
			PraeceptaRuleDaoProvider provider = subTypeProvider.get(subType);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(provider)) {
				logger.info("Provider Exist for Type - {} and Sub Type - {}. Returning Group Dao", type, subType);
				return provider.getRuleGroupDao();
			}
		}
		
		return null;
	}
	
	/**
	 * This method will be called to store any custom DAO providers created and wanted them to register for usage
	 * @param Pass any Type from Enum or Pass a Custom type 
	 * 		- Enum - RULE_SET_UP_PERSISTANT_TYPE
	 * @param Pass any subType from Enum or Pass a Custom Sub Type
	 * 		- Enum - RULE_SET_UP_PERSISTANT_SUB_TYPE
	 * @param ruleSpaceDao - Implementation of the Space Dao Interface
	 * @param ruleGroupDao - Implementation of the Group Dao Interface
	 */
	public static void registerRuleDaoProviderForTypeAndSubType(String type, String subType, IPraeceptaRuleSpaceDao ruleSpaceDao, IPraeceptaRuleGroupDao ruleGroupDao) {
		logger.info("Dao Provider Register Request for Type - {} and Sub Type - {}", type, subType);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(type) && !PraeceptaObjectHelper.isObjectEmpty(subType) &&
				!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceDao) && 
				!PraeceptaObjectHelper.isObjectEmpty(ruleGroupDao)  
			) {
			
			logger.info("About to Register Request for Type - {} and Sub Type - {} As Space and Group Daos are not Empty", type, subType);
				// Adding a Type that doesn't exist in Default from praeceptaRuleDaoProviders
				daoProvidersMapAtRunTime.computeIfAbsent(type, subTypeProvider -> new HashMap<>());
			
				Map<String, PraeceptaRuleDaoProvider> subTypeProvider = daoProvidersMapAtRunTime.get(type);
				subTypeProvider.put(subType, new PraeceptaRuleDaoProvider(ruleSpaceDao, ruleGroupDao));
				
				daoProvidersMapAtRunTime.put(type, subTypeProvider);
			}
			
	}

}
