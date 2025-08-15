package io.praecepta.rules.executor.inspira.spring.config;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;

import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.*;

public class PraeceptaRecordMagnifierTest {

	@Test
	public void test() {
		
//		RecordEntry inputRecord = new RecordEntry("{\"id\":1,\"name\":\"test1\"}");
		RecordEntry inputRecord = new RecordEntry("{\r\n" + 
				"	\"age\": 26,\r\n" + 
				"	\"hasSalaryAccount\" : \"Y\",\r\n" + 
				"	\"eligibleForQuickCredit\" : \"Y\",\r\n" + 
				"	\"activeFixedDepositAcc\" : \"Y\",\r\n" + 
				"	\"average12MonthsBal\" : 2700000,\r\n" + 
				"	\"turnOver12Mon\" : 6000000		\r\n" + 
				"}");
		
		Map<String, Object> ruleSpaceDetailsFromDictionary = new HashMap<String, Object>();
		
		ruleSpaceDetailsFromDictionary.put(SPACE_NAME, "INSPIRA");
		ruleSpaceDetailsFromDictionary.put(CLIENT_ID, "ABC_BANK");
		ruleSpaceDetailsFromDictionary.put(APP_NAME, "RETAIL_DIVISION");
		ruleSpaceDetailsFromDictionary.put(VERSION, "V1");
		ruleSpaceDetailsFromDictionary.put(RULE_GRP_NAME, "CREDIT_CHECK");
		
		String inputJson = inputRecord.getMessageText();
		
		Map<String, Object> spaceDetailsMap = buildRuleSpaceKeyDetailsMap(
				ruleSpaceDetailsFromDictionary.get(SPACE_NAME).toString(), ruleSpaceDetailsFromDictionary.get(CLIENT_ID).toString(), ruleSpaceDetailsFromDictionary.get(APP_NAME).toString()   
				);
		
		Map<String, Object> grpDetailsMap = buildRuleGrpDetailsMap(
				ruleSpaceDetailsFromDictionary.get(RULE_GRP_NAME).toString(), ruleSpaceDetailsFromDictionary.get(VERSION).toString()   
				);
		
		String ruleStrForExecution = buildRuleStrForExecution(spaceDetailsMap, grpDetailsMap, inputJson, "JSON");
		
		System.out.println(ruleStrForExecution);
		
		inputRecord.setMessageText(ruleStrForExecution);
	}

}
