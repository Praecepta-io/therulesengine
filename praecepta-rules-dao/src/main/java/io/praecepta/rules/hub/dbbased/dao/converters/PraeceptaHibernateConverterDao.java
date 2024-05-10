package io.praecepta.rules.hub.dbbased.dao.converters;

import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.convertModelToRuleGroup;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.convertModelToRuleSideCars;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.convertToRuleGroupDbModel;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.getIdsListByKey;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.getLstRuleGroupIdsByKeyAndName;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.getRuleSpaceList;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.populateRuleGroupDbModel;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.populateRuleSideCarDbModel;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.prepareKeyRuleSpaceIdMap;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.prepareListRuleSpaceIdsByKey;
import static io.praecepta.rules.hub.dao.helper.ConverterDaoHelper.prepareRuleSpaceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.hibernate.dao.HibernateDao;
import io.praecepta.rules.hub.dao.helper.ConverterDaoHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleGroupDbModel;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSideCarsDbModel;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSpaceDbModel;

@Transactional
public class PraeceptaHibernateConverterDao {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaHibernateConverterDao.class);

	private final PraeceptaHibernateRuleSpaceDao ruleSpaceHibernateDao;

	private final PraeceptaHibernateRuleGroupDao ruleGroupHibernateDao;
	
	private final PraeceptaHibernateRuleSideCarsDao ruleSideCarsHibernateDao;
	
	private static final Map<String, Long> ruleSpaceIdByCompositeKeyMap = new ConcurrentHashMap<>();
	private static final Map<String, Long> ruleGrpIdByKeyAndGrpNameMap = new ConcurrentHashMap<>();
	private static final Map<String, Long> ruleSideCarIdByKeyAndGrpNameMap = new ConcurrentHashMap<>();

	public PraeceptaHibernateConverterDao(PraeceptaHibernateRuleSpaceDao ruleSpaceHibernateDao,
			PraeceptaHibernateRuleGroupDao ruleGroupHibernateDao,PraeceptaHibernateRuleSideCarsDao ruleSideCarsHibernateDao) {
		this.ruleSpaceHibernateDao = ruleSpaceHibernateDao;
		this.ruleGroupHibernateDao = ruleGroupHibernateDao;
		this.ruleSideCarsHibernateDao=ruleSideCarsHibernateDao;
		logger.debug("Done initializing Hibernate converter Dao");
		loadCacheMap();
	}

	/* method to fetch rule space models and convert them to rule space dtos */
	public Collection<PraeceptaRuleSpace> fetchAllRuleSpaces() {
		logger.debug("Inside get all rule spaces");
		Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels = ruleSpaceHibernateDao.fetchAllRuleSpaceModels();
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceModels)) {
			logger.debug("ruleSpaceModels size-{}", ruleSpaceModels.size());
			// converting rule space models to rule space dtos
			return getRuleSpaceList(ruleSpaceModels);
		}
		return Collections.EMPTY_LIST;
	}

	/*
	 * method to fetch rule space model by using rule space composite key and
	 * converting to rule space dto
	 */
	public List<PraeceptaRuleSpace> fetchRuleSpaceByKey(PraeceptaRuleSpaceCompositeKey key) {
		logger.debug("inside fetchRuleSpaces By Key-{} ", key.toString());
		// getting rule space models from DB by rule space compositeKey
		Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels = fetchByRuleSpaceKey(key);
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceModels)) {
			logger.debug("ruleSpaceModels size-{}", ruleSpaceModels.size());
			List<PraeceptaRuleSpace> ruleSpaces= getRuleSpaceList(ruleSpaceModels);
			
			
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaces)) {

				ruleSpaces.stream().forEach(objRuleSpace -> {

					List<PraeceptaRuleGroup> ruleGrps = fetchRuleGroupByKeyAndName(objRuleSpace.getRuleSpaceKey(),
							null);

					if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrps)) {
						logger.info(" Rule Groups also Presetn in DB for Key {} and Version {}", key, key.getVersion());
						objRuleSpace.setPraeceptaRuleGrps(ruleGrps);
					}

				});

			}
			return ruleSpaces;
		}
		return Collections.EMPTY_LIST;
	}

	/* method to fetch rule space models by rule space example */
	public List<PraeceptaRuleSpace> findRuleSpaceByExample(PraeceptaRuleSpace ruleSpace) {

		logger.debug("inside findRuleSpaceByExample By Key-{} ", ruleSpace.toString());

		PraeceptaRuleSpaceDbModel ruleSpaceModel = prepareRuleSpaceModel(ruleSpace,ruleSpaceIdByCompositeKeyMap);

		// getting rule space models from DB by rule space entity
		Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels = ruleSpaceHibernateDao.findByExample(ruleSpaceModel);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceModels)) {
			logger.debug("inside findRuleSpaceByExample, ruleSpaceModels size-{}", ruleSpaceModels.size());
			return getRuleSpaceList(ruleSpaceModels);
		}
		return Collections.EMPTY_LIST;
	}

	public void saveUpdateRuleSpace(PraeceptaRuleSpace entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity) || PraeceptaObjectHelper.isObjectNull(entity.getRuleSpaceKey())) {
			logger.debug("Rule space/rule space key found null");
			return;
		}
		logger.debug("inside saveUpdate rule space");
		PraeceptaRuleSpaceDbModel ruleSpaceModel = prepareRuleSpaceModel(entity,ruleSpaceIdByCompositeKeyMap);
		ruleSpaceHibernateDao.update(ruleSpaceModel);
		
		logger.debug("Done save/update rule space");
		
		loadRuleSpaceCacheMap();
	}

	public void saveUpdateAllRuleSpace(Collection<PraeceptaRuleSpace> entities) {
		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("Rule space entities found empty");
			return;
		}

		List<PraeceptaRuleSpaceDbModel> ruleSpaceModelsList = new ArrayList<>();
		logger.debug("before save/update all RuleSpace");
		entities.stream().forEach(objRuleSpace -> {
			PraeceptaRuleSpaceDbModel ruleSpaceModel = prepareRuleSpaceModel(objRuleSpace,ruleSpaceIdByCompositeKeyMap);
			ruleSpaceModelsList.add(ruleSpaceModel);
		});
		logger.debug("Before inserting All RuleSpaceModels");
		ruleSpaceHibernateDao.updateAll(ruleSpaceModelsList);
		loadRuleSpaceCacheMap();
	}

	public void updateRuleSpace(PraeceptaRuleSpace entity) {
		logger.debug("inside update rule space");
		saveUpdateRuleSpace(entity);
		logger.debug("Done updating rule space");
		loadRuleSpaceCacheMap();
	}

	public void updateAllRuleSpace(Collection<PraeceptaRuleSpace> entities) {
		saveUpdateAllRuleSpace(entities);
		logger.debug("Done updating all rule spaces");
		loadRuleSpaceCacheMap();
	}

	/*
	 * method to delete rule space and rule groups defined to it based on input rule
	 * space key
	 */
	public void deleteRuleSpaceById(PraeceptaRuleSpaceCompositeKey id) {
		if (PraeceptaObjectHelper.isObjectNull(id)) {
			logger.debug("inside deleteRuleSpaceById- Rule space composite key found empty/null");
		}

		Long ruleSpaceId = ruleSpaceIdByCompositeKeyMap.get(prepareKeyRuleSpaceIdMap(id));

		if (!PraeceptaObjectHelper.isObjectNull(ruleSpaceId)) {
			// to delete rule group mappings
			deleteRuleGroupdByKeyAndVersion(id, id.getVersion());
			ruleSpaceHibernateDao.deleteById(ruleSpaceId);
		}
		logger.debug("Done delete rule space by key-{}", id);
		loadRuleSpaceCacheMap();
	}

	/*
	 * method to iterate over the list of keys and delete rule space and rule groups
	 * defined for this space by rule space key
	 */
	public void deleteByRuleSpaceIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if (PraeceptaObjectHelper.isObjectEmpty(ids)) {
			logger.debug("inside deleteByRuleSpaceIds- Rule space composite keys found empty/null");
		}
		List<Long> ruleSpaceIdsToDelete = new ArrayList<>();
		
		ids.stream().forEach(objRuleSpaceKey -> {
			prepareListRuleSpaceIdsByKey(objRuleSpaceKey, ruleSpaceIdsToDelete,ruleSpaceIdByCompositeKeyMap);
		});
		deleteRuleGroupdByRuleSpaceKeys(ids);
		deleteRuleSpaceByIds(ruleSpaceIdsToDelete);
		
		logger.debug("done  deleteByRuleSpaceIds");
	}

	/*
	 * method to delete rule space and rule groups for given list of rule space
	 * objects
	 */
	public void deleteAllRuleSpace(Collection<PraeceptaRuleSpace> entities) {
		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("inside deleteAllRuleSpace- Rule space list found empty/null");
		}
		List<Long> ruleSpaceIdsToDelete = new ArrayList<>();
		Collection<PraeceptaRuleSpaceCompositeKey> ruleSpaceKeys=new ArrayList<>();
		entities.stream().forEach(objRuleSpace -> {
			if (!PraeceptaObjectHelper.isObjectNull(objRuleSpace.getRuleSpaceKey())) {
				ruleSpaceKeys.add(objRuleSpace.getRuleSpaceKey());
				prepareListRuleSpaceIdsByKey(objRuleSpace.getRuleSpaceKey(), ruleSpaceIdsToDelete,ruleSpaceIdByCompositeKeyMap);
			}
		});
		deleteRuleGroupdByRuleSpaceKeys(ruleSpaceKeys);
		deleteRuleSpaceByIds(ruleSpaceIdsToDelete);
		logger.debug("done  deleteAllRuleSpaces");
	}

	/* method to delete rule space by rule space key and version */
	public boolean deleteRuleSpaceByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		if (PraeceptaObjectHelper.isObjectNull(key) || PraeceptaObjectHelper.isStringEmpty(version)) {
			logger.debug("inside deleteByKeyAndVersion ruleSpaceKey/version found empty ");
			return false;
		}

		Long ruleSpaceIdToDelete = ruleSpaceIdByCompositeKeyMap.get(prepareKeyRuleSpaceIdMap(key, version));

		if (!PraeceptaObjectHelper.isObjectNull(ruleSpaceIdToDelete)) {
			logger.debug("Getting rule groups for rule space Id: {}", ruleSpaceIdToDelete);
			// to get and delete rule groups defined for rule space
			deleteRuleGroupdByKeyAndVersion(key, version);
			// deleting rule space by id
			logger.debug("Before deleting  rule space with Id: {}", ruleSpaceIdToDelete);
			ruleSpaceHibernateDao.deleteById(ruleSpaceIdToDelete);
			loadRuleSpaceCacheMap();
		}
		return true;
	}

	public void deleteRuleGroupdByRuleSpaceKeys(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		ids.stream().forEach(objRuleSpaceKey -> {
			deleteRuleGroupdByKeyAndVersion(objRuleSpaceKey, objRuleSpaceKey.getVersion());
		});
	}

	public void deleteRuleGroupdByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {

		if (!PraeceptaObjectHelper.isStringEmpty(version)) {
			key.setVersion(version);
		}
		List<Long> ruleGroupIdsToDelete = getIdsListByKey(key, ruleGrpIdByKeyAndGrpNameMap);

		deleteEntityByIds(ruleGroupIdsToDelete,ruleGroupHibernateDao);
		
		loadRuleGroupCacheMap();
	}

	private boolean deleteEntityByIds(List<Long> idsToDelete, HibernateDao dao) {
		if (!PraeceptaObjectHelper.isObjectEmpty(idsToDelete)) {
			// deleting the entities  by ids
			logger.debug("Before deleting entities by Ids list: {}", idsToDelete);
			dao.deleteByIds(idsToDelete);
		}
		return true;
	}

	private void deleteRuleSpaceByIds(List<Long> ruleSpaceIdsToDelete) {
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceIdsToDelete)) {
			logger.debug("before  deleting rule space info for ids {}", ruleSpaceIdsToDelete);
			ruleSpaceHibernateDao.deleteByIds(ruleSpaceIdsToDelete);
			loadRuleSpaceCacheMap();
		}
	}

	/* method to fetch rule group models by rule space key and rule group name */
	public List<PraeceptaRuleGroup> fetchRuleGroupByKeyAndName(PraeceptaRuleSpaceCompositeKey key,
			String ruleGroupName) {
		logger.debug("inside fetchRuleGroupByKeyAndName By Key-{},RuleGroupName -{} ", key.toString(), ruleGroupName);

		// getting rule group models from DB by rule space compositeKey
		Collection<PraeceptaRuleGroupDbModel> ruleGroupModels = fetchByKeyAndRuleGroupName(key,ruleGroupName);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupModels)) {
			logger.debug("ruleGroupModels size-{}", ruleGroupModels.size());
			return ruleGroupModels.stream().map(objModel -> {
				return convertModelToRuleGroup(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}

	/* method fetch all active rule groups */
	public Collection<PraeceptaRuleGroup> getAllRuleGroups() {
		logger.debug("Inside get all rule groups");
		Collection<PraeceptaRuleGroupDbModel> ruleGroupModels = ruleGroupHibernateDao.fetchAllRuleGroups();
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupModels)) {
			logger.debug("ruleGroupModels size-{}", ruleGroupModels.size());
			// converting rule group models to rule group dtos
			return ruleGroupModels.stream().map(objModel -> {
				return convertModelToRuleGroup(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}

	public List<PraeceptaRuleGroup> findRuleGroupByExample(PraeceptaRuleGroup ruleGroup) {

		logger.debug("inside findRuleGroupByExample By Key-{} ", ruleGroup.toString());

		PraeceptaRuleGroupDbModel ruleGroupModelToQuery = populateRuleGroupDbModel(ruleGroup);

		// getting rule group models from DB by rule group entity
		Collection<PraeceptaRuleGroupDbModel> ruleGroupDbModels = ruleGroupHibernateDao
				.findByExample(ruleGroupModelToQuery);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupDbModels)) {
			logger.debug("inside findRuleGroupByExample, ruleGroupModels size-{}", ruleGroupDbModels.size());
			return ruleGroupDbModels.stream().map(objModel -> {
				return convertModelToRuleGroup(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
	
	public void saveOrUpdateRuleGroup(PraeceptaRuleGroup entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("Rule Group found null");
			return;
		}
		logger.debug("inside saveOrUpdate  rule group");
		PraeceptaRuleGroupDbModel ruleGroupModel = convertToRuleGroupDbModel(entity,ruleSpaceIdByCompositeKeyMap,ruleGrpIdByKeyAndGrpNameMap);
		
		if (!PraeceptaObjectHelper.isObjectNull(ruleGroupModel)) {
			logger.debug("before  adding rule group model");
			ruleGroupHibernateDao.update(ruleGroupModel);
		}
		
		logger.debug("Done saveOrUpdate rule group");
		
		loadRuleGroupCacheMap();
	}

	public void saveOrUpdateAllRuleGroups(Collection<PraeceptaRuleGroup> entities) {
		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("Rule Group objects found null/empty");
			return;
		}

		logger.debug("inside saveOrUpdate all rule groups");
		Collection<PraeceptaRuleGroupDbModel> ruleGroupModels = entities.stream().map(objRuleGroup -> {
			return convertToRuleGroupDbModel(objRuleGroup,ruleSpaceIdByCompositeKeyMap,ruleGrpIdByKeyAndGrpNameMap);
		}).collect(Collectors.toList());

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupModels)) {
			logger.debug("before  inserting all rule group models");
			ruleGroupHibernateDao.updateAll(ruleGroupModels);
		}
		logger.debug("Done saveOrUpdate all rule groups");
		loadRuleGroupCacheMap();
	}
	
	public void deleteRuleGroup(PraeceptaRuleGroup entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("inside deleteRuleGroup- Rule group found null");
		}

		deleteRuleGroupdByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupName());

		logger.debug("done  delete RuleGroup");
	}
	
	public boolean deleteRuleGroupdByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		if (PraeceptaObjectHelper.isObjectNull(key) || PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			logger.debug("inside deleteRuleGroupdByKeyAndName ruleSpaceKey/ruleGroupName found empty ");
			return false;
		}

		List<Long> ruleGroupIdsToDelete = getLstRuleGroupIdsByKeyAndName(key, ruleGroupName,
				ruleGrpIdByKeyAndGrpNameMap);

		boolean deleteStatus = deleteEntityByIds(ruleGroupIdsToDelete, ruleGroupHibernateDao);

		loadRuleGroupCacheMap();

		return deleteStatus;
		
	}
	public void deleteAllRuleGroups(Collection<PraeceptaRuleGroup> entities) {
		if(PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("inside deleteAllRuleGroups- Rule groups list found empty/null");
		}
		
		entities.stream().forEach(objRuleGroup->{
			deleteRuleGroupdByKeyAndName(objRuleGroup.getRuleSpaceKey(), objRuleGroup.getRuleGroupName());
		});
		
		logger.debug("done  deleteAllRuleGroups");
	}
	public void deleteRuleGroupdByKey(PraeceptaRuleSpaceCompositeKey key) {
		deleteRuleGroupdByKeyAndVersion(key,null);
	}
	private void loadCacheMap() {
		logger.debug("loading Rule Space ,Rule Group Models and Map cache");
		loadRuleSpaceCacheMap();
		loadRuleGroupCacheMap();
		loadRuleSideCarsCacheMap();
	}
	
	/*method to fetch rule space db models by  praeceptaRuleSpaceCompositeKey*/ 
	public Collection<PraeceptaRuleSpaceDbModel> fetchByRuleSpaceKey(PraeceptaRuleSpaceCompositeKey key) {
		
		logger.debug("inside  fetchByRuleSpaceKey {}",key);
		
		return ruleSpaceHibernateDao.fetchAllRuleSpaceModels().stream().filter(ConverterDaoHelper.matchRuleSpaceKey(key)).collect(Collectors.toList());
	}
	
	
	
	/*method to fetch rule group db models by  praeceptaRuleSpaceCompositeKey and rule group name*/ 
	public Collection<PraeceptaRuleGroupDbModel> fetchByKeyAndRuleGroupName(PraeceptaRuleSpaceCompositeKey key,String ruleGrpName) {
		
		logger.debug("inside  fetchByKey {} And RuleGroupName {}",key,ruleGrpName);
		
		return ruleGroupHibernateDao.fetchAllRuleGroups().stream().filter(ConverterDaoHelper.matchKeyAndRuleGroupName(ruleGrpName,key)).collect(Collectors.toList());
	}
	
	private void loadRuleGroupCacheMap() {
		logger.debug("loading Rule Group Models and Map cache");
		this.ruleGroupHibernateDao.fetchAll();
		ruleGroupHibernateDao.fetchAllRuleGroups().stream().forEach(objModel -> {
			ruleGrpIdByKeyAndGrpNameMap.put(ConverterDaoHelper.getKeyRuleGroupIdCompositeKeyMap(objModel),
					objModel.getId());
		});
	}
	
	private void loadRuleSpaceCacheMap() {
		logger.debug("loading Rule Space db models and Map cache");
		this.ruleSpaceHibernateDao.fetchAll();
		ruleSpaceHibernateDao.fetchAllRuleSpaceModels().stream().forEach(objModel->{
			ruleSpaceIdByCompositeKeyMap.put(ConverterDaoHelper.getKeyRuleSpaceIdCompositeKeyMap(objModel),
					objModel.getId());
		});
	}
	
	// For Rule Side Cars
	
	
	private void loadRuleSideCarsCacheMap() {
		logger.debug("loading Rule Side Car Models and Map cache");
		this.ruleSideCarsHibernateDao.fetchAll();
		ruleSideCarsHibernateDao.fetchRuleSideCars().stream().forEach(objModel -> {
			ruleSideCarIdByKeyAndGrpNameMap.put(ConverterDaoHelper.getRuleSideCarsIdMapKeyMap(objModel),
					objModel.getId());
		});
	}
	
	/* method fetch all active rule side cars */
	public Collection<PraeceptaSideCarsInfo> getAllRuleSideCars() {
		logger.debug("Inside get all rule groups");
		Collection<PraeceptaRuleSideCarsDbModel> ruleSideCarModels = ruleSideCarsHibernateDao.fetchRuleSideCars();
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarModels)) {
			logger.debug("ruleSideCarModels size-{}", ruleSideCarModels.size());
			// converting rule side car models to rule side car dtos
			return ruleSideCarModels.stream().map(objModel -> {
				return convertModelToRuleSideCars(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
	
	/* method to fetch rule side car models by rule space key and rule group name */
	public List<PraeceptaSideCarsInfo> fetchRuleSideCarsByKeyAndName(PraeceptaRuleSpaceCompositeKey key,
			String ruleGroupName) {
		logger.debug("inside fetchRuleSideCarsByKeyAndName By Key-{},RuleGroupName -{} ", key.toString(), ruleGroupName);

		// getting rule side cars models from DB by rule space compositeKey
		Collection<PraeceptaRuleSideCarsDbModel> ruleSideCarsModels = fetchSideCarsByKeyAndRuleGroupName(key,ruleGroupName);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarsModels)) {
			logger.debug("ruleSideCarModels size-{}", ruleSideCarsModels.size());
			
			return ruleSideCarsModels.stream().map(objModel->{
				return convertModelToRuleSideCars(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
	
	/*method to fetch rule group db models by  praeceptaRuleSpaceCompositeKey and rule group name*/ 
	public Collection<PraeceptaRuleSideCarsDbModel> fetchSideCarsByKeyAndRuleGroupName(PraeceptaRuleSpaceCompositeKey key,String ruleGrpName) {
		
		logger.debug("inside  fetchByKey {} And RuleGroupName {}",key,ruleGrpName);
		
		return ruleSideCarsHibernateDao.fetchRuleSideCars().stream().filter(ConverterDaoHelper.matchSidecarsByKeyAndRuleGrpName(ruleGrpName,key)).collect(Collectors.toList());
	}
	
	public List<PraeceptaSideCarsInfo> findRuleSideCarsByExample(PraeceptaSideCarsInfo ruleSideCar) {

		logger.debug("inside findRuleSideCarsByExample By Key-{} ", ruleSideCar.toString());

		PraeceptaRuleSideCarsDbModel ruleSideCarModelToQuery = populateRuleSideCarDbModel(ruleSideCar);

		// getting rule side cars models from DB by rule side car entity
		Collection<PraeceptaRuleSideCarsDbModel> ruleSideCarModels = ruleSideCarsHibernateDao.findByExample(ruleSideCarModelToQuery);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarModels)) {
			logger.debug("inside findRuleSideCarsByExample, ruleSideCarModels size-{}", ruleSideCarModels.size());
			return ruleSideCarModels.stream().map(objModel -> {
				return convertModelToRuleSideCars(objModel);
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
	
	public void saveOrUpdateRuleSideCar(PraeceptaSideCarsInfo entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("Rule SideCar entity found null");
			return;
		}
		logger.debug("inside saveOrUpdate  rule side car");
		PraeceptaRuleSideCarsDbModel ruleSideCarModel = ConverterDaoHelper.convertToRuleSideCarDbModel(entity,ruleSideCarIdByKeyAndGrpNameMap);
		
		if (!PraeceptaObjectHelper.isObjectNull(ruleSideCarModel)) {
			logger.debug("before  adding rule side car model");
			ruleSideCarsHibernateDao.update(ruleSideCarModel);
		}
		
		logger.debug("Done saveOrUpdate rule side car");
		
		loadRuleSideCarsCacheMap();
	}

	public void saveOrUpdateAllRuleSideCars(Collection<PraeceptaSideCarsInfo> entities) {
		if (PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("Rule Side Car objects found null/empty");
			return;
		}

		logger.debug("inside saveOrUpdate all rule side cars");
		Collection<PraeceptaRuleSideCarsDbModel> ruleSideCarModels = entities.stream().map(objSideCar -> {
			return ConverterDaoHelper.convertToRuleSideCarDbModel(objSideCar,ruleSideCarIdByKeyAndGrpNameMap);
		}).collect(Collectors.toList());

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarModels)) {
			logger.debug("before  inserting all rule side car models");
			ruleSideCarsHibernateDao.updateAll(ruleSideCarModels);
		}
		logger.debug("Done saveOrUpdate all rule side cars");
		loadRuleSideCarsCacheMap();
	}
	
	public void deleteRuleSideCarsByKey(PraeceptaRuleSpaceCompositeKey key) {
		deleteRuleSideCarsByKeyAndVersion(key,null);
	}
	
	public void deleteRuleSideCar(PraeceptaSideCarsInfo entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("inside deleteRuleSideCar- Rule Side Car found null");
		}

		deleteRuleSideCarsByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupSideCars().getRuleGrpName());

		logger.debug("done  delete Rule Side Car");
	}
	public void deleteRuleSideCarsByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {

		if (!PraeceptaObjectHelper.isStringEmpty(version)) {
			key.setVersion(version);
		}
		List<Long> ruleSideCarIdsToDelete = getIdsListByKey(key, ruleSideCarIdByKeyAndGrpNameMap);
		deleteEntityByIds(ruleSideCarIdsToDelete,ruleSideCarsHibernateDao);
		loadRuleSideCarsCacheMap();
	}
	
	public boolean deleteRuleSideCarsByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		if (PraeceptaObjectHelper.isObjectNull(key) || PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			
			logger.debug("inside deleteRuleSideCarsByKeyAndName ruleSpaceKey/ruleGroupName found empty ");
			return false;
		}

		List<Long> ruleGroupIdsToDelete = getLstRuleGroupIdsByKeyAndName(key, ruleGroupName,
				ruleSideCarIdByKeyAndGrpNameMap);

		boolean deleteStatus = deleteEntityByIds(ruleGroupIdsToDelete, ruleSideCarsHibernateDao);

		loadRuleSideCarsCacheMap();

		return deleteStatus;
	}
	
	public void deleteRuleSideCarsByRuleSpaceKeys(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		ids.stream().forEach(objRuleSpaceKey -> {
			deleteRuleSideCarsByKeyAndVersion(objRuleSpaceKey, objRuleSpaceKey.getVersion());
		});
	}
	
	public void deleteAllRuleSideCars(Collection<PraeceptaSideCarsInfo> entities) {
		if(PraeceptaObjectHelper.isObjectEmpty(entities)) {
			logger.debug("inside deleteAllRuleSideCars- Rule Side Car list found empty/null");
		}
		
		entities.stream().forEach(objRuleGroup->{
			deleteRuleSideCarsByKeyAndName(objRuleGroup.getRuleSpaceKey(), objRuleGroup.getRuleGroupSideCars().getRuleGrpName());
		});
		
		logger.debug("done  deleteAllRuleSideCars");
	}
}
