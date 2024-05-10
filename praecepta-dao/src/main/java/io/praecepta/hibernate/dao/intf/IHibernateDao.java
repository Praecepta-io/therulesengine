package io.praecepta.hibernate.dao.intf;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import io.praecepta.core.data.intf.IBaseModel;
import io.praecepta.dao.intf.IDAO;

public interface IHibernateDao<KEY extends Serializable , ENTITY extends IBaseModel<KEY>> extends IDAO<KEY, ENTITY>{

	Collection<ENTITY> findBySql(String sql);
	
	Collection<ENTITY> findByHql(String hql);
	
	Collection<ENTITY> findBySqlAndParmas(String sql, Map<String, Object> paramsmap);
	
	Collection<ENTITY> findByHqlAndParmas(String hql, Map<String, Object> paramsmap);
	
	<T> Collection<T> findBySqlAndGetEntity(String sql, Class<T> clazz); 

	<T> Collection<T> findByHqlAndGetEntity(String hql, Class<T> clazz); 

	<T> Collection<T> findBySqlAndParmasAndGetEntity(String sql, Map<String, Object> paramsmap, Class<T> clazz); 
	
	<T> Collection<T> findByHqlAndParmasAndGetEntity(String hql, Map<String, Object> paramsmap, Class<T> clazz);  
	
	void clearById(KEY id);
	
	void clear(ENTITY entity);
	
	void clearByIds(Collection<KEY> ids);
	
	void clear(Collection<ENTITY> entitys);
	
}
