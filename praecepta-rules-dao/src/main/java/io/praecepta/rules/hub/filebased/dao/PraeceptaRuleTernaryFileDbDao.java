package io.praecepta.rules.hub.filebased.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.intf.IModel;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceMetadata;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.filebased.util.PraeceptaFileUtil;

public class PraeceptaRuleTernaryFileDbDao{
//	public class PraeceptaRuleTernaryFileDbDao extends PraeceptaWindowsDbDao{
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleTernaryFileDbDao.class);
	//private String location;
	
	// TestSpaceName_TestClientId_TestAppName_TestVersion
	private static final String ruleSpaceFileNamePattern = "%1$s_%2$s_%3$s_%4$s_praecepta";
	
	// TestSpaceName_TestClientId_TestAppName_TestVersion_GrpName
	private static final String ruleGrpFileNamePattern = "%1$s_%2$s_%3$s_%4$s_%5$s_praecepta";
	
	// TestSpaceName_TestClientId_TestAppName_TestVersion_GrpName
	private static final String ruleSidecarFileNamePattern = "%1$s_%2$s_%3$s_%4$s_%5$s_praecepta";
	
	// Map<PraeceptaRuleSpaceCompositeKey, Map<VERSION, SPACE FILE PATH>>
	private static final Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> compoisteKeyToVersionAndSpaceFileNameMap = new HashMap<>();
	
	// Map<PraeceptaRuleSpaceCompositeKey, Map<VERSION, GROUP FILE PATH>>
	private static final Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> compoisteKeyToVersionAndGrpFileNameMap = new HashMap<>();
	
	// Map<PraeceptaRuleSpaceCompositeKey, Map<VERSION, SIDECAR FILE PATH>>
	private static final Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> compositeKeyToVersionAndSidecarFileNameMap = new HashMap<>();
	
