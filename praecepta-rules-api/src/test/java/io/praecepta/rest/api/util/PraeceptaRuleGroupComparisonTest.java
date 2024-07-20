package io.praecepta.rest.api.util;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.enums.AUDIT_ELEMENT_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement;
import io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PraeceptaRuleGroupComparisonTest {

    @Test
    public void testSimpleCondition(){
        /*PraeceptaRuleGroup ruleGroup = new PraeceptaRuleGroup();
        ruleGroup.setRuleGroupName("LoanEligibilityCheck");
        ruleGroup.setRuleGroupType("simpleCondition");
        ruleGroup.setRuleSpaceKey(new PraeceptaRuleSpaceCompositeKey("ICICI", "001", "Loan"));

        Collection<PraeceptaCriteria> criteriaList = new ArrayList<>();
        PraeceptaCriteria criteria = new PraeceptaCriteria();
        criteria.setRuleName("Age_Validation");

        PraeceptaMultiNestedCondition multiNestedCondition = new PraeceptaMultiNestedCondition();
        PraeceptaMultiCondition multiCondition = new PraeceptaMultiCondition();
        PraeceptaSimpleCondition simpleCondition = new PraeceptaSimpleCondition();
        simpleCondition.setSubjectName("age");
        simpleCondition.setValueToCompare(25);
        multiCondition.setCondition(simpleCondition);
        multiNestedCondition.setMultiCondition(multiCondition);
        criteria.setPredicates(multiNestedCondition);

        Collection<PraeceptaActionDetails> actionDetailsList = new ArrayList<>();
        PraeceptaActionDetails actionDetails = new PraeceptaActionDetails();
        actionDetails.setActionAttributeName("status");
        actionDetails.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
        actionDetails.setValueToAssign("true");
        actionDetailsList.add(actionDetails);

        criteria.setActionToPerform(actionDetailsList);

        criteriaList.add(criteria);

        ruleGroup.setPraeceptaCriterias(criteriaList);

        System.out.println(GsonHelper.toJson(ruleGroup));
*/
        /*Collection<PraeceptaActionDetails> failureActionDetailsList = new ArrayList<>();
        ruleGroup.setActionToPerformOnFailure(failureActionDetailsList);*/

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        System.out.println(praeceptaRuleGroupAuditPoint);
    }


    @Test
    public void testMultipleSimpleCondition(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(3, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(2, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(2, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaAuditElement = auditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals(JoinOperatorType.AND.toString(),praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(JoinOperatorType.OR.toString(),praeceptaAuditElement.getValueHolder().getToValue());

        PraeceptaRuleAttributeAuditPoint salValidation = conditionChanges.get(1);
        Assert.assertEquals("sal", salValidation.getAttributeName());
        Assert.assertEquals(1, salValidation.getAuditElements().size());
        auditElementList = salValidation.getAuditElements();
        praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("10000.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("15000.0",praeceptaAuditElement.getValueHolder().getToValue());




        Assert.assertNull( ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION));


    }

    @Test
    public void testMultipleSimpleConditionWithActionChange(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(3, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION).size());
        List<PraeceptaRuleAttributeAuditPoint> actionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION);
        Assert.assertEquals(1, actionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = actionChanges.get(0);
        Assert.assertEquals("status", ageValidation.getAttributeName());
        Assert.assertEquals(1, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("true",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("false",praeceptaAuditElement.getValueHolder().getToValue());

    }



    @Test
    public void testMultipleSimpleConditionWithFailureActionChange(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(3, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.FAILURE_ACTION).size());
        List<PraeceptaRuleAttributeAuditPoint> actionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.FAILURE_ACTION);
        Assert.assertEquals(1, actionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = actionChanges.get(0);
        Assert.assertEquals("status", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(PraeceptaActionStrategyType.ADD_TO_PAYLOAD.toString(),praeceptaAuditElement.getValueHolder().getToValue());
        praeceptaAuditElement = auditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE,praeceptaAuditElement.getElementType());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("false",praeceptaAuditElement.getValueHolder().getToValue());

    }
}
