package io.praecepta.rules.hub;

import java.util.List;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSideCars;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

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
			PraeceptaRuleSideCars sideCars);

	boolean createRuleSideCars(PraeceptaRuleSideCars ruleSideCar);

	boolean updateRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName,
			PraeceptaRuleSideCars ruleSideCar);

	PraeceptaRuleSideCars fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);

	List<PraeceptaRuleSideCars> fetchRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version);

	boolean deleteSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version, String ruleGrpName);

	boolean deleteRuleSideCars(PraeceptaRuleSpaceCompositeKey spaceKey, String version);

	boolean deleteRuleSideCars(PraeceptaRuleSideCars ruleSideCar);
	// End of Rule SideCar Methods
	
	void fetchActiveUniverse();
}
