package io.praecepta.audit.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaRuleSpaceHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleSpaceHelper.class);
	
	public static boolean validateNonEmptySpaceKeyInfo(String spaceName, String clientId, String appName, String version,
			String groupname) {
		
		LOG.info(" Doing Epty checks for Space Name = " + spaceName + " , Client Id = " + clientId +
				", App Name = " + appName + " , Version = " + version + " , Group Name = "+groupname);
		boolean valid = false;
		
		if(!PraeceptaObjectHelper.isObjectEmpty(spaceName) &&
				!PraeceptaObjectHelper.isObjectEmpty(clientId) &&
				!PraeceptaObjectHelper.isObjectEmpty(appName) &&
				!PraeceptaObjectHelper.isObjectEmpty(version) &&
				!PraeceptaObjectHelper.isObjectEmpty(groupname)
				) {
			valid = true;
		}
		
		return valid;
	}

}
