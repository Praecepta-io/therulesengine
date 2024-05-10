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
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;
import io.praecepta.rules.hub.filebased.util.PraeceptaFileUtil;

public class PraeceptaSidecarWindowsDbDao extends PraeceptaWindowsDbDao implements IPraeceptaRuleSideCarsDao {

    private static final Logger LOG = LoggerFactory.getLogger(PraeceptaSidecarWindowsDbDao.class);
    
//    GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());

    private PraeceptaRuleTernaryFileDbDao ternaryFileDbDao;

    public PraeceptaSidecarWindowsDbDao(String folderName, String metaDataFileName) {
        ternaryFileDbDao = PraeceptaRuleTernaryFileDbDao.getInstanceOfTernaryFileDbDao(folderName, metaDataFileName);
    }
    @Override
    public PraeceptaSideCarsInfo fetchById(PraeceptaRuleSpaceCompositeKey id) {
        return null;
    }

    @Override
    public void insert(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
    	ternaryFileDbDao.upsertSidecars(praeceptaRuleSideCars);
    }

    @Override
    public void insertAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
    	ternaryFileDbDao.upsertAllSidecars(praeceptaRuleSideCars);
    }

    @Override
    public void update(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
    	ternaryFileDbDao.upsertSidecars(praeceptaRuleSideCars);
    }

    @Override
    public void updateAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
    	ternaryFileDbDao.upsertAllSidecars(praeceptaRuleSideCars);
    }

    @Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		LOG.debug("inside  delete rule side cars by key and rule group name");
		ternaryFileDbDao.deleteRuleSideCarsByKeyAndName(key, ruleGroupName);
		return false;
	}
    @Override
    public void delete(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
    	LOG.debug("inside  delete rule side cars");
		ternaryFileDbDao.deleteRuleSideCar(praeceptaRuleSideCars);
    }

    @Override
    public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
    	LOG.debug("inside  delete rule side cars by list of composite key");
		ternaryFileDbDao.deleteRuleSideCarsByRuleSpaceKeys(ids);
    }

    @Override
    public void deleteAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
    	LOG.debug("inside  delete all rule side cars");
		ternaryFileDbDao.deleteAllRuleSideCars(praeceptaRuleSideCars);
    }
    
    @Override
	public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		LOG.debug("inside  delete rule side cars by composite key");
		ternaryFileDbDao.deleteRuleSideCarsByKey(id);
	}

    @Override
    public void clear() {

    }
    

    @Override
    public Collection<PraeceptaSideCarsInfo> fetchAll() {
    	
    	Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeSidecarFileDetails = ternaryFileDbDao.getActiveSidecarFilePathDetails();
    	
        Collection<PraeceptaSideCarsInfo> sideCarList = new ArrayList<>();
        
        activeSidecarFileDetails.forEach((sapceKey, versionAndSidecarPath) -> {

			versionAndSidecarPath.forEach((version, sidecarPath) -> {
				
				extractRuleSidecarsForASidecarPath(sideCarList, sidecarPath);

			});
		});
        
        return sideCarList;
    }
	
    @Override
	public PraeceptaSideCarsInfo fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		PraeceptaSideCarsInfo sideCarToReturn = null;

		String sidecarPath = getSidecarPathForAKey(key);

		try {

			List<String> sidecarFileContents = PraeceptaFileUtil.readAllFilesInDirectoryWithAFormat(sidecarPath,
					PraeceptaWindowsDbDao
							.getActiveFilePredicate(ternaryFileDbDao.getRuleSidecarFileName(key, ruleGroupName)));

			if (!PraeceptaObjectHelper.isObjectEmpty(sidecarFileContents)) {

				sideCarToReturn = GsonHelper.toEntityPreserveNumber(sidecarFileContents.get(0),
						PraeceptaSideCarsInfo.class);
			}
			;

		} catch (Exception e) {
			LOG.error("Exception occurred while fetching Rule Sidecar info {} ", e);
		}

		return sideCarToReturn;
	}
    
    @Override
	public List<PraeceptaSideCarsInfo> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {
    	
    	if(key == null || key.getVersion() == null) {
			throw new UnsupportedOperationException("Method is not supported if Id and version are empty");
		}
    	
    	List<PraeceptaSideCarsInfo> sideCarList = new ArrayList<>();
    	
    	String sidecarPath = getSidecarPathForAKey(key);
    	
    	extractRuleSidecarsForASidecarPath(sideCarList, sidecarPath);
    	
    	return sideCarList;
	}

	private String getSidecarPathForAKey(PraeceptaRuleSpaceCompositeKey key) {
		
		String sidecarPath = "";
		
		if (key != null || key.getVersion() != null) {
			
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeSidecarFileDetails = ternaryFileDbDao
					.getActiveSidecarFilePathDetails();

			sidecarPath = activeSidecarFileDetails.entrySet().stream()
					.filter(entry -> entry.getKey().equals(key) && entry.getValue().get(key.getVersion()) != null)
					.map(e -> e.getValue().get(key.getVersion())).findFirst().orElse("");
		}
		
		return sidecarPath;
	}
	
	private void extractRuleSidecarsForASidecarPath(Collection<PraeceptaSideCarsInfo> sideCarList,
			String sidecarPath) {
		try {
			
			List<String> fileSidecarNameList = PraeceptaFileUtil.readAllFilesAbsolutPathsInDirectoryWithAFormat(sidecarPath, 
					PraeceptaWindowsDbDao.getActiveFilePredicate(FILE_TYPE.SIDECAR.fileName));
			
			
			fileSidecarNameList.forEach( eachSidecarFile -> {
				try {
					sideCarList.add(GsonHelper.toEntityPreserveNumber(PraeceptaFileUtil.readFile(eachSidecarFile),
							PraeceptaSideCarsInfo.class));
				} catch (IOException e) {
					LOG.error("Exception occurred while fetching Rule Sidecar File  ", e);
				}
			});
			
		} catch (Exception e) {
			LOG.error("Exception occurred while fetching Rule Sidecar Files From Path {} ", e);
		}
	}
    
    @Override
    public Collection<PraeceptaSideCarsInfo> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
    	throw new UnsupportedOperationException("Fetch By Ids is not supported. Instead Use fetch by key, version and Group Name");
    }

    @Override
    public Collection<PraeceptaSideCarsInfo> findByExample(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
    	throw new UnsupportedOperationException("Fetch By Example is not supported. Instead Use fetch by key, version and Group Name");
    }
}
