package io.praecepta.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import io.praecepta.core.data.intf.IBaseModel;
import io.praecepta.dao.intf.IDAO;

public abstract class AbstractDao<KEY extends Serializable , ENTITY extends IBaseModel<KEY>> implements IDAO<KEY, ENTITY> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDao.class); 

	@Override
	public void insert(ENTITY entity) {
		LOG.debug("Inside insert");
		upsert(entity);
		LOG.debug("Exiting insert");
	}

	@Override
	public void update(ENTITY entity) {
		LOG.debug("Inside update");
		upsert(entity);
		LOG.debug("Exiting update");
	}

	protected abstract void upsert(ENTITY entity);

	@Override
	public void insertAll(Collection<ENTITY> entities) {
		LOG.debug("Inside insertAll");
		if (!CollectionUtils.isEmpty(entities)) {
			upsertAll(entities);
		}
		LOG.debug("Exiting insertAll");
	}

	@Override
	public void updateAll(Collection<ENTITY> entities) {
		LOG.debug("Inside updateAll");
		if (!CollectionUtils.isEmpty(entities)) {
			upsertAll(entities);
		}
		LOG.debug("Exiting updateAll");
	}

	protected abstract void upsertAll(Collection<ENTITY> entity);

	@Override
	public Collection<ENTITY> fetchByIds(Collection<KEY> ids) {
		LOG.debug("Inside fetchByIds");
		if (!CollectionUtils.isEmpty(ids)) {
			Set<KEY> uniqueIds = getUniqueIds(ids);
			return fetchForUniqueKeys(uniqueIds);
		}

		LOG.debug("Exiting fetchByIds");
		return Collections.emptyList();
	}

	protected abstract Collection<ENTITY> fetchForUniqueKeys(Collection<KEY> ids);

	private Set<KEY> getUniqueIds(Collection<KEY> ids) {
		LOG.debug("Inside getUniqueIds");
		Set<KEY> uniqueIds = new HashSet<>(ids);

		/*LOG.info("Working on Unique Keys "+ids);
		for (KEY eachId : ids) {
			uniqueIds.add(eachId);
		}*/

		LOG.debug("Exiting getUniqueIds");
		return uniqueIds;
	}

	@Override
	public void deleteByIds(Collection<KEY> ids) {
		LOG.debug("Inside deleteByIds");

		if (!CollectionUtils.isEmpty(ids)) {
			Set<KEY> uniqueIds = getUniqueIds(ids);
			deleteForUniqueKeys(uniqueIds);
		}

		LOG.debug("Exiting deleteByIds");
	}

	protected abstract void deleteForUniqueKeys(Collection<KEY> ids);

}
