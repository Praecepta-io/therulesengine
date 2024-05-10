package io.praecepta.rules.hub.dao.models;

import java.util.Objects;

public class PraeceptaRuleSpaceMetadata {
    private PraeceptaRuleSpaceCompositeKey key;
    private String ruleSpacePath;
    private String ruleGroupPath;
    private String sideCarPath;

    public PraeceptaRuleSpaceCompositeKey getKey() {
        return key;
    }

    public void setKey(PraeceptaRuleSpaceCompositeKey key) {
        this.key = key;
    }

    public String getRuleSpacePath() {
        return ruleSpacePath;
    }

    public void setRuleSpacePath(String ruleSpacePath) {
        this.ruleSpacePath = ruleSpacePath;
    }

    public String getRuleGroupPath() {
        return ruleGroupPath;
    }

    public void setRuleGroupPath(String ruleGroupPath) {
        this.ruleGroupPath = ruleGroupPath;
    }

    public String getSideCarPath() {
        return sideCarPath;
    }

    public void setSideCarPath(String sideCarPath) {
        this.sideCarPath = sideCarPath;
    }

	@Override
	public String toString() {
		return "PraeceptaRuleSpaceMetadata [key=" + key + ", ruleSpacePath=" + ruleSpacePath + ", ruleGroupPath="
				+ ruleGroupPath + ", sideCarPath=" + sideCarPath + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, key.getVersion());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PraeceptaRuleSpaceMetadata other = (PraeceptaRuleSpaceMetadata) obj;
		return Objects.equals(key, other.key) && Objects.equals(key.getVersion(), other.key.getVersion());
	}
	
}
