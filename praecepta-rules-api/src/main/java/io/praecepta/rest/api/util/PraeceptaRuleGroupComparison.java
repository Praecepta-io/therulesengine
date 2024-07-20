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
import org.springframework.util.CollectionUtils;

import java.util.*;

public class PraeceptaRuleGroupComparison {

    public static PraeceptaRuleGroupAuditPoint compare(PraeceptaRuleGroup existingRuleGroup, PraeceptaRuleGroup updatedRuleGroup){
        Map<String, PraeceptaCriteria> ruleCriteriasMap = getCriteriaListByRuleName(updatedRuleGroup.getPraeceptaCriterias());
        Map<String, PraeceptaCriteria> existingRuleCriteriasMap = getCriteriaListByRuleName(existingRuleGroup.getPraeceptaCriterias());

        Collection<PraeceptaCriteria> deletedCriteriaList = getDeletedCriteriaList(existingRuleCriteriasMap, ruleCriteriasMap);
        Collection<PraeceptaCriteria> newCriteriaList = getNewCriteriaList(existingRuleCriteriasMap, ruleCriteriasMap);


        Map<PraeceptaCriteria, PraeceptaCriteria> modifiedCriteriaList = getModifiedCriteriaList(existingRuleCriteriasMap, ruleCriteriasMap);

        PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = new PraeceptaRuleGroupAuditPoint(updatedRuleGroup.getRuleGroupName());
        Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleOperationAuditPoints = new HashMap<>();
        List<PraeceptaRuleAuditPoint> addedRulesAudit = new ArrayList<>();
        newCriteriaList.forEach(criteria-> addedRulesAudit.add(getRuleAuditPoint(null, criteria)));
        if(addedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.ADD, addedRulesAudit);
        }
        List<PraeceptaRuleAuditPoint> deletedRulesAudit = new ArrayList<>();
        deletedCriteriaList.forEach(criteria-> deletedRulesAudit.add(getRuleAuditPoint(criteria, null)));