//	GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());
	
	private String fileLocation;
	
	private String fileNameSuffix;
	
	private static PraeceptaRuleTernaryFileDbDao ternaryFileDbDao = new PraeceptaRuleTernaryFileDbDao();
	
	static boolean bootStrapActiveMetaData;
	
	private PraeceptaRuleTernaryFileDbDao() {
	}
	
	public static PraeceptaRuleTernaryFileDbDao getInstanceOfTernaryFileDbDao(String fileLocation, String fileNameSuffix) {
		ternaryFileDbDao.fileLocation = fileLocation;
		ternaryFileDbDao.fileNameSuffix = fileNameSuffix;
		return ternaryFileDbDao;
	}
	
	public enum FILE_TYPE{
		SPACE("rulespace"), GRP("rulegroup"), SIDECAR("sidecar");
		
		private FILE_TYPE(String fileName) {
			this.fileName = fileName;
		}
		
		String fileName;
	}
	
	private List<PraeceptaRuleSpaceMetadata> getMetaDataFileList() {
		LOG.debug("inside getMetaDataFileList" );

		final List<PraeceptaRuleSpaceMetadata> metaDataList = new ArrayList<>();
		
		List<String> metaDataFilesContent;
		try {
			
			LOG.debug("Get Meta Data File List for File Location {} and Meta Data File Name Suffix {}", fileLocation, fileNameSuffix );
			// Get Only Meta Data Files that doesn't have Delete extension in it. This will make sure to pull only Active Meta Data Files
			metaDataFilesContent = PraeceptaFileUtil.readAllFilesInDirectoryWithAFormat(fileLocation, PraeceptaWindowsDbDao.getActiveFilePredicate(fileNameSuffix));

			if(!PraeceptaObjectHelper.isObjectEmpty(metaDataFilesContent)) {
				
				metaDataFilesContent.stream().forEach( eachFilesContent -> {
					metaDataList.add(GsonHelper.toEntity(eachFilesContent, PraeceptaRuleSpaceMetadata.class));
				});
			}
			
		} catch (Exception e) {
			LOG.error("Exception While Getting the Meta Data ");
		}

		return metaDataList;
	}
	
	void bootstrapActiveMetaDataFiles() {
		LOG.debug("inside bootstrapActiveMetaDataFiles" );
		
		if(!bootStrapActiveMetaData) {
		
			LOG.debug("Bootstrapping the Active Meta Data Files" );
			
			List<PraeceptaRuleSpaceMetadata> metaDataList = getMetaDataFileList();
			
			LOG.debug("Active Meta Data Files Size is {} ", metaDataList.size() );
			
			captureRuleSpacePathDetails(metaDataList);
			captureRuleGroupPathDetails(metaDataList);
			captureRuleSidecarsPathDetails(metaDataList);
		}
		
	}

	private void loadRuleSpaceMetaDataFileInfo() {

		List<PraeceptaRuleSpaceMetadata> metaDataList = getMetaDataFileList();

		captureRuleSpacePathDetails(metaDataList);
	}

	private void captureRuleSpacePathDetails(List<PraeceptaRuleSpaceMetadata> metaDataList) {
		LOG.debug(" Inside captureRuleSpacePathDetails");
		
		for (PraeceptaRuleSpaceMetadata metaData : metaDataList) {

			LOG.debug(" PraeceptaRuleSpaceMetadata to add to Cache {} ", metaData);

			if (!PraeceptaObjectHelper.isObjectEmpty(metaData.getKey())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getKey().getVersion())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getRuleSpacePath())) {

				compoisteKeyToVersionAndSpaceFileNameMap.putIfAbsent(metaData.getKey(), new HashMap<>());

				compoisteKeyToVersionAndSpaceFileNameMap.get(metaData.getKey())
						.putIfAbsent(metaData.getKey().getVersion(), metaData.getRuleSpacePath());

			}

		}
		
		LOG.debug(" Exiting captureRuleSpacePathDetails");
	}

	private void loadRuleGroupMetaDataFileInfo() {

		List<PraeceptaRuleSpaceMetadata> metaDataList = getMetaDataFileList();

		captureRuleGroupPathDetails(metaDataList);
	}

	private void captureRuleGroupPathDetails(List<PraeceptaRuleSpaceMetadata> metaDataList) {
		LOG.debug(" Inside captureRuleGroupPathDetails");
		
		for (PraeceptaRuleSpaceMetadata metaData : metaDataList) {

			if (!PraeceptaObjectHelper.isObjectEmpty(metaData.getKey())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getKey().getVersion())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getRuleGroupPath())) {

				compoisteKeyToVersionAndGrpFileNameMap.putIfAbsent(metaData.getKey(), new HashMap<>());

				compoisteKeyToVersionAndGrpFileNameMap.get(metaData.getKey())
						.putIfAbsent(metaData.getKey().getVersion(), metaData.getRuleGroupPath());

			}
		}
		
		LOG.debug(" Exiting captureRuleGroupPathDetails");
	}

	private void loadRuleSideCarMetaDataFileInfo() {

		List<PraeceptaRuleSpaceMetadata> metaDataList = getMetaDataFileList();

		captureRuleSidecarsPathDetails(metaDataList);
	}

	private void captureRuleSidecarsPathDetails(List<PraeceptaRuleSpaceMetadata> metaDataList) {
		LOG.debug(" Inside captureRuleSidecarsPathDetails");
		
		for (PraeceptaRuleSpaceMetadata metaData : metaDataList) {

			if (!PraeceptaObjectHelper.isObjectEmpty(metaData.getKey())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getKey().getVersion())
					&& !PraeceptaObjectHelper.isObjectEmpty(metaData.getSideCarPath())) {

				compositeKeyToVersionAndSidecarFileNameMap.putIfAbsent(metaData.getKey(), new HashMap<>());

				compositeKeyToVersionAndSidecarFileNameMap.get(metaData.getKey())
						.putIfAbsent(metaData.getKey().getVersion(), metaData.getSideCarPath());
			}
		}
		
		LOG.debug(" Exiting captureRuleSidecarsPathDetails");
	}
	
	/**
	 * @param entity
	 */
	/*Method to create/update rule space file*/
	protected void upsertRuleSpace(PraeceptaRuleSpace entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity) || PraeceptaObjectHelper.isObjectNull(entity.getRuleSpaceKey())) {
			LOG.info("Rule space/rule space key found null");
			return;
		}
		LOG.debug("inside upsertRuleSpace");
		
//		List<PraeceptaRuleSpaceMetadata> ruleSpaceMetaDataFileList=new ArrayList<>();
		PraeceptaRuleSpaceMetadata ruleSpaceMetaDataFileWithDetails = new PraeceptaRuleSpaceMetadata();
		
		//Creating file
		upsertFile(ruleSpaceMetaDataFileWithDetails, entity, FILE_TYPE.SPACE);
		
		LOG.debug("Done save/update rule space");
		
		//logic to create/update metaDataFile after upserting rulespaec
