package io.praecepta.rest.api.service;

import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.junit.Assert;

import io.praecepta.rest.api.service.impl.PraeceptaRuleGroupServiceImpl;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;

//import org.springframework.test.util.ReflectionTestUtils;
public class PraeceptaRuleGroupServiceTest {

//	@Test
	public void getRuleGroupsTest() {
		IPraeceptaRulesGroupService iPraeceptaRulesGroupService = new PraeceptaRuleGroupServiceImpl();
		PraeceptaPivotalRulesHubManager praeceptaPivotalRulesHubManager = mock(PraeceptaPivotalRulesHubManager.class);
//		ReflectionTestUtils.setField(iPraeceptaRulesGroupService,"praeceptaPivotalRulesHubManager",praeceptaPivotalRulesHubManager);
		Collection<PraeceptaRuleGroup> ruleGroups = iPraeceptaRulesGroupService.getRuleGroups("BRD", "001", "App1", "V1");
		
		Assert.assertNull(ruleGroups);
		
	}
	
//	@Test
	public void getRuleGroupByNameTest() {
		IPraeceptaRulesGroupService iPraeceptaRulesGroupService = new PraeceptaRuleGroupServiceImpl();
		PraeceptaPivotalRulesHubManager praeceptaPivotalRulesHubManager = mock(PraeceptaPivotalRulesHubManager.class);
//		ReflectionTestUtils.setField(iPraeceptaRulesGroupService,"praeceptaPivotalRulesHubManager",praeceptaPivotalRulesHubManager);
		PraeceptaRuleGroup ruleGroup = iPraeceptaRulesGroupService.getRuleGroupByName("BRD", "001", "App1", "V1","Group1");
		
		Assert.assertNull(ruleGroup);
		
	}
	
	
	
//	@Test
	public void addOrUpdateRuleGroupTest() {
		IPraeceptaRulesGroupService iPraeceptaRulesGroupService = new PraeceptaRuleGroupServiceImpl();
		RuleGroupInfo dto = new RuleGroupInfo();
		dto.setRuleGroupName("Group1");
        RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
        ruleSpaceInfo.setSpaceName("BRB");
        ruleSpaceInfo.setClientId("001");
        ruleSpaceInfo.setAppName("App1");
        ruleSpaceInfo.setVersion("V1");
		dto.setRuleSpaceInfo(ruleSpaceInfo);


	/*	String  message = iPraeceptaRulesGroupService.addOrUpdateRuleGroup("BRD", "001", "App1", "V1",dto);
		
		Assert.assertEquals("Rule Group added/updated succesfully", message);
		*/

	}
	
//	@Test
	public void deleteRuleGroupTest() {
		IPraeceptaRulesGroupService iPraeceptaRulesGroupService = new PraeceptaRuleGroupServiceImpl();
		

		
		
		boolean status = iPraeceptaRulesGroupService.deleteRuleGroup("BRD", "001", "App1", "V1","Group1");
		
		Assert.assertTrue(status);
		

	}


	public static void main(String[] args) {
		/*PraeceptaRuleGroupServiceImpl service = new PraeceptaRuleGroupServiceImpl();

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
		ruleSpaceInfo.setVersion("V1");
		ruleSpaceInfo.setAppName("App5");
		ruleSpaceInfo.setSpaceName("ICICI");
		ruleSpaceInfo.setClientId("007");
		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();
		ruleGroupInfo.setRuleGroupName("Group1");
		List<RuleCriteriaInfo> criteriaInfoList = new ArrayList<>();
		RuleCriteriaInfo criteriaInfo = new RuleCriteriaInfo();
		criteriaInfo.setConditionType("MULTI_NESTED_CONDITION");
		RuleMultiNestedConditionInfo ruleMultiNestedConditionInfo = new RuleMultiNestedConditionInfo();

		RuleMultiConditionInfo ruleMultiConditionInfo = new RuleMultiConditionInfo();

		RuleConditionInfo conditionInfo = new RuleConditionInfo();
		conditionInfo.setAttributeName("email");
		conditionInfo.setValueToCompare("varam.kotapati@gmail.com");
		conditionInfo.setOperatorType(ConditionOperatorType.EQUAL_CHARS);

		ruleMultiConditionInfo.setConditionInfo(conditionInfo);
		ruleMultiConditionInfo.setJoinOperatorType(JoinOperatorType.AND);

		RuleConditionInfo nextConditionInfo = new RuleConditionInfo();
		nextConditionInfo.setAttributeName("name");
		nextConditionInfo.setValueToCompare("varam");
		nextConditionInfo.setOperatorType(ConditionOperatorType.EQUAL_CHARS);
		RuleMultiConditionInfo nextCondition = new RuleMultiConditionInfo();
		nextCondition.setConditionInfo(nextConditionInfo);

		ruleMultiConditionInfo.setNextMultiCondition(nextCondition);

		criteriaInfo.setMultiCondition(ruleMultiConditionInfo);
		List<RuleActionInfo> actionList = new ArrayList<>();
		RuleActionInfo actionInfo = new RuleActionInfo();
		actionInfo.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		actionInfo.setActionAttributeName("emailValidation");
		actionInfo.setValueToAssign("true");
		actionList.add(actionInfo);
		criteriaInfo.setActionList(actionList);

		List<RuleActionInfo> failureActionList = new ArrayList<>();
		RuleActionInfo failureActionInfo = new RuleActionInfo();
		failureActionInfo.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
		failureActionInfo.setActionAttributeName("emailValidation");
		failureActionInfo.setValueToAssign("false");
		failureActionList.add(failureActionInfo);
		criteriaInfo.setFailureActionList(failureActionList);

		criteriaInfoList.add(criteriaInfo);
		ruleGroupInfo.setCriteriaList(criteriaInfoList);
		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);
		//service.addOrUpdateRuleGroup(ruleSpaceInfo.getSpaceName(), ruleSpaceInfo.getClientId(), ruleSpaceInfo.getAppName(), ruleSpaceInfo.getVersion(), ruleGroupInfo);


		//String groupInfo = "{\"ruleGroupName\":\"Group1\",\"active\":true,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"email\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"varam.kotapati@gmail.com\"}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"emailValidation\",\"valueToAssign\":\"true\"}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"emailValidation\",\"valueToAssign\":\"false\"}],\"orderNumber\":0}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"007\",\"appName\":\"App5\",\"version\":\"V1\"},\"spaceName\":\"ICICI\",\"clientName\":\"007\",\"appName\":\"App5\"}";

		String groupInfo = "{\"ruleGroupName\":\"Group1\",\"active\":true,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"email\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"varam.kotapati@gmail.com\"},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"name\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"varam\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"emailValidation\",\"valueToAssign\":\"true\"}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"emailValidation\",\"valueToAssign\":\"false\"}],\"orderNumber\":0}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"007\",\"appName\":\"App5\",\"version\":\"V1\"},\"spaceName\":\"ICICI\",\"clientName\":\"007\",\"appName\":\"App5\"}";
		PraeceptaRuleGroup praeceptaRuleGroup = GsonHelper.toEntity(groupInfo, PraeceptaRuleGroup.class);

		List<PraeceptaRuleGroup> ruleGroupList = new ArrayList<>();
		ruleGroupList.add(praeceptaRuleGroup);

		List<RuleGroupInfo>  ruleGroupInfos = (List<RuleGroupInfo>) service.convertRuleGroup(ruleGroupList);

		System.out.println(ruleGroupInfos);

*/


	}

}
