package io.praecepta.rest.api.service.impl;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rest.api.util.PraeceptaRuleGroupComparison;
import io.praecepta.rest.client.PraeceptaWsRestClient;
import io.praecepta.rest.client.builder.PraeceptaRestClientBuilder;
import io.praecepta.rest.client.config.PraeceptaWebServiceClientConfig;
import io.praecepta.rest.client.dto.PraeceptaWsRequestResponseHolder;
import io.praecepta.rules.dto.*;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.filter.PraeceptaMultiCondition;
import io.praecepta.rules.model.filter.PraeceptaMultiNestedCondition;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 
 * @author vara
 *
 */
public class PraeceptaRuleGroupServiceImpl implements IPraeceptaRulesGroupService {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaRuleGroupServiceImpl.class);
	public static final String MULTI_NESTED = "multiNested";
	public static final String SIMPLE = "simple";
	public static final String MULTI = "multi";

	String PATH_PARAM_SPACE_NAME = "spacename";

	String PATH_PARAM_CLIENT_ID = "clientid";

	String PATH_PARAM_APP_NAME = "appname";

	String PATH_PARAM_VERSION = "version";

	String PATH_PARAM_GROUP_NAME = "groupname";

	@Autowired
	IPraeceptaPivotalRulesHubManager pivotalRuleHubManager;

	@Value("${praecepta.auditservice.url}")
	private String url;

	ExecutorService executor = Executors.newFixedThreadPool(1);


	/**
	 * Method to return all rule groups by appName
	 */
	@Override
	public Collection<PraeceptaRuleGroup>  getRuleGroups(String spaceName, String clientId, String appName, String version) {
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName);
		compositeKey.setVersion(version);
		return pivotalRuleHubManager.fetchRuleGrps(compositeKey, version);
	}

	private PraeceptaMultiNestedCondition getLastNodeForMultiNestedCondition(PraeceptaMultiNestedCondition multiNestedCondition) {
		if(multiNestedCondition.getNextMultiNestedCondition() == null)
			return multiNestedCondition;
		return getLastNodeForMultiNestedCondition(multiNestedCondition.getNextMultiNestedCondition());
	}

	/**
	 * Method to convert SimpleCondition DB model
	 */
	private PraeceptaSimpleCondition getRuleConditionInfoReverse(List<ConditionInfo> conditionInfoList){
		PraeceptaSimpleCondition simpleCondition = new PraeceptaSimpleCondition();
		for(int index = 0; index <conditionInfoList.size(); index++) {
			ConditionInfo conditionInfo = conditionInfoList.get(index);
			if(index == 0) {
				simpleCondition = getRuleConditionInfoReverse(conditionInfo);
			}else{
				PraeceptaSimpleCondition nextCondition = getRuleConditionInfoReverse(conditionInfo);

				PraeceptaSimpleCondition lastCondition = getSimpleConditionLastNestedInfo(simpleCondition);

				lastCondition.setNextConditionInfo(conditionInfo.getJoinOperatorType(),nextCondition);
			}
		}
		return simpleCondition;
	}


	/**
	 * Method to convert SimpleCondition DB model
	 */
	private PraeceptaSimpleCondition getRuleConditionInfoReverse(ConditionInfo conditionInfo){
		PraeceptaSimpleCondition condition  = new PraeceptaSimpleCondition(conditionInfo.getAttributeName(), conditionInfo.getOperatorType(), null);
			condition.setSubjectValue(conditionInfo.getAttributeValue());
			condition.setSubjectName(conditionInfo.getAttributeName());
			condition.setDefinedAttributeToCompare(conditionInfo.getAttributeToCompare());
			condition.setValueToCompare(conditionInfo.getValueToCompare());
			condition.setParameters(conditionInfo.getAdditionalParameters());
		return condition;
	}


	private PraeceptaMultiCondition getMultiRuleConditionInfoReverse(List<SimpleConditionInfo> simpleConditionInfoList) {

		PraeceptaMultiCondition multiCondition = new PraeceptaMultiCondition();

		for(int index = 0 ; index < simpleConditionInfoList.size(); index++){
			if(index == 0){
				multiCondition =  new PraeceptaMultiCondition(getRuleConditionInfoReverse(simpleConditionInfoList.get(index).getConditionInfoList()));
			}else{

				PraeceptaMultiCondition lastCondition = getLastNodeForMultiCondition(multiCondition);
				lastCondition.setNextConditionJoinOperator(simpleConditionInfoList.get(index).getJoinOperatorType());
				lastCondition.setNextMultiCondition(new PraeceptaMultiCondition(getRuleConditionInfoReverse(simpleConditionInfoList.get(index).getConditionInfoList())));
			}
		}
		return multiCondition;
	}

	/**
	 * Method to get Last node from Multi Condition to add next element to last node.
	 */
	private PraeceptaSimpleCondition getSimpleConditionLastNestedInfo(PraeceptaSimpleCondition simpleCondition){
		if(simpleCondition.getNextCondition() == null){
			return simpleCondition;
		}
		return getSimpleConditionLastNestedInfo(simpleCondition.getNextCondition());

	}


	/**
	 * Method to convert Multi Nested DB model.
	 */
	private PraeceptaMultiNestedCondition getMultiNestedRuleConditionInfoReverse(List<MultiNestedConditionInfo> multiNestedConditionInfoList){

		PraeceptaMultiNestedCondition multiNestedCondition = null;

		for(int index = 0; index< multiNestedConditionInfoList.size(); index++){
			MultiNestedConditionInfo multiNestedConditionInfo = multiNestedConditionInfoList.get(index);
			if(index == 0){
				multiNestedCondition = new PraeceptaMultiNestedCondition(getMultiConditionInfoReverse(multiNestedConditionInfo.getMultiConditionsList()));
			}else{
				PraeceptaMultiNestedCondition lastMultiCondition =  getLastNodeForMultiNestedCondition(multiNestedCondition);
				lastMultiCondition.setNextConditionJoinOperator(multiNestedConditionInfo.getJoinOperatorType());
				lastMultiCondition.setNextMultiNestedCondition(new PraeceptaMultiNestedCondition(getMultiConditionInfoReverse(multiNestedConditionInfo.getMultiConditionsList())));
			}
		}
		LOG.debug("Converted Multi Nested Condition Info: {}", multiNestedCondition);
		return multiNestedCondition;

	}

	/**
	 * Method to convert Multi Condition DB Model.
	 */
	private PraeceptaMultiCondition getMultiConditionInfoReverse(List<MultiConditionInfo> multiConditionInfoList){
		PraeceptaMultiCondition multiConditionInfo = null;

		for(int index = 0; index< multiConditionInfoList.size(); index++){
			MultiConditionInfo multiConditionModel = multiConditionInfoList.get(index);
			if(index == 0){
				multiConditionInfo = getMultiRuleConditionInfoReverse(multiConditionModel.getConditionInfo());
			}else{
				PraeceptaMultiCondition lastMultiCondition =  getLastNodeForMultiCondition(multiConditionInfo);
				lastMultiCondition.setNextConditionJoinOperator(multiConditionModel.getJoinOperatorType());
				PraeceptaMultiCondition nestedCondition = getMultiRuleConditionInfoReverse(multiConditionModel.getConditionInfo());
				lastMultiCondition.setNextMultiCondition(nestedCondition);
			}
		}
		LOG.debug("Converted Multi Nested Condition Info: {}", multiConditionInfo);
		return multiConditionInfo;
	}

	private PraeceptaMultiCondition getLastNodeForMultiCondition(PraeceptaMultiCondition multiConditionInfo) {
		if(multiConditionInfo.getNextMultiCondition() == null)
			return multiConditionInfo;
		return getLastNodeForMultiCondition(multiConditionInfo.getNextMultiCondition());
	}

	/**
	 * Method to return rule group by appName and rule group name
	 */
	@Override
	public PraeceptaRuleGroup getRuleGroupByName(String spaceName, String clientId, String appName, String version, String ruleGroupName) {
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName);
		return pivotalRuleHubManager.fetchRuleGrp(compositeKey, version, ruleGroupName);
	}

	@Override
	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, MultiNestedConditionGroupInfo ruleGroup) {
		PraeceptaRuleGroup praeceptaRuleGroup = new PraeceptaRuleGroup(spaceName, clientId, appName);
		praeceptaRuleGroup.setRuleGroupType(MULTI_NESTED);
		praeceptaRuleGroup.getRuleSpaceKey().setVersion(version);
		List<PraeceptaCriteria> criteriaList = new ArrayList<>();

		for(MultiNestedConditionCriteriaInfo criteriaInfo : ruleGroup.getMultiNestedConditionCriteriaInfos()){
			PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();
			praeceptaCriteria.setRuleName(criteriaInfo.getRuleName());

			PraeceptaMultiNestedCondition multiNestedCondition = getMultiNestedRuleConditionInfoReverse((List<MultiNestedConditionInfo>) criteriaInfo.getMultiNestedConditionList());

			praeceptaCriteria.setPredicates(multiNestedCondition);

			if (criteriaInfo.getActionList() != null) {
				praeceptaCriteria.setActionToPerform(convertActionInfo(criteriaInfo.getActionList()));
			}
			if (criteriaInfo.getFailureActionList() != null) {
				praeceptaCriteria.setActionToPerformOnFailure(convertActionInfo(criteriaInfo.getFailureActionList()));
			}

			criteriaList.add(praeceptaCriteria);
		}
		praeceptaRuleGroup.setPraeceptaCriterias(criteriaList);

		praeceptaRuleGroup.setRuleGroupName(ruleGroup.getRuleGroupName());
		praeceptaRuleGroup.setActive(true);
		praeceptaRuleGroup.setAttributeSchemaTemplate(ruleGroup.getAttributeSchemaTemplate());

		PraeceptaRuleGroup existingRuleGroup = pivotalRuleHubManager.fetchRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());
		if(existingRuleGroup != null){
			pivotalRuleHubManager.deleteRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());
		}

		pivotalRuleHubManager.createRuleGrp(praeceptaRuleGroup);
		executor.submit(() -> auditRuleGroupUpdate(existingRuleGroup, praeceptaRuleGroup));

		return "Rule Group added/updated successfully";

	}

	private void auditRuleGroupUpdate(PraeceptaRuleGroup existingRuleGroup , PraeceptaRuleGroup updatedRuleGroup){
		try {
			PraeceptaRuleGroupAuditPoint praeceptaRuleGroupAuditPoint = PraeceptaRuleGroupComparison.compare(existingRuleGroup, updatedRuleGroup);


			Map<String, String> pathParams = new HashMap<>();
			pathParams.put(PATH_PARAM_SPACE_NAME, updatedRuleGroup.getRuleSpaceKey().getSpaceName());
			pathParams.put(PATH_PARAM_CLIENT_ID, updatedRuleGroup.getRuleSpaceKey().getClientId());
			pathParams.put(PATH_PARAM_APP_NAME, updatedRuleGroup.getRuleSpaceKey().getAppName());
			pathParams.put(PATH_PARAM_VERSION, updatedRuleGroup.getRuleSpaceKey().getVersion());
			pathParams.put(PATH_PARAM_GROUP_NAME, updatedRuleGroup.getRuleGroupName());

			PraeceptaWsRequestResponseHolder wsReqResHolder = new PraeceptaWsRequestResponseHolder(PraeceptaWsRequestResponseHolder.PraeceptaWsOperationType.PUT,
					GsonHelper.toJson(praeceptaRuleGroupAuditPoint), new HashMap<>(), pathParams, new HashMap<>(), new HashMap<>());

			PraeceptaWebServiceClientConfig config = new PraeceptaWebServiceClientConfig();

			config.setEndpointUrl(url);
			config.setConnectionTimeOut(10000L);
			config.setReadTimeOut(7500L);

			PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig> simpleRestBuilder = new PraeceptaRestClientBuilder<>(config);

			PraeceptaWsRestClient<PraeceptaRestClientBuilder<PraeceptaWebServiceClientConfig>> restClient =
					new PraeceptaWsRestClient<>(simpleRestBuilder);

			restClient.initialize();

			restClient.triggerCall(wsReqResHolder);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		/**
         * Method to add or update RuleGroup
         */
	@Override
	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, SimpleConditionGroupInfo ruleGroup) {
		PraeceptaRuleGroup praeceptaRuleGroup = new PraeceptaRuleGroup(spaceName, clientId, appName);
		praeceptaRuleGroup.setRuleGroupType(SIMPLE);
		praeceptaRuleGroup.getRuleSpaceKey().setVersion(version);
		List<PraeceptaCriteria> criteriaList = new ArrayList<>();
		for(SimpleConditionCriteriaInfo criteriaInfo : ruleGroup.getSimpleConditionCriteriaInfos()){
			PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();

			praeceptaCriteria.setRuleName(criteriaInfo.getRuleName());

			PraeceptaSimpleCondition simpleCondition = getRuleConditionInfoReverse(criteriaInfo.getConditionInfo().getConditionInfoList());
			PraeceptaMultiCondition multiCondition = new PraeceptaMultiCondition(simpleCondition);
			praeceptaCriteria.setPredicates(new PraeceptaMultiNestedCondition(multiCondition));
			
			if (criteriaInfo.getActionList() != null) {
				praeceptaCriteria.setActionToPerform(convertActionInfo(criteriaInfo.getActionList()));
			}


			if (criteriaInfo.getFailureActionList() != null) {
				praeceptaCriteria.setActionToPerformOnFailure(convertActionInfo(criteriaInfo.getFailureActionList()));
			}

			criteriaList.add(praeceptaCriteria);
		}
		praeceptaRuleGroup.setPraeceptaCriterias(criteriaList);

		praeceptaRuleGroup.setRuleGroupName(ruleGroup.getRuleGroupName());
		praeceptaRuleGroup.setActive(true);
		praeceptaRuleGroup.setAttributeSchemaTemplate(ruleGroup.getAttributeSchemaTemplate());

		PraeceptaRuleGroup existingRuleGroup = pivotalRuleHubManager.fetchRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());
		if(existingRuleGroup != null){
			pivotalRuleHubManager.deleteRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());
		}

		pivotalRuleHubManager.createRuleGrp(praeceptaRuleGroup);
		executor.submit(() -> auditRuleGroupUpdate(existingRuleGroup, praeceptaRuleGroup));

		return "Rule Group added/updated successfully";
	}


	private Collection<PraeceptaActionDetails> convertActionInfo(Collection<RuleActionInfo> criteriaInfo) {
		Collection<PraeceptaActionDetails> actions = new ArrayList<>();
		for (RuleActionInfo actionInfo : criteriaInfo) {
				PraeceptaActionDetails simpleAction = new PraeceptaActionDetails();
				BeanUtils.copyProperties(actionInfo, simpleAction);
				actions.add(simpleAction);
		}
		return actions;
	}


	/**
	 * Method to add or update RuleGroup
	 */
	@Override
	public String addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version, MultiConditionGroupInfo ruleGroup) {


		PraeceptaRuleGroup praeceptaRuleGroup = new PraeceptaRuleGroup(spaceName, clientId, appName);
		praeceptaRuleGroup.setRuleGroupType(MULTI);
		praeceptaRuleGroup.getRuleSpaceKey().setVersion(version);
		List<PraeceptaCriteria> criteriaList = new ArrayList<>();
		for(MultiConditionCriteriaInfo criteriaInfo : ruleGroup.getMultiConditionCriteriaInfos()){
			PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();

			praeceptaCriteria.setRuleName(criteriaInfo.getRuleName());
			PraeceptaMultiNestedCondition multiNestedCondition = new PraeceptaMultiNestedCondition(getMultiConditionInfoReverse((List<MultiConditionInfo>) criteriaInfo.getMultiConditionList()));
			praeceptaCriteria.setPredicates(multiNestedCondition);

			if (criteriaInfo.getActionList() != null) {
				praeceptaCriteria.setActionToPerform(convertActionInfo(criteriaInfo.getActionList()));
			}

			if (criteriaInfo.getFailureActionList() != null) {
				praeceptaCriteria.setActionToPerformOnFailure(convertActionInfo(criteriaInfo.getFailureActionList()));
			}

			criteriaList.add(praeceptaCriteria);
		}
		praeceptaRuleGroup.setPraeceptaCriterias(criteriaList);

		praeceptaRuleGroup.setRuleGroupName(ruleGroup.getRuleGroupName());
		praeceptaRuleGroup.setActive(true);
		praeceptaRuleGroup.setAttributeSchemaTemplate(ruleGroup.getAttributeSchemaTemplate());
		PraeceptaRuleGroup existingRuleGroup = pivotalRuleHubManager.fetchRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());

		if(existingRuleGroup != null){
			pivotalRuleHubManager.deleteRuleGrp(praeceptaRuleGroup.getRuleSpaceKey(), praeceptaRuleGroup.getRuleSpaceKey().getVersion(), praeceptaRuleGroup.getRuleGroupName());
		}

		pivotalRuleHubManager.createRuleGrp(praeceptaRuleGroup);

		executor.submit(() -> auditRuleGroupUpdate(existingRuleGroup, praeceptaRuleGroup));

		return "Rule Group added/updated successfully";
	}

	/**
	 * Method to delete the RuleGroup
	 */
	@Override
	public boolean deleteRuleGroup(String spaceName, String clientId, String appName, String version, String ruleGroupName) {
		return pivotalRuleHubManager.deleteRuleGrp(new PraeceptaRuleSpaceCompositeKey(spaceName, clientId, appName), version, ruleGroupName);
	}

	@Override
	public String addRuleSpace(RuleSpaceInfo ruleSpaceInfo) {
		PraeceptaRuleSpace praeceptaRuleSpace = new PraeceptaRuleSpace();

		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(ruleSpaceInfo.getSpaceName(),ruleSpaceInfo.getClientId(),ruleSpaceInfo.getAppName());
		compositeKey.setVersion(ruleSpaceInfo.getVersion());
		praeceptaRuleSpace.setRuleSpaceKey(compositeKey);
		praeceptaRuleSpace.setActive(Boolean.TRUE);
		pivotalRuleHubManager.createRuleSpace(praeceptaRuleSpace);

		return "Rule Space added successfully.";
	}

	@Override
	public String deleteRuleSpace(String spaceName, String clientId, String appName, String version) {
		PraeceptaRuleSpace praeceptaRuleSpace = new PraeceptaRuleSpace();

		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName,clientId,appName);
		compositeKey.setVersion(version);
		praeceptaRuleSpace.setRuleSpaceKey(compositeKey);
		pivotalRuleHubManager.deleteRuleSpace(praeceptaRuleSpace);

		return "Rule Space deleted successfully.";
	}

	@Override
	public RuleSpaceInfo getRuleSpace(String spaceName, String clientId, String appName, String version) {

		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey(spaceName,clientId,appName);
		PraeceptaRuleSpace praeceptaRuleSpace =  pivotalRuleHubManager.fetchRuleSpace(compositeKey, version);

		if(praeceptaRuleSpace != null) {
			RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
			ruleSpaceInfo.setSpaceName(praeceptaRuleSpace.getRuleSpaceKey().getSpaceName());
			ruleSpaceInfo.setClientId(praeceptaRuleSpace.getRuleSpaceKey().getClientId());
			ruleSpaceInfo.setAppName(praeceptaRuleSpace.getRuleSpaceKey().getAppName());
			ruleSpaceInfo.setVersion(praeceptaRuleSpace.getRuleSpaceKey().getVersion());

			return ruleSpaceInfo;
		}else
			return null;
	}


	@Override
	public List<RuleSpaceInfo> getRuleSpaceList() {
		List<PraeceptaRuleSpace> praeceptaRuleSpaceList =  pivotalRuleHubManager.fetchRuleSpaceList();
		List<RuleSpaceInfo> spaceInfos = new ArrayList<>();
		if(praeceptaRuleSpaceList != null) {
			praeceptaRuleSpaceList.forEach(praeceptaRuleSpace-> {
				RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
				ruleSpaceInfo.setSpaceName(praeceptaRuleSpace.getRuleSpaceKey().getSpaceName());
				ruleSpaceInfo.setClientId(praeceptaRuleSpace.getRuleSpaceKey().getClientId());
				ruleSpaceInfo.setAppName(praeceptaRuleSpace.getRuleSpaceKey().getAppName());
				ruleSpaceInfo.setVersion(praeceptaRuleSpace.getRuleSpaceKey().getVersion());
				spaceInfos.add(ruleSpaceInfo);
			});
			return spaceInfos;
		}else
			return null;
	}


}
