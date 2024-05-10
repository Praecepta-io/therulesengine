package io.praecepta.rules.hub.filebased.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceMetadata;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;
import io.praecepta.rules.hub.filebased.util.PraeceptaFileUtil;

public class PraeceptaWindowsDbDao {

    private static final Logger LOG = LoggerFactory.getLogger(PraeceptaWindowsDbDao.class);
//    public static final String RULEGROUP = "rulegroup";
//    public static final String RULESPACE = "rulespace";
//    public static final String SIDECAR = "sidecar";
    public static final String FILE_EXT=".json";
    public static final String RENAME_FILE_EXT=".delete";
    
 
 	
 	//private static final String ruleGrpMetaDataFileName = "PraeceptaRuleGrpMetaData.csv";
 	
    /**
     * Fetch the list of files under the composite key folder
     * @param key
     * @param version
     * @param type
     * @param folderName
     * @param fileName
     * @return
     */
    protected List<File> getFilesByCompositeKeyAndType(PraeceptaRuleSpaceCompositeKey key, String version, String type, String folderName, String fileName){

        LOG.debug("inside getFilesByCompositeKeyAndType");
        LOG.info("Composite Key {} : ", key.toString());
        LOG.info("Version {}, File Type {} ", version, type);

        List<File> filesList = new ArrayList<>();
        File metdDataFile = new File(folderName+ "\\"+fileName);
        try {

            List<Map<String,Object>> metaDataList =  GsonHelper.toEntity(PraeceptaFileUtil.readFile(metdDataFile), List.class);
           if(!PraeceptaObjectHelper.isObjectEmpty(metaDataList)) {
           for(Map<String,Object> metaDataMap : metaDataList){

               PraeceptaRuleSpaceMetadata metaData =  GsonHelper.toEntity(GsonHelper.toJson(metaDataMap), PraeceptaRuleSpaceMetadata.class);

               PraeceptaRuleSpaceCompositeKey compositeKey = metaData.getKey();

               if(key.getSpaceName() == null){
                   getFilesByType(type, filesList, metaData);
               }else if(key.getSpaceName().equalsIgnoreCase(compositeKey.getSpaceName())){
                   if(key.getClientId().equalsIgnoreCase(compositeKey.getClientId())){
                       if(key.getAppName().equalsIgnoreCase(compositeKey.getAppName())){
                           if(version == null || version.equalsIgnoreCase(compositeKey.getVersion())){
                               getFilesByType(type, filesList, metaData);
                           }
                       }
                   }
               }
           }
           }
        }catch(Exception e){
            LOG.error("Failed to read file : {}", e);
        }

        return filesList;
    }

    /**
     * Method to get file list under the composite key folder by type (rulespace, rulegroup or sidecar).
     * @param type
     * @param filesList
     * @param metaData
     */
    private static void getFilesByType(String type, List<File> filesList, PraeceptaRuleSpaceMetadata metaData) {
    	
    	if(type != null) {
	    	
    		FILE_TYPE fileType = FILE_TYPE.valueOf(FILE_TYPE.class, type);
	    	
	        if(FILE_TYPE.GRP == fileType){
	        	getActiveFiles(metaData.getRuleGroupPath(), filesList);
	        }else  if(FILE_TYPE.SPACE == fileType){
	        	getActiveFiles(metaData.getRuleSpacePath(), filesList);
	        }else if(FILE_TYPE.SIDECAR == fileType){
	        	getActiveFiles(metaData.getSideCarPath(), filesList);
	        }
    	}
    }
    
	private static void getActiveFiles(String filePath, List<File> filesList) {

		if (Files.exists(Paths.get(filePath))) {

			File[] exisitngFiles = new File(filePath).listFiles();

			for (File file : exisitngFiles) {
				if (!file.getName().toLowerCase().endsWith(RENAME_FILE_EXT)) {
					filesList.add(file);
				}
			}
		}
	}
	
	public static Predicate<String> getActiveFilePredicate(String fileNameToFilter) {
		
		 return (fileName) -> fileName.contains(fileNameToFilter) && !fileName.contains(PraeceptaWindowsDbDao.RENAME_FILE_EXT);
	}
    
}
