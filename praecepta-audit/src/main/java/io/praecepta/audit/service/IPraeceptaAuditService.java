package io.praecepta.audit.service;

import java.util.List;

import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;

public interface IPraeceptaAuditService {
	
	PraeceptaRuleSpaceAuditPoint captureRuleGroupAudit(String spaceName, String clientId, String appName, String version, String groupname, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint);

	List<PraeceptaRuleSpaceAuditPoint> fetchRuleGroupAudit(String spaceName, String clientId, String appName, String version, String groupname);
	
	void refurbishRuleGroupAudit(String uniqueId, PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish);
}
