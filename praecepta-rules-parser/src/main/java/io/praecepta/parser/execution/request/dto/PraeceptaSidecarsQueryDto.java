package io.praecepta.parser.execution.request.dto;

import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class PraeceptaSidecarsQueryDto {
	
	private RuleSpaceInfo ruleSpaceInfo;
	private PraeceptaRuleGroupSideCarsInfo sideCarsInfo;
	private String version;
	
	public PraeceptaRuleGroupSideCarsInfo getSideCarsInfo() {
		return sideCarsInfo;
	}
	public void setSideCarsInfo(PraeceptaRuleGroupSideCarsInfo sideCarsInfo) {
		this.sideCarsInfo = sideCarsInfo;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public RuleSpaceInfo getRuleSpaceInfo() {
		return ruleSpaceInfo;
	}
	public void setRuleSpaceInfo(RuleSpaceInfo ruleSpaceInfo) {
		this.ruleSpaceInfo = ruleSpaceInfo;
	}
	@Override
	public String toString() {
		return "PraeceptaSidecarsQueryDto [ruleSpaceInfo=" + ruleSpaceInfo + ", sideCarsInfo=" + sideCarsInfo
				+ ", version=" + version + "]";
	}
}
