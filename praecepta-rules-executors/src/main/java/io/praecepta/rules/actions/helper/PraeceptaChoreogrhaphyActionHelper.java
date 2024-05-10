package io.praecepta.rules.actions.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.praecepta.data.injestors.common.enums.RULE_GROUP_INFO_KEYS;
import io.praecepta.data.injestors.common.impl.PraeceptaDBDataInjestor;
import io.praecepta.data.injestors.common.impl.PraeceptaKafkaDataInjestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaInjestorConfig;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaChoreogrhaphyActionHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaChoreogrhaphyActionHelper.class);

	/*public static IPraeceptaDataInjestor<IPraeceptaDataConfig> initializeDataInjestor(
			List<Map<String, Object>> actionParameters, PraeceptaActionStrategyType strategyType,Map<String, Object> actionMetadata) {

		if (strategyType != null) {

			switch (strategyType) {

			case SEND_MESSAGE_TO_KAFKA:
				return initializeKafkaInjestor(actionParameters);
			case SEND_MESSAGE_TO_DB:
				return initializeDBInjestor(actionParameters,actionMetadata);
			default:
				return null;
			}
		}
		return null;
	}*/

	private static PraeceptaKafkaDataInjestor initializeKafkaInjestor(List<Map<String, Object>> actionParameters) {

		Map<String, Object> actionConfigProperties = getActionParemetersMap(actionParameters);
		
		PraeceptaKafkaInjestorConfig kafkaConfig = new PraeceptaKafkaInjestorConfig();

		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaKafkaDataConfigType.BROKERS.getElementName(), actionConfigProperties));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER,
				getConfigPropName(PraeceptaKafkaDataConfigType.PORT.getElementName(), actionConfigProperties));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaKafkaDataConfigType.USERNAME.getElementName(), actionConfigProperties));
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaKafkaDataConfigType.PASSWORD.getElementName(), actionConfigProperties));

		kafkaConfig.addNonMandatoryConfigElements("max.timeout",
				getConfigPropName("max.timeout", actionConfigProperties));
		kafkaConfig.addNonMandatoryConfigElements("key.serializer",
				getConfigPropName("key.serializer", actionConfigProperties));
		kafkaConfig.addNonMandatoryConfigElements("value.serializer",
				getConfigPropName("value.serializer", actionConfigProperties));
		kafkaConfig.addNonMandatoryConfigElements("kafka.sender.topic",
				getConfigPropName("kafka.sender.topic", actionConfigProperties));

		PraeceptaKafkaDataInjestor kafkaInjestor = new PraeceptaKafkaDataInjestor();

		kafkaInjestor.openInjestorConnection(kafkaConfig);

		return kafkaInjestor;
	}

	private static String getConfigPropName(String configPropertyKey, Map<String, Object> actionConfigProperties) {
		if (!PraeceptaObjectHelper.isObjectEmpty(actionConfigProperties)) {
			return (String) actionConfigProperties.get(configPropertyKey);
		}
		return null;
	}
	public static PraeceptaDataRecord buildDataRecord(PraeceptaRequestStore input) {
		PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(1);
		
		String rulesResponse=(String) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE);
		
		Map<String,Object> rulesResponseMeataData=(Map<String, Object>) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA);
		
		RuleGroupInfo ruleGroupWithSpaceKey = (RuleGroupInfo) input
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY);
        
		if(PraeceptaObjectHelper.isObjectEmpty(rulesResponseMeataData)) {
			rulesResponseMeataData=new HashMap<>();
        }
		
		populateRuleGroupInfoHeaders(ruleGroupWithSpaceKey, rulesResponseMeataData);
		
		rulesResponseMeataData.put(RULE_GROUP_INFO_KEYS.RULE_GROUP_EXECUTION_STATUS.name(), input
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_OVERALL_EXECUTION_STATUS));
        
		dataRecord.addRecordEntry(rulesResponse,null, rulesResponseMeataData);
		
		return dataRecord;
	}
	private static PraeceptaDBDataInjestor initializeDBInjestor(List<Map<String, Object>> actionParameters,Map<String, Object> dbMetadata) {

		Map<String, Object> actionConfigProperties = getActionParemetersMap(actionParameters);
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();

		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.DB_DRIVER.getElementName(), actionConfigProperties));
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.DB_URL.getElementName(), actionConfigProperties));
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.USERNAME.getElementName(), actionConfigProperties));
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				getConfigPropName(PraeceptaDBDataConfigType.PASSWORD.getElementName(), actionConfigProperties));
		
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dbMetadata)) {
			
			dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName(),
					getConfigPropName(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName(), dbMetadata));
			
			if (dbMetadata.containsKey("query.column.names")) {
				dbConfig.addNonMandatoryConfigElements("query.column.names",
						getConfigPropName("query.column.names", dbMetadata));
			}
		}
		
		PraeceptaDBDataInjestor dbInjestor = new PraeceptaDBDataInjestor();

		dbInjestor.openInjestorConnection(dbConfig);

		return dbInjestor;
	}

	private static Map<String, Object> getActionParemetersMap(List<Map<String, Object>> actionParameters) {
		return actionParameters.stream().collect(Collectors.toMap(objMap -> (String) objMap.get("attributeName"),
				objMap -> objMap.get("attributeValue")));
	}
	
	private static void populateRuleGroupInfoHeaders(RuleGroupInfo ruleGroupWithSpaceKey,Map<String,Object> rulesResponseMeataData) {
		rulesResponseMeataData.putIfAbsent(RULE_GROUP_INFO_KEYS.RULE_GROUP_NAME.name(), ruleGroupWithSpaceKey.getRuleGroupName());
		
		if(!PraeceptaObjectHelper.isObjectNull(ruleGroupWithSpaceKey.getRuleSpaceInfo())) {
			rulesResponseMeataData.putIfAbsent(RULE_GROUP_INFO_KEYS.SPACE_NAME.name(), ruleGroupWithSpaceKey.getRuleSpaceInfo().getSpaceName());
			rulesResponseMeataData.putIfAbsent(RULE_GROUP_INFO_KEYS.APP_NAME.name(), ruleGroupWithSpaceKey.getRuleSpaceInfo().getAppName());
			rulesResponseMeataData.putIfAbsent(RULE_GROUP_INFO_KEYS.CLIENT_NAME.name(), ruleGroupWithSpaceKey.getRuleSpaceInfo().getClientId());
			rulesResponseMeataData.putIfAbsent(RULE_GROUP_INFO_KEYS.VERSION.name(), ruleGroupWithSpaceKey.getRuleSpaceInfo().getVersion());
		}
	}
}
