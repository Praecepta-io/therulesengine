package io.praecepta.dao.elastic.model;

import java.util.List;
import java.util.Map;

import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;

public class PraeceptaRuleAttributeAuditPoint {
	
	private String attributeName;
	
	private List<PraeceptaAuditElement> auditElements;
	
	public PraeceptaRuleAttributeAuditPoint(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public List<PraeceptaAuditElement> getAuditElements() {
		return auditElements;
	}

	public void setAuditElements(List<PraeceptaAuditElement> auditElements) {
		this.auditElements = auditElements;
	}
	
}
