package io.praecepta.rules.hub.dao.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleGroupDbModel;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSpaceDbModel;

public class ConverterDaoHelperTest {

	@Test
	public void testgetKeyRuleSpaceIdCompositeKeyMap() {
		String key = ConverterDaoHelper.getKeyRuleSpaceIdCompositeKeyMap(prepareRuleSpaceModel("CARMA", "005", "BRD"));
		assertEquals("BRD_CARMA_005_V1", key);
	}

	@Test
	public void testgetKeyRuleGroupIdCompositeKeyMap() {
		String key = ConverterDaoHelper
				.getKeyRuleGroupIdCompositeKeyMap(prepareRuleGroupModel("CARMA", "005", "BRD", "RuleGrpTest"));
		assertEquals("BRD_CARMA_005_V1_RuleGrpTest", key);
	}

	@Test
	public void testPrepareRuleSpaceModel() {
		PraeceptaRuleSpaceDbModel model = ConverterDaoHelper
				.prepareRuleSpaceModel(prepareRuleSpace("App1", "003", "test1"), new HashMap<String, Long>());
		assertNotNull(model);
		assertEquals("test1", model.getSpaceName());
	}

	@Test
	public void testGetRuleSpaceList() {
		Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels = new ArrayList<>();
		ruleSpaceModels.add(prepareRuleSpaceModel("CARMA", "005", "BRD"));
		List<PraeceptaRuleSpace> ruleSpaceDtos = ConverterDaoHelper.getRuleSpaceList(ruleSpaceModels);
		assertNotNull(ruleSpaceDtos);
		assertEquals(1, ruleSpaceDtos.size());
	}

	@Test
	public void testPrepareKeyRuleSpaceIdMap() {
		PraeceptaRuleSpaceCompositeKey compositeKey = getRuleSpaceKey("App1", "003", "test1");
		String key = ConverterDaoHelper.prepareKeyRuleSpaceIdMap(compositeKey, "V2");
		assertEquals("test1_App1_003_V2", key);
	}

	@Test
	public void testPrepareListRuleSpaceIdsByKey() {
		List<Long> ruleSpaceIdsList = new ArrayList<>();
		Map<String, Long> ruleSpaceIdMap = new HashMap<>();
		ruleSpaceIdMap.put("test1_App1_003_V1", 2l);
		ConverterDaoHelper.prepareListRuleSpaceIdsByKey(getRuleSpaceKey("App1", "003", "test1"), ruleSpaceIdsList,
				ruleSpaceIdMap);
		assertEquals(1, ruleSpaceIdsList.size());
		assertNotNull(ruleSpaceIdsList.get(0));
	}

	@Test
	public void testGetListRuleGroupIdsByKey() {
		Map<String, Long> ruleGrpIdMap = new HashMap<>();
		ruleGrpIdMap.put("test1_App1_003_V1_rg1", 1l);
		ruleGrpIdMap.put("test1_App1_003_V1_rg2", 2l);
		ruleGrpIdMap.put("test1_App1_003_V2_rg2", 3l);
		ruleGrpIdMap.put("test1_App1_003_V2_rg1", 4l);

		PraeceptaRuleSpaceCompositeKey key = getRuleSpaceKey("App1", "003", "test1");
		List<Long> ruleGrpIdList = ConverterDaoHelper.getIdsListByKey(key, ruleGrpIdMap);
		assertEquals(2, ruleGrpIdList.size());

		key.setVersion(null);
		List<Long> ruleGrpIdList2 = ConverterDaoHelper.getIdsListByKey(key, ruleGrpIdMap);
		assertEquals(4, ruleGrpIdList2.size());
	}

	@Test
	public void testprepareKeyRuleGroupIdMap() {
		PraeceptaRuleSpaceCompositeKey key = getRuleSpaceKey("App1", "003", "test1");
		String keyToCheck = ConverterDaoHelper.prepareKeyRuleGroupIdMap(key, null);
		assertEquals("test1_App1_003_V1", keyToCheck);

		String keyToCheckWithRuleGroupName = ConverterDaoHelper.prepareKeyRuleGroupIdMap(key, "testRg");
		assertEquals("test1_App1_003_V1_testRg", keyToCheckWithRuleGroupName);
	}

