package io.praecepta.rules.hub.dbbased.dao.converters;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.hibernate.dao.HibernateDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSpaceDbModel;

public class PraeceptaHibernateRuleSpaceDao extends HibernateDao<Long, PraeceptaRuleSpaceDbModel> {

	private static final Logger logger = LoggerFactory.getLogger(PraeceptaHibernateRuleSpaceDao.class);
	
	public Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels;

	@Override
	protected Class<PraeceptaRuleSpaceDbModel> getEntityClazz() {
		return PraeceptaRuleSpaceDbModel.class;
	}
    /*method to fetch rule space db models by  praeceptaRuleSpaceCompositeKey*/ 
	public Collection<PraeceptaRuleSpaceDbModel> fetchRuleSpaceByKey(PraeceptaRuleSpaceCompositeKey key) {
		
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createCriteria(getEntityClazz());
		
		criteria.add(Restrictions.eq("spaceName", key.getSpaceName()));
		
		criteria.add(Restrictions.eq("appName", key.getAppName()));
		
		criteria.add(Restrictions.eq("clientId", key.getClientId()));
		
		if (!PraeceptaObjectHelper.isStringEmpty(key.getVersion())) {
			criteria.add(Restrictions.eq("version", key.getVersion()));
		}
		
		criteria.add(Restrictions.eq("active", true));

		return criteria.list();
		
	}
	
	@Override
	public Collection<PraeceptaRuleSpaceDbModel> fetchAll() {
		logger.debug("Inside Rule Space  fetchAll");
		PraeceptaRuleSpaceDbModel ruleSpaceModel=new PraeceptaRuleSpaceDbModel();
		ruleSpaceModel.setActive(true);
		ruleSpaceModels=super.findByExample(ruleSpaceModel);
		return ruleSpaceModels;
	}
	
	
	public Collection<PraeceptaRuleSpaceDbModel> fetchAllRuleSpaceModels() {
		if(PraeceptaObjectHelper.isObjectEmpty(ruleSpaceModels)) {
			ruleSpaceModels=fetchAll();
		}
		return ruleSpaceModels;
	}
}
