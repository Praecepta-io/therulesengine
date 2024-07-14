package io.praecepta.dao.elastic.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import io.praecepta.dao.elastic.PraeceptaAuditDao;
import io.praecepta.dao.elastic.config.PraeceptaElasticConfiguration;

@Import(PraeceptaElasticConfiguration.class)
public class PraeceptaAuditDaoSpringConfig {

	@Autowired
	private ElasticsearchOperations esOperations;
	
	
	@Bean(name = "praeceptaAuditDao")
	public PraeceptaAuditDao getPraeceptaAuditDao() {
		return new PraeceptaAuditDao(esOperations);
	}
}
