package io.praecepta.rest.api.util;

import io.praecepta.dao.elastic.enums.AUDIT_ELEMENT_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement;
import io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.JoinOperatorType;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

import java.util.*;

public class PraeceptaRuleGroupComparison {

    public static PraeceptaRuleGroupAuditPoint compare(PraeceptaRuleGroup existingRuleGroup, PraeceptaRuleGroup updatedRuleGroup){
        Map<String, PraeceptaCriteria> ruleCriteriasMap = getCriteriasByRuleName(updatedRuleGroup.getPraeceptaCriterias());
        Map<String, PraeceptaCriteria> existingRuleCriteriasMap = getCriteriasByRuleName(existingRuleGroup.getPraeceptaCriterias());

        Collection<PraeceptaCriteria> deletedCriterias = deletedCriterias(existingRuleCriteriasMap, ruleCriteriasMap);
        Collection<PraeceptaCriteria> newCriterias = newCriterias(existingRuleCriteriasMap, ruleCriteriasMap);


        Map<PraeceptaCriteria, PraeceptaCriteria> modifiedCriterias = modifiedCriterias(existingRuleCriteriasMap, ruleCriteriasMap);

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = new PraeceptaRuleGroupAuditPoint(updatedRuleGroup.getRuleGroupName());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleOperationAuditPoints = new HashMap<>();
        List<PraeceptaRuleAuditPoint> addedRulesAudit = new ArrayList<>();
        newCriterias.forEach(criteria->{
            addedRulesAudit.add(getRuleAuditPoint(null, criteria));
        });
        if(addedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.ADD, addedRulesAudit);
        }
        List<PraeceptaRuleAuditPoint> deletedRulesAudit = new ArrayList<>();
        deletedCriterias.forEach(criteria->{
            addedRulesAudit.add(getRuleAuditPoint(criteria, null));
        });

