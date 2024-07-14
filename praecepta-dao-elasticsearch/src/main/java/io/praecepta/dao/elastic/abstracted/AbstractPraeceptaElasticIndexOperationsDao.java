package io.praecepta.dao.elastic.abstracted;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.index.AliasData;
import org.springframework.data.elasticsearch.core.index.DeleteTemplateRequest;
import org.springframework.data.elasticsearch.core.index.ExistsTemplateRequest;
import org.springframework.data.elasticsearch.core.index.GetTemplateRequest;
import org.springframework.data.elasticsearch.core.index.PutTemplateRequest;
import org.springframework.data.elasticsearch.core.index.TemplateData;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.AliasQuery;

import io.praecepta.dao.elastic.model.IPraeceptaEsEntityModel;

public abstract class AbstractPraeceptaElasticIndexOperationsDao<KEY extends Serializable, ENTITY extends IPraeceptaEsEntityModel<KEY>>
		extends AbstractPraeceptaElasticDao<KEY, ENTITY> implements IndexOperations {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPraeceptaElasticIndexOperationsDao.class);

	private IndexOperations esIndexOperations;

	public AbstractPraeceptaElasticIndexOperationsDao(ElasticsearchOperations esOperations) {
		super(esOperations);

		this.esIndexOperations = esOperations.indexOps(getEntityClazz());

	}
	
	@Override
	public void upsert(ENTITY entity) {

		LOG.info("Checking whether Index Exist or not");
		if(!exists()) {
			LOG.info("Index Doesn't Exist. Creating new");
			create();
		}
		super.upsert(entity);
	}

	@Override
	public boolean create() {
		LOG.info("Creating a new Index ");
		return esIndexOperations.create();
	}

	@Override
	public boolean create(Document settings) {
		return esIndexOperations.create(settings);
	}

	@Override
	public boolean delete() {
		return esIndexOperations.delete();
	}

	@Override
	public boolean exists() {
		return esIndexOperations.exists();
	}

	@Override
	public Document createMapping() {
		return esIndexOperations.createMapping();
	}

	@Override
	public Document createMapping(Class<?> clazz) {
		return esIndexOperations.createMapping(clazz);
	}

	@Override
	public boolean putMapping(Document mapping) {
		return esIndexOperations.putMapping(mapping);
	}

	@Override
	public Document createSettings() {
		return esIndexOperations.createSettings();
	}

	@Override
	public Document createSettings(Class<?> clazz) {
		return esIndexOperations.createSettings(clazz);
	}

	@Override
	public Map<String, Object> getMapping() {
		return esIndexOperations.getMapping();
	}

	@Override
	public Map<String, Object> getSettings() {
		return esIndexOperations.getSettings();
	}

	@Override
	public Map<String, Object> getSettings(boolean includeDefaults) {
		return esIndexOperations.getSettings(includeDefaults);
	}

	@Override
	public boolean addAlias(AliasQuery query) {
		return esIndexOperations.addAlias(query);
	}

	@Override
	public List<AliasMetadata> queryForAlias() {
		return esIndexOperations.queryForAlias();
	}

	@Override
	public boolean removeAlias(AliasQuery query) {
		return esIndexOperations.removeAlias(query);
	}

	@Override
	public boolean alias(AliasActions aliasActions) {
		return esIndexOperations.alias(aliasActions);
	}

	@Override
	public Map<String, Set<AliasData>> getAliases(String... aliasNames) {
		return esIndexOperations.getAliases(aliasNames);
	}

	@Override
	public Map<String, Set<AliasData>> getAliasesForIndex(String... indexNames) {
		return esIndexOperations.getAliasesForIndex(indexNames);
	}

	@Override
	public boolean putTemplate(PutTemplateRequest putTemplateRequest) {
		return esIndexOperations.putTemplate(putTemplateRequest);
	}

	@Override
	public TemplateData getTemplate(GetTemplateRequest getTemplateRequest) {
		return esIndexOperations.getTemplate(getTemplateRequest);
	}

	@Override
	public boolean existsTemplate(ExistsTemplateRequest existsTemplateRequest) {
		return esIndexOperations.existsTemplate(existsTemplateRequest);
	}

	@Override
	public boolean deleteTemplate(DeleteTemplateRequest deleteTemplateRequest) {
		return esIndexOperations.deleteTemplate(deleteTemplateRequest);
	}

	@Override
	public IndexCoordinates getIndexCoordinates() {
		return esIndexOperations.getIndexCoordinates();
	}

	@Override
	public void refresh() {
		esIndexOperations.refresh();
	}

}
