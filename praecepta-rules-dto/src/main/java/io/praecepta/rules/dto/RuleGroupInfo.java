package io.praecepta.rules.dto;

import java.util.Objects;

public class RuleGroupInfo {
    private String ruleGroupName;
    private RuleSpaceInfo ruleSpaceInfo;


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

    @Override
    public String toString() {
        return "RuleGroupInfo{" +
                "ruleGroupName='" + ruleGroupName + '\'' +
                ", ruleSpaceInfo=" + ruleSpaceInfo +
                '}';
    }
}
