package io.praecepta.dao.elastic.enums.execution;

import io.praecepta.dao.elastic.model.execution.PraeceptaProcessTracer;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleGroupExecutionAuditPoint;

public enum EXECUTION_AUDIT_OPERATION_TYPE {
	
	PRE_RULE_GROUP_HAWK_EYE, POST_RULE_GROUP_HAWK_EYE, PRE_RULE_HAWK_EYE, POST_RULE_HAWK_EYE;

	public static PraeceptaProcessTracer getExecutionAuditPoint(EXECUTION_AUDIT_OPERATION_TYPE operationType) {

		switch (operationType) {

		case PRE_RULE_GROUP_HAWK_EYE:

			return new PraeceptaRuleGroupExecutionAuditPoint(PRE_RULE_GROUP_HAWK_EYE);
			
		case POST_RULE_GROUP_HAWK_EYE:
			
			return new PraeceptaRuleGroupExecutionAuditPoint(POST_RULE_GROUP_HAWK_EYE);
			
		case PRE_RULE_HAWK_EYE:
			
			return new PraeceptaRuleExecutionAuditPoint(PRE_RULE_HAWK_EYE);
			
		case POST_RULE_HAWK_EYE:
			
			return new PraeceptaRuleExecutionAuditPoint(POST_RULE_HAWK_EYE);

		default:
			return null;
		}

	}
}
