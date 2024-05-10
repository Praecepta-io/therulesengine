package io.praecepta.rules.hub.dbbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateConverterDao;

public class PraeceptaRuleSideCarsMySQLDao implements IPraeceptaRuleSideCarsDao {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleSideCarsMySQLDao.class);

	@Autowired
	private PraeceptaHibernateConverterDao converterDao;

	@Override
	public PraeceptaSideCarsInfo fetchById(PraeceptaRuleSpaceCompositeKey id) {
		if (PraeceptaObjectHelper.isObjectEmpty(id)) {
			logger.debug("Rule space composite key found empty/null");
		}

		List<PraeceptaSideCarsInfo> listRuleSideCars = converterDao.fetchRuleSideCarsByKeyAndName(id, null);

		if (!PraeceptaObjectHelper.isObjectEmpty(listRuleSideCars)) {
			logger.debug("Rule Side Cars list size -{} found for the key-{}", listRuleSideCars.size(), id.toString());
			return listRuleSideCars.get(0);
		}
		logger.debug("done fetch rule side car by composite key Id ");
		return null;
	}

	@Override
	public Collection<PraeceptaSideCarsInfo> fetchAll() {
		return converterDao.getAllRuleSideCars();
	}

	@Override
	public Collection<PraeceptaSideCarsInfo> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		if (PraeceptaObjectHelper.isObjectEmpty(ids)) {
			logger.debug("Rule space composite keys found empty/null");
		}

		Collection<PraeceptaSideCarsInfo> ruleSideCars = new ArrayList();

		ids.stream().forEach(objKey -> {
			List<PraeceptaSideCarsInfo> listRuleGroups = converterDao.fetchRuleSideCarsByKeyAndName(objKey, null);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleGroups)) {
				ruleSideCars.addAll(listRuleGroups);
			}
		});

		return ruleSideCars;
	}

	@Override
	public Collection<PraeceptaSideCarsInfo> findByExample(PraeceptaSideCarsInfo entity) {
		if (PraeceptaObjectHelper.isObjectNull(entity)) {
			logger.debug("inside  findByExample rule side car found null");
		}
		logger.debug("before getting rule side car list with  findByExample");
		return converterDao.findRuleSideCarsByExample(entity);
	}

	@Override
	public void insert(PraeceptaSideCarsInfo entity) {
		converterDao.saveOrUpdateRuleSideCar(entity);
	}

	@Override
	public void insertAll(Collection<PraeceptaSideCarsInfo> entities) {
		converterDao.saveOrUpdateAllRuleSideCars(entities);
	}

	@Override
	public void update(PraeceptaSideCarsInfo entity) {
		converterDao.saveOrUpdateRuleSideCar(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaSideCarsInfo> entities) {
		converterDao.saveOrUpdateAllRuleSideCars(entities);
	}

	@Override
	public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
		logger.debug("inside  delete rule side cars by composite key");
		converterDao.deleteRuleSideCarsByKey(id);
	}

	@Override
	public void delete(PraeceptaSideCarsInfo entity) {
		logger.debug("inside  delete rule group by rule group object");
		converterDao.deleteRuleSideCar(entity);
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		logger.debug("inside  delete rule group by rule space keys");
		converterDao.deleteRuleSideCarsByRuleSpaceKeys(ids);
	}

	@Override
	public void deleteAll(Collection<PraeceptaSideCarsInfo> entities) {
		logger.debug("inside  delete all rule groups  by rule group objects");
		converterDao.deleteAllRuleSideCars(entities);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public PraeceptaSideCarsInfo fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			logger.debug("inside fetchByKeyAndName ruleSpaceKey-{},RuleGroupName-{}", key.toString(), ruleGroupName);

			List<PraeceptaSideCarsInfo> listRuleSideCars = converterDao.fetchRuleSideCarsByKeyAndName(key, ruleGroupName);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleSideCars)) {
				logger.debug("Rule Side Cars list size-{}", listRuleSideCars.size());
				// returning first element from the list as the list contains only one element
				return listRuleSideCars.get(0);
			}
		}
		return null;
	}

	@Override
	public List<PraeceptaSideCarsInfo> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {

		if (!PraeceptaObjectHelper.isObjectNull(key)) {
			logger.debug("inside fetchByKey ruleSpaceKey-{}", key.toString());

			List<PraeceptaSideCarsInfo> listRuleSideCars = converterDao.fetchRuleSideCarsByKeyAndName(key, null);
			if (!PraeceptaObjectHelper.isObjectEmpty(listRuleSideCars)) {
				logger.debug("Rule Side Cars list size-{}", listRuleSideCars.size());
				return listRuleSideCars;
			}
		}
		return null;
	}

	@Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		if (!PraeceptaObjectHelper.isObjectNull(key) && !PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			logger.debug("inside deleteByKeyAndName ruleSpaceKey-{}", key.toString());

			return converterDao.deleteRuleSideCarsByKeyAndName(key, ruleGroupName);

		}
		return false;
	}
}
