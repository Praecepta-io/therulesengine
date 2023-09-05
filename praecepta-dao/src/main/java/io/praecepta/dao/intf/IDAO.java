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
 * @param <KEY> --> Class that implements Serializable. Most of the cases the Wrapper classes will suffice [Integer, Long, String etc]
 * @param <ENTITY> --> Model Class Used for this DAO class.
 */

public interface IDAO<KEY extends Serializable, ENTITY extends IModel> {

	ENTITY fetchById(KEY id);

	Collection<ENTITY> fetchAll();

	Collection<ENTITY> fetchByIds(Collection<KEY> ids);

	Collection<ENTITY> findByExample(ENTITY entity);

	void insert(ENTITY entity);

	void insertAll(Collection<ENTITY> entities);

	void update(ENTITY entity);

	void updateAll(Collection<ENTITY> entities);

	void deleteById(KEY id);

	void delete(ENTITY entity);

	void deleteByIds(Collection<KEY> ids);
	
	void deleteAll(Collection<ENTITY> entities);

	void clear(); // Delete All

}