	@Test
	public void testconvertModelToRuleGroup() {
		PraeceptaRuleGroupDbModel rgModel = prepareRuleGroupModel("CARMA", "005", "BRD", "RuleGrpTest");
		rgModel.setId(1l);
		PraeceptaRuleGroup ruleGroup = ConverterDaoHelper.convertModelToRuleGroup(rgModel);
		assertNotNull(ruleGroup);
		assertEquals("BRD", ruleGroup.getSpaceName());
	}

	@Test
	public void testpopulateRuleGroupDbModel() {
		PraeceptaRuleGroupDbModel rgModel = ConverterDaoHelper
				.populateRuleGroupDbModel(prepareRuleGroup("CARMA", "005", "BRD", "RuleGrpTest"));
		assertNotNull(rgModel);
	}

	@Test
	public void testconvertToRuleGroupDbModel() {
		PraeceptaRuleGroup ruleGroupObj=prepareRuleGroup("CARMA", "005", "BRD", "RuleGrpTest");
		ruleGroupObj.setUniqueId("1");
		PraeceptaRuleGroupDbModel rgModel = ConverterDaoHelper.convertToRuleGroupDbModel(ruleGroupObj
				, new HashMap<>(), new HashMap<>());
		assertNotNull(rgModel);
		assertEquals("RuleGrpTest", rgModel.getRuleGroupName());
	}
	
	@Test
	public void testgetLstRuleGroupIdsByKeyAndName() {
		PraeceptaRuleSpaceCompositeKey key = getRuleSpaceKey("App1", "003", "test1");
		Map<String, Long> ruleGrpIdMap = new HashMap<>();
		ruleGrpIdMap.put("test1_App1_003_V1_rg1", 1l);
		ruleGrpIdMap.put("test1_App1_003_V1_rg2", 2l);
		ruleGrpIdMap.put("test1_App1_003_V2_rg2", 3l);
		ruleGrpIdMap.put("test1_App1_003_V2_rg1", 4l);
		List<Long> ruleGrpIdsList=ConverterDaoHelper.getLstRuleGroupIdsByKeyAndName(key,"rg1",ruleGrpIdMap);
		assertNotNull(ruleGrpIdsList);
		assertEquals(1, ruleGrpIdsList.size());
	}

	private PraeceptaRuleSpaceDbModel prepareRuleSpaceModel(String appName, String clientId, String spaceName) {
		PraeceptaRuleSpaceDbModel ruleSpaceModel = new PraeceptaRuleSpaceDbModel();
		ruleSpaceModel.setAppName(appName);
		ruleSpaceModel.setClientId(clientId);
		ruleSpaceModel.setSpaceName(spaceName);
		ruleSpaceModel.setVersion("V1");
		return ruleSpaceModel;
	}

	private PraeceptaRuleSpace prepareRuleSpace(String appName, String clientId, String spaceName) {
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		ruleSpace.setRuleSpaceKey(getRuleSpaceKey(appName, clientId, spaceName));
		ruleSpace.setActive(true);
		return ruleSpace;
	}

	private PraeceptaRuleGroupDbModel prepareRuleGroupModel(String appName, String clientId, String spaceName,
			String ruleGrpName) {
		PraeceptaRuleGroupDbModel ruleGrpModel = new PraeceptaRuleGroupDbModel();
		ruleGrpModel.setAppName(appName);
		ruleGrpModel.setClientName(clientId);
		ruleGrpModel.setSpaceName(spaceName);
		ruleGrpModel.setVersion("V1");
		ruleGrpModel.setRuleGroupName(ruleGrpName);
		return ruleGrpModel;
	}

	private PraeceptaRuleGroup prepareRuleGroup(String appName, String clientId, String spaceName,
			String ruleGrpName) {
		PraeceptaRuleGroup ruleGrpModel = new PraeceptaRuleGroup(spaceName, clientId, appName);
		ruleGrpModel.setRuleGroupName(ruleGrpName);
		return ruleGrpModel;
	}

	private PraeceptaRuleSpaceCompositeKey getRuleSpaceKey(String appName, String clientId, String spaceName) {
		return new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName);
	}
}
