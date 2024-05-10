package io.praecepta.rules.engine.helper;

import java.util.Map;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleExecutionEngineHelperWithUpdatedInputTest {
	
	private static final String SPACE_KEY_DETAILS = "spaceKeyDetails"; 
	private static final String RULE_GRP_DETAILS = "rulGrpDetails"; 
	private static final String RULE_INPUT = "ruleInput"; 
	private static final String RULE_INPUT_TYPE = "ruleInputType"; 

	private static final String INPUT_WITH_SPACE_GRP_JSON = "{\r\n" + 
			"   \"spaceKeyDetails\":{\r\n" + 
			"      \"spaceName\":\"RBC\",\r\n" + 
			"      \"clientId\":\"TRADE_EXECUTION\",\r\n" + 
			"      \"appName\":\"RISK_CALCULATOR\"\r\n" + 
			"   },\r\n" + 
			"   \"rulGrpDetails\":{\r\n" + 
			"      \"ruleGroupName\":\"STOP_TRADE_EXECUTION\",\r\n" + 
			"      \"version\":\"1.3\"\r\n" + 
			"   },\r\n" + 
			"	\"ruleInputType\" : \"JSON\"," + 
			"   \"ruleInput\":\"{\\\"emp\\\":{\\\"name\\\":\\\"Raja\\\",\\\"company\\\":\\\"Broadridge\\\",\\\"id\\\":25},\\\"zip\\\":\\\"08512\\\",\\\"outerCompany\\\":\\\"Broadridge\\\",\\\"location\\\":\\\"Cranbury\\\"}\"\r\n" + 
			"}";
	
	@Test
    public void test(){
		
		Map<String, Object> dataMap = GsonHelper.toMapEntityPreserveNumber(INPUT_WITH_SPACE_GRP_JSON);
		
		PraeceptaRuleSpaceCompositeKey spaceCompositeKey = getRuleSpaceKey(dataMap);
		
		System.out.println(spaceCompositeKey);
		
		String version = getRuleVersion(dataMap);
		
		System.out.println(version);
		
		String ruleGroupName = getRuleGrpName(dataMap);
		
		System.out.println(ruleGroupName);
		
		String ruleInput = getRuleInput(dataMap);
		
		System.out.println(ruleInput);
		
		String ruleInputType = getRuleInputType(dataMap);
		
		System.out.println(ruleInputType);
		
	}
	
	private static PraeceptaRuleSpaceCompositeKey getRuleSpaceKey(Map<String, Object> dataMap) {
		
		PraeceptaRuleSpaceCompositeKey compositeKey = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> spaceKeyDetailsMap = (Map<String, Object>) dataMap.get(SPACE_KEY_DETAILS);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(spaceKeyDetailsMap)) {
				
				compositeKey = new PraeceptaRuleSpaceCompositeKey(
						(String) spaceKeyDetailsMap.get("spaceName"), (String) spaceKeyDetailsMap.get("clientId"), (String) spaceKeyDetailsMap.get("appName"));
			}
			
		}
		return compositeKey;
	}
	
	private static String getRuleVersion(Map<String, Object> dataMap) {
		
		String version = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> rulGrpDetailsMap = (Map<String, Object>) dataMap.get(RULE_GRP_DETAILS);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(rulGrpDetailsMap)) {
				
				version = (String) rulGrpDetailsMap.get("version");
			}
			
		}
		return version;
	}
	
	private static String getRuleGrpName(Map<String, Object> dataMap) {
		
		String ruleGroupName = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> rulGrpDetailsMap = (Map<String, Object>) dataMap.get(RULE_GRP_DETAILS);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(rulGrpDetailsMap)) {
				
				ruleGroupName = (String) rulGrpDetailsMap.get("ruleGroupName");
			}
			
		}
		return ruleGroupName;
	}
	
	private static String getRuleInput(Map<String, Object> dataMap) {
		
		String ruleInput = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInput = (String) dataMap.get(RULE_INPUT);
			
		}
		return ruleInput;
	}
	
	private static String getRuleInputType(Map<String, Object> dataMap) {
		
		String ruleInput = null;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInput = (String) dataMap.get(RULE_INPUT_TYPE);
			
		}
		return ruleInput;
	}
}
