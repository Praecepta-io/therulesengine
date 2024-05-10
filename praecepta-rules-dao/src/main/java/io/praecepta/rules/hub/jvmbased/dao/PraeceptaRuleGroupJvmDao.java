package io.praecepta.rules.hub.jvmbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleGroupJvmDao implements IPraeceptaRuleGroupDao{

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleGroupJvmDao.class);
			
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpMap = new HashMap<>();
	
	@Override
	public Collection<PraeceptaRuleGroup> fetchAll() {
		
		Collection<PraeceptaRuleGroup> ruleGrpsToRetrun = new ArrayList<>();
		
		List<Collection<Collection<PraeceptaRuleGroup>>> ruleGrpCollections = ruleGrpMap.values().stream().map( versionToGrpsMap -> versionToGrpsMap.values() ).collect(Collectors.toList());
		
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrpCollections)) {

			ruleGrpCollections.stream().forEach(eachVersionCollection -> {
				eachVersionCollection
						.forEach(eachInnerGrpCollection -> ruleGrpsToRetrun.addAll(eachInnerGrpCollection));
			});
		}
		
		return ruleGrpsToRetrun;
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		
		Collection<PraeceptaRuleGroup> ruleGrpsToRetrun = new ArrayList<>();

		if (!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			
			ids.stream().forEach( eachKey ->  ruleGrpsToRetrun.addAll(fetchByKey(eachKey)));
		}
		
		return ruleGrpsToRetrun;
	}
	
	@Override
	public List<PraeceptaRuleGroup> fetchByKey(PraeceptaRuleSpaceCompositeKey spaceKey){
		
		if (spaceKey != null && spaceKey.getVersion() != null) {
			
			Map<String, Collection<PraeceptaRuleGroup>> versionToGrpsMap = ruleGrpMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToGrpsMap)) {
				return new ArrayList<>(versionToGrpsMap.get(spaceKey.getVersion()));
			}
		}
		
		return Collections.emptyList();
	}

	@Override
	public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup entity) {

		Collection<PraeceptaRuleGroup> ruleGrpsToRetrun = new ArrayList<>(1);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(entity) && !PraeceptaObjectHelper.isObjectEmpty(entity.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(entity.getRuleGroupName())) {
			
			ruleGrpsToRetrun.add(fetchByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupName()));
			
		}

		return ruleGrpsToRetrun;
	}

	@Override
	public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey spaceKey, String ruleGroupName) {

		if (spaceKey != null && spaceKey.getVersion() != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGroupName)) {
			Map<String, Collection<PraeceptaRuleGroup>> versionToGrpsMap = ruleGrpMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToGrpsMap)) {
				Collection<PraeceptaRuleGroup> ruleGrps = versionToGrpsMap.get(spaceKey.getVersion());
				
				Optional<PraeceptaRuleGroup> filteredRuleGrp = 
						ruleGrps.stream().filter(eachGrp -> ruleGroupName.equals(eachGrp.getRuleGroupName())).findFirst();
				
				if (filteredRuleGrp.isPresent()) {
					return filteredRuleGrp.get();
				}
			}
		}
		return null;
	}

	@Override
	public void insert(PraeceptaRuleGroup ruleGrp) {

		if(ruleGrp != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey().getVersion())) {
			
			ruleGrpMap.putIfAbsent(ruleGrp.getRuleSpaceKey(), new HashMap<>());
			
			Map<String, Collection<PraeceptaRuleGroup>> versionToRuleGrps = ruleGrpMap.get(ruleGrp.getRuleSpaceKey());
			
			versionToRuleGrps.putIfAbsent(ruleGrp.getRuleSpaceKey().getVersion(), new ArrayList<>());
			
			versionToRuleGrps.get(ruleGrp.getRuleSpaceKey().getVersion()).add(ruleGrp);
			
		}
		
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleGroup> entities) {
		entities.forEach( eachSapce -> insert(eachSapce));
	}

	@Override
	public void update(PraeceptaRuleGroup entity) {
		insert(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleGroup> entities) {
		insertAll(entities);
	}

	@Override
	public void delete(PraeceptaRuleGroup entity) {

		if(!PraeceptaObjectHelper.isObjectEmpty(entity)) {
			deleteByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupName());
		}
		
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			ids.stream().forEach(eachSpaceKey -> {
	
				if (eachSpaceKey != null && eachSpaceKey.getVersion() != null) {
					Map<String, Collection<PraeceptaRuleGroup>> versionToGrpsMap = ruleGrpMap.get(eachSpaceKey);
	
					if (!PraeceptaObjectHelper.isObjectEmpty(versionToGrpsMap)) {
						versionToGrpsMap.remove(eachSpaceKey.getVersion());
					}
				}
			});
		}
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleGroup> entities) {

		if(!PraeceptaObjectHelper.isObjectEmpty(entities)) {
			
			Map<PraeceptaRuleSpaceCompositeKey, String> idsToRuleGrpNameToDelete = 
					entities.stream().filter( eachGrp -> (eachGrp != null && eachGrp.getRuleSpaceKey() != null && eachGrp.getRuleSpaceKey().getVersion() != null ) )
					.collect(Collectors.toMap(eachGrp -> eachGrp.getRuleSpaceKey(), eachGrp -> eachGrp.getRuleGroupName()));		
			
			if (!PraeceptaObjectHelper.isObjectEmpty(idsToRuleGrpNameToDelete)) {
				
				idsToRuleGrpNameToDelete.forEach( (spaceKey, grpName) -> deleteByKeyAndName(spaceKey, grpName));
			}
		
		}
		
	}

	@Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey spaceKey, String ruleGroupName) {
		
		if (spaceKey != null && spaceKey.getVersion() != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGroupName)) {
			Map<String, Collection<PraeceptaRuleGroup>> versionToGrpsMap = ruleGrpMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToGrpsMap)) {
				Collection<PraeceptaRuleGroup> ruleGrps = versionToGrpsMap.get(spaceKey.getVersion());
				
				Collection<PraeceptaRuleGroup> ruleGrpsListAfterExclusion = 
						ruleGrps.stream().filter(eachGrp -> !ruleGroupName.equals(eachGrp.getRuleGroupName())).collect(Collectors.toList());
				
				if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsListAfterExclusion)) {
					
					versionToGrpsMap.put(spaceKey.getVersion(), ruleGrpsListAfterExclusion);
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void clear() {
	}

}
