package io.praecepta.rules.builders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.builders.exceptions.PraeceptaRuleBuilderException;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

/**
 * 
 * @author rajasrikar
 *
 */
public class PraeceptaRuleBuilder {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleBuilder.class);
			
	private static final PraeceptaRuleBuilder ruleBuilder = new PraeceptaRuleBuilder();
	
	private static final String RULE_BUILDER_PROPS = "praecepta-rule-load.properties";

	private static final String RULE_BUILDER_ENV_PROPS = "praecepta.rule.load.props.location";
	
	private static String ruleBuilderFileToUse = RULE_BUILDER_PROPS;
	
	private static final Map<String, String> ruleLoadProps = new HashMap<>();
	
	private static final Map<PraeceptaRuleSpaceCompositeKey, List<PraeceptaSideCarsInfo>> ruleSideCarsMap = new ConcurrentHashMap<>();
	
	private static InputStream streamToRead;
	
	private PraeceptaRuleBuilder() {
	}

	public static PraeceptaRuleBuilder buildWithPropsFileLocation(String ruleLoadPropsFile) {
		
		if(PraeceptaObjectHelper.isStringEmpty(ruleLoadPropsFile)) {
			
			buildWithPropsClasspath(null);
			
		} else {
			
			try {
				streamToRead = new FileInputStream(ruleLoadPropsFile);

				captureRuleLoadProps();
			} catch (FileNotFoundException e) {
				throw new PraeceptaRuleBuilderException(" Rule Loader File " + ruleLoadPropsFile + " is not available in the location provided ");
			}
			
		}
		
		return ruleBuilder;
	}
	
	public static PraeceptaRuleBuilder buildWithDefaultProps() {
		return buildWithPropsClasspath(null);
	}
	
	public static PraeceptaRuleBuilder buildWithPropsClasspath(String ruleLoadClassPathFile) {

		if (!PraeceptaObjectHelper.isStringEmpty(ruleLoadClassPathFile)) {

			ruleBuilderFileToUse = ruleLoadClassPathFile;

		}
		logger.info("Rule Props File to Use from Classpath {} ", ruleBuilderFileToUse);
		
		streamToRead = PraeceptaRuleBuilder.class.getClassLoader().getResourceAsStream(ruleBuilderFileToUse);
		
		captureRuleLoadProps();

		return ruleBuilder;
	}

	public static PraeceptaRuleBuilder buildWithProps(Map<String, String> ruleLoadPropsInput) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleLoadPropsInput)) {
			ruleLoadProps.putAll(ruleLoadPropsInput);
		}
		
		printRuleLoadProps();
		
		return ruleBuilder;
	}
	
	public static PraeceptaRuleBuilder buildWithEnvParmPropsFile() {
		
		String ruleLoadPropsFile = System.getProperty(RULE_BUILDER_ENV_PROPS);
				
		return buildWithPropsFileLocation(ruleLoadPropsFile);
	}
	
	private static void captureRuleLoadProps() {

		if(!PraeceptaObjectHelper.isObjectEmpty(streamToRead)) {
			Properties props = new Properties();
			
			try {
				props.load(streamToRead);
				
				logger.info("Props Loaded  --> {} ", props);
			} catch (IOException e) {
				throw new PraeceptaRuleBuilderException(" Unable to load Rule builder Properties " );
			}
			
			props.forEach( (propKey, propValue) -> {
					if(!PraeceptaObjectHelper.isObjectEmpty(propKey)) {
						
						String propValueToUse = PraeceptaObjectHelper.isObjectEmpty(propValue) ? "" : propValue.toString();
						
						ruleLoadProps.put(propKey.toString(), propValueToUse);
					}
				}
			);
		}
		
		printRuleLoadProps();
	}

	private static void printRuleLoadProps() {
		logger.info("Props Loaded from Hash Map --> {}", ruleLoadProps);
		
	}
	
	public PraeceptaRuleSpace build(String spaceName, String clientId, String appName, String version) {
		
		PraeceptaRuleSpace ruleSpace = null;
		
		PraeceptaRuleBuilderOptions builderOptions = prepareBuilderOption();
		
		if(builderOptions != null) {
			
			PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName);
			compositeKey.setVersion(version);
			
			Collection<PraeceptaRuleSpace> ruleSpaces = builderOptions.withCompositeKey(compositeKey).build();
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaces)) {
				ruleSpace = ruleSpaces.iterator().next();
			}
		}
		
		return ruleSpace;
	}
	
	public Collection<PraeceptaRuleSpace> buildAll() {
		
		logger.info(" Building the Rule Space ");
		
		Collection<PraeceptaRuleSpace> ruleSpaces =  Collections.emptyList();
		// rule_loader_type posible values --> SQL_DB, FILE_SYSTEM, CACHE, NO_SQL_DB, REST_API
		
		PraeceptaRuleBuilderOptions builderOptions = prepareBuilderOption();
		
		if(builderOptions != null) {
			ruleSpaces = builderOptions.build();
		}
		
		return ruleSpaces;
	}

	private PraeceptaRuleBuilderOptions prepareBuilderOption() {
		
		PraeceptaRuleBuilderOptions builderOptions = null; 
		String rulePersistantType = ruleLoadProps.get(RULE_BUILDER_CONFIG_KEYS.RULE_LOADER_TYPE.value);
		
		logger.info(" Rule Persistant Type from Props File --> {} ", rulePersistantType);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantType) && RULE_SET_UP_PERSISTANT_TYPE.valueOf(rulePersistantType) != null) {
			
			String rulePersistantSubType = ruleLoadProps.get(new String().join(".", rulePersistantType, RULE_BUILDER_CONFIG_KEYS.TYPE.value).trim());
			
			/* 	For SQL_DB Type - Possible Sub Types are --> MySQL, Oracle, H2, Postgres, SQLServer,
				For FILE_SYSTEM - Possible Sub Types are --> Windows, Unix,
				For CACHE - Possible Sub Types are --> Redis, EhCache, Memcache, Hazelcast, OracleCoherance,
				For NO_SQL_DB - Possible Sub Types are --> MongoDb, Cassandra, ElasticSearch, DynamoDb
			 */
			
			logger.info(" Rule Persistant Sub Type from Props File --> {} ", rulePersistantSubType);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantSubType) && RULE_SET_UP_PERSISTANT_SUB_TYPE.valueOf(rulePersistantSubType) != null) {
				
				String connectionPropsPrefix = new String().join(".", rulePersistantType, RULE_BUILDER_CONFIG_KEYS.CONNECTION_PROPS.value, rulePersistantSubType);
				
				System.setProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value, connectionPropsPrefix);
				logger.info(" Rule Builder Connection Props Prefix {} ", connectionPropsPrefix);
				Map<String, String> connectionProps = ruleLoadProps.entrySet().stream()
					.filter( entry -> entry.getKey().contains( connectionPropsPrefix ) )
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				
				logger.info(" Rule Builder Connection Props from Props File --> {} ", connectionProps);
				
				builderOptions = new PraeceptaRuleBuilderOptions()
															.type(RULE_SET_UP_PERSISTANT_TYPE.valueOf(rulePersistantType))
															.subType(RULE_SET_UP_PERSISTANT_SUB_TYPE.valueOf(rulePersistantSubType))
															.withConnectionProps(connectionProps);
			}
		}
		
		return builderOptions;
	}
	
	private static class PraeceptaRuleBuilderOptions{
		
		private RULE_SET_UP_PERSISTANT_TYPE rulePersistantType;
		
		private RULE_SET_UP_PERSISTANT_SUB_TYPE rulePersistantSubType;
		
		private Map<String, String> connectionProps;
		
		private PraeceptaRuleSpaceCompositeKey compositeKey;
		
		public PraeceptaRuleBuilderOptions() {
		}
		
		public PraeceptaRuleBuilderOptions withCompositeKey(PraeceptaRuleSpaceCompositeKey compositeKey) {
			this.compositeKey = compositeKey;
			return this;
		}

		public PraeceptaRuleBuilderOptions type(RULE_SET_UP_PERSISTANT_TYPE rulePersistantType) {
			this.rulePersistantType = rulePersistantType;
			return this;
		}
		
		public PraeceptaRuleBuilderOptions subType(RULE_SET_UP_PERSISTANT_SUB_TYPE rulePersistantSubType) {
			this.rulePersistantSubType = rulePersistantSubType;
			return this;
		}
		
		public PraeceptaRuleBuilderOptions withConnectionProps(Map<String, String> connectionProps) {
			this.connectionProps = connectionProps;
			return this;
		}
		
		public Collection<PraeceptaRuleSpace> build() {
			
			Collection<PraeceptaRuleSpace> ruleSpaces =  Collections.emptyList();
			
//			PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
			
			if (!PraeceptaObjectHelper.isObjectEmpty(connectionProps)) {
				connectionProps.forEach((propName, PropValue) -> {
					logger.info(" Adding System Prop Key {} with Value {}", propName, PropValue);
					System.setProperty(propName, PropValue);
				});
			}
			
			if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantType)) {
				System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), rulePersistantType.toString());
			}
			
			if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantSubType)) {
				System.setProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName(), rulePersistantSubType.toString());
			}

			String conetxClassName = System.getProperty("contextPath", "io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig");
			Class<?> clazz;
			try {
				
				clazz = Class.forName(conetxClassName);
				ApplicationContext context = new AnnotationConfigApplicationContext(clazz);
				
				/*IPraeceptaRuleSpaceDao ruleSpaceDao = (IPraeceptaRuleSpaceDao) context.getBean("ruleSpaceDao");
				IPraeceptaRuleGroupDao ruleGrpDao = (IPraeceptaRuleGroupDao) context.getBean("ruleGroupDao");
				
				PraeceptaPivotalRulesHubManager hubManager = new PraeceptaPivotalRulesHubManager(ruleSpaceDao, ruleGrpDao);*/
				IPraeceptaPivotalRulesHubManager hubManager = (IPraeceptaPivotalRulesHubManager)context.getBean("pivotalRuleHubManager");
				
				if(hubManager != null) {
					logger.info("Hub Manager is not empty");
				} else {
					logger.info("Hub Manager is Empty");
				}
				
//				if (!PraeceptaObjectHelper.isObjectEmpty(compositeKey)) {
					hubManager.fetchActiveUniverse();
					/*ruleSpaces = hubManager.fetchRuleSpace(compositeKey);
					List<PraeceptaRuleSideCars> ruleSideCars = hubManager.fetchRuleSideCars(compositeKey,
							compositeKey.getVersion());
					ruleSideCarsMap.put(compositeKey,ruleSideCars);*/
//				}
				
				PraeceptaPivotalRulesHubContextHolder.addHubContext(context);
				PraeceptaPivotalRulesHubContextHolder.addHubManager(hubManager);
				
				PraeceptaPivotalRulesHubStore hubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
				
				ruleSpaces = hubStore.getAllActiveRuleSpaces();
				
				if(compositeKey != null) {
					ruleSpaces = ruleSpaces.stream().filter(space -> space.getRuleSpaceKey().equals(compositeKey) ).collect(Collectors.toList());
				}

			} catch (ClassNotFoundException e) {
				logger.error(" Exception While Building the Application Context and Preparing Rules Hub Manager", e);
			}
			
			return ruleSpaces;
		}
		
	}
	
	public enum RULE_SET_UP_PERSISTANT_TYPE{
		SQL_DB, FILE_SYSTEM, CACHE, NO_SQL_DB;
	}
	
	public enum RULE_SET_UP_PERSISTANT_SUB_TYPE{
		MySQL, Oracle, H2, Postgres, SQLServer,
		Windows, Unix,
		MongoDb, Cassandra, ElasticSearch, DynamoDb,
		Redis, EhCache, Memcache, Hazelcast, OracleCoherance
		;
	}
}
