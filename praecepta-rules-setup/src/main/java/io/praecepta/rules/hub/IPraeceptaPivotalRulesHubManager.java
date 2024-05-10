package io.praecepta.rules.hub;

import java.util.List;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

/**
 * 
 * @author rajasrikar
 *
 */

public interface IPraeceptaPivotalRulesHubManager {
	
	// Start Rule Space Methods Here 
	PraeceptaRuleSpace createRuleSpace(PraeceptaRuleSpaceCompositeKey key);
	
	void createRuleSpace(PraeceptaRuleSpace ruleSpace, String version);
	
	String getLatestVersionForAKey(PraeceptaRuleSpaceCompositeKey key);
	
	String getNextVersionForAKey(PraeceptaRuleSpaceCompositeKey key);

	void createRuleSpace(PraeceptaRuleSpace ruleSpace);

	void updateRuleSpace(PraeceptaRuleSpaceCompositeKey key, PraeceptaRuleSpace ruleSpace);

	void updateRuleSpace(PraeceptaRuleSpace ruleSpace);

	void updateRuleSpace(PraeceptaRuleSpace ruleSpace, boolean overrideTheCurrentVersion);
	
	List<PraeceptaRuleSpace> fetchRuleSpace(PraeceptaRuleSpaceCompositeKey key);
	
	List<PraeceptaRuleSpace> fetchRuleSpaceList();

	PraeceptaRuleSpace fetchRuleSpace(PraeceptaRuleSpaceCompositeKey key, String version);
	
	void deleteRuleSpace(PraeceptaRuleSpaceCompositeKey key, String version);
	
	void deleteRuleSpace(PraeceptaRuleSpaceCompositeKey key, PraeceptaRuleSpace ruleSpace);

	void deleteRuleSpace(PraeceptaRuleSpace ruleSpace);
	// End Rule Space Methods Here 
	
	// Start Rule Group Methods Here 
	boolean createRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName, PraeceptaRuleGroup ruleGrp);

	boolean createRuleGrp(PraeceptaRuleGroup ruleGrp);
	
	boolean updateRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName, PraeceptaRuleGroup ruleGrp);

	PraeceptaRuleGroup fetchRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);
	
	List<PraeceptaRuleGroup> fetchRuleGrps(PraeceptaRuleSpaceCompositeKey spaceKey, String version);
	
	boolean deleteRuleGrp(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);
	
	boolean deleteRuleGrps(PraeceptaRuleSpaceCompositeKey spaceKey, String version);
	
	boolean deleteRuleGrp(PraeceptaRuleGroup ruleGrp);
	// End Rule Group Methods Here

	// Start of  Rule SideCar Methods
	boolean createRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaSideCarsInfo sideCars);

	boolean createRuleSideCars(PraeceptaSideCarsInfo ruleSideCar);

	boolean updateRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaSideCarsInfo ruleSideCar);

	PraeceptaSideCarsInfo fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);

	List<PraeceptaSideCarsInfo> fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version);

	boolean deleteSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);

	boolean deleteRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version);

	boolean deleteRuleSideCars(PraeceptaSideCarsInfo ruleSideCar);
	// End of Rule SideCar Methods
	
	void fetchActiveUniverse();
}
