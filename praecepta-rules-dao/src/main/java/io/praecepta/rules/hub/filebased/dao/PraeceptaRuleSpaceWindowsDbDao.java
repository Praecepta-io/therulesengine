package io.praecepta.rules.hub.filebased.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;
import io.praecepta.rules.hub.filebased.util.PraeceptaFileUtil;

public class PraeceptaRuleSpaceWindowsDbDao extends  PraeceptaWindowsDbDao implements  IPraeceptaRuleSpaceDao {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleSpaceWindowsDbDao.class);
			
	private PraeceptaRuleTernaryFileDbDao ternaryFileDbDao;
	
	public PraeceptaRuleSpaceWindowsDbDao(String folderName, String metaDataFileName) {
		ternaryFileDbDao = PraeceptaRuleTernaryFileDbDao.getInstanceOfTernaryFileDbDao(folderName, metaDataFileName);
	}
	
	public PraeceptaRuleSpaceWindowsDbDao(){
	}

	@Override
	public void insert(PraeceptaRuleSpace entity) {
		ternaryFileDbDao.upsertRuleSpace(entity);
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleSpace> entities) {
		ternaryFileDbDao.upsertAllRuleSpace(entities);
	}

	@Override
	public void update(PraeceptaRuleSpace entity) {
		ternaryFileDbDao.upsertRuleSpace(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleSpace> entities) {
		ternaryFileDbDao.upsertAllRuleSpace(entities);
	}
	
	@Override
    public void delete(PraeceptaRuleSpace ruleSpace) {
        deleteById(ruleSpace.getRuleSpaceKey());
    }
	
	@Override
    public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		ternaryFileDbDao.deleteRuleSpaceById(id);
    }

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		ternaryFileDbDao.deleteByRuleSpaceIds(ids);
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleSpace> entities) {
		ternaryFileDbDao.deleteAllRuleSpace(entities);
	}
	
	
	@Override
    public boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
        key.setVersion(version);
        deleteById(key);
        return true;
    }

	@Override
	public void clear() {
	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchAll() {

		Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeSpaceFileDetails = ternaryFileDbDao.getActiveSpaceFilePathDetails();

		Collection<PraeceptaRuleSpace> praeceptaSpaceList = new ArrayList<>();

		activeSpaceFileDetails.forEach((sapceKey, versionAndSpacePath) -> {

			versionAndSpacePath.forEach((version, spacePath) -> {
				
				try {
					
					List<String> fileSpaceNameList = PraeceptaFileUtil.readAllFilesAbsolutPathsInDirectoryWithAFormat(spacePath, 
							PraeceptaWindowsDbDao.getActiveFilePredicate(FILE_TYPE.SPACE.fileName));
					
					
					fileSpaceNameList.forEach( eachSpaceFile -> {
						try {
							praeceptaSpaceList.add(GsonHelper.toEntity(PraeceptaFileUtil.readFile(eachSpaceFile), PraeceptaRuleSpace.class));
						} catch (IOException e) {
							LOG.error("Exception occurred while fetching Rule Space File  ", e);
						}
					});
					
				} catch (Exception e) {
					LOG.error("Exception occurred while fetching Rule Space Files From Path {} ", e);
				}

			});
		});

		return praeceptaSpaceList;

	}

	@Override
	public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		
		PraeceptaRuleSpace spaceToReturn =  null;
		
		Map<PraeceptaRuleSpaceCompositeKey, Map<String, String>> activeSpaceFileDetails = ternaryFileDbDao.getActiveSpaceFilePathDetails();
		
		if (!PraeceptaObjectHelper.isObjectEmpty(activeSpaceFileDetails)) {

			Map<String, String> versionToSpacePathMap = activeSpaceFileDetails.get(key);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToSpacePathMap)) {
				
				String spacePath = versionToSpacePathMap.get(version);
				LOG.info(" Space Path - {} Received for Version {} ", spacePath, version);

				if (!PraeceptaObjectHelper.isObjectEmpty(spacePath)) {
					try {
						key.setVersion(version);

						List<String> spaceFileContents = PraeceptaFileUtil.readAllFilesInDirectoryWithAFormat(
								spacePath,
								PraeceptaWindowsDbDao.getActiveFilePredicate(ternaryFileDbDao.getSpaceFileName(key)));

						if (!PraeceptaObjectHelper.isObjectEmpty(spaceFileContents)) {

							spaceToReturn = GsonHelper.toEntity(spaceFileContents.get(0), PraeceptaRuleSpace.class);
						}
					} catch (Exception e) {
						LOG.error("Exception occurred while fetching Rule Space Info {} ", e);
					}
				}
			} else {
				LOG.warn("There is no Space Key exist with details {} ", key);
			}
		} else {
			LOG.warn("There is no Acive Space Keys Available");
		}
		return spaceToReturn;
	}

	@Override
	public List<PraeceptaRuleSpace> fetchByKey(PraeceptaRuleSpaceCompositeKey id) {

		if(id == null || id.getVersion() == null) {
			throw new UnsupportedOperationException("Method is not supported if Id and version are empty");
		}
		
		PraeceptaRuleSpace ruleSpace = fetchByKeyAndVersion(id, id.getVersion());
		
		if(ruleSpace != null) {
			
			List<PraeceptaRuleSpace> spaceList = new ArrayList<>(1);
			spaceList.add(ruleSpace);
			
			return spaceList;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		throw new UnsupportedOperationException("Fetch By Ids is not supported. Instead Use fetch by key and version method");
	}

	@Override
	public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace entity) {
		throw new UnsupportedOperationException("Fetch By Example is not supported. Instead Use fetch by key and version");
	}

}
