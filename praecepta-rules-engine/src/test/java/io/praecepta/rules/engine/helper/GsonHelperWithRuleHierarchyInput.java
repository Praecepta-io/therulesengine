package io.praecepta.rules.engine.helper;

import java.util.Map;

import io.praecepta.core.helper.GsonHelper;

public class GsonHelperWithRuleHierarchyInput {

	private static final String inputJson = "\r\n" + 
			"{\r\n" + 
			"	\"spaceKeyDetails\" : {\r\n" + 
			"		\"spaceName\" : \"RBC\",\r\n" + 
			"		\"clientId\" : \"TRADE_EXECUTION\",\r\n" + 
			"		\"appName\" : \"RISK_CALCULATOR\"\r\n" + 
			"	},\r\n" + 
			"	\"rulGrpDetails\" : {\r\n" + 
			"		\"ruleGroupName\" : \"STOP_TRADE_EXECUTION\",\r\n" + 
			"		\"version\" : \"1.3\"\r\n" + 
			"	},\r\n" + 
			"	\"ruleInput\" : \"sdfsdfsdfsdfsdfsd\"\r\n" + 
//			"	\"ruleInput\" : \" {\"emp\":{\"name\":\"Raja\",\"company\":\"Xploretch\",\"id\":25},\"zip\":\"08512\",\"outerCompany\":\"Xploretch\",\"location\":\"Cranbury\"}  \"\r\n" + 
			"\r\n" + 
			"}";
	
	private static final String anotherJson = "{\"spaceKeyDetails\":{\"spaceName\":\"RBC\",\"clientId\":\"TRADE_EXECUTION\",\"appName\":\"RISK_CALCULATOR\"},\"rulGrpDetails\":{\"ruleGroupName\":\"STOP_TRADE_EXECUTION\",\"version\":\"1.3\"},\"ruleInput\":\"{\\\"emp\\\":{\\\"name\\\":\\\"Raja\\\",\\\"company\\\":\\\"Xploretch\\\",\\\"id\\\":25},\\\"zip\\\":\\\"08512\\\",\\\"outerCompany\\\":\\\"Xploretch\\\",\\\"location\\\":\\\"Cranbury\\\"}\"}";
	
	public static void main(String[] args) {
		
		Map<String, Object> inputMap = GsonHelper.toMapEntityPreserveNumber(inputJson);
		
		System.out.println(inputMap.get("spaceKeyDetails"));
		System.out.println(inputMap.get("ruleInput"));
		
		inputMap.put("ruleInput", "{\"emp\":{\"name\":\"Raja\",\"company\":\"Xploretch\",\"id\":25},\"zip\":\"08512\",\"outerCompany\":\"Xploretch\",\"location\":\"Cranbury\"}");
//		
		System.out.println(GsonHelper.toJsonPreserveNumber(inputMap));
		
		System.out.println("Another Json --> "+anotherJson);
		
		Map<String, Object> anotherMap = GsonHelper.toMapEntityPreserveNumber(anotherJson);
		
		System.out.println(anotherMap.get("ruleInput"));
	}
}
