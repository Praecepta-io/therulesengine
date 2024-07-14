package io.praecepta.dao.elastic.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

public interface IPraeceptaEsDao<ENTITY> {

	Page<ENTITY> searchByCriteria(CriteriaQuery criteria);

	Page<ENTITY> searchByCriteria(CriteriaQuery criteria, Pageable pageable);
}
