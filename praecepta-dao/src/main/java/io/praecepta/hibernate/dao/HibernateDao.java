package io.praecepta.hibernate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.util.CollectionUtils;

import io.praecepta.dao.AbstractDao;
import io.praecepta.dao.model.AbstractHibernateBaseModel;
import io.praecepta.hibernate.dao.intf.IHibernateDao;

public abstract class HibernateDao<KEY extends Serializable, ENTITY extends AbstractHibernateBaseModel<KEY>>
		extends AbstractDao<KEY, ENTITY> implements IHibernateDao<KEY, ENTITY>{

	private static final Logger LOG = LoggerFactory.getLogger(HibernateDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private HibernateTemplate hibernateTemplate;

//	private ITransformer<ICriteria, DetachedCriteria> transformer;

	protected abstract Class<ENTITY> getEntityClazz();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	public ENTITY fetchById(KEY id) {
		return getById(id);
	}

	protected ENTITY getById(KEY id) {
		LOG.debug("Inside HibernateDAO getById");
		return getHibernateTemplate().get(getEntityClazz(), id);
	}

	@Override
	public Collection<ENTITY> fetchAll() {
		LOG.debug("Inside HibernateDAO fetchAll");
		return getHibernateTemplate().loadAll(getEntityClazz());
	}

	@Override
	public Collection<ENTITY> findByExample(ENTITY entity) {
		LOG.debug("Inside HibernateDAO findByExample");
		return getHibernateTemplate().findByExample(entity);
	}

	@Override
	public void deleteById(KEY id) {
		LOG.debug("Inside HibernateDAO deleteById");
		/*ENTITY entity = getById(id);
		delete(entity);*/
		hibernateTemplate.delete(hibernateTemplate.get(getEntityClazz(),id));
		LOG.debug("Exiting HibernateDAO deleteById");
	}

	@Override
	public void delete(ENTITY entity) {
		LOG.debug("Inside HibernateDAO delete");
		getHibernateTemplate().delete(entity);
		getHibernateTemplate().flush();
		LOG.debug("Exiting HibernateDAO delete");
	}

	@Override
	public void deleteAll(Collection<ENTITY> entities) {
		LOG.debug("Inside HibernateDAO deleteAll");
		getHibernateTemplate().deleteAll(entities);
		LOG.debug("Exiting HibernateDAO deleteAll");
	}

	@Override
	public void clear() {
		LOG.debug("Inside HibernateDAO clear");
		getHibernateTemplate().clear();
		LOG.debug("Exiting HibernateDAO clear");
	}
	
	@Override
	public void clearById(KEY id) {
		LOG.debug("Inside HibernateDAO clearById");
		deleteById(id);
		LOG.debug("Exiting HibernateDAO clearById");
	}

	@Override
	public void clear(ENTITY entity) {
		LOG.debug("Inside HibernateDAO clearById");
		delete(entity);
		LOG.debug("Exiting HibernateDAO clearById");
	}

	@Override
	public void clearByIds(Collection<KEY> ids) {
		LOG.debug("Inside HibernateDAO clearById");
		deleteByIds(ids);
		LOG.debug("Exiting HibernateDAO clearById");
		
	}

	@Override
	public void clear(Collection<ENTITY> entitys) {
		LOG.debug("Inside HibernateDAO clearById");
		deleteAll(entitys);
		LOG.debug("Exiting HibernateDAO clearById");
		
	}

	/*@Override
	public Collection<ENTITY> findByCriteria(ICriteria criteria) {
		DetachedCriteria hibernateCriteria = transformer.transform(criteria);
		
		return (Collection<ENTITY>) getHibernateTemplate().findByCriteria(hibernateCriteria);
	}*/

	@Override
	public Collection<ENTITY> findBySql(String sql) {
		return getSqlQuery(sql, getEntityClazz()).list();
	}

	private SQLQuery getSqlQuery(String sql, Class<?> clazz) {
		SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql);
		sqlQuery.addEntity(clazz);
		
		return sqlQuery;
	}

	@Override
	public Collection<ENTITY> findByHql(String hql) {
		return (Collection<ENTITY>) findByHqlAndGetEntity(hql, getEntityClazz());
	}

	@Override
	public Collection<ENTITY> findBySqlAndParmas(String sql, Map<String, Object> paramsmap) {
		if(paramsmap != null && !paramsmap.isEmpty()){
			return (Collection<ENTITY>) findBySqlAndParmasAndGetEntity(sql, paramsmap, getEntityClazz());
		}
		return null;
	}

	@Override
	public Collection<ENTITY> findByHqlAndParmas(String hql, Map<String, Object> paramsmap) {
		if(paramsmap != null && !paramsmap.isEmpty()){
			return (Collection<ENTITY>) findByHqlAndParmasAndGetEntity(hql, paramsmap, getEntityClazz());
		}
		return null;
	}

	@Override
	protected void upsert(ENTITY entity) {
		LOG.debug("Inside HibernateDAO upsert");
		if(entity != null){
			addAuditInfo(entity);
			getHibernateTemplate().saveOrUpdate(entity);
		}
		LOG.debug("Exiting HibernateDAO upsert");
	}

	private void addAuditInfo(ENTITY entity) {
		LOG.debug("Inside HibernateDAO addAuditInfo");
		if(entity.getId() == null){
			entity.assignDefaultValues();
		} else {
			entity.setLastUpdatedDate(new Date());
		}
		LOG.debug("Exiting HibernateDAO addAuditInfo");
	}

	@Override
	protected void upsertAll(Collection<ENTITY> entities) {
		LOG.debug("Inside HibernateDAO upsertAll");
		if(!CollectionUtils.isEmpty(entities)){
			for(ENTITY eachEntity : entities){
				upsert(eachEntity);
			}
		}
		LOG.debug("Exiting HibernateDAO upsertAll");
	}

	@Override
	protected Collection<ENTITY> fetchForUniqueKeys(Collection<KEY> ids) {
		LOG.debug("Inside HibernateDAO fetchForUniqueKeys");
		Collection<ENTITY> entyties = new ArrayList<>();
		if(!CollectionUtils.isEmpty(ids)){
			for(KEY eachId : ids){
				entyties.add(getById(eachId));
			}
		}
		LOG.debug("Exiting HibernateDAO fetchForUniqueKeys");
		return entyties;
	}

	@Override
	protected void deleteForUniqueKeys(Collection<KEY> ids) {
		LOG.debug("Inside HibernateDAO deleteForUniqueKeys");
		if(!CollectionUtils.isEmpty(ids)){
			for(KEY eachId : ids){
				deleteById(eachId);
			}
		}
		LOG.debug("Exiting HibernateDAO deleteForUniqueKeys");
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public <T> Collection<T> findBySqlAndGetEntity(String sql, Class<T> clazz) {
		return getSqlQuery(sql, clazz).list();
	}

	@Override
	public <T> Collection<T> findByHqlAndGetEntity(String hql, Class<T> clazz) {
		return (Collection<T>) getHibernateTemplate().find(hql, null);
	}

	@Override
	public <T> Collection<T> findBySqlAndParmasAndGetEntity(String sql, Map<String, Object> paramsmap, Class<T> clazz) {
		LOG.debug("Inside HibernateDAO findBySqlAndParmasAndGetEntity");
		if(paramsmap != null && !paramsmap.isEmpty()){
			String[] paramNames = new String[paramsmap.size()];
			Object[] paramValues = new Object[paramsmap.size()];
			
			int i = 0;
			for(Map.Entry<String, Object> eachEntry : paramsmap.entrySet()){
				paramNames[i] = eachEntry.getKey();
				paramValues[i] = eachEntry.getValue();
				i++;
			}
			LOG.debug("Exiting HibernateDAO findBySqlAndParmasAndGetEntity");
			return (Collection<T>) getHibernateTemplate().findByNamedParam(sql, paramNames, paramValues);
		}
		return null;
	}

	@Override
	public <T> Collection<T> findByHqlAndParmasAndGetEntity(String hql, Map<String, Object> paramsmap, Class<T> clazz) {
		
		LOG.debug("Inside HibernateDAO findByHqlAndParmasAndGetEntity");
		if(paramsmap != null && !paramsmap.isEmpty()){
			String[] paramNames = new String[paramsmap.size()];
			Object[] paramValues = new Object[paramsmap.size()];
			
			int i = 0;
			for(Map.Entry<String, Object> eachEntry : paramsmap.entrySet()){
				paramNames[i] = eachEntry.getKey();
				paramValues[i] = eachEntry.getValue();
				i++;
			}
			LOG.debug("Exiting HibernateDAO findByHqlAndParmasAndGetEntity");
			return (Collection<T>) getHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
		}
		return null;
	}

}
