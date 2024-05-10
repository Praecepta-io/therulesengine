package io.praecepta.rules.hub.dbbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateConverterDao;

public class PraeceptaRuleGroupMySQLDao implements IPraeceptaRuleGroupDao {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleGroupMySQLDao.class);
	
	@Autowired
	private PraeceptaHibernateConverterDao converterDao;

	@Override
	public PraeceptaRuleGroup fetchById(PraeceptaRuleSpaceCompositeKey id) {
		if (PraeceptaObjectHelper.isObjectEmpty(id)) {
			logger.debug("Rule space composite key found empty/null");
		}

		List<PraeceptaRuleGroup> listRuleGroups = converterDao.fetchRuleGroupByKeyAndName(id, null);

		if (!PraeceptaObjectHelper.isObjectEmpty(listRuleGroups)) {
			logger.debug("Rule groups list size -{} found for the key-{}", listRuleGroups.size(), id.toString());
			return listRuleGroups.get(0);
		}
		logger.debug("done fetch rule group by composite key Id ");
		return null;
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchAll() {
		return converterDao.getAllRuleGroups();
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if (PraeceptaObjectHelper.isObjectEmpty(ids)) {
			logger.debug("Rule space composite keys found empty/null");
		}

		Collection<PraeceptaRuleGroup> ruleGroups = new ArrayList();

		ids.stream().forEach(objKey -> {
			List<PraeceptaRuleGroup> listRuleGroups = converterDao.fetchRuleGroupByKeyAndName(objKey, null);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleGroups)) {
				ruleGroups.addAll(listRuleGroups);
			}
		});

		return ruleGroups;
	}

	@Override
	public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("inside  findByExample rule group found null");
		}
		logger.debug("before getting rule group list with  findByExample");
		return converterDao.findRuleGroupByExample(entity);
	}

	@Override
	public void insert(PraeceptaRuleGroup entity) {
		converterDao.saveOrUpdateRuleGroup(entity);
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleGroup> entities) {
		converterDao.saveOrUpdateAllRuleGroups(entities);
	}

	@Override
	public void update(PraeceptaRuleGroup entity) {
		converterDao.saveOrUpdateRuleGroup(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleGroup> entities) {
		converterDao.saveOrUpdateAllRuleGroups(entities);
	}

	@Override
	public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		logger.debug("inside  delete rule group by composite key");
		converterDao.deleteRuleGroupdByKey(id);
	}

	@Override
	public void delete(PraeceptaRuleGroup entity) {
		logger.debug("inside  delete rule group by rule group object");
		converterDao.deleteRuleGroup(entity);
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		logger.debug("inside  delete rule group by rule space keys");
		converterDao.deleteRuleGroupdByRuleSpaceKeys(ids);
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleGroup> entities) {
		logger.debug("inside  delete all rule groups  by rule group objects");
		converterDao.deleteAllRuleGroups(entities);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			logger.debug("inside fetchByKeyAndName ruleSpaceKey-{},RuleGroupName-{}", key.toString(), ruleGroupName);

			List<PraeceptaRuleGroup> listRuleGroups = converterDao.fetchRuleGroupByKeyAndName(key, ruleGroupName);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleGroups)) {
				logger.debug("Rule Groups list size-{}", listRuleGroups.size());
				// returning first element from the list as the list contains only one element
				return listRuleGroups.get(0);
			}
		}
		return null;
	}

	@Override
	public List<PraeceptaRuleGroup> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {

		if (!PraeceptaObjectHelper.isObjectNull(key)) {
			logger.debug("inside fetchByKey ruleSpaceKey-{}", key.toString());

			List<PraeceptaRuleGroup> listRuleGroups = converterDao.fetchRuleGroupByKeyAndName(key, null);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleGroups)) {
				logger.debug("Rule Groups list size-{}", listRuleGroups.size());
				return listRuleGroups;
			}
		}
		return null;
	}

	@Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			logger.debug("inside deleteByKeyAndName ruleSpaceKey-{}", key.toString());

			return converterDao.deleteRuleGroupdByKeyAndName(key, ruleGroupName);
			
		}
		return false;
	}
}
