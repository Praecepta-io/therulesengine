package io.praecepta.rest.api.service;

import java.util.Collection;
import java.util.List;

import io.praecepta.rules.dto.MultiConditionGroupInfo;
import io.praecepta.rules.dto.MultiNestedConditionGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.dto.SimpleConditionGroupInfo;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;

public interface IPraeceptaRulesGroupService {

	public List<RuleSpaceInfo> getRuleSpaceList();

	public String addRuleSpace(RuleSpaceInfo ruleSpaceInfo);

	public String deleteRuleSpace(String spaceName, String clientId, String appName, String version);

	public RuleSpaceInfo getRuleSpace(String spaceName, String clientId, String appName, String version);


	public Collection<PraeceptaRuleGroup> getRuleGroups(String spaceName, String clientId, String appName, String version);
	
	public PraeceptaRuleGroup getRuleGroupByName(String spaceName, String clientId, String appName, String version,String ruleGroupName);
	
	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, SimpleConditionGroupInfo simpleConditionGroupInfo);

	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, MultiConditionGroupInfo multiConditionGroupInfo);

	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, MultiNestedConditionGroupInfo multiNestedConditionGroupInfo);
	
	public boolean deleteRuleGroup(String spaceName, String clientId, String appName, String version,String ruleGroupName);

}
