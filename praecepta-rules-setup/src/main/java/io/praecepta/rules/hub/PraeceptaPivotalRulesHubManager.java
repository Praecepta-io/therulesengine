package io.praecepta.rules.hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.hub.helper.PraeceptaVersionHelper;

public class PraeceptaPivotalRulesHubManager implements IPraeceptaPivotalRulesHubManager{
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPivotalRulesHubManager.class);
	
	private PraeceptaPivotalRulesHubStore pivotalRuleHubStore;
	
	@Autowired(required = false)
	private IPraeceptaRuleSpaceDao ruleSpaceDao;

	@Autowired(required = false)
	private IPraeceptaRuleGroupDao ruleGroupDao;

	@Autowired(required = false)
	private IPraeceptaRuleSideCarsDao ruleSideCarsDao;

	public PraeceptaPivotalRulesHubManager(IPraeceptaRuleSpaceDao ruleSpaceDao, IPraeceptaRuleGroupDao ruleGroupDao) {
		this.ruleSpaceDao = ruleSpaceDao;
		this.ruleGroupDao = ruleGroupDao;
	}
	public PraeceptaPivotalRulesHubManager(IPraeceptaRuleSpaceDao ruleSpaceDao, IPraeceptaRuleGroupDao ruleGroupDao,IPraeceptaRuleSideCarsDao ruleSideCarsDao) {
		this.ruleSpaceDao = ruleSpaceDao;
		this.ruleGroupDao = ruleGroupDao;
		this.ruleSideCarsDao=ruleSideCarsDao;
	}

	@Override
	public PraeceptaRuleSpace createRuleSpace(PraeceptaRuleSpaceCompositeKey key) {
		logger.info("Inside the Create Rule Space for Key {}", key);

		PraeceptaRuleSpace ruleSpace = null;
		
		if(key != null) {
			
			ruleSpace = new PraeceptaRuleSpace();
			ruleSpace.setRuleSpaceKey(key);

			createRuleSpace(ruleSpace);
		}
		return ruleSpace;
	}

	@Override
	public void createRuleSpace(PraeceptaRuleSpace ruleSpace) {
		logger.debug("Inside the Create Rule Space");

		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) ){
			String version = ruleSpace.getRuleSpaceKey().getVersion();
			
			if(version == null) {
				
				PraeceptaRuleSpaceCompositeKey key = IPraeceptaRuleSpaceDao.getKeyFromRuleSapce(ruleSpace);
				
				if(key != null) {
					// Get the Latest version exist in DB for this Key
					version = getLatestVersionForAKey(key);
					
					logger.info("Here is the Latest Version - {} - Available in DB for Rule Space", version);
					
					if(PraeceptaObjectHelper.isObjectEmpty(version)) {
						
						// Generate a New Version if there is no Version at all in DB for Create
						version = PraeceptaVersionHelper.defaultVersion();
						
						logger.info("Since there is no Version Available in DB, Here is the Default Verison Created Generated", version);
					}
				}
			}
			
			logger.info("Here is the Version {} to be used for Create Rule Space", version);
			createRuleSpace(ruleSpace, version);
		}
				
		
	}

	@Override
	public void createRuleSpace(PraeceptaRuleSpace ruleSpace, String version) {
		logger.info("Inside the Create Rule Space with Version {}", version);
		
		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) && !PraeceptaObjectHelper.isObjectEmpty(version)) {
			
			ruleSpace.getRuleSpaceKey().setVersion(version);
			
			performRuleSpaceUpsert(ruleSpace, true);
		}
	}
	
	@Override
	public void updateRuleSpace(PraeceptaRuleSpace ruleSpace) {
		
		logger.info("Inside Default Update Rule Space method ");
		// Default assume to create a new version always
//		updateRuleSpace(ruleSpace, false);
		updateRuleSpace(ruleSpace, true);
	}

	@Override
	public void updateRuleSpace(PraeceptaRuleSpace ruleSpaceToUpdate, boolean overrideTheCurrentVersion) {
		logger.info("Inside Update Rule Space method with Override Version Falg {}", overrideTheCurrentVersion);
		
		// If the Override the current version is false, then - Get the next version for the Key and Create it
		if(!overrideTheCurrentVersion) {
			
			// Check the current Version exist in DB and Increment it to get a new version for it
			String veriosnToUpdate = getNextVersionForAKey(ruleSpaceToUpdate.getRuleSpaceKey());
			
			ruleSpaceToUpdate.getRuleSpaceKey().setVersion(veriosnToUpdate);
			
			// Create New Incremented Version of the Space
			createRuleSpace(ruleSpaceToUpdate);
			
		} else { // If the Override the current version is true, then - Update that version, if it already exist in DB
			
			PraeceptaRuleSpace ruleSpaceInPersistantLayer = 
					fetchRuleSpace(ruleSpaceToUpdate.getRuleSpaceKey(), ruleSpaceToUpdate.getRuleSpaceKey().getVersion());
			
			// Checking whether the version exist in DB or not
			if(ruleSpaceInPersistantLayer != null){
				
				performRuleSpaceUpsert(ruleSpaceToUpdate, false);
			}
		}
		
	}

	private void performRuleSpaceUpsert(PraeceptaRuleSpace ruleSpace, boolean isAnInsert) {
		logger.debug(" Inside performRuleSpaceUpsert with Upsert Falg {} ", isAnInsert);
		
		Collection<PraeceptaRuleGroup> ruleGrpsToUpdate = ruleSpace.getPraeceptaRuleGrps();
		ruleSpace.setActive(true);
		ruleSpace.setPraeceptaRuleGrps(null);
		
		if(!isAnInsert) {
			ruleSpaceDao.update(ruleSpace);
		} else {
			ruleSpaceDao.insert(ruleSpace);
		}
		
		logger.info(" Rule Space Dao Call is Performed  ");
				
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsToUpdate)) {
			logger.info(" Rule Groups Are Aavilable and About to Trigger Dao Call");
			
			ruleGrpsToUpdate.stream().forEach( eachRuleGrp -> {
				
				if(!isAnInsert) {
					updateRuleGrp(ruleSpace.getRuleSpaceKey(), 
						ruleSpace.getRuleSpaceKey().getVersion(), eachRuleGrp.getRuleGroupName(), eachRuleGrp);
				} else {
					createRuleGrp(eachRuleGrp);
				}
				
				logger.info(" Rule Group Dao Call Trigger is Performed for Rule Group Name {} with Space Key {} ", eachRuleGrp.getRuleGroupName(), ruleSpace.getRuleSpaceKey());
			});
		}
	}

	@Override
	public void updateRuleSpace(PraeceptaRuleSpaceCompositeKey key, PraeceptaRuleSpace ruleSpaceToUpdate) {
		
		if(key != null && ruleSpaceToUpdate != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpaceToUpdate.getRuleSpaceKey())) {
			
			if(key.equals(ruleSpaceToUpdate.getRuleSpaceKey())) {
				
				//This is the Current Version in DB
				String currentVersion = ruleSpaceToUpdate.getRuleSpaceKey().getVersion();
				
				//This is the Version to be updated
				String veriosnToUpdate = key.getVersion();
				
				if(veriosnToUpdate == null || !PraeceptaObjectHelper.stringEquals(currentVersion, veriosnToUpdate)) {
					
					PraeceptaRuleSpace ruleSpaceInPersistantLayer = 
							fetchRuleSpace(ruleSpaceToUpdate.getRuleSpaceKey(), currentVersion);
					
					// Checking whether the current version exist in DB or not
					if(ruleSpaceInPersistantLayer != null){
						
						if(veriosnToUpdate == null) {
							veriosnToUpdate = PraeceptaVersionHelper.generateNextVersionNumber(currentVersion);
						}
						// Assigning new version passed in the Key to Rule Space to be updated
						ruleSpaceToUpdate.getRuleSpaceKey().setVersion(veriosnToUpdate);
						
						performRuleSpaceUpsert(ruleSpaceToUpdate, false);
						
					} else {
						// Create it
						createRuleSpace(ruleSpaceToUpdate);
					}
				}
				
			}
		}
	}

	@Override
	public List<PraeceptaRuleSpace> fetchRuleSpace(PraeceptaRuleSpaceCompositeKey key) {

		if(key != null) {
			return ruleSpaceDao.fetchByKey(key);
		}
		return Collections.emptyList();
	}

	@Override
	public List<PraeceptaRuleSpace> fetchRuleSpaceList() {

		return (List<PraeceptaRuleSpace>) ruleSpaceDao.fetchAll();
	}

	@Override
	public PraeceptaRuleSpace fetchRuleSpace(PraeceptaRuleSpaceCompositeKey key, String version) {
		logger.info(" Fetching the Rule Space for Key {} and Version {}", key, version);
		
		if(validateMandatorySpaceKeyAndVersion(key, version)) {
			
			key.setVersion(version);
			
			PraeceptaRuleSpace ruleSpaceFromDb = ruleSpaceDao.fetchByKeyAndVersion(key, version);
			
			if(ruleSpaceFromDb != null) {
				logger.info(" Rule Space Exist in DB for Key {} and Version {}", key, version);
				
				List<PraeceptaRuleGroup> ruleGrps = ruleGroupDao.fetchByKey(key);
				
				if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrps)) { 
					logger.info(" Rule Groups also Presetn in DB for Key {} and Version {}", key, version);
					ruleSpaceFromDb.setPraeceptaRuleGrps(ruleGrps);
				}
			}
			
			logger.warn(" There is no Rule Space Exists for Key {} and Version {}", key, version);
			return ruleSpaceFromDb;
		}
		
		return null;
	}

	boolean validateMandatorySpaceKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		logger.debug(" Inside validating  Key {} and Version {}", key, version);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(key) && !PraeceptaObjectHelper.isStringEmpty(version)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void deleteRuleSpace(PraeceptaRuleSpaceCompositeKey key, String version) {
		logger.info(" Deleting the Rule Space for Key {} and Version {}", key, version);
		
		ruleSpaceDao.deleteByKeyAndVersion(key, version);
		
	}

	@Override
	public void deleteRuleSpace(PraeceptaRuleSpaceCompositeKey key, PraeceptaRuleSpace ruleSpace) {
		logger.debug(" Inside Delete Rule Space and Key ");

		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) ){
			String version = ruleSpace.getRuleSpaceKey().getVersion();
			
			logger.debug(" Inside Delete Rule Space and Key Having version {} ", version);
			
			if(!PraeceptaObjectHelper.isStringEmpty(version)) {
				
				PraeceptaRuleSpaceCompositeKey compositeKeyFromRuleSpace = IPraeceptaRuleSpaceDao.getKeyFromRuleSapce(ruleSpace);
				
				if(key != null && compositeKeyFromRuleSpace.equals(key)) {
					
					logger.info(" Inside Delete Rule Space and Key Having version {} With Key Mathc to delete ", version);
					deleteRuleSpace(key, version);
				}
			}
		}
		
	}

	@Override
	public void deleteRuleSpace(PraeceptaRuleSpace ruleSpace) {
		logger.debug(" Inside Delete Rule Space");

		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey()) ){
			String version = ruleSpace.getRuleSpaceKey().getVersion();
			
			logger.debug(" Inside Delete Rule Space and Having version {} ", version);
			
			if(!PraeceptaObjectHelper.isStringEmpty(version)) {
				
				PraeceptaRuleSpaceCompositeKey key = IPraeceptaRuleSpaceDao.getKeyFromRuleSapce(ruleSpace);
				
				if(key != null) {
					logger.info(" Inside Delete Rule Space With non empty Key Having ");
					deleteRuleSpace(key, version);
				}
			}else {
				deleteRuleSpace(ruleSpace.getRuleSpaceKey(), version);
			}
		}
		
	}

	@Override
	public String getLatestVersionForAKey(PraeceptaRuleSpaceCompositeKey key) {
		return null;
	}
	
	@Override
	public String getNextVersionForAKey(PraeceptaRuleSpaceCompositeKey key) {
		
		String currentVersion = getLatestVersionForAKey(key);
		
		return PraeceptaVersionHelper.generateNextVersionNumber(currentVersion);
		
	}

	@Override
	public boolean createRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaRuleGroup ruleGrp) {

		logger.info(" Create Rule Group for Space Key With Version {}, Rule Group Name {}", String.join("@@", spaceKey.toString(), version), ruleGrpName);
				
		if(isTheRuleSpaceExist(spaceKey, version)) {
			
			if(ruleGrp != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getPraeceptaCriterias())) {
				
				PraeceptaRuleSpaceCompositeKey spaceKeyFromGrp = ruleGrp.getRuleSpaceKey();
				
				if(spaceKey.equals(spaceKeyFromGrp)) {
					
					if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpName)) {
						ruleGrp.setRuleGroupName(ruleGrpName);
						spaceKeyFromGrp.setVersion(version);
						ruleGrp.setActive(true);
						ruleGroupDao.insert(ruleGrp);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean createRuleGrp(PraeceptaRuleGroup ruleGrp) {
		
		if(ruleGrp != null && ruleGrp.getRuleSpaceKey() != null) {
			return createRuleGrp(ruleGrp.getRuleSpaceKey(), ruleGrp.getRuleSpaceKey().getVersion(), ruleGrp.getRuleGroupName(), ruleGrp);
		}
		
		return false;
	}

	@Override
	public boolean updateRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaRuleGroup ruleGrp) {

		PraeceptaRuleGroup ruleGrpFromDb = fetchRuleGrp(spaceKey, version, ruleGrpName);

		if (ruleGrpFromDb != null) {

			ruleGrp.setRuleGroupName(ruleGrpName);
			spaceKey.setVersion(version);

			if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey())) {
				ruleGrp.setRuleSpaceKey(ruleGrp.getRuleSpaceKey());
			}

			ruleGroupDao.update(ruleGrp);
		}else {
			logger.debug(" Creating new rule group for key {} with Rule groupName-{}",spaceKey,ruleGrpName);
			ruleGrp.setRuleGroupName(ruleGrpName);
			spaceKey.setVersion(version);
			ruleGrp.setRuleSpaceKey(spaceKey);
			ruleGrp.setUniqueId(null);
			ruleGrp.setActive(true);
			ruleGroupDao.insert(ruleGrp);
		}
		return false;
	}

	private boolean isTheRuleSpaceExist(PraeceptaRuleSpaceCompositeKey spaceKey, String version) {
		logger.debug(" Inside Rule Space Exist Or not");
		
		PraeceptaRuleSpace ruleSpace =  fetchRuleSpace(spaceKey, version);
		
		if(ruleSpace != null) {
			logger.info("  Rule Space Exist for Space Key {} and Version {} ", spaceKey, version);
			return true;
		}
		logger.info("  Rule Space Doesn't Exist for Space Key {} and Version {} ", spaceKey, version);
		
		return false;
	}

	@Override
	public PraeceptaRuleGroup fetchRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version,
			String ruleGrpName) {
		logger.debug(" Inside Fetch Rule Group ");
		
		if(isTheRuleSpaceExist(spaceKey, version)) {
		
			logger.info(" Fetching Rule Group for Space Key With Version {}, Rule Group Name {}", String.join("@@", spaceKey.toString(), version), ruleGrpName);
			if(spaceKey != null && !PraeceptaObjectHelper.isObjectEmpty(version) && !PraeceptaObjectHelper.isObjectEmpty(ruleGrpName)) {
				
				spaceKey.setVersion(version);
				
				return ruleGroupDao.fetchByKeyAndName(spaceKey, ruleGrpName);
			}
		}
		
		
		return null;
	}

	@Override
	public List<PraeceptaRuleGroup> fetchRuleGrps(PraeceptaRuleSpaceCompositeKey spaceKey, String version) {
		
		if(isTheRuleSpaceExist(spaceKey, version)) {
			
			if(spaceKey != null && !PraeceptaObjectHelper.isObjectEmpty(version)) {
				
				spaceKey.setVersion(version);
				
				return ruleGroupDao.fetchByKey(spaceKey);
			}
		}
		
		return null;
	}

	@Override
	public boolean deleteRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName) {


		PraeceptaRuleGroup ruleGrpFromDb = fetchRuleGrp(spaceKey, version, ruleGrpName);

		if (ruleGrpFromDb != null) {
			
			return ruleGroupDao.deleteByKeyAndName(spaceKey, ruleGrpName);

		}
		return false;
	}

	@Override
	public boolean deleteRuleGrps(PraeceptaRuleSpaceCompositeKey spaceKey, String version) {
		
		List<PraeceptaRuleGroup> ruleGrpsFromDb = fetchRuleGrps(spaceKey, version);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleGrpsFromDb)) {
			spaceKey.setVersion(version);
			
			ruleGroupDao.deleteById(spaceKey);
		}
		
		return false;
	}

	@Override
	public boolean deleteRuleGrp(PraeceptaRuleGroup ruleGrp) {
		
		if(ruleGrp != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleGroupName()) && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey()) 
				&& !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey().getVersion()) ) {
			
			return deleteRuleGrp(ruleGrp.getRuleSpaceKey(), ruleGrp.getRuleSpaceKey().getVersion(), ruleGrp.getRuleGroupName());
		}
		
		return false;
	}
	@Override
	public boolean createRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaSideCarsInfo sideCars) {

		logger.info(" Create Rule SideCar for Space Key With Version {}, Rule Group Name {}",
				String.join("@@", spaceKey.toString(), version), ruleGrpName);

		if (sideCars != null && !PraeceptaObjectHelper.isObjectEmpty(sideCars.getRuleGroupSideCars())) {

			PraeceptaRuleSpaceCompositeKey spaceKeyFromSideCar = sideCars.getRuleSpaceKey();

			if (spaceKey.equals(spaceKeyFromSideCar)) {

				if (!PraeceptaObjectHelper.isObjectEmpty(ruleGrpName)) {
					sideCars.getRuleGroupSideCars().setRuleGrpName(ruleGrpName);
					spaceKeyFromSideCar.setVersion(version);
					sideCars.setActive(true);
					ruleSideCarsDao.insert(sideCars);
				}
			}
		}

		return false;
	}
	@Override
	public boolean createRuleSideCars(PraeceptaSideCarsInfo ruleSideCar) {
		if(ruleSideCar != null && ruleSideCar.getRuleSpaceKey() != null) {
			ruleSideCar.setActive(true);
			return createRuleSideCars(ruleSideCar.getRuleSpaceKey(), ruleSideCar.getRuleSpaceKey().getVersion(), ruleSideCar.getRuleGroupSideCars().getRuleGrpName(), ruleSideCar);
		}
		return false;
	}
	@Override
	public boolean updateRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaSideCarsInfo ruleSideCar) {
		PraeceptaSideCarsInfo ruleSideCarFrmDB = fetchRuleSideCars(spaceKey, version, ruleGrpName);

		if (ruleSideCarFrmDB != null) {

			ruleSideCar.getRuleGroupSideCars().setRuleGrpName(ruleGrpName);
			spaceKey.setVersion(version);

			if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCar.getRuleSpaceKey())) {
				ruleSideCar.setRuleSpaceKey(ruleSideCar.getRuleSpaceKey());
			}
			ruleSideCarsDao.update(ruleSideCar);
		}else {
			logger.debug(" Creating new rule side car for key {} with Rule groupName-{}",spaceKey,ruleGrpName);
			ruleSideCar.getRuleGroupSideCars().setRuleGrpName(ruleGrpName);
			spaceKey.setVersion(version);
			ruleSideCar.setRuleSpaceKey(spaceKey);
			ruleSideCarsDao.insert(ruleSideCar);
		}
		return false;
	}

	@Override
	public PraeceptaSideCarsInfo fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version,
			String ruleGrpName) {
		logger.debug(" Inside Fetch Rule Side Cars info ");

		logger.info(" Fetching Rule SideCar info for Space Key With Version {}, Rule Group Name {}",
				String.join("@@", spaceKey.toString(), version), ruleGrpName);

		if (spaceKey != null && !PraeceptaObjectHelper.isObjectEmpty(version) && !PraeceptaObjectHelper.isObjectEmpty(ruleGrpName)) {

			spaceKey.setVersion(version);

			return ruleSideCarsDao.fetchByKeyAndName(spaceKey, ruleGrpName);
		}

		return null;
	}
	@Override
	public List<PraeceptaSideCarsInfo> fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version) {
		if(spaceKey != null && !PraeceptaObjectHelper.isObjectEmpty(version)) {

			spaceKey.setVersion(version);

			return ruleSideCarsDao.fetchByKey(spaceKey);
		}
		return null;
	}
	@Override
	public boolean deleteSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName) {
		PraeceptaSideCarsInfo ruleSideCarFromDB = fetchRuleSideCars(spaceKey, version, ruleGrpName);

		if (ruleSideCarFromDB != null) {

			return ruleSideCarsDao.deleteByKeyAndName(spaceKey, ruleGrpName);

		}
		return false;
	}

	@Override
	public boolean deleteRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version) {
		List<PraeceptaSideCarsInfo> ruleSideCarsFrmDB = fetchRuleSideCars(spaceKey, version);

		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSideCarsFrmDB)) {
			spaceKey.setVersion(version);

			ruleSideCarsDao.deleteById(spaceKey);
		}

		return false;
	}
	@Override
	public boolean deleteRuleSideCars(PraeceptaSideCarsInfo ruleSideCar) {
		if(ruleSideCar != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSideCar.getRuleGroupSideCars().getRuleGrpName()) && !PraeceptaObjectHelper.isObjectEmpty(ruleSideCar.getRuleSpaceKey())
				&& !PraeceptaObjectHelper.isObjectEmpty(ruleSideCar.getRuleSpaceKey().getVersion()) ) {

			return deleteSideCars(ruleSideCar.getRuleSpaceKey(), ruleSideCar.getRuleSpaceKey().getVersion(), ruleSideCar.getRuleGroupSideCars().getRuleGrpName());
		}
		return false;
	}
	
	/**
	 * this should be called with Active spaces, groups and keys only
	 */
	@Override
	public void fetchActiveUniverse() {
		
		Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesMap = new HashMap<>();
		
		Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpMap = new HashMap<>();
		
		Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarMap = new HashMap<>();
		
		Collection<PraeceptaRuleSpace> activeRuleSpaces = ruleSpaceDao.fetchAll();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(activeRuleSpaces)) {
			
			activeRuleSpaces.stream().forEach( eachSpace -> {
				
				PraeceptaRuleSpaceCompositeKey spaceKey = IPraeceptaRuleSpaceDao.getKeyFromRuleSapce(eachSpace);
				
				ruleSpacesMap.putIfAbsent(spaceKey, new HashMap<String, PraeceptaRuleSpace>());
				
				Map<String, PraeceptaRuleSpace> versionToRuleSpaceMap = ruleSpacesMap.get(spaceKey);
				
				versionToRuleSpaceMap.put(spaceKey.getVersion(), eachSpace);
			});
		}
		
		Collection<PraeceptaRuleGroup> activeRuleGrps = ruleGroupDao.fetchAll();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(activeRuleGrps)) {
			
			activeRuleGrps.stream().forEach( eachGrp -> {
				
				PraeceptaRuleSpaceCompositeKey spaceKey = IPraeceptaRuleGroupDao.getKeyFromRuleGroup(eachGrp);
				
				ruleGrpMap.putIfAbsent(spaceKey, new HashMap<String, Collection<PraeceptaRuleGroup>>());
				
				Map<String, Collection<PraeceptaRuleGroup>> versionToRuleGrpMap = ruleGrpMap.get(spaceKey);
				
				versionToRuleGrpMap.putIfAbsent(spaceKey.getVersion(), new ArrayList<>());
				
				Collection<PraeceptaRuleGroup> ruleGrpsForAVersion = versionToRuleGrpMap.get(spaceKey.getVersion());
				
				ruleGrpsForAVersion.add(eachGrp);
				
				versionToRuleGrpMap.put(spaceKey.getVersion(), ruleGrpsForAVersion);
			});
		}

		Collection<PraeceptaSideCarsInfo> activeSidecCars = ruleSideCarsDao.fetchAll();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(activeSidecCars)) {
			
			activeSidecCars.stream().forEach( eachSideCar -> {
				
				PraeceptaRuleSpaceCompositeKey spaceKey = IPraeceptaRuleSideCarsDao.getKeyFromSideCar(eachSideCar);
				
				ruleSideCarMap.putIfAbsent(spaceKey, new HashMap<String, Collection<PraeceptaSideCarsInfo>>());
				
				Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCarMap = ruleSideCarMap.get(spaceKey);
				
				versionToSideCarMap.putIfAbsent(spaceKey.getVersion(), new ArrayList<>());
				
				Collection<PraeceptaSideCarsInfo> ruleSideCarsForAVersion = versionToSideCarMap.get(spaceKey.getVersion());
				
				ruleSideCarsForAVersion.add(eachSideCar);
				
				versionToSideCarMap.put(spaceKey.getVersion(), ruleSideCarsForAVersion);
			});
		}
		
		pivotalRuleHubStore = new PraeceptaPivotalRulesHubStore(ruleSpacesMap, ruleGrpMap, ruleSideCarMap);
		
		PraeceptaPivotalRulesHubContextHolder.addHubStore(pivotalRuleHubStore);
		
	}
	
}
