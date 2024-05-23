package io.praecepta.rules.storeandpivotalmanager;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaTestRuleConstants {

	public static final String ACATS_ACTIVE_SPACE_VERSION = "1.0";

	public static final String ACATS_SPACE_NAME = "BRD";

	public static final String ACATS_CLIENT_NAME = "GTO";

	public static final String ACATS_APP_NAME = "ACATS";

	public static final String ACATS_TRANSFER_STOP_GRP = "TRANSFER_STOP";

	public static final String ACATS_RULE_NAME = "NameAndAgeRule";
	
	public static final String FXL_ACTIVE_SPACE_VERSION = "2.0";

	public static final String FXL_SPACE_NAME = "BRD";

	public static final String FXL_CLIENT_NAME = "ICICI";

	public static final String FXL_APP_NAME = "FXL";

	public static final String FXL_NAME_AGE_VALIDATION = "NameAndAgevalidation";

	public static PraeceptaRuleSpaceCompositeKey getAcatsSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(ACATS_SPACE_NAME, ACATS_CLIENT_NAME, ACATS_APP_NAME);
	}

	public static  PraeceptaRuleSpaceCompositeKey getAcatsSpaceKeyWithInitialVersion() {
		PraeceptaRuleSpaceCompositeKey key = getAcatsSpaceKey();
		key.setVersion(ACATS_ACTIVE_SPACE_VERSION);
		return key;
	}

	public static  PraeceptaRuleSpaceCompositeKey getFxlSpaceKey() {
		return new PraeceptaRuleSpaceCompositeKey(FXL_SPACE_NAME, FXL_CLIENT_NAME, FXL_APP_NAME);
	}

	public static  PraeceptaRuleSpaceCompositeKey getFxlSpaceKeyWithInitialVersion() {
		PraeceptaRuleSpaceCompositeKey key = getFxlSpaceKey();
		key.setVersion(FXL_ACTIVE_SPACE_VERSION);
		return key;
	}
}
