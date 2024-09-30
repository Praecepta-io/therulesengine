package io.praecepta.rules.engine.helper;

import java.util.Map;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleDetailCaptureHelperTest {
	
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
			"   \"ruleInput\":\"{\\\"emp\\\":{\\\"name\\\":\\\"Raja\\\",\\\"company\\\":\\\"Xploretch\\\",\\\"id\\\":25},\\\"zip\\\":\\\"08512\\\",\\\"outerCompany\\\":\\\"Xploretch\\\",\\\"location\\\":\\\"Cranbury\\\"}\"\r\n" + 
			"}";

	@Test
	public void test() {

		Map<String, Object> dataMap = GsonHelper.toMapEntityPreserveNumber(INPUT_WITH_SPACE_GRP_JSON);
		
		PraeceptaRuleSpaceCompositeKey spaceCompositeKey = PraeceptaRuleDetailCaptureHelper.getRuleSpaceKey(dataMap);
		
		System.out.println(spaceCompositeKey);
		
		String version = PraeceptaRuleDetailCaptureHelper.getRuleVersion(dataMap);
		
		System.out.println(version);
		
		String ruleGroupName = PraeceptaRuleDetailCaptureHelper.getRuleGrpName(dataMap);
		
		System.out.println(ruleGroupName);
		
		String ruleInput = PraeceptaRuleDetailCaptureHelper.getRuleInput(dataMap);
		
		System.out.println(ruleInput);
		
		String ruleInputType = PraeceptaRuleDetailCaptureHelper.getRuleInputType(dataMap);
		
		System.out.println(ruleInputType);
	}

}
