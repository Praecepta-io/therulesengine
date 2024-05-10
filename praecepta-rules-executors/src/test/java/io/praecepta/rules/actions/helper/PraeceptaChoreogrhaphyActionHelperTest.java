package io.praecepta.rules.actions.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaChoreogrhaphyActionHelperTest {

	List<Map<String, Object>> actionParametersList = new ArrayList<>();

	@Before
	public void init() {
		String actionParametersJson_Kafka = "[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"},{\"attributeName\":\"key.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"value.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"kafka.sender.topic\",\"attributeValue\":\"TEST.TOPIC2\"}]";

		actionParametersList = GsonHelper.toEntity(actionParametersJson_Kafka, List.class);

	}

	@Test
	public void testInitializeDataInjestor() {

		/*PraeceptaKafkaDataInjestor dataInjestor = mock(PraeceptaKafkaDataInjestor.class);
		
		try (MockedStatic<PraeceptaChoreogrhaphyActionHelper> mocked = Mockito
				.mockStatic(PraeceptaChoreogrhaphyActionHelper.class)) {


			mocked.when(() -> PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(actionParametersList,
				PraeceptaActionStrategyType.SEND_MESSAGE_TO_KAFKA, null)).thenReturn(dataInjestor);

		IPraeceptaDataInjestor injestorObj = PraeceptaChoreogrhaphyActionHelper
				.initializeDataInjestor(actionParametersList, PraeceptaActionStrategyType.ADD_TO_PAYLOAD, null);

		assertNotNull(injestorObj);

		// FOR DB

		String actionParametersJson_DB = "[{\"attributeName\":\"db.driver\",\"attributeValue\":\"com.mysql.jdbc.Driver\"},{\"attributeName\":\"db.url\",\"attributeValue\":\"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true\"},{\"attributeName\":\"connection.min.pool.size\",\"attributeValue\":\"1\"},{\"attributeName\":\"userName\",\"attributeValue\":\"root\"},{\"attributeName\":\"password\",\"attributeValue\":\"root\"},{\"attributeName\":\"connection.max.pool.size\",\"attributeValue\":\"1\"}]";

		List<Map<String, Object>> actionParametersListDB = GsonHelper.toEntity(actionParametersJson_DB, List.class);

		String dbMetaData = "{\"insert.query\":\"INSERT INTO `praecepta`.`rule_response_info`(`RULE_GROUP_ID`,`RESPONSE`,`RESPONSE_METADATA`,`CREATED_DATE`)VALUES(?,?,?,NOW())\"}";

		Map<String, Object> dbMetaMap = GsonHelper.toEntity(dbMetaData, Map.class);

		PraeceptaDBDataInjestor dataInjestor_DB = mock(PraeceptaDBDataInjestor.class);

		mocked.when(() -> PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(actionParametersListDB,
				PraeceptaActionStrategyType.SEND_MESSAGE_TO_DB, dbMetaMap)).thenReturn(dataInjestor_DB);

		IPraeceptaDataInjestor objDBInjestor = PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(
				actionParametersListDB, PraeceptaActionStrategyType.SEND_MESSAGE_TO_DB, dbMetaMap);

		assertNotNull(objDBInjestor);
		}*/

	}

	@Test
	public void testInitializeKafkaInjestor() {
/*
		PraeceptaKafkaDataInjestor dataInjestor = mock(PraeceptaKafkaDataInjestor.class);

		doNothing().when(dataInjestor).openInjestorConnection(any(PraeceptaKafkaInjestorConfig.class));

		IPraeceptaDataInjestor injestorObj = PraeceptaChoreogrhaphyActionHelper
				.initializeDataInjestor(actionParametersList, PraeceptaActionStrategyType.SEND_MESSAGE_TO_KAFKA, null);

		assertNotNull(injestorObj);*/
	}

	@Test
	public void test1GetConfigPropName() throws Exception {

		Map<String, Object> actionConfigProperties = actionParametersList.stream().collect(Collectors
				.toMap(objMap -> (String) objMap.get("attributeName"), objMap -> objMap.get("attributeValue")));

		Method privateMethod = PraeceptaChoreogrhaphyActionHelper.class.getDeclaredMethod("getConfigPropName",
				String.class, Map.class);

		privateMethod.setAccessible(true);

		Object propValue = privateMethod.invoke(PraeceptaChoreogrhaphyActionHelper.class, "kafka.sender.topic",
				actionConfigProperties);

		assertNotNull(propValue);

		assertEquals("TEST.TOPIC2", propValue);
	}

	@Test
	public void testBuildDataRecord() {

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				new RuleGroupInfo());

		PraeceptaDataRecord dataRecord = PraeceptaChoreogrhaphyActionHelper.buildDataRecord(input);

		assertNotNull(dataRecord);

		assertEquals(1, dataRecord.getRecordEntries().size());
	}

}
