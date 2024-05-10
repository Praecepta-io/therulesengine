package io.praecepta.rules.hub.filebased.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;
import io.praecepta.rules.hub.filebased.util.PraeceptaFileUtil;

public class PraeceptaRuleGroupWindowsDbDao extends PraeceptaWindowsDbDao implements  IPraeceptaRuleGroupDao{

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaWindowsDbDao.class);

//	GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());

	private PraeceptaRuleTernaryFileDbDao ternaryFileDbDao;

	public PraeceptaRuleGroupWindowsDbDao(String folderName, String metaDataFileName) {
		ternaryFileDbDao = PraeceptaRuleTernaryFileDbDao.getInstanceOfTernaryFileDbDao(folderName, metaDataFileName);
	}
	
	public PraeceptaRuleGroupWindowsDbDao(){}

	@Override
	public void insert(PraeceptaRuleGroup entity) {
		ternaryFileDbDao.upsertRuleGroup(entity);
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleGroup> entities) {
		ternaryFileDbDao.upsertAllRuleGroup(entities);
	}

	@Override
	public void update(PraeceptaRuleGroup entity) {
		ternaryFileDbDao.upsertRuleGroup(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleGroup> entities) {
		ternaryFileDbDao.upsertAllRuleGroup(entities);
	}

	@Override
	public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		LOG.debug("inside  delete rule group by composite key");
		ternaryFileDbDao.deleteRuleGroupByKey(id);
	}

	@Override
	public void delete(PraeceptaRuleGroup entity) {
		LOG.debug("inside  delete rule group by rule group object");
		ternaryFileDbDao.deleteRuleGroup(entity);
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		LOG.debug("inside  delete rule group by rule space keys");
		ternaryFileDbDao.deleteRuleGroupsByRuleSpaceKeys(ids);
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleGroup> entities) {
		LOG.debug("inside  delete all rule groups  by rule group objects");
		ternaryFileDbDao.deleteAllRuleGroups(entities);
	}
	
	@Override
	public boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		return ternaryFileDbDao.deleteRuleGroupByKeyAndVersion(key, version);
	}
	
	@Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			LOG.debug("inside deleteByKeyAndName ruleSpaceKey-{}", key.toString());

			ternaryFileDbDao.deleteRuleGroupdByKeyAndName(key, ruleGroupName);
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchAll() {
		
		Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeGrpFileDetails = ternaryFileDbDao.getActiveRuleGrpFilePathDetails();
		
		Collection<PraeceptaRuleGroup> praeceptaRuleGroupList = new ArrayList<>();
		
		activeGrpFileDetails.forEach((sapceKey, versionAndGrpPath) -> {

			versionAndGrpPath.forEach((version, grpPath) -> {

				extractRuleGroupsForAGrpPath(praeceptaRuleGroupList, grpPath);
			});
		});
		
		return praeceptaRuleGroupList;
	}

	@Override
	public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		
		PraeceptaRuleGroup ruleGrpToReturn = null;
		
		String ruleGrpPath = getRuleGroupPathForAKey(key);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpPath)) {
			
			try {
				
				List<String> grpFileContents = 
						PraeceptaFileUtil.readAllFilesInDirectoryWithAFormat(ruleGrpPath, 
								PraeceptaWindowsDbDao.getActiveFilePredicate(ternaryFileDbDao.getRuleGrpFileName(key, ruleGroupName)));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(grpFileContents)) {
					
					ruleGrpToReturn = GsonHelper.toEntity(grpFileContents.get(0), PraeceptaRuleGroup.class);
				}
			} catch (Exception e) {
				LOG.error("Exception occurred while fetching Rule Grp Info {} ", e);
			}
		}
		return ruleGrpToReturn;
	}
	
	@Override
	public List<PraeceptaRuleGroup> fetchByKey(PraeceptaRuleSpaceCompositeKey id) {
		
		if(id == null || id.getVersion() == null) {
			throw new UnsupportedOperationException("Method is not supported if Id and version are empty");
		}
		
		List<PraeceptaRuleGroup> praeceptaRuleGroupList = new ArrayList<>();
		
		String ruleGrpPath = getRuleGroupPathForAKey(id);
		
		extractRuleGroupsForAGrpPath(praeceptaRuleGroupList, ruleGrpPath);
		
		return praeceptaRuleGroupList;
	}

	private String getRuleGroupPathForAKey(PraeceptaRuleSpaceCompositeKey key) {
		
		String ruleGrpPath = "";
		
		if (key != null || key.getVersion() != null) {
			
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeGrpFileDetails = ternaryFileDbDao
					.getActiveRuleGrpFilePathDetails();

			ruleGrpPath = activeGrpFileDetails.entrySet().stream().filter(entry -> entry.getKey().equals(key) && entry.getValue().get(key.getVersion()) != null )
					.map(e -> e.getValue().get(key.getVersion()))
					.findFirst()
					.orElse("");
		}
		
		return ruleGrpPath;
	}

	private void extractRuleGroupsForAGrpPath(Collection<PraeceptaRuleGroup> praeceptaRuleGroupList, String grpPath) {
		try {
			
			List<String> fileGrpNameList = PraeceptaFileUtil.readAllFilesAbsolutPathsInDirectoryWithAFormat(grpPath, 
					PraeceptaWindowsDbDao.getActiveFilePredicate(FILE_TYPE.GRP.fileName));
			
			
			fileGrpNameList.forEach( eachGrpFile -> {
				try {
					praeceptaRuleGroupList.add(GsonHelper.toEntity(PraeceptaFileUtil.readFile(eachGrpFile), PraeceptaRuleGroup.class));
				} catch (IOException e) {
					LOG.error("Exception occurred while fetching Rule Group File  ", e);
				}
			});
			
		} catch (Exception e) {
			LOG.error("Exception occurred while fetching Rule Group Files From Path {} ", e);
		}
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		throw new UnsupportedOperationException("Fetch By Ids is not supported. Instead Use fetch by key, version and Group Name");
	}

	@Override
	public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup entity) {
		throw new UnsupportedOperationException("Fetch By Example is not supported. Instead Use fetch by key, version and Group Name");
	}

}
