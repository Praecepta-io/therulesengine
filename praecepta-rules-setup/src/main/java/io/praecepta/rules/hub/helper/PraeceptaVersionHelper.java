package io.praecepta.rules.hub.helper;

import java.util.Iterator;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaVersionHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaVersionHelper.class);
	
	private static final String MAJOR_PREFIX = "V";
	
	private static final String MINOR_PREFIX = "M";
	
	private static final String PATCH_PREFIX = "P";
	
	private static final String INITIAL_VERSION = "1";
	
	
	public static String generateAVersion() {
		
		return UUID.randomUUID().toString();
	}

	public static String defaultVersion() {
		
		return String.join(".", defaultMajorVersion(), defaultMinorVersion(), defaultPatchVersion());
	}
	
	public static int compareVersions(String leftVersion, String righVersion) {
		
		Iterator<String> v1Iter = Splitter.on('.').trimResults().split(leftVersion).iterator();
        Iterator<String> v2Iter = Splitter.on('.').trimResults().split(righVersion).iterator();
        while (v1Iter.hasNext() && v2Iter.hasNext()) {
            int v1Part = Integer.parseInt(v1Iter.next());
            int v2Part = Integer.parseInt(v2Iter.next());
            if (v1Part != v2Part) {
                return v1Part - v2Part;
            }
        }
        while (v1Iter.hasNext()) {
            if (Integer.parseInt(v1Iter.next()) != 0) {
                return 1;
            }
        }
        while (v2Iter.hasNext()) {
            if (Integer.parseInt(v2Iter.next()) != 0) {
                return -1;
            }
        }
		return 0;
	}
	
	private static String defaultMajorVersion() {
		return MAJOR_PREFIX.concat(initialVersion());
	}
	
	public static String nextMajorVersion(String currentMajorVersion) {
		logger.info("Getting Next Major Version for "+currentMajorVersion);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(currentMajorVersion)) {
			
			return MAJOR_PREFIX.concat(generateNextVersionNumber(MAJOR_PREFIX, currentMajorVersion));
		}
		return defaultMajorVersion();
	}
	
	public static String generateNextVersionNumber(String currentVersion) {
		
		return null;
	}

	private static String generateNextVersionNumber(String prefix, String currentVersion) {
		logger.info("Getting Next  Version for Prefix "+ prefix + " And version " +currentVersion);
		
		String currentVersionDigit = "0";
		
		String nextVersionDigit = currentVersionDigit;
		
		if(currentVersion.startsWith(prefix)) {
			currentVersionDigit = currentVersion.substring(1);
			
			logger.info("curren Version Digits After Subsctring "+currentVersionDigit);
		} else {
			currentVersionDigit = currentVersion;
		}

		if(currentVersionDigit.chars().allMatch( Character::isDigit )) {
			nextVersionDigit = String.valueOf(Integer.parseInt(currentVersionDigit) + 1);
		}
		
		return nextVersionDigit;
	}
	
	private static String defaultMinorVersion() {
		return MINOR_PREFIX.concat(initialVersion());
	}
	
	public static String nextMinorVersion(String currentMinorVersion) {

		if(!PraeceptaObjectHelper.isObjectEmpty(currentMinorVersion)) {
			return MINOR_PREFIX.concat(generateNextVersionNumber(MINOR_PREFIX, currentMinorVersion));
		}
		return defaultMinorVersion();
	}
	
	private static String defaultPatchVersion() {
		return PATCH_PREFIX.concat(initialVersion());
	}
	
	public static String nextPatchVersion(String currentPatchVersion) {

		if(!PraeceptaObjectHelper.isObjectEmpty(currentPatchVersion)) {
			return PATCH_PREFIX.concat(generateNextVersionNumber(PATCH_PREFIX, currentPatchVersion));
		}
		
		return defaultPatchVersion();
	}
	
	private static String initialVersion() {
		return INITIAL_VERSION;
	}

}