        if(deletedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.DELETE, deletedRulesAudit);
        }

        List<PraeceptaRuleAuditPoint> modifiedRulesAudit = new ArrayList<>();
        modifiedCriterias.forEach((existingCriteria, newCriteria)->{
            modifiedRulesAudit.add(getRuleAuditPoint(existingCriteria, newCriteria));
        });

        if(modifiedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.UPDATE, modifiedRulesAudit);
        }
        praeceptaRuleGroupAuditPoint.setRuleOperationAuditPoints(ruleOperationAuditPoints);


        return praeceptaRuleGroupAuditPoint;
    }




    private static PraeceptaRuleAuditPoint getRuleAuditPoint(PraeceptaCriteria existingCriteria, PraeceptaCriteria newCriteria) {
        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = new PraeceptaRuleAuditPoint(existingCriteria.getRuleName());

        Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAditMap = new HashMap<>();
        if(existingCriteria != null && newCriteria != null) {
            ruleAditMap.put(AUDIT_POINT_TYPE.CONDITION, getConditionAuditPoint(existingCriteria.getPredicates(), newCriteria.getPredicates()));
            ruleAditMap.put(AUDIT_POINT_TYPE.ACTION, getActionAuditPoint(existingCriteria.getActionToPerform(), newCriteria.getActionToPerform()));
            ruleAditMap.put(AUDIT_POINT_TYPE.FAILURE_ACTION, getActionAuditPoint(existingCriteria.getActionToPerformOnFailure(), newCriteria.getActionToPerformOnFailure()));
        }else if(existingCriteria != null){
            ruleAditMap.put(AUDIT_POINT_TYPE.CONDITION, getConditionAuditPoint(existingCriteria.getPredicates(), null));
            ruleAditMap.put(AUDIT_POINT_TYPE.ACTION, getActionAuditPoint(existingCriteria.getActionToPerform(), null));
            ruleAditMap.put(AUDIT_POINT_TYPE.FAILURE_ACTION, getActionAuditPoint(existingCriteria.getActionToPerformOnFailure(), null));
        }else{
            ruleAditMap.put(AUDIT_POINT_TYPE.CONDITION, getConditionAuditPoint(null, newCriteria.getPredicates()));
            ruleAditMap.put(AUDIT_POINT_TYPE.ACTION, getActionAuditPoint(null, newCriteria.getActionToPerform()));
            ruleAditMap.put(AUDIT_POINT_TYPE.FAILURE_ACTION, getActionAuditPoint(null,  newCriteria.getActionToPerformOnFailure()));
        }

        praeceptaRuleAuditPoint.setRuleAuditInfo(ruleAditMap);

        return praeceptaRuleAuditPoint;
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getActionAuditPoint(Collection<PraeceptaActionDetails> existingActionToPerform, Collection<PraeceptaActionDetails> actionToPerform) {
        List<PraeceptaRuleAttributeAuditPoint> attributeAuditPoints = new ArrayList<>();
        Map<String, PraeceptaActionDetails> existingActionMap = new HashMap<>();
        if(existingActionToPerform != null) {
            existingActionToPerform.forEach(action -> {
                existingActionMap.put(action.getActionAttributeName(), action);
            });
        }

        Map<String, PraeceptaActionDetails> updatedActionMap = new HashMap<>();
        if(actionToPerform != null) {
            actionToPerform.forEach(action -> {
                updatedActionMap.put(action.getActionAttributeName(), action);
            });
        }

        if(existingActionToPerform != null && actionToPerform != null){
            existingActionToPerform.forEach(action->{
                PraeceptaRuleAttributeAuditPoint actionAttributeAudit = new PraeceptaRuleAttributeAuditPoint(action.getActionAttributeName());
                if(updatedActionMap.get(action.getActionAttributeName()) == null){
                    List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                    populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy(), null), auditElements);
                    populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), null), auditElements);

                    compareActionMetaData(action.getAdditionalParameters(), null, auditElements);
                    actionAttributeAudit.setAuditElements(auditElements);
                }else{
                    List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                    PraeceptaActionDetails newActionDetails = updatedActionMap.get(action.getActionAttributeName());
                    if(!action.getActionStrategy().equals(newActionDetails.getActionStrategy())) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy(), newActionDetails.getActionStrategy()), auditElements);
                    }
                    if(!action.getValueToAssign().equals(newActionDetails.getValueToAssign())) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), newActionDetails.getValueToAssign()), auditElements);
                    }

                    compareActionMetaData(action.getAdditionalParameters(), newActionDetails.getAdditionalParameters(), auditElements);

                    actionAttributeAudit.setAuditElements(auditElements);
                }
                if(actionAttributeAudit.getAuditElements().size() > 0)
                    attributeAuditPoints.add(actionAttributeAudit);
            });
        }else if(existingActionToPerform != null){
            existingActionToPerform.forEach(action->{
                PraeceptaRuleAttributeAuditPoint actionAttributeAudit = new PraeceptaRuleAttributeAuditPoint(action.getActionAttributeName());
                List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy(), null), auditElements);
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), null), auditElements);

                compareActionMetaData(action.getAdditionalParameters(), null, auditElements);
                actionAttributeAudit.setAuditElements(auditElements);
                if(actionAttributeAudit.getAuditElements().size() > 0)
                    attributeAuditPoints.add(actionAttributeAudit);
            });
        }else{
            actionToPerform.forEach(action->{
                PraeceptaRuleAttributeAuditPoint actionAttributeAudit = new PraeceptaRuleAttributeAuditPoint(action.getActionAttributeName());
                List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(null, action.getActionStrategy()), auditElements);
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(null, action.getValueToAssign()), auditElements);

                compareActionMetaData(null, action.getAdditionalParameters(), auditElements);
                actionAttributeAudit.setAuditElements(auditElements);
                if(actionAttributeAudit.getAuditElements().size() > 0)
                    attributeAuditPoints.add(actionAttributeAudit);
            });
        }
        if(attributeAuditPoints.size() > 0)
            return attributeAuditPoints;
        else
            return null;
    }

    private static void compareActionMetaData(Map<PraeceptaActionParametersType, Object> additionalParameters, Map<PraeceptaActionParametersType, Object> newAdditionalParameters, List<PraeceptaAuditElement> auditElements) {
        if(additionalParameters != null && newAdditionalParameters != null){
            Set<PraeceptaActionParametersType> existingParametrKeySet =  additionalParameters.keySet();
            existingParametrKeySet.forEach(key->{
                if(newAdditionalParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key), null), auditElements);
                }else{
                    if(!additionalParameters.get(key).equals(newAdditionalParameters.get(key))) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key), newAdditionalParameters.get(key)), auditElements);
                    }
                }
            });

            Set<PraeceptaActionParametersType> conditionalParameterKeySet =  newAdditionalParameters.keySet();
            conditionalParameterKeySet.forEach(key->{
                if(additionalParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, newAdditionalParameters.get(key)), auditElements);
                }
            });
        }else if(additionalParameters != null){
            Set<PraeceptaActionParametersType> existingParametrKeySet =  additionalParameters.keySet();
            existingParametrKeySet.forEach(key-> {
                populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key), null), auditElements);
            });
        }else if(newAdditionalParameters != null){
            Set<PraeceptaActionParametersType> conditionalParameterSet =  newAdditionalParameters.keySet();
            conditionalParameterSet.forEach(key-> {
                populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, additionalParameters.get(key)), auditElements);
            });
        }
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getConditionAuditPoint(PraeceptaMultiNestedCondition existingPredicates, PraeceptaMultiNestedCondition newPredicates) {
        List<PraeceptaRuleAttributeAuditPoint> attributeAuditPoints = new ArrayList<>();
        if(existingPredicates != null && newPredicates != null){
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(existingPredicates.getMultiCondition(), newPredicates.getMultiCondition()));
            if(existingPredicates.getNextMultiNestedCondition() != null){
                attributeAuditPoints.addAll(getConditionAuditPoint(existingPredicates.getNextMultiNestedCondition(), null));
            }
        }else if(existingPredicates == null){
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(existingPredicates.getMultiCondition(), null));
            if(existingPredicates.getNextMultiNestedCondition() != null){
                attributeAuditPoints.addAll(getConditionAuditPoint(existingPredicates.getNextMultiNestedCondition(), null));
            }
        }else{
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(null,newPredicates.getMultiCondition()));
            if(existingPredicates.getNextMultiNestedCondition() != null){
                attributeAuditPoints.addAll(getConditionAuditPoint(null, newPredicates.getNextMultiNestedCondition()));
            }
        }
        if((existingPredicates != null && existingPredicates.getNextMultiNestedCondition() != null) || (newPredicates != null && newPredicates.getNextMultiNestedCondition() != null)){
            attributeAuditPoints.addAll(getConditionAuditPoint(existingPredicates != null?existingPredicates.getNextMultiNestedCondition():null, newPredicates!=null?newPredicates.getNextMultiNestedCondition():null));
        }

        if(attributeAuditPoints.size() > 0)
            return attributeAuditPoints;
        else
            return null;
    }

    private static PraeceptaRuleAttributeAuditPoint getRuleAttributePoint(PraeceptaSimpleCondition existingSimpleCondition, PraeceptaSimpleCondition simpleCondition){

        if(simpleCondition != null && existingSimpleCondition != null) {
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(simpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            if (existingSimpleCondition.getValueToCompare() != null && !existingSimpleCondition.getValueToCompare().equals(simpleCondition.getValueToCompare())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare(), simpleCondition.getValueToCompare()), auditElements);
            }
            if (existingSimpleCondition.getConditionOperator() != null && !existingSimpleCondition.getConditionOperator().equals(simpleCondition.getConditionOperator())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator(), simpleCondition.getConditionOperator()), auditElements);
            }

            if (existingSimpleCondition.getNextConditionJoinOperator() != null && !existingSimpleCondition.getNextConditionJoinOperator().equals(simpleCondition.getNextConditionJoinOperator())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getNextConditionJoinOperator(), simpleCondition.getNextConditionJoinOperator()), auditElements);
            }

            compareMetaData(existingSimpleCondition.getParameters(), simpleCondition.getParameters(), auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(existingSimpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(existingSimpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare(), null), auditElements);
            populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator(), null), auditElements);

            compareMetaData(existingSimpleCondition.getParameters(), null, auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(simpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(simpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getValueToCompare()), auditElements);
            populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getConditionOperator()), auditElements);

            compareMetaData(null, simpleCondition.getParameters(), auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }

        return null;
    }

    private static void compareMetaData(Map<String, Object> existingConditionParameters, Map<String, Object> conditionParameters, List<PraeceptaAuditElement> auditElements) {
        if(existingConditionParameters != null && conditionParameters != null){
            Set<String> existingParametrKeySet =  existingConditionParameters.keySet();
            existingParametrKeySet.forEach(key->{
                if(conditionParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key), null), auditElements);
                }else{
                    if(!existingConditionParameters.get(key).equals(conditionParameters.get(key))) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key), conditionParameters.get(key)), auditElements);
                    }
                }
            });

            Set<String> conditionalParameterKeySet =  conditionParameters.keySet();
            conditionalParameterKeySet.forEach(key->{
                if(existingConditionParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, conditionParameters.get(key)), auditElements);
                }
            });
        }else if(existingConditionParameters != null){
            Set<String> existingParametrKeySet =  existingConditionParameters.keySet();
            existingParametrKeySet.forEach(key-> {
                populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key), null), auditElements);
            });
        }else{
            Set<String> conditionalParameterSet =  conditionParameters.keySet();
            conditionalParameterSet.forEach(key-> {
                populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, existingConditionParameters.get(key)), auditElements);
            });
        }
    }

    private static void populateAuditElement(AUDIT_ELEMENT_TYPE mataDataChange, PraeceptaAuditElement.ValueHolder existingConditionParameters, List<PraeceptaAuditElement> auditElements) {
        PraeceptaAuditElement praeceptaAuditElement = new PraeceptaAuditElement();
        praeceptaAuditElement.setElementType(mataDataChange);
        praeceptaAuditElement.setValueHolder(existingConditionParameters);
        auditElements.add(praeceptaAuditElement);
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getMultiConditionAuditPoint(PraeceptaMultiCondition existingCondition, PraeceptaMultiCondition multiCondition){
        List<PraeceptaRuleAttributeAuditPoint> multiAttributeAuditPoints = new ArrayList<>();
        if(existingCondition != null && multiCondition != null) {
            multiAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingCondition.getCondition(), multiCondition.getCondition()));

            if(existingCondition.getNextConditionJoinOperator() != null){
                if(!existingCondition.getNextConditionJoinOperator().equals(multiCondition.getNextConditionJoinOperator())){
                    PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(existingCondition.getNextConditionJoinOperator(), multiCondition.getNextConditionJoinOperator());
                    multiAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
                }
            }

            if (existingCondition.getNextMultiCondition() != null || multiCondition.getNextMultiCondition() != null) {
                multiAttributeAuditPoints.addAll(getMultiConditionAuditPoint(existingCondition.getNextMultiCondition(), multiCondition.getNextMultiCondition()));
            }
        }else if(existingCondition != null) {
            multiAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingCondition.getCondition(), null));

            if(existingCondition.getNextConditionJoinOperator() != null){
                PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(existingCondition.getNextConditionJoinOperator(), null);
                multiAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            }

            if (existingCondition.getNextMultiCondition() != null ) {
                multiAttributeAuditPoints.addAll(getMultiConditionAuditPoint(existingCondition.getNextMultiCondition(), null));
            }
        }else if(multiCondition != null) {
            multiAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(null, multiCondition.getCondition()));

            if(multiCondition.getNextConditionJoinOperator() != null){
                PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(null, multiCondition.getNextConditionJoinOperator());
                multiAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            }

            if (multiCondition.getNextMultiCondition() != null ) {
                multiAttributeAuditPoints.addAll(getMultiConditionAuditPoint(null, multiCondition.getNextMultiCondition()));
            }
        }
        return multiAttributeAuditPoints;
    }

    private static PraeceptaRuleAttributeAuditPoint getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(JoinOperatorType existingCondition, JoinOperatorType multiCondition) {
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("JOIN_OPERATOR");
        List<PraeceptaAuditElement> auditElements = new ArrayList<>();
        populateAuditElement(AUDIT_ELEMENT_TYPE.NEXT_CONDITION_JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingCondition, multiCondition), auditElements);
        praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
        return praeceptaRuleAttributeAuditPoint;
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getSimpleConditionAuditPoint(PraeceptaSimpleCondition existingSimpleCondition, PraeceptaSimpleCondition simpleCondition){
        List<PraeceptaRuleAttributeAuditPoint> simpleConditionAttributeAuditPoints = new ArrayList<>();
        if(existingSimpleCondition != null && simpleCondition != null) {
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getRuleAttributePoint(existingSimpleCondition, simpleCondition);

            simpleConditionAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            if (simpleCondition.getNextCondition() != null || existingSimpleCondition.getNextCondition() != null) {
                simpleConditionAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingSimpleCondition.getNextCondition(), simpleCondition.getNextCondition()));
            }
        }else if(existingSimpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getRuleAttributePoint(existingSimpleCondition, null);
            simpleConditionAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            if (existingSimpleCondition.getNextCondition() != null) {
                simpleConditionAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingSimpleCondition.getNextCondition(), null));
            }
        }else if(simpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getRuleAttributePoint(null, simpleCondition);
            simpleConditionAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);

            if (simpleCondition.getNextCondition() != null) {
                simpleConditionAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(null, simpleCondition.getNextCondition()));
            }
        }
        return simpleConditionAttributeAuditPoints;
    }

    private static Collection<PraeceptaCriteria> deletedCriterias(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Collection<PraeceptaCriteria> deletedCriterias = new ArrayList<>();
        Set<String>  existingCriteriaKeySet =  existingRuleCriteriasMap.keySet();
        existingCriteriaKeySet.forEach(ruleName->{
            if(ruleCriteriasMap.get(ruleName) == null){
                deletedCriterias.add(existingRuleCriteriasMap.get(ruleName));
            }
        });
        return deletedCriterias;
    }

    private static Collection<PraeceptaCriteria> newCriterias(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Collection<PraeceptaCriteria> newCriterias = new ArrayList<>();
        Set<String>  criteriaKeySet =  ruleCriteriasMap.keySet();
        criteriaKeySet.forEach(ruleName->{
            if(existingRuleCriteriasMap.get(ruleName) == null){
                newCriterias.add(existingRuleCriteriasMap.get(ruleName));
            }
        });
        return newCriterias;
    }

    private static Map<PraeceptaCriteria, PraeceptaCriteria> modifiedCriterias(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Map<PraeceptaCriteria, PraeceptaCriteria> modifiedCriterias = new HashMap<>();
        Set<String>  criteriaKeySet =  ruleCriteriasMap.keySet();
        criteriaKeySet.forEach(ruleName->{
            if(existingRuleCriteriasMap.get(ruleName) != null){
                modifiedCriterias.put(existingRuleCriteriasMap.get(ruleName), ruleCriteriasMap.get(ruleName));
            }
        });
        return modifiedCriterias;
    }

    private static Map<String, PraeceptaCriteria> getCriteriasByRuleName(Collection<PraeceptaCriteria> criteriaCollection){
        Map<String, PraeceptaCriteria> ruleCriteriasMap = new HashMap<>();
        criteriaCollection.forEach(criteria ->{
            ruleCriteriasMap.put(criteria.getRuleName(), criteria);
        });
        return ruleCriteriasMap;
    }
}
