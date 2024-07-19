package io.praecepta.audit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.audit.service.IPraeceptaAuditService;
import io.praecepta.audit.service.PraeceptaAuditServiceConstants;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.swagger.v3.oas.annotations.Operation;

@Path(PraeceptaAuditServiceConstants.AUDIT_SERVICE_PATH)
@PraeceptaExposeAsRestService(serviceName = PraeceptaAuditServiceConstants.AUDIT_CONTROLLER_NAME, servicePath = PraeceptaAuditServiceConstants.AUDIT_SERVICE_PATH)
public class PraeceptaAuditController implements IPraeceptaApiService {

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
		
		String spaceName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_SPACE_NAME);
		String clientId = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_CLIENT_ID);
		String appName = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_APP_NAME);
		String version = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_VERSION);
		String groupname = (String) pathParams.get(PraeceptaAuditServiceConstants.PATH_PARAM_RULEGROUPNAME);
		
		LOG.info("Path Params: {}", pathParams);
		
		switch (operation) {
		
			case PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT:
				
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, getRuleGroupAduitUsingSpaceInfo(spaceName, clientId, appName, version, groupname));
				
				break; 
			case PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT:
				
				PraeceptaRuleGroupAuditPoint ruleGrpAuditPoint = GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), PraeceptaRuleGroupAuditPoint.class);
				
				requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, captureAuditForRuleGroup(spaceName, clientId, appName, version, groupname, ruleGrpAuditPoint) );
				
				break;
		}
		
	}

}