        if(deletedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.DELETE, deletedRulesAudit);
        }

        List<PraeceptaRuleAuditPoint> modifiedRulesAudit = new ArrayList<>();
        modifiedCriteriaList.forEach((existingCriteria, newCriteria)-> modifiedRulesAudit.add(getRuleAuditPoint(existingCriteria, newCriteria)));

        if(modifiedRulesAudit.size() > 0) {
            ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.UPDATE, modifiedRulesAudit);
        }
        praeceptaRuleGroupAuditPoint.setRuleOperationAuditPoints(ruleOperationAuditPoints);


        return praeceptaRuleGroupAuditPoint;
    }




    private static PraeceptaRuleAuditPoint getRuleAuditPoint(PraeceptaCriteria existingCriteria, PraeceptaCriteria newCriteria) {

        PraeceptaRuleAuditPoint praeceptaRuleAuditPoint = new PraeceptaRuleAuditPoint(existingCriteria != null? existingCriteria.getRuleName(): newCriteria.getRuleName());

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

        Map<String, PraeceptaActionDetails> updatedActionMap = new HashMap<>();
        if(actionToPerform != null) {
            actionToPerform.forEach(action -> updatedActionMap.put(action.getActionAttributeName(), action));
        }

        if(existingActionToPerform != null && actionToPerform != null){
            existingActionToPerform.forEach(action->{
                PraeceptaRuleAttributeAuditPoint actionAttributeAudit = new PraeceptaRuleAttributeAuditPoint(action.getActionAttributeName());
                if(updatedActionMap.get(action.getActionAttributeName()) == null){
                    List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                    populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy()!= null?action.getActionStrategy().toString():null, null), auditElements);
                    populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign()!=null? action.getValueToAssign() :null, null), auditElements);

                    compareActionMetaData(action.getAdditionalParameters(), null, auditElements);
                    actionAttributeAudit.setAuditElements(auditElements);
                }else{
                    List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                    PraeceptaActionDetails newActionDetails = updatedActionMap.get(action.getActionAttributeName());
                    if(!action.getActionStrategy().equals(newActionDetails.getActionStrategy())) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy()!=null?action.getActionStrategy().toString():null, newActionDetails.getActionStrategy()!=null?newActionDetails.getActionStrategy().toString():null), auditElements);
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
                populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy() != null?action.getActionStrategy().toString():null, null), auditElements);
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), null), auditElements);

                compareActionMetaData(action.getAdditionalParameters(), null, auditElements);
                actionAttributeAudit.setAuditElements(auditElements);
                if(actionAttributeAudit.getAuditElements().size() > 0)
                    attributeAuditPoints.add(actionAttributeAudit);
            });
        }else if(actionToPerform != null){
            actionToPerform.forEach(action->{
                PraeceptaRuleAttributeAuditPoint actionAttributeAudit = new PraeceptaRuleAttributeAuditPoint(action.getActionAttributeName());
                List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(null, action.getActionStrategy() != null?action.getActionStrategy().toString():null), auditElements);
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
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key) != null? additionalParameters.get(key).toString(): null, null), auditElements);
                }else{
                    if(!additionalParameters.get(key).equals(newAdditionalParameters.get(key))) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key) != null? additionalParameters.get(key).toString():null, newAdditionalParameters.get(key) != null?newAdditionalParameters.get(key).toString():null), auditElements);
                    }
                }
            });

            Set<PraeceptaActionParametersType> conditionalParameterKeySet =  newAdditionalParameters.keySet();
            conditionalParameterKeySet.forEach(key->{
                if(additionalParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, newAdditionalParameters.get(key) != null? newAdditionalParameters.get(key).toString():null), auditElements);
                }
            });
        }else if(additionalParameters != null){
            Set<PraeceptaActionParametersType> existingParametrKeySet =  additionalParameters.keySet();
            existingParametrKeySet.forEach(key-> populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(additionalParameters.get(key) != null? additionalParameters.get(key).toString(): null, null), auditElements));
        }else if(newAdditionalParameters != null){
            Set<PraeceptaActionParametersType> conditionalParameterSet =  newAdditionalParameters.keySet();
            conditionalParameterSet.forEach(key-> populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, newAdditionalParameters.get(key) != null? newAdditionalParameters.get(key).toString():null), auditElements));
        }
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getConditionAuditPoint(PraeceptaMultiNestedCondition existingPredicates, PraeceptaMultiNestedCondition newPredicates) {
        List<PraeceptaRuleAttributeAuditPoint> attributeAuditPoints = new ArrayList<>();
        if(existingPredicates != null && newPredicates != null){
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(existingPredicates.getMultiCondition(), newPredicates.getMultiCondition()));
            if(existingPredicates.getNextMultiNestedCondition() != null){
                List<PraeceptaRuleAttributeAuditPoint> existingAttributeAuditPoints = getConditionAuditPoint(existingPredicates.getNextMultiNestedCondition(), null);
                if(!CollectionUtils.isEmpty(existingAttributeAuditPoints))
                    attributeAuditPoints.addAll(existingAttributeAuditPoints);
            }
        }else if(existingPredicates != null){
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(existingPredicates.getMultiCondition(), null));
            if(existingPredicates.getNextMultiNestedCondition() != null){
                List<PraeceptaRuleAttributeAuditPoint> existingAttributeAuditPoints = getConditionAuditPoint(existingPredicates.getNextMultiNestedCondition(), null);
                if(!CollectionUtils.isEmpty(existingAttributeAuditPoints))
                    attributeAuditPoints.addAll(existingAttributeAuditPoints);
            }
        }else if(newPredicates != null){
            attributeAuditPoints.addAll(getMultiConditionAuditPoint(null,newPredicates.getMultiCondition()));
            if(newPredicates.getNextMultiNestedCondition() != null){
                List<PraeceptaRuleAttributeAuditPoint> newPredicateAttributeAuditPoints  = getConditionAuditPoint(null, newPredicates.getNextMultiNestedCondition());
                if(!CollectionUtils.isEmpty(newPredicateAttributeAuditPoints))
                    attributeAuditPoints.addAll(newPredicateAttributeAuditPoints);
            }
        }
        if((existingPredicates != null && existingPredicates.getNextMultiNestedCondition() != null) || (newPredicates != null && newPredicates.getNextMultiNestedCondition() != null)){
            List<PraeceptaRuleAttributeAuditPoint> auditPoints = getConditionAuditPoint(existingPredicates != null?existingPredicates.getNextMultiNestedCondition():null, newPredicates!=null?newPredicates.getNextMultiNestedCondition():null);
            if(!CollectionUtils.isEmpty(auditPoints))
                attributeAuditPoints.addAll(auditPoints);
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
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare() != null? existingSimpleCondition.getValueToCompare().toString():null, simpleCondition.getValueToCompare() != null? simpleCondition.getValueToCompare().toString():null), auditElements);
            }
            if (existingSimpleCondition.getConditionOperator() != null && !existingSimpleCondition.getConditionOperator().equals(simpleCondition.getConditionOperator())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator() != null? existingSimpleCondition.getConditionOperator().toString():null, simpleCondition.getConditionOperator() != null?simpleCondition.getConditionOperator().toString(): null), auditElements);
            }

            if (existingSimpleCondition.getNextConditionJoinOperator() != null && !existingSimpleCondition.getNextConditionJoinOperator().equals(simpleCondition.getNextConditionJoinOperator())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getNextConditionJoinOperator() != null? existingSimpleCondition.getNextConditionJoinOperator().toString():null, simpleCondition.getNextConditionJoinOperator()!= null?simpleCondition.getNextConditionJoinOperator().toString():null), auditElements);
            }

            compareMetaData(existingSimpleCondition.getParameters(), simpleCondition.getParameters(), auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(existingSimpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(existingSimpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare() != null? existingSimpleCondition.getValueToCompare().toString():null, null), auditElements);
            populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator() != null? existingSimpleCondition.getConditionOperator().toString():null, null), auditElements);

            compareMetaData(existingSimpleCondition.getParameters(), null, auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(simpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(simpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getValueToCompare() != null? simpleCondition.getValueToCompare().toString():null), auditElements);
            populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getConditionOperator() != null? simpleCondition.getConditionOperator().toString():null), auditElements);

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
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key) != null? existingConditionParameters.get(key).toString():null, null), auditElements);
                }else{
                    if(!existingConditionParameters.get(key).equals(conditionParameters.get(key))) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key) != null? existingConditionParameters.get(key).toString():null, conditionParameters.get(key) != null?conditionParameters.get(key).toString():null), auditElements);
                    }
                }
            });

            Set<String> conditionalParameterKeySet =  conditionParameters.keySet();
            conditionalParameterKeySet.forEach(key->{
                if(existingConditionParameters.get(key) == null){
                    populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, conditionParameters.get(key) != null?conditionParameters.get(key).toString():null), auditElements);
                }
            });
        }else if(existingConditionParameters != null && !CollectionUtils.isEmpty(existingConditionParameters.keySet())){
            Set<String> existingParametrKeySet =  existingConditionParameters.keySet();
            existingParametrKeySet.forEach(key-> populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(existingConditionParameters.get(key) != null? existingConditionParameters.get(key).toString():null, null), auditElements));
        }else if(conditionParameters != null && !CollectionUtils.isEmpty(conditionParameters.keySet())){
            Set<String> conditionalParameterSet =  conditionParameters.keySet();
            conditionalParameterSet.forEach(key-> populateAuditElement(AUDIT_ELEMENT_TYPE.MATA_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(null, conditionParameters.get(key) != null?conditionParameters.get(key).toString():null), auditElements));
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
        populateAuditElement(AUDIT_ELEMENT_TYPE.NEXT_CONDITION_JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingCondition!=null?existingCondition.toString():null, multiCondition!=null?multiCondition.toString():null), auditElements);
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

    private static Collection<PraeceptaCriteria> getDeletedCriteriaList(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Collection<PraeceptaCriteria> deletedCriteriaList = new ArrayList<>();
        Set<String>  existingCriteriaKeySet =  existingRuleCriteriasMap.keySet();
        existingCriteriaKeySet.forEach(ruleName->{
            if(ruleCriteriasMap.get(ruleName) == null){
                deletedCriteriaList.add(existingRuleCriteriasMap.get(ruleName));
            }
        });
        return deletedCriteriaList;
    }

    private static Collection<PraeceptaCriteria> getNewCriteriaList(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Collection<PraeceptaCriteria> newCriteriaList = new ArrayList<>();
        Set<String>  criteriaKeySet =  ruleCriteriasMap.keySet();
        criteriaKeySet.forEach(ruleName->{
            if(existingRuleCriteriasMap.get(ruleName) == null){
                newCriteriaList.add(existingRuleCriteriasMap.get(ruleName));
            }
        });
        return newCriteriaList;
    }

    private static Map<PraeceptaCriteria, PraeceptaCriteria> getModifiedCriteriaList(Map<String, PraeceptaCriteria> existingRuleCriteriasMap, Map<String, PraeceptaCriteria> ruleCriteriasMap){
        Map<PraeceptaCriteria, PraeceptaCriteria> modifiedCriteriaList = new HashMap<>();
        Set<String>  criteriaKeySet =  ruleCriteriasMap.keySet();
        criteriaKeySet.forEach(ruleName->{
            if(existingRuleCriteriasMap.get(ruleName) != null){
                modifiedCriteriaList.put(existingRuleCriteriasMap.get(ruleName), ruleCriteriasMap.get(ruleName));
            }
        });
        return modifiedCriteriaList;
    }

    private static Map<String, PraeceptaCriteria> getCriteriaListByRuleName(Collection<PraeceptaCriteria> criteriaCollection){
        Map<String, PraeceptaCriteria> ruleCriteriasMap = new HashMap<>();
        criteriaCollection.forEach(criteria -> ruleCriteriasMap.put(criteria.getRuleName(), criteria));
        return ruleCriteriasMap;
    }
}
