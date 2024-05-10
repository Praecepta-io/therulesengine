package io.praecepta.rules.hub.dbbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateConverterDao;

public class PraeceptaRuleSpaceMySQLDao implements IPraeceptaRuleSpaceDao {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleSpaceMySQLDao.class);
    
	@Autowired
	private PraeceptaHibernateConverterDao converterDao;

//	public PraeceptaRuleSpaceMySQLDao() {
//		this.converterDao = converterDao;
//		logger.debug("Initialized Db Based RuleSpace Dao");
//	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchAll() {
		logger.debug("Inside fetchAll rule space");
		return converterDao.fetchAllRuleSpaces();
	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if (PraeceptaObjectHelper.isObjectEmpty(ids)) {
			logger.debug("Rule space composite keys found empty/null");

		}
		Collection<PraeceptaRuleSpace> ruleSpaceList = new ArrayList<>();
		ids.stream().forEach(objKey -> {
			List<PraeceptaRuleSpace> ruleSpaceObjects = converterDao.fetchRuleSpaceByKey(objKey);
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceObjects)) {
				ruleSpaceList.addAll(ruleSpaceObjects);
			}
		});
		logger.debug("Rule space Objects list  {}", ruleSpaceList.size());
		return ruleSpaceList;
	}

	@Override
	public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("inside  findByExample rule space found null");
		}
		logger.debug("before getting rule space list with  findByExample");
		return converterDao.findRuleSpaceByExample(entity);
	}

	@Override
	public void insert(PraeceptaRuleSpace entity) {
		logger.debug("before  inserting rule space");
		converterDao.saveUpdateRuleSpace(entity);
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleSpace> entities) {
		logger.debug("inside insertAll rule spaces");
		converterDao.saveUpdateAllRuleSpace(entities);
		logger.debug("Done inserting all rule spaces");
	}

	@Override
	public void update(PraeceptaRuleSpace entity) {
		logger.debug("before  updating rule space");
		converterDao.updateRuleSpace(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleSpace> entities) {
		logger.debug("inside  updating all rule spaces");
		converterDao.updateAllRuleSpace(entities);
	}

	@Override
	public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		logger.debug("inside  delete rule space by composite key");
		converterDao.deleteRuleSpaceById(id);
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		logger.debug("inside  delete rule space by composite keys list");
		converterDao.deleteByRuleSpaceIds(ids);
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleSpace> entities) {
		logger.debug("inside  delete rule space by composite key");
		converterDao.deleteAllRuleSpace(entities);
	}
	@Override
	public boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		return converterDao.deleteRuleSpaceByKeyAndVersion(key, version);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(version)) {
			logger.debug("inside fetchByKeyAndVersion ruleSpaceKey-{},version-{}", key.toString(), version);
			key.setVersion(version);
			List<PraeceptaRuleSpace> listRuleSpace = converterDao.fetchRuleSpaceByKey(key);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleSpace)) {
				logger.debug("Rule space list size-{}", listRuleSpace.size());
				// returning first element from the list as the list contains only one element
				return listRuleSpace.get(0);
			}
		}

		return null;
	}

	@Override
	public List<PraeceptaRuleSpace> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {
		if (!PraeceptaObjectHelper.isObjectNull(key)) {
			logger.debug("inside fetchByKey ruleSpaceKey-{}", key.toString());
			return converterDao.fetchRuleSpaceByKey(key);
		}
		return Collections.EMPTY_LIST;
	}
}
