package io.praecepta.rules.hub.jvmbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleSpaceJvmDao implements IPraeceptaRuleSpaceDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleSpaceJvmDao.class);

	private Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesMap = new HashMap<>();
	
	@Override
	public Collection<PraeceptaRuleSpace> fetchAll() {
		
		Collection<PraeceptaRuleSpace> ruleSpacesToRetrun = new ArrayList<>();
		
		Collection<Collection<PraeceptaRuleSpace>> spaceCollections = ruleSpacesMap.values().stream().map( versionSpaceMap -> versionSpaceMap.values()).collect(Collectors.toList());
		
		if(!PraeceptaObjectHelper.isObjectEmpty(spaceCollections)) {
				
			spaceCollections.stream().forEach( eachSpaceCollection -> ruleSpacesToRetrun.addAll(eachSpaceCollection) );
		}
		
		return ruleSpacesToRetrun;
	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		
		Collection<PraeceptaRuleSpace> ruleSpacesToReturn = new ArrayList<>();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			ids.stream().forEach(eachSpaceKey -> {
	//			Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>>
	
				if (eachSpaceKey != null && eachSpaceKey.getVersion() != null) {
					Map<String, PraeceptaRuleSpace> versionToSapceMap = ruleSpacesMap.get(eachSpaceKey);
	
					if (!PraeceptaObjectHelper.isObjectEmpty(versionToSapceMap)) {
						
						PraeceptaRuleSpace ruleSpace = versionToSapceMap.get(eachSpaceKey.getVersion());
						
						if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpace)) {
							ruleSpacesToReturn.add(ruleSpace);
						}
					}
				}
			});
		}
		return ruleSpacesToReturn;
	}

	@Override
	public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace ruleSpace) {
		
		Collection<PraeceptaRuleSpace> ruleSpacesToReturn = new ArrayList<>(1);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpace) && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey().getVersion())) {
			
			ruleSpacesToReturn.add( fetchByKeyAndVersion(ruleSpace.getRuleSpaceKey(), ruleSpace.getRuleSpaceKey().getVersion()) );
		}
		return ruleSpacesToReturn;
	}
	
	@Override
	public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(key) && !PraeceptaObjectHelper.isObjectEmpty(version)) {
			
			Map<String, PraeceptaRuleSpace> ruleSpaceVerionMap = ruleSpacesMap.get(key);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceVerionMap)) {
				return ruleSpaceVerionMap.get(version);
			}
		}
		return null;
	}

	@Override
	public void insert(PraeceptaRuleSpace ruleSpace) {
		LOG.debug("Inside Insert");
		
		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey().getVersion())) {
			
			Map<String, PraeceptaRuleSpace> ruleSpaceVerionMap = new HashMap<>(1);
			ruleSpaceVerionMap.put(ruleSpace.getRuleSpaceKey().getVersion(), ruleSpace);
			
			ruleSpacesMap.put(ruleSpace.getRuleSpaceKey(), ruleSpaceVerionMap);
		}
		
		LOG.debug("Exiting Insert");
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleSpace> entities) {
		LOG.debug("Inside Insert All");
		
		entities.forEach( eachSapce -> insert(eachSapce));
		
		LOG.debug("Exiting Insert All");
	}

	@Override
	public void update(PraeceptaRuleSpace entity) {
		LOG.debug("Inside Update");
		
		insert(entity);
		
		LOG.debug("Exiting Update");
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleSpace> entities) {
		LOG.debug("Inside Update All ");
		
		insertAll(entities);
		
		LOG.debug("Exiting Update All");
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		LOG.debug("Inside Delete By Ids ");

		if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			ids.stream().forEach(eachSpaceKey -> {
	
				if (eachSpaceKey != null && eachSpaceKey.getVersion() != null) {
					
					Map<String, PraeceptaRuleSpace> versionToSapceMap = ruleSpacesMap.get(eachSpaceKey);
	
					if (!PraeceptaObjectHelper.isObjectEmpty(versionToSapceMap)) {
						LOG.debug("Version To Space Map exists for Space Key - {}", eachSpaceKey);
						
						versionToSapceMap.remove(eachSpaceKey.getVersion());
					}
				}
			});
		}
		
		LOG.debug("Exiting Delete By Ids ");
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleSpace> entities) {
		LOG.debug("Inside Delete All ");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(entities)) {
				
			Collection<PraeceptaRuleSpaceCompositeKey> idsToDelete = 
					entities.stream().filter( eachSapce -> (eachSapce != null && eachSapce.getRuleSpaceKey() != null && eachSapce.getRuleSpaceKey().getVersion() != null ) )
			.map( space -> space.getRuleSpaceKey()).collect(Collectors.toList());
			
			LOG.info(" Filtered Ids that needs to delete {} ",idsToDelete);
			deleteByIds(idsToDelete);
		
		}
		
		LOG.debug("Exiting Delete All ");
	}

	@Override
	public void clear() {
	}

}
