package io.praecepta.rules.engine.helper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleDetailCaptureHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleDetailCaptureHelper.class);
	
	public static final String SPACE_KEY_DETAILS = "spaceKeyDetails"; 
	public static final String RULE_GRP_DETAILS = "rulGrpDetails"; 
	public static final String RULE_INPUT = "ruleInput"; 
	public static final String RULE_INPUT_TYPE = "ruleInputType"; 
	public static final String SPACE_NAME = "spaceName"; 
	public static final String CLIENT_ID = "clientId"; 
	public static final String APP_NAME = "appName"; 
	public static final String VERSION = "version"; 
	public static final String RULE_GRP_NAME = "ruleGroupName"; 

	public static PraeceptaRuleSpaceCompositeKey getRuleSpaceKey(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Space Key Information from Input Text");

		PraeceptaRuleSpaceCompositeKey compositeKey = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> spaceKeyDetailsMap = (Map<String, Object>) dataMap.get(SPACE_KEY_DETAILS);

			if (!PraeceptaObjectHelper.isObjectEmpty(spaceKeyDetailsMap)) {

				compositeKey = new PraeceptaRuleSpaceCompositeKey((String) spaceKeyDetailsMap.get(SPACE_NAME),
						(String) spaceKeyDetailsMap.get(CLIENT_ID), (String) spaceKeyDetailsMap.get(APP_NAME));
			}

		}
		
		LOG.debug("Here is the Space Key captured from Input Text - {} ", compositeKey);
		return compositeKey;
	}
	
	public static Map<String, Object> buildRuleSpaceKeyDetailsMap(String spaceName, String clientId, String appName) {
		
		Map<String, Object> spaceDetailsMap = new HashMap<String, Object>();
		
			Map<String, Object> spaceMap = new HashMap<String, Object>();
			spaceDetailsMap.put(SPACE_NAME, spaceName);
			spaceDetailsMap.put(CLIENT_ID, clientId);
			spaceDetailsMap.put(APP_NAME, appName);
		
		spaceDetailsMap.put(SPACE_KEY_DETAILS, spaceMap);
		
		return spaceDetailsMap;
	}

	public static String getRuleVersion(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Version Information from Input Text ");

		String version = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> rulGrpDetailsMap = (Map<String, Object>) dataMap.get(RULE_GRP_DETAILS);

			if (!PraeceptaObjectHelper.isObjectEmpty(rulGrpDetailsMap)) {

				version = (String) rulGrpDetailsMap.get(VERSION);
			}

		}
		
		LOG.debug("Here is the Version captured from Input Text - {} ", version);
		return version;
	}

	public static Map<String, Object> buildRuleGrpDetailsMap(String ruleGrpName, String version) {
		
		Map<String, Object> grpDetailsMap = new HashMap<String, Object>();
		
			Map<String, Object> grpMap = new HashMap<String, Object>();
			grpDetailsMap.put(RULE_GRP_NAME, ruleGrpName);
			grpDetailsMap.put(VERSION, version);
		
		grpDetailsMap.put(RULE_GRP_DETAILS, grpMap);
		
		return grpDetailsMap;
	}

	public static String getRuleGrpName(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Rule Group Name Information from Input Text ");

		String ruleGroupName = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			Map<String, Object> rulGrpDetailsMap = (Map<String, Object>) dataMap.get(RULE_GRP_DETAILS);

			if (!PraeceptaObjectHelper.isObjectEmpty(rulGrpDetailsMap)) {

				ruleGroupName = (String) rulGrpDetailsMap.get(RULE_GRP_NAME);
			}

		}
		
		LOG.debug("Here is the Rule Group Name from Input Text - {} ", ruleGroupName);
		return ruleGroupName;
	}

	public static String getRuleInput(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Rule Group Name Information from Input Text ");

		String ruleInput = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInput = (String) dataMap.get(RULE_INPUT);

		}
		
		LOG.debug("Here is the Rule Input from Input Text - {} ", ruleInput);
		return ruleInput;
	}
	
	public static Map<String, Object> buildRuleInputMap(String inputStr) {
		
		Map<String, Object> ruleInputMap = new HashMap<String, Object>(1);
		
		ruleInputMap.put(RULE_INPUT, inputStr);
		
		return ruleInputMap;
	}

	public static String getRuleInputType(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Rule Input Type Information from Input Text ");

		String ruleInputType = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInputType = (String) dataMap.get(RULE_INPUT_TYPE);

		}
		
		LOG.debug("Here is the Rule Input Type from Input Text - {} ", ruleInputType);
		return ruleInputType;
	}
	
	public static Map<String, Object> buildRuleInputTypeMap(String ruleInputType) {
		
		Map<String, Object> ruleInputTypeMap = new HashMap<String, Object>(1);
		
		ruleInputTypeMap.put(RULE_INPUT_TYPE, ruleInputType);
		
		return ruleInputTypeMap;
	}
	
	public static String buildRuleStrForExecution(Map<String, Object> spaceDetailsMap, Map<String, Object> grpDetailsMap, 
			Map<String, Object> ruleInputStrMap, Map<String, Object> ruleInputTypeMap) {
		
		Map<String, Object> ruleStrForExecutionMap = new HashMap<String, Object>(4);
		
		ruleStrForExecutionMap.put(SPACE_KEY_DETAILS, spaceDetailsMap);
		ruleStrForExecutionMap.put(RULE_GRP_DETAILS, grpDetailsMap);
		ruleStrForExecutionMap.put(RULE_INPUT, ruleInputStrMap);
		ruleStrForExecutionMap.put(RULE_INPUT_TYPE, ruleInputTypeMap);
		
		return GsonHelper.toJsonPreserveNumber(ruleStrForExecutionMap);
		
	}
	
	public static String buildRuleStrForExecution(Map<String, Object> spaceDetailsMap, Map<String, Object> grpDetailsMap, 
			String ruleInputStr, String ruleInputType) {
		
		Map<String, Object> ruleStrForExecutionMap = new HashMap<String, Object>(4);
		
		ruleStrForExecutionMap.put(SPACE_KEY_DETAILS, spaceDetailsMap);
		ruleStrForExecutionMap.put(RULE_GRP_DETAILS, grpDetailsMap);
		ruleStrForExecutionMap.put(RULE_INPUT, ruleInputStr);
		ruleStrForExecutionMap.put(RULE_INPUT_TYPE, ruleInputType);
		
		return GsonHelper.toJsonPreserveNumber(ruleStrForExecutionMap);
		
	}
}
