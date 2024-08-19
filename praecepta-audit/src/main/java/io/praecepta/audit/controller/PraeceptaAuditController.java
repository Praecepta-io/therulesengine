package io.praecepta.audit.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;
import javax.ws.rs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.audit.service.IPraeceptaAuditService;
import io.praecepta.audit.service.IPraeceptaExecutionAuditService;
import io.praecepta.audit.service.PraeceptaAuditServiceConstants;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;

public class PraeceptaAuditController implements IPraeceptaAuditController {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaAuditController.class);

	@Autowired
	private IPraeceptaAuditService  praeceptaAuditService;

	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT_FUNCTION_PATH, methodName = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT)
    @GET
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
    @Path(PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT_PATH)
	public List<PraeceptaRuleSpaceAuditPoint> getRuleGroupAduitUsingSpaceInfo(@PathParam(value = "spacename")String spaceName,
			@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version,
			@PathParam(value = "groupname") String groupname) {


		List<PraeceptaRuleSpaceAuditPoint> ruleAuditEntitities = praeceptaAuditService.fetchRuleGroupAudit(spaceName, clientId, appName, version, groupname);

		return ruleAuditEntitities;
	}

	@PraeceptaExposeAsRestServiceMethod(put = true, functionPath = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT_FUNCTION_PATH, methodName = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT)
    @PUT
    @Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
    @Path(PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT_PATH)
	public PraeceptaRuleSpaceAuditPoint captureAuditForRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId,
			@PathParam(value = "appname")String appName,
			@PathParam(value = "version")String version, @PathParam(value = "rulegroupname") String rulegroupname, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {

		PraeceptaRuleSpaceAuditPoint ruleAuditEntity = praeceptaAuditService.captureRuleGroupAudit(spaceName, clientId, appName, version, rulegroupname, ruleGroupAuditPoint);

		return ruleAuditEntity;
	}

	@PraeceptaExposeAsRestServiceMethod(put = true, functionPath = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_PATH, methodName = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT)
	@POST
	@Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Path(PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_PATH)
	public PraeceptaRuleSpaceAuditPoint refurbishAuditForRuleGroup(@PathParam(value = "uniqueId")String uniqueId, PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish) {

		praeceptaAuditService.refurbishRuleGroupAudit(uniqueId ,ruleGroupAuditPointToRefurbish);

		return ruleGroupAuditPointToRefurbish;
	}

	@Override
	public void execute(PraeceptaRequestStore requestStore) {

		String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);

		LOG.info("Operation Name: {}", operation);

		Map<String, Object> pathParams = (Map<String, Object>) requestStore
				.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);

		LOG.info("Path Params: {}", pathParams);

		switch (operation) {

		case PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT:

			String spaceName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
			String clientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
			String appName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
			String version = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
			String groupname = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);

			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
					getRuleGroupAduitUsingSpaceInfo(spaceName, clientId, appName, version, groupname));

			break;
		case PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT:

			String addSpaceName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
			String addClientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
			String addAppName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
			String addVersion = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
			String addGroupname = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);

			PraeceptaRuleGroupAuditPoint ruleGrpAuditPoint = GsonHelper.toEntity(
					(String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT),
					PraeceptaRuleGroupAuditPoint.class);

			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, captureAuditForRuleGroup(
					addSpaceName, addClientId, addAppName, addVersion, addGroupname, ruleGrpAuditPoint));

			break;

		case PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT:

			String uniqueId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_UNIQUE_ID);

			PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish = GsonHelper.toEntity(
					(String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT),
					PraeceptaRuleSpaceAuditPoint.class);

			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
					refurbishAuditForRuleGroup(uniqueId, ruleGroupAuditPointToRefurbish));

			break;

		case PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDIT:

			String addExecutionSpaceName = (String) pathParams
					.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
			String addExecutionClientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
			String addExecutionAppName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
			String addExecutionVersion = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
			String addExecutionGroupname = (String) pathParams
					.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);

				PraeceptaExecutionAuditPoint ruleExecutionAuditPoint = GsonHelper.toEntity(
						(String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT),
						PraeceptaExecutionAuditPoint.class);

				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						captureExectionAuditForRuleGroup(addExecutionSpaceName, addExecutionClientId,
								addExecutionAppName, addExecutionVersion, addExecutionGroupname,
								ruleExecutionAuditPoint));

			break;

		case PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDITS:

			String addExecutionsSpaceName = (String) pathParams
			.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
			String addExecutionsClientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
			String addExecutionsAppName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
			String addExecutionsVersion = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
			String addExecutionsGroupname = (String) pathParams
					.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);

				PraeceptaExecutionAuditPoints ruleExecutionAuditPoints = GsonHelper.toEntity(
						(String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT),
						PraeceptaExecutionAuditPoints.class);

				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
						captureExectionAuditsForRuleGroup(addExecutionsSpaceName, addExecutionsClientId,
								addExecutionsAppName, addExecutionsVersion, addExecutionsGroupname,
								ruleExecutionAuditPoints));

			break;

		case PraeceptaAuditServiceConstants.GET_RULE_GROUP_EXECUTION_AUDIT:

			String getExecutionSpaceName = (String) pathParams
			.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
			String getExecutionClientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
			String getExecutionAppName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
			String getExecutionVersion = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
			String getExecutionGroupname = (String) pathParams
					.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);

			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, getRuleGroupExecutionAudits(getExecutionSpaceName,
					getExecutionClientId, getExecutionAppName, getExecutionVersion, getExecutionGroupname));


		}

		LOG.info("Finished Execution of the Function Being Called in the Controller");

	}

	@Override
	public List<PraeceptaRuleSpaceAuditPoint> getRuleGroupAduitUsingSpaceInfo(String spaceName, String clientId,
			String appName, String version, String groupname) {

		List<PraeceptaRuleSpaceAuditPoint> ruleAuditEntitities = praeceptaAuditService.fetchRuleGroupAudit(spaceName,
				clientId, appName, version, groupname);

		return ruleAuditEntitities;
	}

	@Override
	public PraeceptaRuleSpaceAuditPoint captureAuditForRuleGroup(String spaceName, String clientId, String appName,
			String version, String rulegroupname, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {

		PraeceptaRuleSpaceAuditPoint ruleAuditEntity = praeceptaAuditService.captureRuleGroupAudit(spaceName, clientId,
				appName, version, rulegroupname, ruleGroupAuditPoint);

		return ruleAuditEntity;
	}

	@Override
	public PraeceptaRuleSpaceAuditPoint refurbishAuditForRuleGroup(String uniqueId,
			PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish) {

		praeceptaAuditService.refurbishRuleGroupAudit(uniqueId, ruleGroupAuditPointToRefurbish);

		return ruleGroupAuditPointToRefurbish;
	}

	@Override
	public PraeceptaExecutionAuditPoint captureExectionAuditForRuleGroup(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaExecutionAuditPoint ruleExecutionAuditPoint) {

		praeceptaExecutionAuditService.captureRuleGroupExecutionAudit(spaceName, clientId, appName, version, groupname, ruleExecutionAuditPoint);

		return ruleExecutionAuditPoint;
	}

	@Override
	public PraeceptaExecutionAuditPoints captureExectionAuditsForRuleGroup(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaExecutionAuditPoints ruleExecutionAuditPoints) {

		Collection<PraeceptaExecutionAuditPoint> ruleExecutionAuditPointDtos =  ruleExecutionAuditPoints.getRuleExecutionAuditPoints();

		praeceptaExecutionAuditService.captureRuleGroupExecutionAudits(spaceName, clientId, appName, version, groupname, ruleExecutionAuditPointDtos);

		return ruleExecutionAuditPoints;

	}

	@Override
	public PraeceptaExecutionAuditPoints getRuleGroupExecutionAudits(String spaceName, String clientId, String appName, String version,
			String groupname) {

		return praeceptaExecutionAuditService.fetchRuleGroupExecutionAudit(spaceName, clientId, appName, version, groupname);
	}

}
