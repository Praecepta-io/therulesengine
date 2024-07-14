package io.praecepta.dao.elastic;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import io.praecepta.dao.elastic.abstracted.AbstractPraeceptaElasticIndexOperationsDao;
import io.praecepta.dao.elastic.model.PraeceptaAuditEntity;

public class PraeceptaAuditDao extends AbstractPraeceptaElasticIndexOperationsDao<String,PraeceptaAuditEntity > {

	public PraeceptaAuditDao(ElasticsearchOperations esOperations) {
		super(esOperations);
	}

	@Override
	protected Class<PraeceptaAuditEntity> getEntityClazz() {
		return PraeceptaAuditEntity.class;
	}

}
