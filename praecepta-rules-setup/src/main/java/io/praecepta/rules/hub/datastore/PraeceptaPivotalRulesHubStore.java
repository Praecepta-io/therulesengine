package io.praecepta.rules.hub.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

public class PraeceptaPivotalRulesHubStore {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPivotalRulesHubStore.class);
	
	// <PraeceptaRuleSpaceCompositeKey, <Version, Space>>
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesData = new HashMap<>();
	
	// <PraeceptaRuleSpaceCompositeKey, <Version, Rule Grps>>
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpData = new HashMap<>();
	
	// <PraeceptaRuleSpaceCompositeKey, <Version, Rule Side Cars>>
	Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarData = new HashMap<>();
	
	public PraeceptaPivotalRulesHubStore(
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesData,
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpData,
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarData
			) {
		this.ruleSpacesData = ruleSpacesData;
		this.ruleGrpData = ruleGrpData;
		this.ruleSideCarData = ruleSideCarData;
	}

	public Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> getRuleSpacesData() {
		return ruleSpacesData;
	}

	public void setRuleSpacesData(Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesData) {
		this.ruleSpacesData = ruleSpacesData;
	}

	public Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> getRuleGrpData() {
		return ruleGrpData;
	}

	public void setRuleGrpData(Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpData) {
		this.ruleGrpData = ruleGrpData;
	}

	public Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> getRuleSideCarData() {
		return ruleSideCarData;
	}

	public void setRuleSideCarData(
			Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarData) {
		this.ruleSideCarData = ruleSideCarData;
	}
	
	public Collection<PraeceptaRuleSpace> getAllActiveRuleSpaces(){
		logger.debug("Inside getAllActiveRuleSpaces");
		
		Collection<PraeceptaRuleSpace> activeRuleSpaces = new ArrayList<>();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpacesData)) {
			
			logger.info("Getting All Active RuleSpaces ");
			
			ruleSpacesData.forEach( (spaceKey, versionWithSpace) -> {
				activeRuleSpaces.addAll(versionWithSpace.values());
			});
			
			logger.info("Active RuleSpaces size --> {}",activeRuleSpaces.size());
		}
		
		logger.debug("Exiting getAllActiveRuleSpaces");
		return activeRuleSpaces;
		
	}
	
	public Collection<PraeceptaRuleGroup> getAllActiveRuleGrps(){
		logger.debug("Inside getAllActiveRuleGrps");
		
		Collection<PraeceptaRuleGroup> activeRuleGrps = new ArrayList<>();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpData)) {
			
			logger.info("Getting All Active Rule Groups ");
			
			ruleGrpData.forEach( (spaceKey, versionWithGrp) -> {
				Collection<Collection<PraeceptaRuleGroup>> ruleGrpsForAVersion = versionWithGrp.values();
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsForAVersion)) {
					ruleGrpsForAVersion.forEach( ruleGrpForAVersion -> {
						
						activeRuleGrps.addAll(ruleGrpForAVersion);
					});
				}
			});
			
			logger.info("Active Rule Grp size --> {}",activeRuleGrps.size());
		}
		
		logger.debug("Exiting getAllActiveRuleGrps");
		
		return activeRuleGrps;
		
	}
	
	public Collection<PraeceptaSideCarsInfo> getAllActiveRuleSideCars(){
		logger.debug("Inside getAllActiveRuleSideCars");
		
		Collection<PraeceptaSideCarsInfo> activeRuleSideCars = new ArrayList<>();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarData)) {
			
			logger.info("Getting All Active Rule Side Cars ");
			
			ruleSideCarData.forEach( (spaceKey, versionWithSideCar) -> {
				
				Collection<Collection<PraeceptaSideCarsInfo>> ruleSideCarsForAVersion = versionWithSideCar.values();
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarsForAVersion)) {
					
					ruleSideCarsForAVersion.forEach( ruleSideCarForAVersion -> {
						
						activeRuleSideCars.addAll(ruleSideCarForAVersion);
					});
				}
				
			});
			
			logger.info("Active Rule Side Cars Size --> {}",activeRuleSideCars.size());
		}
		
		logger.debug("Exiting getAllActiveRuleSideCars");
		
		return activeRuleSideCars;
		
	}
	

	public Collection<PraeceptaRuleSpace> getAllActiveRuleSpacesWithRuleGrp(){
		logger.debug("Inside getAllActiveRuleSpacesWithRuleGrp");
		
		Collection<PraeceptaRuleSpace> activeRuleSpaces = Collections.emptyList();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpacesData)) {
			
			logger.info(" Collecting Rule Space and Rule Group Data ");
			ruleSpacesData.forEach( (spaceKey, versionWithSpace) -> {
				
				Map<String, Collection<PraeceptaRuleGroup>> ruleGrpsWithDifferentVersionsForAKey = ruleGrpData.get(spaceKey);
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsWithDifferentVersionsForAKey)) {
					
					versionWithSpace.forEach( (VERSION, SPACE) -> {
						
						logger.info(" Collecting Rule Group Data for Rule Space - {} and Version - {}", SPACE, VERSION); 
						Collection<PraeceptaRuleGroup> ruleGrpsForAVersion = ruleGrpsWithDifferentVersionsForAKey.get(VERSION);
						
						if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsForAVersion)) {
							logger.info(" Size of Rule Groups for Rule Space and Version - {} is ", SPACE + VERSION , ruleGrpsForAVersion.size());
							SPACE.setPraeceptaRuleGrps(ruleGrpsForAVersion);
						}
					});
				}
				
			});
		}
		
		logger.debug("Exiting getAllActiveRuleSpacesWithRuleGrp");
		return activeRuleSpaces;
		
	}
	
	public PraeceptaRuleSpace getActiveRuleSpacesWithVersion(PraeceptaRuleSpaceCompositeKey compositeKey, String version){
		logger.debug("Inside getActiveRuleSpacesWithVersion");
		
		PraeceptaRuleSpace ruleSpace = null;
		
		logger.info(" Collecting Rule Space {} and Version {} ", compositeKey, version);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpacesData) && !PraeceptaObjectHelper.isObjectEmpty(compositeKey) && !PraeceptaObjectHelper.isObjectEmpty(version)) {
			
			Map<String, PraeceptaRuleSpace> ruleSpaceWithVersions = ruleSpacesData.get(compositeKey);
			
			logger.info(" Rule Space With Versions Available");
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceWithVersions)) {
				
				ruleSpace = ruleSpaceWithVersions.get(version);
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleSpace)) {
					logger.info(" Rule Space With Version Exit");
					
					logger.info("  Here are the Rule Space Retrived --> ", ruleSpace); 
				} else {
					logger.info(" Rule Space With Version Doesn't Exit");
				}
				
			}
		}
		
		logger.debug("Exiting getActiveRuleSpacesWithVersion");
		return ruleSpace;
		
	}
	
	public PraeceptaRuleSpace getActiveRuleSpacesWithRuleGrps(PraeceptaRuleSpaceCompositeKey compositeKey, String version){
		logger.debug("Inside getActiveRuleSpacesWithRuleGrps");
		
		PraeceptaRuleSpace ruleSpace = getActiveRuleSpacesWithVersion(compositeKey, version);
		logger.info(" Collecting Groups Information for a Rule Space {} and Version {} ", compositeKey, version);
		
		if(ruleSpace != null) {
			
			Map<String, Collection<PraeceptaRuleGroup>> ruleGrpsWithDifferentVersionsForAKey = ruleGrpData.get(compositeKey);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsWithDifferentVersionsForAKey)) {
				logger.info(" Rule Grps with With Versions Available for the Space ");
				
				Collection<PraeceptaRuleGroup> ruleGrpsForAVersion = ruleGrpsWithDifferentVersionsForAKey.get(version);
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsForAVersion)) {
					logger.info(" Rule Groups Exit for a Rule Space {} and Version {} ", compositeKey, version);
					
					ruleSpace.setPraeceptaRuleGrps(ruleGrpsForAVersion);
					logger.info("  Here are the Rule Groups are added --> {} ", ruleGrpsForAVersion); 
				}
			}
		}
		
		logger.debug("Exiting getActiveRuleSpacesWithRuleGrps");
		return ruleSpace;
	}
	
	public PraeceptaRuleGroup getRuleGrpForAndSpaceVersionAndGroupName(PraeceptaRuleSpaceCompositeKey compositeKey, String version, String ruleGroupName){
		logger.debug("Inside getRuleGrpForAndSpaceVersionAndGroupName");
		
		PraeceptaRuleSpace ruleSpace = getActiveRuleSpacesWithRuleGrps(compositeKey, version);
		
		PraeceptaRuleGroup ruleGrp = null;
		
		if (!PraeceptaObjectHelper.isObjectNull(ruleSpace)) {
			
			logger.info(" Filtering Rule Group Name - {} from Sapce ", ruleGroupName);
			Optional<PraeceptaRuleGroup> ruleGroup = ruleSpace.getPraeceptaRuleGrps().stream()
					.filter(obj -> obj.getRuleGroupName().equalsIgnoreCase(ruleGroupName)).findFirst();
			
			if (ruleGroup != null && ruleGroup.isPresent()) {
				logger.info(" Rule Group Name - {} present in Sapce ", ruleGroupName);
				ruleGrp = ruleGroup.get();
			}
		}
		logger.info("  Here is Rule Group Retrived {} ", ruleGrp); 
		
		logger.debug("Exiting getRuleGrpForAndSpaceVersionAndGroupName");
		return ruleGrp;
		
	}
	
	public Collection<PraeceptaSideCarsInfo> getActiveSidecarsForARuleSpaces(PraeceptaRuleSpaceCompositeKey compositeKey, String version){
		logger.debug("Inside getActiveSidecarsForARuleSpaces");
		
		Collection<PraeceptaSideCarsInfo> sideCarsForAKeyAndVersion = Collections.emptyList();
		
		PraeceptaRuleSpace ruleSpace = getActiveRuleSpacesWithVersion(compositeKey, version);
		
		if(ruleSpace != null) {
			logger.info(" Rule Space Present to pull Side cars information ");
			
			Map<String, Collection<PraeceptaSideCarsInfo>> sideCarsWithDifferentVersionsForAKey = ruleSideCarData.get(compositeKey);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(sideCarsWithDifferentVersionsForAKey)) {
				logger.info(" Side Cars exist with Multiple Verisons for Space Key {} ", compositeKey);
				
				sideCarsForAKeyAndVersion = sideCarsWithDifferentVersionsForAKey.get(version);
				
				if(!PraeceptaObjectHelper.isObjectEmpty(sideCarsForAKeyAndVersion)) {
					logger.info("  Side Cars exist for Version {}", version);
				} else {
					logger.info("  Side Cars Doesn't exist for Version {}", version);
				}
			}
		}
				
		logger.info("  Here are the Side Cars info Retrived {} ", sideCarsForAKeyAndVersion); 
		
		logger.debug("Exiting getActiveSidecarsForARuleSpaces");
		return sideCarsForAKeyAndVersion;
	}
	
	public PraeceptaSideCarsInfo getSideCarsForSpaceAndVersionAndGroupName(PraeceptaRuleSpaceCompositeKey compositeKey, String version, String ruleGroupName){
		logger.debug("Inside getSideCarsForSpaceAndVersionAndGroupName");
		
		Collection<PraeceptaSideCarsInfo> sideCarsForAKeyAndVersion = getActiveSidecarsForARuleSpaces(compositeKey, version);
		
		PraeceptaSideCarsInfo sideCarsToReturn = null;
		
		if (!PraeceptaObjectHelper.isObjectNull(sideCarsForAKeyAndVersion)) {
			
			logger.info("  Filtering the Side Cars for Rule grp name - {} ", ruleGroupName);
					
			Optional<PraeceptaSideCarsInfo> sideCarsForARuleGrpName = sideCarsForAKeyAndVersion.stream()
					.filter(sideCars -> 
						(
							sideCars.getRuleGroupSideCars() != null &&
							sideCars.getRuleGroupSideCars().getRuleGrpName().equalsIgnoreCase(ruleGroupName)
						)
					).findFirst();
			
			if (sideCarsForARuleGrpName != null && sideCarsForARuleGrpName.isPresent()) {
				sideCarsToReturn = sideCarsForARuleGrpName.get();
				
				logger.info("  Here is the Filtered Side Car - {} for Rule grp name - {} ", sideCarsToReturn, ruleGroupName);
			}
		}
		
		logger.debug("Exiting getSideCarsForSpaceAndVersionAndGroupName");
		return sideCarsToReturn;
		
	}
	
	
}
