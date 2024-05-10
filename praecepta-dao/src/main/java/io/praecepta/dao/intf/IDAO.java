package io.praecepta.dao.intf;

import java.io.Serializable;
import java.util.Collection;

import io.praecepta.core.data.intf.IModel;

/**
 * 
 * @author PunugotiR
 * 
 * Generic Data Access Layer Interface
 *
 */

public interface IDAO<K extends Serializable, E extends IModel> {

	E fetchById(K id);

	Collection<E> fetchAll();

	Collection<E> fetchByIds(Collection<K> ids);

	Collection<E> findByExample(E entity);

	void insert(E entity);

	void insertAll(Collection<E> entities);

	void update(E entity);

	void updateAll(Collection<E> entities);

	void deleteById(K id);

	void delete(E entity);

	void deleteByIds(Collection<K> ids);
	
	void deleteAll(Collection<E> entities);

	void clear(); // Delete All

}
