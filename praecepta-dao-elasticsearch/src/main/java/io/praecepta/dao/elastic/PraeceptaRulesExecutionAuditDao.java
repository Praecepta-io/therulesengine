package io.praecepta.dao.elastic;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import io.praecepta.dao.elastic.abstracted.AbstractPraeceptaElasticIndexOperationsDao;
import io.praecepta.dao.elastic.model.PraeceptaRulesExecutionEntity;

public class PraeceptaRulesExecutionAuditDao extends AbstractPraeceptaElasticIndexOperationsDao<String,PraeceptaRulesExecutionEntity > {

	public PraeceptaRulesExecutionAuditDao(ElasticsearchOperations esOperations) {
		super(esOperations);
	}

	@Override
	protected Class<PraeceptaRulesExecutionEntity> getEntityClazz() {
		return PraeceptaRulesExecutionEntity.class;
	}

}
