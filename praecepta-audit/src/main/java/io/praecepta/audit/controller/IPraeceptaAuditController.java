package io.praecepta.audit.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.praecepta.audit.service.PraeceptaAuditServiceConstants;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.swagger.v3.oas.annotations.Operation;

@PraeceptaExposeAsRestService(serviceName = PraeceptaAuditServiceConstants.AUDIT_CONTROLLER_NAME, servicePath = PraeceptaAuditServiceConstants.AUDIT_SERVICE_PATH)
@Path(PraeceptaAuditServiceConstants.AUDIT_SERVICE_PATH)
public interface IPraeceptaAuditController extends IPraeceptaApiService{
	
	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT_FUNCTION_PATH, methodName = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT)
    @GET
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
    @Path(PraeceptaAuditServiceConstants.GET_RULE_GROUP_AUDIT_PATH)
	public List<PraeceptaRuleSpaceAuditPoint> getRuleGroupAduitUsingSpaceInfo(@PathParam(value = "spacename")String spaceName,
			@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, 
			@PathParam(value = "groupname") String groupname);
	
	@PraeceptaExposeAsRestServiceMethod(put = true, functionPath = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT_FUNCTION_PATH, methodName = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT)
	@PUT
    @Path(PraeceptaAuditServiceConstants.ADD_RULE_GROUP_AUDIT_PATH)
    @Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	public PraeceptaRuleSpaceAuditPoint captureAuditForRuleGroup(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,
			@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String groupname
			, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint
			) ;
	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_FUNCTION_PATH, methodName = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_RULE_GROUP_AUDIT)
	@POST
	@Path(PraeceptaAuditServiceConstants.REFURBISH_RULE_GROUP_AUDIT_PATH)
	@Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	public PraeceptaRuleSpaceAuditPoint refurbishAuditForRuleGroup(@PathParam(value = "uniqueId")String uniqueId, PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish);
	
	@PraeceptaExposeAsRestServiceMethod(put = true, functionPath = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDIT_FUNCTION_PATH, 
			methodName = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDIT)
    @PUT
    @Path(PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDIT_PATH)
    @Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	public PraeceptaExecutionAuditPoint captureExectionAuditForRuleGroup(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,
			@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String groupname, 
			PraeceptaExecutionAuditPoint ruleExecutionAuditPoint);
	
	@PraeceptaExposeAsRestServiceMethod(put = true, functionPath = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDITS_FUNCTION_PATH, 
			methodName = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDITS)
	@Operation(operationId = PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDITS)
	@PUT
	@Path(PraeceptaAuditServiceConstants.ADD_RULE_GROUP_EXECUTION_AUDITS_PATH)
	@Consumes(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
	public PraeceptaExecutionAuditPoints captureExecutionAuditsForRuleGroup(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,
			@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String groupname,
			PraeceptaExecutionAuditPoints ruleExecutionAuditPoints);
//	
	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = PraeceptaAuditServiceConstants.GET_RULE_GROUP_EXECUTION_AUDIT_FUNCTION_PATH, 
			methodName = PraeceptaAuditServiceConstants.GET_RULE_GROUP_EXECUTION_AUDIT)
	@Operation(operationId = PraeceptaAuditServiceConstants.GET_RULE_GROUP_EXECUTION_AUDIT)
    @GET
	@Produces(PraeceptaAuditServiceConstants.JSON_PRODUCE)
    @Path(PraeceptaAuditServiceConstants.GET_RULE_GROUP_EXECUTION_AUDIT_PATH)
	public PraeceptaExecutionAuditPoints getRuleGroupExecutionAudits(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,
			@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String groupname);

}
