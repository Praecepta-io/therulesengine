package io.praecepta.rules.actions.impl;

//import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

public class PraeceptaChoreogrhaphyActionTest {

	static List<Map<String, Object>> actionParametersList;

	@Before
	public void init() {

		String actionParametersJson = "[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"},{\"attributeName\":\"key.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"value.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"kafka.sender.topic\",\"attributeValue\":\"TEST.TOPIC2\"}]";

		actionParametersList = GsonHelper.toEntity(actionParametersJson, List.class);

	}

	@Test
	public void testChoreogrhaphyAction_KafkadoAction() throws Exception {

		PraeceptaChoreogrhaphyAction praeceptaChoreographyAction = new PraeceptaChoreogrhaphyAction();

		praeceptaChoreographyAction.setActionParameters(actionParametersList);

		praeceptaChoreographyAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				getTestRuleGroupNamewithSpaceKey());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

//		PraeceptaKafkaDataInjestor dataInjestor = mock(PraeceptaKafkaDataInjestor.class);

		Field field1 = PraeceptaChoreogrhaphyAction.class.getDeclaredField("ruleGroupWithDataInjestorMap");
		field1.setAccessible(true);
		field1.set(praeceptaChoreographyAction, new HashMap<>());

		/*try (MockedStatic<PraeceptaChoreogrhaphyActionHelper> mocked = Mockito
				.mockStatic(PraeceptaChoreogrhaphyActionHelper.class)) {

			mocked.when(() -> PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(actionParametersList,
					PraeceptaActionStrategyType.SEND_MESSAGE_TO_KAFKA, null)).thenReturn(dataInjestor);

			praeceptaChoreographyAction.doAction(input);

			Assert.assertEquals(1, praeceptaChoreographyAction.ruleGroupWithDataInjestorMap.size());

		}*/

	}

	@Test
	public void testChoreogrhaphyAction_KafkaPerformAction() {

		PraeceptaChoreogrhaphyAction praeceptaChoreographyAction = new PraeceptaChoreogrhaphyAction();

		String actionParametersJson = "[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"},{\"attributeName\":\"key.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"value.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"kafka.sender.topic\",\"attributeValue\":\"TEST.TOPIC2\"}]";

		List<Map<String, Object>> actionParametersList = GsonHelper.toEntity(actionParametersJson, List.class);

		praeceptaChoreographyAction.setActionParameters(actionParametersList);

		praeceptaChoreographyAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				getTestRuleGroupNamewithSpaceKey());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

		/*PraeceptaKafkaDataInjestor dataInjestor = mock(PraeceptaKafkaDataInjestor.class);

		try (MockedStatic<PraeceptaChoreogrhaphyActionHelper> mocked = Mockito
				.mockStatic(PraeceptaChoreogrhaphyActionHelper.class)) {

			mocked.when(() -> PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(actionParametersList,
					PraeceptaActionStrategyType.SEND_MESSAGE_TO_KAFKA, null)).thenReturn(dataInjestor);

			praeceptaChoreographyAction.performAction(input);

			Assert.assertEquals(true, praeceptaChoreographyAction.initialized);
		}*/

	}

	/* Below test case is to test end to end action with Kafka Data Injestor */
	// @Test
	public void testChoreogrhaphyKafkaAction() {

		PraeceptaChoreogrhaphyAction praeceptaChoreographyAction = new PraeceptaChoreogrhaphyAction();

		String actionParametersJson = "[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"},{\"attributeName\":\"key.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"value.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"kafka.sender.topic\",\"attributeValue\":\"TEST.TOPIC2\"}]";

		List<Map<String, Object>> actionParametersList = GsonHelper.toEntity(actionParametersJson, List.class);

		praeceptaChoreographyAction.setActionParameters(actionParametersList);

		praeceptaChoreographyAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				getTestRuleGroupNamewithSpaceKey());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

		praeceptaChoreographyAction.performAction(input);

	}

	private static RuleGroupInfo getTestRuleGroupNamewithSpaceKey() {
		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();
		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
		ruleSpaceInfo.setVersion("V1");
		ruleSpaceInfo.setSpaceName("test1");
		ruleSpaceInfo.setClientId("CLNT1");
		ruleSpaceInfo.setAppName("praecepta");
		ruleGroupInfo.setRuleGroupName("RuleGroup1");
		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);
		return ruleGroupInfo;

	}
}
