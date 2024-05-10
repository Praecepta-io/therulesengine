package io.praecepta.rules.hub.jvmbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

public class PraeceptaRuleSideCarsJvmDao implements IPraeceptaRuleSideCarsDao {

	Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarMap = new HashMap<>();
	
	@Override
	public PraeceptaSideCarsInfo fetchById(PraeceptaRuleSpaceCompositeKey id) {
		
		List<PraeceptaSideCarsInfo> sideCarsForAnId = fetchByKey(id);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarsForAnId)) {
			
			return sideCarsForAnId.get(0);
		}
		return null;
	}
	
	@Override
	public PraeceptaSideCarsInfo fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey spaceKey, String ruleGroupName) {

		if (spaceKey != null && spaceKey.getVersion() != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGroupName)) {
			Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCars = ruleSideCarMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToSideCars)) {
				Collection<PraeceptaSideCarsInfo> ruleGrps = versionToSideCars.get(spaceKey.getVersion());
				
				Optional<PraeceptaSideCarsInfo> filteredSideCars = 
						ruleGrps.stream().filter(eachGrp -> ruleGroupName.equals(eachGrp.getRuleGroupSideCars().getRuleGrpName())).findFirst();
				
				if (filteredSideCars.isPresent()) {
					return filteredSideCars.get();
				}
			}
		}
		return null;
		
	}


	@Override
	public Collection<PraeceptaSideCarsInfo> fetchAll() {
		
		Collection<PraeceptaSideCarsInfo> sideCarsToRetrun = new ArrayList<>();
		
		List<Collection<Collection<PraeceptaSideCarsInfo>>> sideCarCollections = ruleSideCarMap.values().stream().map
				( versionToGrpsMap -> versionToGrpsMap.values() ).collect(Collectors.toList());
		
		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarCollections)) {

			sideCarCollections.stream().forEach(eachVersionCollection -> {
				eachVersionCollection
						.forEach(eachInnerSideCarCollection -> sideCarsToRetrun.addAll(eachInnerSideCarCollection));
			});
		}
		
		return sideCarsToRetrun;
	}

	@Override
	public Collection<PraeceptaSideCarsInfo> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {

		Collection<PraeceptaSideCarsInfo> sideCarsToRetrun = new ArrayList<>();

		if (!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			
			ids.stream().forEach( eachKey ->  sideCarsToRetrun.addAll(fetchByKey(eachKey)));
		}
		
		return sideCarsToRetrun;
	}

	@Override
	public Collection<PraeceptaSideCarsInfo> findByExample(PraeceptaSideCarsInfo entity) {
		
		Collection<PraeceptaSideCarsInfo> sideCarsToRetrun = new ArrayList<>(1);
		
		if (!PraeceptaObjectHelper.isObjectEmpty(entity) && !PraeceptaObjectHelper.isObjectEmpty(entity.getRuleSpaceKey()) && 
				!PraeceptaObjectHelper.isObjectEmpty(entity.getRuleGroupSideCars().getRuleGrpName())) {
			
			sideCarsToRetrun.add(fetchByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupSideCars().getRuleGrpName()));
			
		}

		return sideCarsToRetrun;
	}
	
	@Override
	public List<PraeceptaSideCarsInfo> fetchByKey(PraeceptaRuleSpaceCompositeKey spaceKey){
		
		if (spaceKey != null && spaceKey.getVersion() != null) {
			
			Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCars = ruleSideCarMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToSideCars)) {
				return new ArrayList<>(versionToSideCars.get(spaceKey.getVersion()));
			}
		}
		
		return Collections.emptyList();
	}

	@Override
	public void insert(PraeceptaSideCarsInfo sideCarInfo) {
		
		if(sideCarInfo != null && !PraeceptaObjectHelper.isObjectEmpty(sideCarInfo.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(sideCarInfo.getRuleSpaceKey().getVersion())) {
			
			ruleSideCarMap.putIfAbsent(sideCarInfo.getRuleSpaceKey(), new HashMap<>());
			
			Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCars = ruleSideCarMap.get(sideCarInfo.getRuleSpaceKey());
			
			versionToSideCars.putIfAbsent(sideCarInfo.getRuleSpaceKey().getVersion(), new ArrayList<>());
			
			versionToSideCars.get(sideCarInfo.getRuleSpaceKey().getVersion()).add(sideCarInfo);
			
		}
	}

	@Override
	public void insertAll(Collection<PraeceptaSideCarsInfo> entities) {
		entities.forEach( eachSapce -> insert(eachSapce));
	}

	@Override
	public void update(PraeceptaSideCarsInfo entity) {
		insert(entity);
	}

	@Override
	public void updateAll(Collection<PraeceptaSideCarsInfo> entities) {
		insertAll(entities);
	}

	@Override
	public void delete(PraeceptaSideCarsInfo entity) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(entity)) {
			deleteByKeyAndName(entity.getRuleSpaceKey(), entity.getRuleGroupSideCars().getRuleGrpName());
		}
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
			ids.stream().forEach(eachSpaceKey -> {
	
				if (eachSpaceKey != null && eachSpaceKey.getVersion() != null) {
					Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCars = ruleSideCarMap.get(eachSpaceKey);
	
					if (!PraeceptaObjectHelper.isObjectEmpty(versionToSideCars)) {
						versionToSideCars.remove(eachSpaceKey.getVersion());
					}
				}
			});
		}
	}

	@Override
	public void deleteAll(Collection<PraeceptaSideCarsInfo> entities) {

		if(!PraeceptaObjectHelper.isObjectEmpty(entities)) {
			
			Map<PraeceptaRuleSpaceCompositeKey, String> idsToRuleGrpNameToDelete = 
					entities.stream()
					.filter( eachSideCar -> 
						(eachSideCar != null && eachSideCar.getRuleSpaceKey() != null && eachSideCar.getRuleSpaceKey().getVersion() != null ) 
					)
					.collect(
						Collectors.toMap(eachSideCar -> eachSideCar.getRuleSpaceKey(), 
								eachSideCar -> eachSideCar.getRuleGroupSideCars().getRuleGrpName())
						)
					;		
			
			if (!PraeceptaObjectHelper.isObjectEmpty(idsToRuleGrpNameToDelete)) {
				
				idsToRuleGrpNameToDelete.forEach( (spaceKey, grpName) -> deleteByKeyAndName(spaceKey, grpName));
			}
		
		}
	}
	
	@Override
	public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey spaceKey, String ruleGroupName) {
		
		if (spaceKey != null && spaceKey.getVersion() != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGroupName)) {
			Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCars = ruleSideCarMap.get(spaceKey);

			if (!PraeceptaObjectHelper.isObjectEmpty(versionToSideCars)) {
				Collection<PraeceptaSideCarsInfo> sideCars = versionToSideCars.get(spaceKey.getVersion());
				
				Collection<PraeceptaSideCarsInfo> ruleGrpsListAfterExclusion = 
						sideCars.stream().filter(eachSideCar -> !ruleGroupName.equals(eachSideCar.getRuleGroupSideCars().getRuleGrpName()))
						.collect(Collectors.toList());
				
				if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsListAfterExclusion)) {
					
					versionToSideCars.put(spaceKey.getVersion(), ruleGrpsListAfterExclusion);
				}
			}
		}
		
		return false;
	}

	@Override
	public void clear() {
	}

}
