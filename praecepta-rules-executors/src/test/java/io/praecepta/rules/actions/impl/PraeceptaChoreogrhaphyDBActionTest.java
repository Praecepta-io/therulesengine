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

public class PraeceptaChoreogrhaphyDBActionTest{

	List<Map<String, Object>> actionParametersList;

	Map<String, Object> dbMetaDataMap;

	@Before
	public void init() {

		String actionParametersJson = "[{\"attributeName\":\"db.driver\",\"attributeValue\":\"com.mysql.jdbc.Driver\"},{\"attributeName\":\"db.url\",\"attributeValue\":\"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true\"},{\"attributeName\":\"connection.min.pool.size\",\"attributeValue\":\"1\"},{\"attributeName\":\"userName\",\"attributeValue\":\"root\"},{\"attributeName\":\"password\",\"attributeValue\":\"root\"},{\"attributeName\":\"connection.max.pool.size\",\"attributeValue\":\"1\"}]";

		actionParametersList = GsonHelper.toEntity(actionParametersJson, List.class);

		String dbMetaData = "{\"insert.query\":\"INSERT INTO `praecepta`.`rule_group_status_info` ( `RULE_GROUP_ID`, `RULE_GROUP_NAME`, `RULE_SPACE_NAME`, `APP_NAME`, `CLIENT_NAME`, `VERSION`, `RESPONSE`, `RESPONSE_METADATA`, `RULE_GROUP_EXECUTION_STATUS`, `CREATED_DATE`) VALUES (?, ?, ?, ?,?,?, ?, ?, ?, NOW());\"}";

		dbMetaDataMap = GsonHelper.toEntity(dbMetaData, Map.class);
	}

	@Test
	public void testChoreogrhaphyAction_DB() {

		PraeceptaChoreogrhaphyDBAction dbAction = new PraeceptaChoreogrhaphyDBAction();

		dbAction.setActionParameters(actionParametersList);

		dbAction.setDbMetadata(dbMetaDataMap);

		dbAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				getTestRuleGroupNamewithSpaceKey());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

		/*PraeceptaDBDataInjestor dataInjestor = mock(PraeceptaDBDataInjestor.class);
		
		try (MockedStatic<PraeceptaChoreogrhaphyActionHelper> mocked = Mockito
				.mockStatic(PraeceptaChoreogrhaphyActionHelper.class)) {

			mocked.when(() -> PraeceptaChoreogrhaphyActionHelper.initializeDataInjestor(actionParametersList,
					PraeceptaActionStrategyType.SEND_MESSAGE_TO_DB, dbMetaDataMap)).thenReturn(dataInjestor);
		
		dbAction.performAction(input);

		Assert.assertEquals(true, dbAction.initialized);
	   }*/

	}

	@Test
	public void testperformInitializeDataInjestor() throws Exception {

		PraeceptaChoreogrhaphyDBAction dbAction = new PraeceptaChoreogrhaphyDBAction();

		dbAction.setActionParameters(actionParametersList);

		dbAction.setDbMetadata(dbMetaDataMap);

		dbAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		Field field1 = PraeceptaChoreogrhaphyAction.class.getDeclaredField("ruleGroupWithDataInjestorMap");
		field1.setAccessible(true);
		field1.set(dbAction, new HashMap<>());

		dbAction.performInitializeDataInjestor(getTestRuleGroupNamewithSpaceKey());

	}

	/* Below test case is to test end to end action with DB Data Injestor */
	// @Test
	public void testChoreogrhaphyDBAction() {

		PraeceptaChoreogrhaphyDBAction dbAction = new PraeceptaChoreogrhaphyDBAction();

		String actionParametersJson = "[{\"attributeName\":\"db.driver\",\"attributeValue\":\"com.mysql.jdbc.Driver\"},{\"attributeName\":\"db.url\",\"attributeValue\":\"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true\"},{\"attributeName\":\"connection.min.pool.size\",\"attributeValue\":1},{\"attributeName\":\"userName\",\"attributeValue\":\"root\"},{\"attributeName\":\"password\",\"attributeValue\":\"root\"},{\"attributeName\":\"connection.max.pool.size\",\"attributeValue\":\"1\"}]";

		List<Map<String, Object>> actionParametersList = GsonHelper.toEntity(actionParametersJson, List.class);

		String dbMetaData = "{\"insert.query\":\"INSERT INTO `praecepta`.`rule_response_info`(`RULE_GROUP_ID`,`RESPONSE`,`RESPONSE_METADATA`,`CREATED_DATE`)VALUES(?,?,?,NOW())\"}";

		Map<String, Object> dbMetaDataMap = GsonHelper.toEntity(dbMetaData, Map.class);

		dbAction.setActionParameters(actionParametersList);

		dbAction.setDbMetadata(dbMetaDataMap);

		dbAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);

		PraeceptaRequestStore input = new PraeceptaRequestStore();

		Map<String, Object> responseMetaData = new HashMap<>();

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY,
				getTestRuleGroupNamewithSpaceKey());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE, "{\"attribute1\":\"val1\"}");

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_RESPONSE_METADATA, responseMetaData);

		dbAction.performAction(input);

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
