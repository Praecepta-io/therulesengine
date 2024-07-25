package io.praecepta.audit.dto;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.model.PraeceptaAuditEntity;

public class PraeceptaRuleGroupAuditPointSerializationTest {
	
	public static void main(String[] args) {
		
		String inputAuditJson = "{\r\n" + 
				"    \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaAuditEntity\",\r\n" + 
				"    \"entityFieldNameAndValue\": {},\r\n" + 
				"    \"spaceName\": \"XYZ Bank\",\r\n" + 
				"    \"clientId\": \"Risk Management\",\r\n" + 
				"    \"appName\": \"Creadit Risk\",\r\n" + 
				"    \"version\": \"V1\",\r\n" + 
				"    \"ruleGroupAuditPoint\": {\r\n" + 
				"      \"ruleGrpName\": \"Credit_Check\",\r\n" + 
				"      \"ruleOperationAuditPoints\": {\r\n" + 
				"        \"ADD\": [\r\n" + 
				"          {\r\n" + 
				"            \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint\",\r\n" + 
				"            \"ruleName\": \"Score_Check\",\r\n" + 
				"            \"ruleAuditInfo\": {\r\n" + 
				"              \"CONDITION\": [\r\n" + 
				"                {\r\n" + 
				"                  \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint\",\r\n" + 
				"                  \"attributeName\": \"employed\",\r\n" + 
				"                  \"auditElements\": [\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"VALUE_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"toValue\": \"yes\"\r\n" + 
				"                      }\r\n" + 
				"                    },\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"CONDITION_OPERATOR_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"toValue\": \"EQUALS\"\r\n" + 
				"                      }\r\n" + 
				"                    }\r\n" + 
				"                  ]\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                  \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint\",\r\n" + 
				"                  \"attributeName\": \"score\",\r\n" + 
				"                  \"auditElements\": [\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"VALUE_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"toValue\": \"800\"\r\n" + 
				"                      }\r\n" + 
				"                    }\r\n" + 
				"                  ]\r\n" + 
				"                }\r\n" + 
				"              ],\r\n" + 
				"              \"ACTION\": [\r\n" + 
				"                {\r\n" + 
				"                  \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint\",\r\n" + 
				"                  \"attributeName\": \"score_level\",\r\n" + 
				"                  \"auditElements\": [\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"VALUE_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"toValue\": \"Excellent\"\r\n" + 
				"                      }\r\n" + 
				"                    }\r\n" + 
				"                  ]\r\n" + 
				"                }\r\n" + 
				"              ]\r\n" + 
				"            }\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        \"UPDATE\": [\r\n" + 
				"          {\r\n" + 
				"            \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint\",\r\n" + 
				"            \"ruleName\": \"Country_Check\",\r\n" + 
				"            \"ruleAuditInfo\": {\r\n" + 
				"              \"CONDITION\": [\r\n" + 
				"                {\r\n" + 
				"                  \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint\",\r\n" + 
				"                  \"attributeName\": \"region_eligible\",\r\n" + 
				"                  \"auditElements\": [\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"VALUE_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"fromValue\": \"ASIA\",\r\n" + 
				"                        \"toValue\": \"NOARTH_AMERICA\"\r\n" + 
				"                      }\r\n" + 
				"                    },\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"CONDITION_OPERATOR_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"fromValue\": \"NOT_EQUALS\",\r\n" + 
				"                        \"toValue\": \"EQUALS\"\r\n" + 
				"                      }\r\n" + 
				"                    }\r\n" + 
				"                  ]\r\n" + 
				"                }\r\n" + 
				"              ],\r\n" + 
				"              \"ACTION\": [\r\n" + 
				"                {\r\n" + 
				"                  \"_class\": \"io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint\",\r\n" + 
				"                  \"attributeName\": \"score_level\",\r\n" + 
				"                  \"auditElements\": [\r\n" + 
				"                    {\r\n" + 
				"                      \"elementType\": \"VALUE_CHANGE\",\r\n" + 
				"                      \"valueHolder\": {\r\n" + 
				"                        \"toValue\": \"Excellent\"\r\n" + 
				"                      }\r\n" + 
				"                    }\r\n" + 
				"                  ]\r\n" + 
				"                }\r\n" + 
				"              ]\r\n" + 
				"            }\r\n" + 
				"          }\r\n" + 
				"        ]\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }";
		
//		PraeceptaRuleSpaceAuditPoint ruleSpaceAuditPoint = GsonHelper.toEntity(inputAuditJson, PraeceptaRuleSpaceAuditPoint.class);
		
//		System.out.println(ruleSpaceAuditPoint);

		PraeceptaAuditEntity ruleAuditPoint = GsonHelper.toEntity(inputAuditJson, PraeceptaAuditEntity.class);
		System.out.println(ruleAuditPoint);
	}

}
