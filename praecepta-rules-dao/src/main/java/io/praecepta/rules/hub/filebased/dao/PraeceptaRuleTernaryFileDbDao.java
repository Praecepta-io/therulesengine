package io.praecepta.rules.hub.filebased.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.praecepta.core.helper.GsonHelperWithAdapter;
import io.praecepta.core.helper.PraeceptaActionStrategyInterfaceAdapter;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.projection.IPraeceptaAction;

public class PraeceptaRuleTernaryFileDbDao {
	
	private String location;
	
	private static final String ruleSpaceMetaDataFileName = "PraeceptaRuleSpaceMetaData.csv";
	
	// TestSpaceName_TestClientId_TestAppName_TestVersion
	private static final String ruleSpaceFileNamePattern = "%1$s_%2$s_%3$s_%4$s";
	
	private static final String ruleGrpMetaDataFileName = "PraeceptaRuleGrpMetaData.csv";
	
	// TestSpaceName_TestClientId_TestAppName_TestVersion
	private static final String ruleGrpFileNamePattern = "%1$s_%2$s_%3$s_%4$s_%5$s";
	
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> compoisteKeyToVersionAndSpaceFileNameMap = new HashMap<>();
	
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> compoisteKeyToVersionAndGrpFileNameMap = new HashMap<>();
	
	GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());

	private String fileLocation;
	
	private String fileNamePrefix;
	
	private static File spaceMetaDataFile;
	
	private static File grpMetaDataFile;
	
	public enum FILE_TYPE{
		SPACE, GRP, SIDECAR;
	}
	
	/*static {
		
		File key = ResourceUtils.getFile(file);
	}*/
	
	public PraeceptaRuleTernaryFileDbDao(FILE_TYPE type, String fileLocation, String fileNamePrefix) {
		this.fileLocation = fileLocation;
		this.fileNamePrefix = fileNamePrefix;
		
		initMetaDataFiles(type);
	}
	
	private static void initMetaDataFiles(FILE_TYPE type) {
		
		if(type == FILE_TYPE.SPACE) {
			
		}
		
	}

	public void upsert(PraeceptaRuleSpace praeceptaRuleSpace) {
		PraeceptaRuleSpaceCompositeKey key = praeceptaRuleSpace.getRuleSpaceKey();
		
		compoisteKeyToVersionAndSpaceFileNameMap.putIfAbsent(key, new HashMap<>());
		
		compoisteKeyToVersionAndSpaceFileNameMap.get(key).putIfAbsent(key.getVersion(),  String.format(ruleSpaceFileNamePattern, key.getSpaceName(), key.getClientId(), key.getAppName(), key.getVersion()));
		
		String spaceFileName = compoisteKeyToVersionAndSpaceFileNameMap.get(key).get(key.getVersion());
		
		
	}
}
