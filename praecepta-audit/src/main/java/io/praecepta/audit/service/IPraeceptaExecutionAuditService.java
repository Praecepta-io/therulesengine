package io.praecepta.audit.service;

import java.util.Collection;

import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;

public interface IPraeceptaExecutionAuditService {
	
	void captureRuleGroupExecutionAudit(String spaceName, String clientId, String appName, String version, String groupname, PraeceptaExecutionAuditPoint executionTraceDto);

	void captureRuleGroupExecutionAudits(String spaceName, String clientId, String appName, String version, String groupname, Collection<PraeceptaExecutionAuditPoint> executionTraceDtos);

	PraeceptaExecutionAuditPoints fetchRuleGroupExecutionAudit(String spaceName, String clientId, String appName, String version, String groupname);
	
}
