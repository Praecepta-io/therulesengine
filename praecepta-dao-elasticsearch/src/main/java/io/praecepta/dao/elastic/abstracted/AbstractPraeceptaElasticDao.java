package io.praecepta.dao.elastic.abstracted;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.AbstractDao;
import io.praecepta.dao.elastic.intf.IPraeceptaEsDao;
import io.praecepta.dao.elastic.model.IPraeceptaEsEntityModel;

public abstract class AbstractPraeceptaElasticDao<KEY extends Serializable , ENTITY extends IPraeceptaEsEntityModel<KEY>> extends AbstractDao<KEY, ENTITY> 
		implements IPraeceptaEsDao<ENTITY> 
{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPraeceptaElasticDao.class); 
	
	private ElasticsearchOperations esOperations;
	
	private Class<ENTITY> entitytClazz;
	
	private SimpleElasticsearchRepository<ENTITY, KEY> simpleEsRepo;
	
	public AbstractPraeceptaElasticDao(ElasticsearchOperations esOperations) {
		this.esOperations = esOperations;
		
		entitytClazz = getEntityClazz();
		
		buildSimpleEsOperations();
	}
	
	private void buildSimpleEsOperations() {
		
		TypeInformation<ENTITY> typeInfo = ClassTypeInformation.from(entitytClazz);
		
		ElasticsearchPersistentEntity<ENTITY> simpleEsPersistentEntity = new SimpleElasticsearchPersistentEntity<>(typeInfo);
		
		ElasticsearchEntityInformation<ENTITY, KEY> mappingElasticsearchEntityInformation = new MappingElasticsearchEntityInformation<>(simpleEsPersistentEntity);
		
		simpleEsRepo = new SimpleElasticsearchRepository<>(mappingElasticsearchEntityInformation, esOperations);
	}

	@Override
	public ENTITY fetchById(KEY id) {
		
		Optional<ENTITY> returnedEntity = simpleEsRepo.findById(id);
		
		if(returnedEntity.isPresent()) {
			return returnedEntity.get();
		}
		
		return null;
	}

	@Override
	public Collection<ENTITY> fetchAll() {
		throw new UnsupportedOperationException("Fetch All is not Supported");
	}

	@Override
	public Collection<ENTITY> findByExample(ENTITY entity) {

//		Pageable pageable = PageRequest.of(0, 250).first();
//		
//		simpleEsRepo.searchSimilar(entity, entity.getEntityFieldNames(), pageable);

		Map<String, Object> filedToValueMap = entity.getEntityFieldNameAndValue();

		Criteria esCriteria = new Criteria();

		if (!PraeceptaObjectHelper.isObjectEmpty(filedToValueMap)) {
			
//			esCriteria = new Criteria("clientId").contains("Risk").and("appName").contains("Creadit");

//			filedToValueMap.forEach((fieldName, value) -> {
//
//				if (!PraeceptaObjectHelper.isObjectEmpty(value)) {
//					esCriteria.and(fieldName).contains(value.toString());
//				}
//
//			});
			
			int loopNum = 0;
			for(Map.Entry<String,Object> entry  : filedToValueMap.entrySet()) {
				
				if (!PraeceptaObjectHelper.isObjectEmpty(entry.getValue())) {
					
					if(loopNum == 0) {
						esCriteria = new Criteria(entry.getKey()).contains(entry.getValue().toString());
					} else {
						esCriteria.and(new Criteria(entry.getKey()).contains(entry.getValue().toString()));
					}
					++loopNum;
				}
			}
			
			LOG.info("ES Criteria  - {} ", esCriteria);
			
			Page<ENTITY> results = searchByCriteria(new CriteriaQuery(esCriteria));
			
			if(!PraeceptaObjectHelper.isObjectEmpty(results)) {
				
				return results.getContent();
			}
		}

		return null;
	}

	@Override
	public void deleteAll(Collection<ENTITY> entities) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(entities)) {
			simpleEsRepo.deleteAll(entities);
		}
	}

	@Override
	public void clear() {
		simpleEsRepo.deleteAll();
	}

	@Override
	public void deleteById(KEY id) {
		simpleEsRepo.deleteById(id);
	}

	@Override
	public void delete(ENTITY entity) {
		simpleEsRepo.delete(entity);
	}

	@Override
	protected void upsert(ENTITY entity) {
		simpleEsRepo.save(entity);		
	}

	@Override
	protected void upsertAll(Collection<ENTITY> entities) {
		simpleEsRepo.saveAll(entities);
		
	}

	@Override
	protected Collection<ENTITY> fetchForUniqueKeys(Collection<KEY> ids) {
		return fetchByIds(ids);
	}

	@Override
	protected void deleteForUniqueKeys(Collection<KEY> ids) {
		deleteByIds(ids);
		
	}
	
	protected abstract Class<ENTITY> getEntityClazz();
	
	protected ElasticsearchOperations getEsOperations() {
		return esOperations;
	}

	protected SimpleElasticsearchRepository<ENTITY, KEY> getEsRepo() {
		return simpleEsRepo;
	}
	
	@Override
	public Page<ENTITY> searchByCriteria(CriteriaQuery criteria) {
		return searchByCriteria(criteria, Pageable.unpaged());
	}

	@Override
	public Page<ENTITY> searchByCriteria(CriteriaQuery criteria, Pageable pageable) {
		
		if(criteria != null) {
			criteria.setPageable(pageable);
			return getEsRepo().search(criteria);
		}
		return null;
	}
}