//		checkAndUpdateFileMetadata(ruleSpaceMetaDataFileList);
		checkAndUpdateFileMetadata(ruleSpaceMetaDataFileWithDetails, FILE_TYPE.SPACE);
	}
	
	/**
	 * @param entities
	 */
	/*Method to upsert all rule spaces*/
	protected void upsertAllRuleSpace(Collection<PraeceptaRuleSpace> entities) {

		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.info("Rule space entities found null");
			return;
		}
		
		//creating files by iterating over the list of objects
		entities.forEach(ruleSpaceObj -> upsertRuleSpace(ruleSpaceObj));

		LOG.info("Done upserting All ruleSpace files");
		
	}
	
	/**
	 * @param entity
	 */
	/*method to create/update rule group file*/
	protected void upsertRuleGroup(PraeceptaRuleGroup entity) {
		
		if (PraeceptaObjectHelper.isObjectNull(entity) || PraeceptaObjectHelper.isObjectNull(entity.getRuleSpaceKey())) {
			LOG.info("Rule Group/composite key found null");
			return;
		}
		LOG.debug("inside upsertRuleGroup");

		PraeceptaRuleSpaceMetadata ruleSpaceMetaDataFileWithDetails = new PraeceptaRuleSpaceMetadata();
        
		//creating rule group file
		upsertFile(ruleSpaceMetaDataFileWithDetails, entity, FILE_TYPE.GRP);

		LOG.debug("Done save/update rule group");

		//logic to create/update metaDataFile after upserting rule groups 
		checkAndUpdateFileMetadata(ruleSpaceMetaDataFileWithDetails, FILE_TYPE.GRP);
	}
	
	/**
	 * @param entities
	 */
	/*method to create/update rule group files for given list of entities*/
	protected void upsertAllRuleGroup(Collection<PraeceptaRuleGroup> entities) {

		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.info("Rule Group entities found null");
			return;
		}
		
		entities.forEach(ruleGrpObj -> upsertRuleGroup(ruleGrpObj));

		LOG.info("Done upserting All ruleGroup files");
		
	}
	
	/*method to create/update sidecars for rule group*/
	protected void upsertSidecars(PraeceptaSideCarsInfo entity) {
		
		if (PraeceptaObjectHelper.isObjectNull(entity) || PraeceptaObjectHelper.isObjectNull(entity.getRuleSpaceKey())|| PraeceptaObjectHelper.isObjectNull(entity.getRuleGroupSideCars())) {
			LOG.info("Rule Group Sidecars/composite key found null");
			return;
		}
		LOG.debug("inside upsertSidecars for Sidecar{} :",entity);
		
		PraeceptaRuleSpaceMetadata ruleSpaceMetaDataFileWithDetails = new PraeceptaRuleSpaceMetadata();
		
		//writing to file
		upsertFile(ruleSpaceMetaDataFileWithDetails, entity, FILE_TYPE.SIDECAR);
		
		
		LOG.debug("Done save/update Sidecars ");
		//logic to create/update metaDataFile after upserting side cars 
		checkAndUpdateFileMetadata(ruleSpaceMetaDataFileWithDetails, FILE_TYPE.SIDECAR);
	}
	
	/**
	 * @param entities
	 */
	/*method to create/update sidecars for the list of entities*/
	protected void upsertAllSidecars(Collection<PraeceptaSideCarsInfo> entities) {

		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.info("Sidecar objects found null");
			return;
		}
		
		// create/update ruleSidecar files
		entities.forEach(sideCarObj -> upsertSidecars(sideCarObj));
		
		LOG.info("Done upserting All Sidecar files");
		
	}
	
    /*Method to update the metaData file with newly inserted/updated files*/
	private void checkAndUpdateFileMetadata(List<PraeceptaRuleSpaceMetadata> metaDataFileListToUpdate, FILE_TYPE fileType) {
    
		if(!PraeceptaObjectHelper.isObjectEmpty(metaDataFileListToUpdate)) {
			
			metaDataFileListToUpdate.forEach( eachMetaDataFile -> checkAndUpdateFileMetadata(eachMetaDataFile, fileType) );
		}
	}
	
	private void writeUpdatedMetaDataFileInfo(PraeceptaRuleSpaceMetadata ruleSpaceMetadataFromFile,
			boolean metaDataFileUpdateNeeded) {
		
		if(metaDataFileUpdateNeeded) {
			
			LOG.debug("Meta Data File Update is needed");
			//overwriting the file with latest metaData
			String metaDataFilePath = getMetaDataFilePath(ruleSpaceMetadataFromFile);
			
			PraeceptaFileUtil.writeToFile(metaDataFilePath, GsonHelper.toJson(ruleSpaceMetadataFromFile));
		}
	}
	
	
	/**
	 * @param <T>
	 * @param ruleSpaceMetaDataFileList
	 * @param entity
	 * @param type
	 */
	/*Method to write data to the file*/
