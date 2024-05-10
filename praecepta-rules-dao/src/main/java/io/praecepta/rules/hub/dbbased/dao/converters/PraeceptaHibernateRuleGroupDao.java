package io.praecepta.rules.hub.dbbased.dao.converters;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.hibernate.dao.HibernateDao;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleGroupDbModel;

public class PraeceptaHibernateRuleGroupDao extends HibernateDao<Long, PraeceptaRuleGroupDbModel> {

	private static final Logger logger = LoggerFactory.getLogger(PraeceptaHibernateRuleGroupDao.class);
	
	public Collection<PraeceptaRuleGroupDbModel> ruleGroupDbModels;

	@Override
	protected Class<PraeceptaRuleGroupDbModel> getEntityClazz() {
		return PraeceptaRuleGroupDbModel.class;
	}
    
//	/*Method to get rule group models for given rule space ids */
//	public Collection<PraeceptaRuleGroupDbModel> fetchRuleGroupsByRuleSpaceIds(List<Long> ruleSpaceIds) {
//
//		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
//				.createCriteria(getEntityClazz());
//		
//		criteria.add(Restrictions.in("ruleSpaceId", ruleSpaceIds));
//		criteria.add(Restrictions.eq("active", true));
//
//		return criteria.list();
//	}
	
	/*method fetch rule group db models by  praeceptaRuleSpaceCompositeKey*/ 
//	public Collection<PraeceptaRuleGroupDbModel> fetchRuleGroupsByKey(PraeceptaRuleSpaceCompositeKey key,String ruleGroupName) {
//		
//		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
//				.createCriteria(getEntityClazz());
//		
//		if(!PraeceptaObjectHelper.isStringEmpty( key.getSpaceName())) {
//			criteria.add(Restrictions.eq("spaceName", key.getSpaceName()));
//		}
//		if(!PraeceptaObjectHelper.isStringEmpty( key.getAppName())) {
//			criteria.add(Restrictions.eq("appName", key.getAppName()));
//		}
//		if(!PraeceptaObjectHelper.isStringEmpty( key.getClientId())) {
//			criteria.add(Restrictions.eq("clientName", key.getClientId()));
//		}
//		if (!PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
//			criteria.add(Restrictions.eq("ruleGroupName",ruleGroupName));
//		}
//		
//		if (!PraeceptaObjectHelper.isStringEmpty(key.getVersion())) {
//			criteria.add(Restrictions.eq("version",key.getVersion()));
//		}
//		
//		criteria.add(Restrictions.eq("active", true));
//
//		return criteria.list();
//	}
	
	@Override
	public Collection<PraeceptaRuleGroupDbModel> fetchAll() {
		logger.debug("Inside Rule Group  fetchAll");
		PraeceptaRuleGroupDbModel ruleGroupModel=new PraeceptaRuleGroupDbModel();
		ruleGroupModel.setActive(true);
		ruleGroupDbModels= super.findByExample(ruleGroupModel);
		return ruleGroupDbModels;
	}
	
	public Collection<PraeceptaRuleGroupDbModel> fetchAllRuleGroups() {
		logger.debug("Inside Rule Group  fetchAllRuleGroups");
		if (PraeceptaObjectHelper.isObjectEmpty(ruleGroupDbModels)) {
			ruleGroupDbModels=this.fetchAll();
		}
		return ruleGroupDbModels;
	}
}
