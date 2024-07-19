package io.praecepta.dao.elastic.model;

public class PraeceptaRuleSpaceAuditPoint {
	
	private String spaceName;
	
	private String clientId;
	
	private String appName;
	
	private String ruleGroupName;

	private String version = "V1";
	
	private PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint;

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRuleGroupName() {
		return ruleGroupName;
	}

	public void setRuleGroupName(String ruleGroupName) {
		this.ruleGroupName = ruleGroupName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public PraeceptaRuleGroupAuditPoint getRuleGroupAuditPoint() {
		return ruleGroupAuditPoint;
	}

	public void setRuleGroupAuditPoint(PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {
		this.ruleGroupAuditPoint = ruleGroupAuditPoint;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSpaceAuditPoint [spaceName=" + spaceName + ", clientId=" + clientId + ", appName="
				+ appName + ", ruleGroupName=" + ruleGroupName + ", version=" + version + ", ruleGroupAuditPoint="
				+ ruleGroupAuditPoint + "]";
	}
	
}
