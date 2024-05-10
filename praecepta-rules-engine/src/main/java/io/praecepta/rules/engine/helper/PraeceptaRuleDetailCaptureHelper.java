package io.praecepta.rules.engine.helper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleDetailCaptureHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleDetailCaptureHelper.class);
	
	private static final String SPACE_KEY_DETAILS = "spaceKeyDetails"; 
	private static final String RULE_GRP_DETAILS = "rulGrpDetails"; 
	private static final String RULE_INPUT = "ruleInput"; 
	private static final String RULE_INPUT_TYPE = "ruleInputType"; 
	private static final String SPACE_NAME = "spaceName"; 
	private static final String CLIENT_ID = "clientId"; 
	private static final String APP_NAME = "appName"; 
	private static final String VERSION = "version"; 
	private static final String RULE_GRP_NAME = "ruleGroupName"; 

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
		
		LOG.info("Here is the Space Key captured from Input Text - {} ", compositeKey);
		return compositeKey;
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
		
		LOG.info("Here is the Version captured from Input Text - {} ", version);
		return version;
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
		
		LOG.info("Here is the Rule Group Name from Input Text - {} ", ruleGroupName);
		return ruleGroupName;
	}

	public static String getRuleInput(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Rule Group Name Information from Input Text ");

		String ruleInput = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInput = (String) dataMap.get(RULE_INPUT);

		}
		
		LOG.info("Here is the Rule Input from Input Text - {} ", ruleInput);
		return ruleInput;
	}

	public static String getRuleInputType(Map<String, Object> dataMap) {
		
		LOG.debug("Capturing the Rule Input Type Information from Input Text ");

		String ruleInputType = null;

		if (!PraeceptaObjectHelper.isObjectEmpty(dataMap)) {
			ruleInputType = (String) dataMap.get(RULE_INPUT_TYPE);

		}
		
		LOG.info("Here is the Rule Input Type from Input Text - {} ", ruleInputType);
		return ruleInputType;
	}
}