//	private void upsertFile(List<PraeceptaRuleSpaceMetadata> ruleSpaceMetaDataFileList, IModel entity, FILE_TYPE type) {
	private void upsertFile(PraeceptaRuleSpaceMetadata ruleSpaceMetaData, IModel entity, FILE_TYPE type) {
		String dirPath = null;
   
		String fileName = null;
		
//		PraeceptaRuleSpaceMetadata fileMetaData =new PraeceptaRuleSpaceMetadata();
		//Logic to generate rootDirectory path and file name based on the type
		if(type == FILE_TYPE.SPACE) {
			
			dirPath = getDirectoryPath(((PraeceptaRuleSpace) entity).getRuleSpaceKey(), type.fileName, fileLocation);
			fileName = getSpaceFileName(((PraeceptaRuleSpace) entity).getRuleSpaceKey());
			ruleSpaceMetaData.setKey(((PraeceptaRuleSpace) entity).getRuleSpaceKey());    
			ruleSpaceMetaData.setRuleSpacePath(dirPath);
			
		}else if (type == FILE_TYPE.GRP) {
			
			dirPath = getDirectoryPath(((PraeceptaRuleGroup) entity).getRuleSpaceKey(), type.fileName, fileLocation);
			fileName = getRuleGrpFileName(((PraeceptaRuleGroup) entity).getRuleSpaceKey(),((PraeceptaRuleGroup) entity).getRuleGroupName());
			ruleSpaceMetaData.setKey(((PraeceptaRuleGroup) entity).getRuleSpaceKey());    
			ruleSpaceMetaData.setRuleGroupPath(dirPath);
			
		}else if (type == FILE_TYPE.SIDECAR) {
			
			dirPath = getDirectoryPath(((PraeceptaSideCarsInfo) entity).getRuleSpaceKey(), type.fileName, fileLocation);
			fileName = getRuleSidecarFileName(((PraeceptaSideCarsInfo) entity).getRuleSpaceKey(),((PraeceptaSideCarsInfo) entity).getRuleGroupSideCars().getRuleGrpName());
			ruleSpaceMetaData.setKey(((PraeceptaSideCarsInfo) entity).getRuleSpaceKey());    
			ruleSpaceMetaData.setSideCarPath(dirPath);
		}
		
		if (!PraeceptaObjectHelper.isStringEmpty(dirPath) && !PraeceptaObjectHelper.isStringEmpty(fileName)) {
			
			String filePath = dirPath + File.separator + fileName;
            //writing data to file
			
			String contentToWrite = "";
			
			if(type == FILE_TYPE.SPACE) {
				contentToWrite = GsonHelper.toJson(entity);
				
			} else if(type == FILE_TYPE.GRP || type == FILE_TYPE.SIDECAR) {
				contentToWrite = GsonHelper.toJson(entity);
			}
			PraeceptaFileUtil.writeToFile(filePath, contentToWrite);
			//adding updated fileMetaData object to the list
//			ruleSpaceMetaDataFileList.add(ruleSpaceMetaData);
		}
	}

	/*Method to get Folder path from Space Key and type*/
    /**
     * @param key
     * @param type
     * @param folderName
     * @return
     */
    private String getDirectoryPath(PraeceptaRuleSpaceCompositeKey key,String type, String folderName) {
    	
    	StringBuilder directoryPathbuilder=new StringBuilder(folderName);
    	
		directoryPathbuilder.append(File.separator)
		        .append(key.getSpaceName()).append(File.separator)
				.append(key.getClientId()).append(File.separator)
				.append(key.getAppName());
		
		if(!PraeceptaObjectHelper.isObjectEmpty(key.getVersion())) {
			directoryPathbuilder.append(File.separator).append(key.getVersion());
		}
		if(!PraeceptaObjectHelper.isObjectEmpty(type)) {
			directoryPathbuilder.append(File.separator).append(type);
		}
    	return directoryPathbuilder.toString();
    }

    /*
	 * method to rename rule space folders  and rule groups defined to it based on input rule
	 * space key
	 */
    protected void deleteRuleSpaceById(PraeceptaRuleSpaceCompositeKey id) {
		if (PraeceptaObjectHelper.isObjectNull(id)) {
			LOG.info("inside deleteRuleSpaceById- Rule space composite key found empty/null");
		}
		
		//Deleting folder for rule space and files inside it
		deleteFileByType(id, FILE_TYPE.SPACE, null);
		
		LOG.debug("Done delete rule space by key-{}", id);
		
		//To remove metaDataFile content for rule spaceKey entries
		checkAndDeleteFileMetadataEntry(id, FILE_TYPE.SPACE);
		
		deleteRuleSpaceFileMetadataForAKey(id);
		
	}


	/*
	 * method to iterate over the list of keys and rename rule space folders and rule groups
	 * defined for this key
	 */
	protected void deleteByRuleSpaceIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if (PraeceptaObjectHelper.isObjectEmpty(ids)) {
			LOG.debug("inside deleteByRuleSpaceIds- Rule space composite keys found empty/null");
		}
		
        //renaming folder for each key
		ids.stream().forEach(objRuleSpaceKey -> {
			deleteRuleSpaceById(objRuleSpaceKey);
		});
	
		LOG.debug("done  deleteByRuleSpace by List of Ids");
		
	}

	/*
	 * method to rename rule space and rule groups for given list of rule space
	 * objects
	 */
	protected void deleteAllRuleSpace(Collection<PraeceptaRuleSpace> entities) {
		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.debug("inside deleteAllRuleSpace- Rule space list found empty/null");
		}

		entities.stream().forEach(objRuleSpace -> {
			if (!PraeceptaObjectHelper.isObjectNull(objRuleSpace.getRuleSpaceKey())) {
				deleteRuleSpaceById(objRuleSpace.getRuleSpaceKey());
			}
		});

		LOG.debug("done  deleteAllRuleSpaces");
		
	}
	
	protected void deleteRuleGroupdByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		
		if (PraeceptaObjectHelper.isObjectNull(key) || PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			LOG.debug("inside deleteRuleGroupdByKeyAndName ruleSpaceKey/ruleGroupName found empty ");
			return ;
		}
		//deleting the file for ruleGroup and space key
		deleteFileByType(key, FILE_TYPE.GRP, ruleGroupName);
		
		//To remove metaDataFile content for rule spaceKey entries
		checkAndDeleteFileMetadataEntry(key, FILE_TYPE.GRP);
	}
	
	/**
	 * @param entity
	 */
	/*method to rename rule group file by rule group entitiy*/
	protected void deleteRuleGroup(PraeceptaRuleGroup entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			LOG.debug("inside deleteRuleGroup- Rule group found null");
		}

		deleteRuleGroupdByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupName());

		LOG.debug("Done  delete RuleGroup");
	}
	
	/**
	 * @param entities
	 */
	/*method to delete all rule groups by renaming files*/
	protected void deleteAllRuleGroups(Collection<PraeceptaRuleGroup> entities) {
		if(PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.info("inside deleteAllRuleGroups- Rule groups list found empty/null");
		}
		
		//deleting rule group files by rule group name
		entities.stream().forEach(objRuleGroup->{
			deleteRuleGroupdByKeyAndName(objRuleGroup.getRuleSpaceKey(), objRuleGroup.getRuleGroupName());
		});
		
		LOG.info("done  delete All RuleGroups");
	}

	/**
	 * @param key
	 * @param version
	 * @return
	 */
	/*method to rename rule group files for space key and version*/
	protected boolean deleteRuleGroupByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {

		throw new UnsupportedOperationException("Delete Rule Group by Key and Version is Not supported for Files. Alternatively, You can try with Delete Rule Groupd By Key And Name ");
	}
	
	
	/*method to delete by renaming all rule groups for given space key*/
	protected void deleteRuleGroupByKey(PraeceptaRuleSpaceCompositeKey key) {
		if (PraeceptaObjectHelper.isObjectNull(key)) {
			LOG.debug("inside deleteRuleGroupByKey- composite key found null");
		}
		deleteRuleGroupByKeyAndVersion(key,null);
		
		LOG.info("done  delete Rule groups for Key {}",key.toString());
	}
	
	/**
	 * @param ids
	 */
	/*method to delete all rule groups for given list of space keys*/
	protected void deleteRuleGroupsByRuleSpaceKeys(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if(PraeceptaObjectHelper.isObjectEmpty(ids)) {
			LOG.debug("inside deleteRuleGroupsByRuleSpaceKeys - Rule Side Car list found empty/null");
		}
		
		ids.stream().forEach(objRuleSpaceKey -> {
			deleteRuleSideCarsByKeyAndVersion(objRuleSpaceKey, objRuleSpaceKey.getVersion());
		});
		
		LOG.debug("done  deleteAllRuleSideCars");
	}
	
	public void deleteRuleSideCarsByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		if (PraeceptaObjectHelper.isObjectNull(key) || PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			
			LOG.debug("inside deleteRuleSideCarsByKeyAndName ruleSpaceKey/ruleGroupName found empty ");
			return;
		}
		deleteFileByType(key, FILE_TYPE.SIDECAR, ruleGroupName);
		
		//To remove metaDataFile content for rule spaceKey entries
		checkAndDeleteFileMetadataEntry(key, FILE_TYPE.SIDECAR);
	}
	
	public void deleteRuleSideCar(PraeceptaSideCarsInfo entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			LOG.debug("inside deleteRuleSideCar- Rule Side Car found null");
		}

		deleteRuleSideCarsByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupSideCars().getRuleGrpName());

		LOG.debug("done  delete Rule Side Car");
	}
	
	public void deleteAllRuleSideCars(Collection<PraeceptaSideCarsInfo> entities) {
		if(PraeceptaObjectHelper.isObjectEmpty(entities)) {
			LOG.debug("inside deleteAllRuleSideCars- Rule Side Car list found empty/null");
		}
		
		entities.stream().forEach(objRuleGroup->{
			deleteRuleSideCarsByKeyAndName(objRuleGroup.getRuleSpaceKey(), objRuleGroup.getRuleGroupSideCars().getRuleGrpName());
		});
		
		LOG.debug("done  deleteAllRuleSideCars");
	}
	
	public void deleteRuleSideCarsByKey(PraeceptaRuleSpaceCompositeKey key) {
		
		throw new UnsupportedOperationException("Delete Rule Group by Key and Version is Not supported for Files. Alternatively, You can try with Delete Rule Groupd By Key And Name ");
		
	}
	
	public void deleteRuleSideCarsByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {

		if (!PraeceptaObjectHelper.isStringEmpty(version)) {
			key.setVersion(version);
		}
		deleteRuleSideCarsByKeyAndVersion(key, version);
		
	}
	
	public void deleteRuleSideCarsByRuleSpaceKeys(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		ids.stream().forEach(objRuleSpaceKey -> {
			deleteRuleSideCarsByKeyAndVersion(objRuleSpaceKey, objRuleSpaceKey.getVersion());
		});
	}
	
	/*Method to deleteFile by renaming it to .delete for ruleGroup/Sidecars Type*/
	private void deleteFileByType(PraeceptaRuleSpaceCompositeKey id, FILE_TYPE type, String ruleGroupName) {
		
		String dirPath = getDirectoryPath(id, type.fileName, fileLocation);
		
		String fileName = null;
		
		if (type == FILE_TYPE.SPACE) {
			
			fileName =  getSpaceFileName(id);
			
		} else if (type == FILE_TYPE.GRP) {
			
			fileName = getRuleGrpFileName(id,ruleGroupName);
			
		} else if (type == FILE_TYPE.SIDECAR) {
			
			fileName = getRuleSidecarFileName(id,ruleGroupName);
			
		}
		
		if (!PraeceptaObjectHelper.isStringEmpty(dirPath) && !PraeceptaObjectHelper.isStringEmpty(fileName)) {
			
			String currentFilePath = String.join(File.separator,dirPath,fileName);
			
			String newfilePath = String.join("",currentFilePath, PraeceptaWindowsDbDao.RENAME_FILE_EXT);

			PraeceptaFileUtil.renameFile(currentFilePath, newfilePath);
			
		}
	}
	
	private void deleteRuleSpaceFileMetadataForAKey(PraeceptaRuleSpaceCompositeKey key) {
		
		String currentMetaDataFilePath = getMetaDataFilePathForASpaceKey(key);

		String newMetaDataFilePath = String.join("",currentMetaDataFilePath, PraeceptaWindowsDbDao.RENAME_FILE_EXT);
		
		PraeceptaFileUtil.renameFile(currentMetaDataFilePath, newMetaDataFilePath);
	}
	
	/*method to delete ruleSpace entries from metaData file*/
	private void deleteRuleSpaceFileMetadata(PraeceptaRuleSpaceMetadata metaDataFileToDelete) {
		
		deleteRuleSpaceFileMetadataForAKey(metaDataFileToDelete.getKey());
		
	}
	
	/*method to delete ruleSpace entries from metaData file*/
	private void deleteRuleSpaceFileMetadata(List<PraeceptaRuleSpaceMetadata> metaDataFileListToDelete) {
		
		metaDataFileListToDelete.stream().forEach( eachMetaDataFileToRename -> deleteRuleSpaceFileMetadata(eachMetaDataFileToRename) );
	}
	
	protected PraeceptaRuleSpaceMetadata fetchFileMetaData(String metaDataFilePath) {
		LOG.debug("inside fetchFileMetaData for meta data file path {} ", metaDataFilePath);
		File metaDataFile = new File(metaDataFilePath);
		
		try {
			
			LOG.debug("Check before Meta data file Exst or not ");
			if(!metaDataFile.exists()) {
				LOG.debug("Create Meta Data File  ");
				PraeceptaFileUtil.createFile(metaDataFile.toPath());
			}
			
			return GsonHelper.toEntity(PraeceptaFileUtil.readFile(metaDataFile), PraeceptaRuleSpaceMetadata.class);
			
		} catch (Exception e) {
			LOG.error("error while reading metaData file", e);
		}
		return null;
	}
	
	protected PraeceptaRuleSpaceMetadata fetchFileMetaData(PraeceptaRuleSpaceMetadata metaDataInfo) {
		
		String metaDataFilePath = getMetaDataFilePath(metaDataInfo);
		
		//reading the contents of metaDataFile if exists
		return fetchFileMetaData(metaDataFilePath);
	}

	private String getMetaDataFilePath(PraeceptaRuleSpaceMetadata metaDataInfo) {
		
		String metaDataFilePath = String.join(File.separator, fileLocation,  getMetaDataFileName(metaDataInfo.getKey()));
//		String metaDataFilePath = fileLocation + File.separator + getMetaDataFileName(metaDataInfo.getKey());
		
		return metaDataFilePath;
	}
	
	private String getMetaDataFilePathForASpaceKey(PraeceptaRuleSpaceCompositeKey key) {
		
		return String.join(File.separator, fileLocation, getMetaDataFileName(key));
		
	}
	
    private String getMetaDataFileName(PraeceptaRuleSpaceCompositeKey key) {
    	return String.format(ruleSpaceFileNamePattern, key.getSpaceName(), key.getClientId(), key.getAppName(), key.getVersion()).concat(fileNameSuffix);
    }
    
    String getSpaceFileName(PraeceptaRuleSpaceCompositeKey key) {
    	return String.format(ruleSpaceFileNamePattern, key.getSpaceName(), key.getClientId(), key.getAppName(), key.getVersion())
    			.concat(FILE_TYPE.SPACE.fileName).concat(PraeceptaWindowsDbDao.FILE_EXT);
    }
    
    String getRuleGrpFileName(PraeceptaRuleSpaceCompositeKey key,String ruleGrpName) {
    	return String.format(ruleGrpFileNamePattern, key.getSpaceName(), key.getClientId(), key.getAppName(), key.getVersion(), ruleGrpName)
    			.concat(FILE_TYPE.GRP.fileName).concat(PraeceptaWindowsDbDao.FILE_EXT);
    }
    
    String getRuleSidecarFileName(PraeceptaRuleSpaceCompositeKey key,String ruleGrpName) {
    	return String.format(ruleSidecarFileNamePattern, key.getSpaceName(), key.getClientId(), key.getAppName(), key.getVersion(),ruleGrpName)
    			.concat(FILE_TYPE.SIDECAR.fileName).concat(PraeceptaWindowsDbDao.FILE_EXT);
    }

	private void checkAndUpdateFileMetadata(PraeceptaRuleSpaceMetadata ruleSpaceMetadata, FILE_TYPE fileType) {
		
		LOG.debug("Inside checkAndUpdateFileMetadata ");
		//reading the contents of metaDataFile if exists
		PraeceptaRuleSpaceMetadata ruleSpaceMetadataFromFile = fetchFileMetaData(ruleSpaceMetadata);
		
		
		// Create/Insert The Metda Data File for the First Time
		if(ruleSpaceMetadataFromFile == null) {
			writeUpdatedMetaDataFileInfo(ruleSpaceMetadata, true);
		}
		
		// Update The Metda Data File based on the updated Data
		if(ruleSpaceMetadataFromFile != null) {
			boolean metaDataFileUpdateNeeded = false;
			
			// Adding Space Details 
			if(FILE_TYPE.SPACE == fileType) {
				LOG.debug("Inside checkAndUpdateFileMetadata with Space File Type");
				
				if(PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getRuleSpacePath())) {
					
					LOG.debug("Space Path is Empty in Meta Data File ");
					
					ruleSpaceMetadataFromFile.setRuleSpacePath(ruleSpaceMetadata.getRuleSpacePath());
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			// U
			if(FILE_TYPE.GRP == fileType) {
				
				if(PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getRuleGroupPath())) {
					
					ruleSpaceMetadataFromFile.setRuleGroupPath(ruleSpaceMetadata.getRuleGroupPath());
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			// U
			if(FILE_TYPE.SIDECAR == fileType) {
				
				if(PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getSideCarPath())) {
					
					ruleSpaceMetadataFromFile.setSideCarPath(ruleSpaceMetadata.getSideCarPath());
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			writeUpdatedMetaDataFileInfo(ruleSpaceMetadataFromFile, metaDataFileUpdateNeeded);
		}
	}
    
	private void checkAndDeleteFileMetadataEntry(PraeceptaRuleSpaceCompositeKey key, FILE_TYPE fileType) {
		
		PraeceptaRuleSpaceMetadata ruleSpaceMetadataFromFile = fetchFileMetaData(getMetaDataFilePathForASpaceKey(key));
		
		if(ruleSpaceMetadataFromFile != null) {
			boolean metaDataFileUpdateNeeded = false;
			
			// Adding Space Details 
			if(FILE_TYPE.SPACE == fileType) {
				
				// If we are deleting the Space, then, remove entries for Group and Side Car also from Meta Data file
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getRuleSpacePath())) {
					
					ruleSpaceMetadataFromFile.setRuleSpacePath(null);
					ruleSpaceMetadataFromFile.setRuleGroupPath(null);
					ruleSpaceMetadataFromFile.setSideCarPath(null);
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			// U
			if(FILE_TYPE.GRP == fileType) {
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getRuleGroupPath())) {
					
					ruleSpaceMetadataFromFile.setRuleGroupPath(null);
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			// U
			if(FILE_TYPE.SIDECAR == fileType) {
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceMetadataFromFile.getSideCarPath())) {
					
					ruleSpaceMetadataFromFile.setSideCarPath(null);
					
					metaDataFileUpdateNeeded = true;
				}
			}
			
			writeUpdatedMetaDataFileInfo(ruleSpaceMetadataFromFile, metaDataFileUpdateNeeded);
		}
		
	}
	
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> getActiveSpaceFilePathDetails() {
		ternaryFileDbDao.bootstrapActiveMetaDataFiles();
		return compoisteKeyToVersionAndSpaceFileNameMap;
	}
	
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> getActiveRuleGrpFilePathDetails() {
		ternaryFileDbDao.bootstrapActiveMetaDataFiles();
		return compoisteKeyToVersionAndGrpFileNameMap;
	}
	
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> getActiveSidecarFilePathDetails() {
		ternaryFileDbDao.bootstrapActiveMetaDataFiles();
		return compositeKeyToVersionAndSidecarFileNameMap;
	}
	
}
