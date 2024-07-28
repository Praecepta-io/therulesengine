package io.praecepta.rest.api.util;

import io.praecepta.core.helper.GsonHelper;
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
import org.springframework.util.ObjectUtils;

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
        List<PraeceptaRuleAttributeAuditPoint> conditionAuditList =  getConditionAuditPoint(existingCriteria != null?existingCriteria.getPredicates():null, newCriteria!=null?newCriteria.getPredicates():null, 1);
        if(!ObjectUtils.isEmpty(conditionAuditList))
            ruleAditMap.put(AUDIT_POINT_TYPE.CONDITION,conditionAuditList);

        List<PraeceptaRuleAttributeAuditPoint> actionAuditList = getActionAuditPoint(existingCriteria != null?existingCriteria.getActionToPerform():null, newCriteria!=null?newCriteria.getActionToPerform():null);
        if(!ObjectUtils.isEmpty(actionAuditList))
            ruleAditMap.put(AUDIT_POINT_TYPE.ACTION, actionAuditList);

        List<PraeceptaRuleAttributeAuditPoint> failureActionAuditList = getActionAuditPoint(existingCriteria != null?existingCriteria.getActionToPerformOnFailure():null, newCriteria!=null?newCriteria.getActionToPerformOnFailure():null);
        if(!ObjectUtils.isEmpty(failureActionAuditList))
            ruleAditMap.put(AUDIT_POINT_TYPE.FAILURE_ACTION, failureActionAuditList);

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
                    populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign()!=null? action.getValueToAssign() :null, null), auditElements);

                    compareActionMetaData(action.getAdditionalParameters(), null, auditElements);
                    actionAttributeAudit.setAuditElements(auditElements);
                }else{
                    List<PraeceptaAuditElement> auditElements = new ArrayList<>();
                    PraeceptaActionDetails newActionDetails = updatedActionMap.get(action.getActionAttributeName());
                    if(!action.getActionStrategy().equals(newActionDetails.getActionStrategy())) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.ACTION_STRATEGY_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getActionStrategy()!=null?action.getActionStrategy().toString():null, newActionDetails.getActionStrategy()!=null?newActionDetails.getActionStrategy().toString():null), auditElements);
                    }
                    if(!action.getValueToAssign().equals(newActionDetails.getValueToAssign())) {
                        populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), newActionDetails.getValueToAssign()), auditElements);
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
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(action.getValueToAssign(), null), auditElements);

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
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(null, action.getValueToAssign()), auditElements);

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

    private static boolean actionMetaDataModified(Map<PraeceptaActionParametersType, Object> additionalParameters, Map<PraeceptaActionParametersType, Object> newAdditionalParameters){
        if(CollectionUtils.isEmpty(additionalParameters) && CollectionUtils.isEmpty(newAdditionalParameters))
            return false;
        else if(!CollectionUtils.isEmpty(additionalParameters) && CollectionUtils.isEmpty(newAdditionalParameters))
            return true;
        else if(CollectionUtils.isEmpty(additionalParameters) && !CollectionUtils.isEmpty(newAdditionalParameters))
            return true;
        else if(additionalParameters.size() != newAdditionalParameters.size())
            return true;
        else{
            Set<PraeceptaActionParametersType> existingParametersKeySet = additionalParameters.keySet();
            Set<PraeceptaActionParametersType> newParametersKeySet = newAdditionalParameters.keySet();
            if(!existingParametersKeySet.equals(newParametersKeySet))
                return true;
           Collection<Object> existingValuesList = additionalParameters.values();
           Collection<Object> newValuesList = newAdditionalParameters.values();
           if(!newValuesList.equals(existingValuesList))
               return true;
        }

        return false;
    }


    private static boolean metaDataModified(Map<String, Object> additionalParameters, Map<String, Object> newAdditionalParameters){
        if(CollectionUtils.isEmpty(additionalParameters) && CollectionUtils.isEmpty(newAdditionalParameters))
            return false;
        else if(!CollectionUtils.isEmpty(additionalParameters) && CollectionUtils.isEmpty(newAdditionalParameters))
            return true;
        else if(CollectionUtils.isEmpty(additionalParameters) && !CollectionUtils.isEmpty(newAdditionalParameters))
            return true;
        else if(additionalParameters.size() != newAdditionalParameters.size())
            return true;
        else{
            Set<String> existingParametersKeySet = additionalParameters.keySet();
            Set<String> newParametersKeySet = newAdditionalParameters.keySet();
            if(!existingParametersKeySet.equals(newParametersKeySet))
                return true;
            Collection<Object> existingValuesList = additionalParameters.values();
            Collection<Object> newValuesList =  newAdditionalParameters.values();
            if(!newValuesList.equals(existingValuesList))
                return true;
        }

        return false;
    }
    private static void compareActionMetaData(Map<PraeceptaActionParametersType, Object> additionalParameters, Map<PraeceptaActionParametersType, Object> newAdditionalParameters, List<PraeceptaAuditElement> auditElements) {
        if(actionMetaDataModified(additionalParameters, newAdditionalParameters)){
            populateAuditElement(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(GsonHelper.toJson(additionalParameters), GsonHelper.toJson(newAdditionalParameters)), auditElements);
        }
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getConditionAuditPoint(PraeceptaMultiNestedCondition existingPredicates, PraeceptaMultiNestedCondition newPredicates, int conditionIndex) {
        List<PraeceptaRuleAttributeAuditPoint> attributeAuditPoints = getMultiConditionAuditPoint(existingPredicates != null?existingPredicates.getMultiCondition():null, newPredicates != null?newPredicates.getMultiCondition():null, 1);



        if(existingPredicates != null && existingPredicates.getNextMultiNestedCondition() != null){
                JoinOperatorType newConditionJoinOperatorType = newPredicates != null?newPredicates.getNextConditionJoinOperator():null;
                if(!existingPredicates.getNextConditionJoinOperator().equals(newConditionJoinOperatorType)){
                    PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(existingPredicates.getNextConditionJoinOperator(), newConditionJoinOperatorType, conditionIndex,"MultiNestedCondition");
                    attributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
                }
            List<PraeceptaRuleAttributeAuditPoint> existingAttributeAuditPoints = getConditionAuditPoint(existingPredicates.getNextMultiNestedCondition(), newPredicates != null ?newPredicates.getNextMultiNestedCondition():null, ++ conditionIndex);
            if(!CollectionUtils.isEmpty(existingAttributeAuditPoints))
                attributeAuditPoints.addAll(existingAttributeAuditPoints);
        }else if(newPredicates != null && newPredicates.getNextMultiNestedCondition() != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(null, newPredicates.getNextConditionJoinOperator(), conditionIndex,"MultiNestedCondition");
            attributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);

                List<PraeceptaRuleAttributeAuditPoint> newPredicateAttributeAuditPoints  = getConditionAuditPoint(null, newPredicates.getNextMultiNestedCondition(), ++conditionIndex);
                if(!CollectionUtils.isEmpty(newPredicateAttributeAuditPoints))
                    attributeAuditPoints.addAll(newPredicateAttributeAuditPoints);
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
                populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare() != null? existingSimpleCondition.getValueToCompare().toString():null, simpleCondition.getValueToCompare() != null? simpleCondition.getValueToCompare().toString():null), auditElements);
            }
            if (existingSimpleCondition.getConditionOperator() != null && !existingSimpleCondition.getConditionOperator().equals(simpleCondition.getConditionOperator())) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator() != null? existingSimpleCondition.getConditionOperator().toString():null, simpleCondition.getConditionOperator() != null?simpleCondition.getConditionOperator().toString(): null), auditElements);
            }

            if (existingSimpleCondition.getNextConditionJoinOperator() != null) {
                if (!existingSimpleCondition.getNextConditionJoinOperator().equals(simpleCondition.getNextConditionJoinOperator())) {
                    populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getNextConditionJoinOperator() != null ? existingSimpleCondition.getNextConditionJoinOperator().toString() : null, simpleCondition.getNextConditionJoinOperator() != null ? simpleCondition.getNextConditionJoinOperator().toString() : null), auditElements);
                }
            }else if(simpleCondition.getNextConditionJoinOperator() != null ) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder( null, simpleCondition.getNextConditionJoinOperator().toString()), auditElements);

            }

            compareMetaData(existingSimpleCondition.getParameters(), simpleCondition.getParameters(), auditElements);

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(existingSimpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(existingSimpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getValueToCompare() != null? existingSimpleCondition.getValueToCompare().toString():null, null), auditElements);
            if(existingSimpleCondition.getConditionOperator() != null)
                populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getConditionOperator() != null? existingSimpleCondition.getConditionOperator().toString():null, null), auditElements);

            compareMetaData(existingSimpleCondition.getParameters(), null, auditElements);

            if (existingSimpleCondition.getNextConditionJoinOperator() != null ) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingSimpleCondition.getNextConditionJoinOperator() != null? existingSimpleCondition.getNextConditionJoinOperator().toString():null, null), auditElements);
            }

            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }else if(simpleCondition != null){
            PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(simpleCondition.getSubjectName());
            List<PraeceptaAuditElement> auditElements = new ArrayList<>();
            populateAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getValueToCompare() != null? simpleCondition.getValueToCompare().toString():null), auditElements);
            if(simpleCondition.getConditionOperator() != null)
                populateAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getConditionOperator() != null? simpleCondition.getConditionOperator().toString():null), auditElements);

            if (simpleCondition.getNextConditionJoinOperator() != null) {
                populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(null, simpleCondition.getNextConditionJoinOperator()!= null?simpleCondition.getNextConditionJoinOperator().toString():null), auditElements);
            }

            compareMetaData(null, simpleCondition.getParameters(), auditElements);


            praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
            return praeceptaRuleAttributeAuditPoint;
        }

        return null;
    }

    private static void compareMetaData(Map<String, Object> existingConditionParameters, Map<String, Object> conditionParameters, List<PraeceptaAuditElement> auditElements) {
        if(metaDataModified(existingConditionParameters, conditionParameters)){
            populateAuditElement(AUDIT_ELEMENT_TYPE.META_DATA_CHANGE, new PraeceptaAuditElement.ValueHolder(GsonHelper.toJson(existingConditionParameters), GsonHelper.toJson(conditionParameters)), auditElements);
        }
    }

    private static void populateAuditElement(AUDIT_ELEMENT_TYPE auditElementType, PraeceptaAuditElement.ValueHolder valueHolder, List<PraeceptaAuditElement> auditElements) {
        PraeceptaAuditElement praeceptaAuditElement = new PraeceptaAuditElement();
        praeceptaAuditElement.setElementType(auditElementType);
        praeceptaAuditElement.setValueHolder(valueHolder);
        auditElements.add(praeceptaAuditElement);
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getMultiConditionAuditPoint(PraeceptaMultiCondition existingCondition, PraeceptaMultiCondition multiCondition, int conditionIndex){
        List<PraeceptaRuleAttributeAuditPoint> multiAttributeAuditPoints = new ArrayList<>();
        if(existingCondition != null && multiCondition != null) {
            multiAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingCondition.getCondition(), multiCondition.getCondition()));

            if(existingCondition.getNextConditionJoinOperator() != null){
                if(!existingCondition.getNextConditionJoinOperator().equals(multiCondition.getNextConditionJoinOperator())){
                    PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(existingCondition.getNextConditionJoinOperator(), multiCondition.getNextConditionJoinOperator(), conditionIndex,"MultiCondition");
                    multiAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
                }
            }

            if (existingCondition.getNextMultiCondition() != null || multiCondition.getNextMultiCondition() != null) {
                multiAttributeAuditPoints.addAll(getMultiConditionAuditPoint(existingCondition.getNextMultiCondition(), multiCondition.getNextMultiCondition(), ++conditionIndex));
            }
        }else {
            multiAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingCondition != null?existingCondition.getCondition():null, multiCondition != null?multiCondition.getCondition():null));

            if((existingCondition != null && existingCondition.getNextConditionJoinOperator() != null) || (multiCondition != null && multiCondition.getNextConditionJoinOperator() != null)){
                PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(existingCondition!=null?existingCondition.getNextConditionJoinOperator():null, multiCondition!=null?multiCondition.getNextConditionJoinOperator():null, conditionIndex, "MultiCondition");
                multiAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            }

            if ((existingCondition != null && existingCondition.getNextMultiCondition() != null)  || (multiCondition != null && multiCondition.getNextMultiCondition() != null) ) {
                multiAttributeAuditPoints.addAll(getMultiConditionAuditPoint(existingCondition != null ?existingCondition.getNextMultiCondition():null, multiCondition != null ?multiCondition.getNextMultiCondition():null, ++conditionIndex));
            }
        }
        return multiAttributeAuditPoints;
    }

    private static PraeceptaRuleAttributeAuditPoint getPraeceptaRuleAttributeAuditPointForNextConditionJoinOperator(JoinOperatorType existingCondition, JoinOperatorType multiCondition, int conditionIndex, String conditionType) {
        PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint(conditionType + conditionIndex);
        List<PraeceptaAuditElement> auditElements = new ArrayList<>();
        populateAuditElement(AUDIT_ELEMENT_TYPE.JOIN_OPERATOR_CHANGE, new PraeceptaAuditElement.ValueHolder(existingCondition!=null?existingCondition.toString():null, multiCondition!=null?multiCondition.toString():null), auditElements);
        praeceptaRuleAttributeAuditPoint.setAuditElements(auditElements);
        return praeceptaRuleAttributeAuditPoint;
    }

    private static List<PraeceptaRuleAttributeAuditPoint> getSimpleConditionAuditPoint(PraeceptaSimpleCondition existingSimpleCondition, PraeceptaSimpleCondition simpleCondition){
        List<PraeceptaRuleAttributeAuditPoint> simpleConditionAttributeAuditPoints = new ArrayList<>();
            if(existingSimpleCondition == null || simpleCondition == null  || existingSimpleCondition.getSubjectName().equalsIgnoreCase(simpleCondition.getSubjectName())) {
                PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getRuleAttributePoint(existingSimpleCondition, simpleCondition);
                if (praeceptaRuleAttributeAuditPoint != null && !ObjectUtils.isEmpty(praeceptaRuleAttributeAuditPoint.getAuditElements()))
                    simpleConditionAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);
            }else {
                PraeceptaRuleAttributeAuditPoint praeceptaRuleAttributeAuditPoint = getRuleAttributePoint(existingSimpleCondition, null);
                if (praeceptaRuleAttributeAuditPoint != null && !ObjectUtils.isEmpty(praeceptaRuleAttributeAuditPoint.getAuditElements()))
                    simpleConditionAttributeAuditPoints.add(praeceptaRuleAttributeAuditPoint);

                PraeceptaRuleAttributeAuditPoint updatedConditionPraeceptaRuleAttributeAuditPoint = getRuleAttributePoint(null, simpleCondition);
                if (updatedConditionPraeceptaRuleAttributeAuditPoint != null && !ObjectUtils.isEmpty(updatedConditionPraeceptaRuleAttributeAuditPoint.getAuditElements()))
                    simpleConditionAttributeAuditPoints.add(updatedConditionPraeceptaRuleAttributeAuditPoint);
            }


            if ((simpleCondition != null && simpleCondition.getNextCondition() != null) || (existingSimpleCondition != null && existingSimpleCondition.getNextCondition() != null)) {
                simpleConditionAttributeAuditPoints.addAll(getSimpleConditionAuditPoint(existingSimpleCondition != null?existingSimpleCondition.getNextCondition():null, simpleCondition!=null?simpleCondition.getNextCondition():null));
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
                newCriteriaList.add(ruleCriteriasMap.get(ruleName));
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
