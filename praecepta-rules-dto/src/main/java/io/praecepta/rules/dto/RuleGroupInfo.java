package io.praecepta.rules.dto;

import java.util.Collection;
import java.util.Objects;

import io.praecepta.core.data.intf.IModel;

public class RuleGroupInfo implements IModel {
    private String ruleGroupName;
    private RuleSpaceInfo ruleSpaceInfo;

    private Collection<RuleCriteriaInfo> criteriaList;

    public String getRuleGroupName() {
        return ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }

    public RuleSpaceInfo getRuleSpaceInfo() {
        return ruleSpaceInfo;
    }

    public void setRuleSpaceInfo(RuleSpaceInfo ruleSpaceInfo) {
        this.ruleSpaceInfo = ruleSpaceInfo;
    }

    public Collection<RuleCriteriaInfo> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(Collection<RuleCriteriaInfo> criteriaList) {
        this.criteriaList = criteriaList;
    }

	@Override
	public int hashCode() {
		return Objects.hash(ruleGroupName, ruleSpaceInfo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleGroupInfo other = (RuleGroupInfo) obj;
		return Objects.equals(ruleGroupName, other.ruleGroupName) && Objects.equals(ruleSpaceInfo, other.ruleSpaceInfo);
	}
}
