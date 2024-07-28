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

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(1, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());
    }


    @Test
    public void testSimpleCondition_params_delete(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{\"dataType\":\"String\", \"decimalFormat\":\"0.00\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaAuditElement = auditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("{\"dataType\":\"String\",\"decimalFormat\":\"0.00\"}",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("{}",praeceptaAuditElement.getValueHolder().getToValue());



    }

    @Test
    public void testSimpleCondition_params_update(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{\"dataType\":\"String\", \"decimalFormat\":\"0.00\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{\"dataType\":\"Number\", \"decimalFormat\":\"0.000\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaAuditElement = auditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("{\"dataType\":\"String\",\"decimalFormat\":\"0.00\"}",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("{\"dataType\":\"Number\",\"decimalFormat\":\"0.000\"}",praeceptaAuditElement.getValueHolder().getToValue());



    }


    @Test
    public void testSimpleCondition_params_add(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{ \"decimalFormat\":\"0.00\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{\"dataType\":\"Number\", \"decimalFormat\":\"0.000\"}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaAuditElement = auditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("{\"decimalFormat\":\"0.00\"}",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("{\"dataType\":\"Number\",\"decimalFormat\":\"0.000\"}",praeceptaAuditElement.getValueHolder().getToValue());
    }

    @Test
    public void testSimpleCondition_Action_update(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"},{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status2\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(1, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());


        Assert.assertEquals(2, ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION).size());
        List<PraeceptaRuleAttributeAuditPoint> actionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION);
        PraeceptaRuleAttributeAuditPoint actionChange = actionChanges.get(0);
        Assert.assertEquals("status", actionChange.getAttributeName());
        List<PraeceptaAuditElement> auditElements = actionChange.getAuditElements();
        Assert.assertEquals(1, auditElements.size());
        PraeceptaAuditElement auditElement = auditElements.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,auditElement.getElementType());
        Assert.assertEquals("false",auditElement.getValueHolder().getToValue());
        Assert.assertEquals("true",auditElement.getValueHolder().getFromValue());


        actionChange = actionChanges.get(1);
        Assert.assertEquals("status2", actionChange.getAttributeName());
        auditElements = actionChange.getAuditElements();
        Assert.assertEquals(2, auditElements.size());

        auditElement = auditElements.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE,auditElement.getElementType());
        Assert.assertNull(auditElement.getValueHolder().getToValue());
        Assert.assertEquals("ADD_TO_PAYLOAD",auditElement.getValueHolder().getFromValue());

        auditElement = auditElements.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,auditElement.getElementType());
        Assert.assertNull(auditElement.getValueHolder().getToValue());
        Assert.assertEquals("true",auditElement.getValueHolder().getFromValue());


        actionChange = actionChanges.get(1);
    }


    @Test
    public void testSimpleCondition_Action_update_action_params_add(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"},{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status2\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":23,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\",\"additionalParameters\":{\"FROM_DATE_FORMAT\":\"yyyy-MM-DD\"}}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(1, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("23.0",praeceptaAuditElement.getValueHolder().getToValue());


        Assert.assertEquals(2, ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION).size());
        List<PraeceptaRuleAttributeAuditPoint> actionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION);
        PraeceptaRuleAttributeAuditPoint actionChange = actionChanges.get(0);
        Assert.assertEquals("status", actionChange.getAttributeName());
        List<PraeceptaAuditElement> auditElements = actionChange.getAuditElements();
        Assert.assertEquals(2, auditElements.size());
        PraeceptaAuditElement auditElement = auditElements.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,auditElement.getElementType());
        Assert.assertEquals("false",auditElement.getValueHolder().getToValue());
        Assert.assertEquals("true",auditElement.getValueHolder().getFromValue());


        auditElement = auditElements.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE,auditElement.getElementType());
        Assert.assertEquals(null,auditElement.getValueHolder().getFromValue());
        Assert.assertEquals("{\"FROM_DATE_FORMAT\":\"yyyy-MM-DD\"}",auditElement.getValueHolder().getToValue());


        actionChange = actionChanges.get(1);
        Assert.assertEquals("status2", actionChange.getAttributeName());
        auditElements = actionChange.getAuditElements();
        Assert.assertEquals(2, auditElements.size());

        auditElement = auditElements.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE,auditElement.getElementType());
        Assert.assertNull(auditElement.getValueHolder().getToValue());
        Assert.assertEquals("ADD_TO_PAYLOAD",auditElement.getValueHolder().getFromValue());

        auditElement = auditElements.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,auditElement.getElementType());
        Assert.assertNull(auditElement.getValueHolder().getToValue());
        Assert.assertEquals("true",auditElement.getValueHolder().getFromValue());


        actionChange = actionChanges.get(1);
    }


    @Test
    public void testMultipleSimpleCondition(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        Assert.assertNull( auditPointsMap.get(AUDIT_OPERATION_TYPE.ADD));
        Assert.assertNull(auditPointsMap.get(AUDIT_OPERATION_TYPE.DELETE));
        Assert.assertEquals(1, auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE).size());
        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(1, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(2, ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION).size());
        List<PraeceptaRuleAttributeAuditPoint> conditionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(2, conditionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = conditionChanges.get(0);
        Assert.assertEquals("age", ageValidation.getAttributeName());
        Assert.assertEquals(2, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
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
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("10000.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("15000.0",praeceptaAuditElement.getValueHolder().getToValue());

        Assert.assertNull( ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION));


    }

    @Test
    public void testMultipleSimpleConditionWithActionChange(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION).size());
        List<PraeceptaRuleAttributeAuditPoint> actionChanges = ruleAuditPointMap.get(AUDIT_POINT_TYPE.ACTION);
        Assert.assertEquals(1, actionChanges.size());

        PraeceptaRuleAttributeAuditPoint ageValidation = actionChanges.get(0);
        Assert.assertEquals("status", ageValidation.getAttributeName());
        Assert.assertEquals(1, ageValidation.getAuditElements().size());
        List<PraeceptaAuditElement> auditElementList = ageValidation.getAuditElements();
        PraeceptaAuditElement praeceptaAuditElement = auditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertEquals("true",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("false",praeceptaAuditElement.getValueHolder().getToValue());

    }



    @Test
    public void testMultipleSimpleConditionWithFailureActionChange(){

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"subjectName\":\"sal\",\"valueToCompare\":15000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"false\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));


        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> auditPointsMap =  praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();

        List<PraeceptaRuleAuditPoint> ruleLevelAudits = auditPointsMap.get(AUDIT_OPERATION_TYPE.UPDATE);
        Assert.assertEquals(1,ruleLevelAudits.size());
        PraeceptaRuleAuditPoint ruleAuditPoint =  ruleLevelAudits.get(0);
        Assert.assertEquals(2, ruleAuditPoint.getRuleAuditInfo().size());
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
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE,praeceptaAuditElement.getElementType());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("false",praeceptaAuditElement.getValueHolder().getToValue());

    }

    @Test
    public void testMultiCondition() {

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"simpleCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleAuditPointMap = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();
        List<PraeceptaRuleAuditPoint> ruleAuditPoints = ruleAuditPointMap.get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint ruleAuditPoint = ruleAuditPoints.get(0);
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> paeceptaRuleAttributeAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, paeceptaRuleAttributeAuditPointMap.size());

        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = paeceptaRuleAttributeAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, ruleAttributeAuditPoints.size());
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);
        List<PraeceptaAuditElement> praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        PraeceptaAuditElement praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR", praeceptaAuditElement.getValueHolder().getToValue());

    }


    @Test
    public void testMultiCondition_deleting_simple_condition() {

        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"companyLevel\",\"valueToCompare\":\"L2\",\"conditionOperator\":\"EQUAL_CHARS\",\"parameters\":{}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}\n";
        String updatedCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(updatedCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleAuditPointMap = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();
        List<PraeceptaRuleAuditPoint> ruleAuditPoints = ruleAuditPointMap.get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint ruleAuditPoint = ruleAuditPoints.get(0);
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> paeceptaRuleAttributeAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, paeceptaRuleAttributeAuditPointMap.size());

        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = paeceptaRuleAttributeAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);
        List<PraeceptaAuditElement> praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        PraeceptaAuditElement praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals( "AND", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(2, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals( "L2", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getToValue());
        praeceptaAuditElement = praeceptaAuditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals( "EQUAL_CHARS", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getToValue());



        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR", praeceptaAuditElement.getValueHolder().getToValue());

    }

    @Test
    public void testMultiCondition_adding_new_condition() {


        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}\n";
        String newCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"subjectName\":\"companyLevel\",\"valueToCompare\":\"L2\",\"conditionOperator\":\"EQUAL_CHARS\",\"parameters\":{}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"valueToCompare\":10000,\"conditionOperator\":\"EQUAL_NUMBER\",\"parameters\":{}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleAuditPointMap = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();
        List<PraeceptaRuleAuditPoint> ruleAuditPoints = ruleAuditPointMap.get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint ruleAuditPoint = ruleAuditPoints.get(0);
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> paeceptaRuleAttributeAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, paeceptaRuleAttributeAuditPointMap.size());

        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = paeceptaRuleAttributeAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);
        List<PraeceptaAuditElement> praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        PraeceptaAuditElement praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertNull(  praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(2, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertNull(  praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("L2",praeceptaAuditElement.getValueHolder().getToValue());
        praeceptaAuditElement = praeceptaAuditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertNull(  praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("EQUAL_CHARS",praeceptaAuditElement.getValueHolder().getToValue());



        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);
        Assert.assertEquals("MultiCondition1",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND", praeceptaAuditElement.getValueHolder().getToValue());

    }


    @Test
    public void testMultiCondition_adding_multiple_new_condition() {


        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":25.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":10000.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":100000,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":100000,\"parameters\":{}}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}";
        String newCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":25.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":10000.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":150000,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":100000,\"parameters\":{}}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleAuditPointMap = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();
        List<PraeceptaRuleAuditPoint> ruleAuditPoints = ruleAuditPointMap.get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint ruleAuditPoint = ruleAuditPoints.get(0);
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> paeceptaRuleAttributeAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, paeceptaRuleAttributeAuditPointMap.size());

        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = paeceptaRuleAttributeAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);
        Assert.assertEquals("MultiCondition1",praeceptaRuleAttributeAuditPoint.getAttributeName());
        List<PraeceptaAuditElement> praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        PraeceptaAuditElement praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);
        Assert.assertEquals("MultiCondition2",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);
        Assert.assertEquals("loanAmount",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("100000.0", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("150000.0", praeceptaAuditElement.getValueHolder().getToValue());

    }


    @Test
    public void testMultiCondition_adding_multiple_new_condition_2() {


        String existingCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":25.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":10000.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":100000,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount1\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":100000,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"companyLevel\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"L2\",\"parameters\":{}}}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}";
        String newCondition = "{\"ruleGroupName\":\"LoanEligibilityCheck\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":30.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":20000.0,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":150000,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"loanAmount2\",\"conditionOperator\":\"EQUAL_NUMBER\",\"valueToCompare\":200000,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"companyLevel\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"L1\",\"parameters\":{}}}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"status\",\"valueToAssign\":\"true\"}],\"orderNumber\":0,\"ruleName\":\"Age_Validation\"}],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiCondition\"}";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleAuditPointMap = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints();
        List<PraeceptaRuleAuditPoint> ruleAuditPoints = ruleAuditPointMap.get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint ruleAuditPoint = ruleAuditPoints.get(0);
        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> paeceptaRuleAttributeAuditPointMap = ruleAuditPoint.getRuleAuditInfo();
        Assert.assertEquals(1, paeceptaRuleAttributeAuditPointMap.size());

        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = paeceptaRuleAttributeAuditPointMap.get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(9, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);
        Assert.assertEquals("age",praeceptaRuleAttributeAuditPoint.getAttributeName());
        List<PraeceptaAuditElement> praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        PraeceptaAuditElement praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);
        Assert.assertEquals("MultiCondition1",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);
        Assert.assertEquals("sal",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("10000.0" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("20000.0",praeceptaAuditElement.getValueHolder().getToValue());



        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(3);
        Assert.assertEquals("MultiCondition2",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(4);
        Assert.assertEquals("loanAmount",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("100000.0" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("150000.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(5);
        Assert.assertEquals("loanAmount1",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(2, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("EQUAL_NUMBER", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertNull( praeceptaAuditElement.getValueHolder().getToValue());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("100000.0", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertNull( praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(6);
        Assert.assertEquals("loanAmount2",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(2, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(1);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertNull(praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("EQUAL_NUMBER", praeceptaAuditElement.getValueHolder().getToValue());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertNull( praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals( "200000.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(7);
        Assert.assertEquals("MultiCondition4",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND" , praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(8);
        Assert.assertEquals("companyLevel",praeceptaRuleAttributeAuditPoint.getAttributeName());
        praeceptaAuditElementList =  praeceptaRuleAttributeAuditPoint.getAuditElements();
        Assert.assertEquals(1, praeceptaAuditElementList.size());
        praeceptaAuditElement = praeceptaAuditElementList.get(0);
        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("L2", praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("L1", praeceptaAuditElement.getValueHolder().getToValue());

    }

    @Test
    public void testMultiNestedCondition(){

        PraeceptaRuleGroup praeceptaRuleGroup = new PraeceptaRuleGroup();
        praeceptaRuleGroup.setRuleGroupName("Test");
        praeceptaRuleGroup.setRuleGroupType("multiNestedCondition");
        Collection<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
        PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();

        praeceptaCriteria.setRuleName("Rule1");

        PraeceptaMultiNestedCondition multiNestedCondition = new PraeceptaMultiNestedCondition();

        PraeceptaMultiCondition praeceptaMultiCondition = new PraeceptaMultiCondition();
        PraeceptaSimpleCondition simpleCondition = new PraeceptaSimpleCondition();
        simpleCondition.setSubjectName("age");
        simpleCondition.setValueToCompare(25);
        praeceptaMultiCondition.setCondition(simpleCondition);
        PraeceptaMultiCondition nextMultiCondition = new PraeceptaMultiCondition();
        PraeceptaSimpleCondition simpleCondition1 = new PraeceptaSimpleCondition();
        simpleCondition1.setSubjectName("sal");
        simpleCondition1.setValueToCompare(25000);
        nextMultiCondition.setCondition(simpleCondition1);
        praeceptaMultiCondition.setNextMultiCondition(nextMultiCondition);
        praeceptaMultiCondition.setNextConditionJoinOperator(JoinOperatorType.OR);
        multiNestedCondition.setMultiCondition(praeceptaMultiCondition);

        multiNestedCondition.setNextConditionJoinOperator(JoinOperatorType.AND);

        PraeceptaMultiNestedCondition multiNestedCondition1 = new PraeceptaMultiNestedCondition();

        PraeceptaMultiCondition praeceptaMultiCondition1 = new PraeceptaMultiCondition();
        PraeceptaSimpleCondition simpleCondition3 = new PraeceptaSimpleCondition();
        simpleCondition3.setSubjectName("age1");
        simpleCondition3.setValueToCompare(25);
        praeceptaMultiCondition1.setCondition(simpleCondition3);

        PraeceptaMultiCondition nextMultiCondition1 = new PraeceptaMultiCondition();
        PraeceptaSimpleCondition simpleCondition4 = new PraeceptaSimpleCondition();
        simpleCondition4.setSubjectName("sal1");
        simpleCondition4.setValueToCompare(25000);
        nextMultiCondition1.setCondition(simpleCondition4);
        praeceptaMultiCondition.setNextMultiCondition(nextMultiCondition1);
        praeceptaMultiCondition.setNextConditionJoinOperator(JoinOperatorType.OR);
        multiNestedCondition1.setMultiCondition(praeceptaMultiCondition1);

        multiNestedCondition.setNextMultiNestedCondition(multiNestedCondition1);

        praeceptaCriteria.setPredicates(multiNestedCondition);

        praeceptaCriterias.add(praeceptaCriteria);
        praeceptaRuleGroup.setPraeceptaCriterias(praeceptaCriterias);
        PraeceptaRuleSpaceCompositeKey praeceptaRuleSpace = new PraeceptaRuleSpaceCompositeKey();
        praeceptaRuleSpace.setSpaceName("ICICI");
        praeceptaRuleSpace.setAppName("Loan");
        praeceptaRuleSpace.setVersion("V1");
        praeceptaRuleSpace.setClientId("001");
        praeceptaRuleGroup.setRuleSpaceKey(praeceptaRuleSpace);

        Collection<PraeceptaActionDetails> actionToPerform = new ArrayList<>();
        praeceptaRuleGroup.setActionToPerform(actionToPerform);

        Collection<PraeceptaActionDetails> failureActionToPerform = new ArrayList<>();
        praeceptaRuleGroup.setActionToPerformOnFailure(failureActionToPerform);

        System.out.println(GsonHelper.toJson(praeceptaRuleGroup));

        String existingCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        String newCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        List<PraeceptaRuleAuditPoint> ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());
        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(1, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        PraeceptaAuditElement praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());


    }


    @Test
    public void testMultiNestedCondition2(){


        String existingCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        String newCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        List<PraeceptaRuleAuditPoint> ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());
        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(2, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        PraeceptaAuditElement praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());


    }

    @Test
    public void testMultiNestedCondition3(){


        String existingCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        String newCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(1, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        List<PraeceptaRuleAuditPoint> ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());
        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        PraeceptaAuditElement praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());


    }



    @Test
    public void testMultiNestedCondition_Add_New_Rule(){


        String existingCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";
        String newCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"},{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule2\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}";
        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(2, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        List<PraeceptaRuleAuditPoint> ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());
        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        PraeceptaAuditElement praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());


        ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.ADD);

        Assert.assertEquals(1, ruleAuditPoints.size());

        praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals("Rule2",praeceptaRuleAuditPoint.getRuleName());
        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());

        ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(5,ruleAttributeAuditPoints.size());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);

        Assert.assertEquals("sal1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25000.0",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(3);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(4);

        Assert.assertEquals("age1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getToValue());

    }

    @Test
    public void testMultiNestedCondition_Delete_Rule(){



        String existingCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"},{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":25,\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule2\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}";
        String newCondition = "{\"ruleGroupName\":\"Test\",\"active\":false,\"praeceptaCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age\",\"valueToCompare\":30,\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"condition\":{\"subjectName\":\"sal1\",\"valueToCompare\":25000,\"parameters\":{}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiNestedCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"age1\",\"valueToCompare\":25,\"parameters\":{}}}}},\"orderNumber\":0,\"ruleName\":\"Rule1\"}],\"actionToPerform\":[],\"actionToPerformOnFailure\":[],\"ruleSpaceKey\":{\"spaceName\":\"ICICI\",\"clientId\":\"001\",\"appName\":\"Loan\",\"version\":\"V1\"},\"ruleGroupType\":\"multiNestedCondition\"}\n";

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(GsonHelper.toEntity(existingCondition, PraeceptaRuleGroup.class), GsonHelper.toEntity(newCondition, PraeceptaRuleGroup.class));

        Assert.assertEquals(2, praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().size());

        List<PraeceptaRuleAuditPoint> ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.UPDATE);

        Assert.assertEquals(1, ruleAuditPoints.size());
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());
        List<PraeceptaRuleAttributeAuditPoint> ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(3, ruleAttributeAuditPoints.size());

        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        PraeceptaAuditElement praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("30.0",praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getToValue());


        ruleAuditPoints = praeceptaRuleGroupAuditPoint.getRuleOperationAuditPoints().get(AUDIT_OPERATION_TYPE.DELETE);

        Assert.assertEquals(1, ruleAuditPoints.size());

        praeceptaRuleAuditPoint = ruleAuditPoints.get(0);

        Assert.assertEquals("Rule2",praeceptaRuleAuditPoint.getRuleName());
        Assert.assertEquals(1, praeceptaRuleAuditPoint.getRuleAuditInfo().size());

        ruleAttributeAuditPoints = praeceptaRuleAuditPoint.getRuleAuditInfo().get(AUDIT_POINT_TYPE.CONDITION);
        Assert.assertEquals(5,ruleAttributeAuditPoints.size());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(0);

        Assert.assertEquals("age", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getToValue());


        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(1);

        Assert.assertEquals("MultiCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("AND",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(2);

        Assert.assertEquals("sal1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("25000.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(3);

        Assert.assertEquals("MultiNestedCondition1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("OR",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getToValue());

        praeceptaRuleAttributeAuditPoint = ruleAttributeAuditPoints.get(4);

        Assert.assertEquals("age1", praeceptaRuleAttributeAuditPoint.getAttributeName());

        Assert.assertEquals(1,praeceptaRuleAttributeAuditPoint.getAuditElements().size());

        praeceptaAuditElement = praeceptaRuleAttributeAuditPoint.getAuditElements().get(0);

        Assert.assertEquals(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, praeceptaAuditElement.getElementType());
        Assert.assertEquals("25.0",praeceptaAuditElement.getValueHolder().getFromValue());
        Assert.assertEquals(null,praeceptaAuditElement.getValueHolder().getToValue());

    }
}
