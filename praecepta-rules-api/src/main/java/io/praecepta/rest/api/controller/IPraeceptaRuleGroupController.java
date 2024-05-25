package io.praecepta.rest.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rules.dto.MultiConditionGroupInfo;
import io.praecepta.rules.dto.MultiNestedConditionGroupInfo;
import io.praecepta.rules.dto.SimpleConditionGroupInfo;
import io.swagger.v3.oas.annotations.Operation;

@PraeceptaExposeAsRestService(serviceName = ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME, servicePath = "/ruleGroupController")
@Path("/ruleGroupController")
public interface IPraeceptaRuleGroupController extends IPraeceptaApiService{



	
	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/getRuleGroups/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.RULE_GROUP_GET_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.RULE_GROUP_GET_METHOD_NAME)
    @GET
	@Produces("application/json")
    @Path("/getRuleGroups/{spacename}/{clientid}/{appname}/{version}")
	Object getRuleGroups(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version);
	
	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/ruleGroups/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.RULE_GROUP_BY_NAME_GET_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.RULE_GROUP_BY_NAME_GET_METHOD_NAME)
    @GET
	@Produces("application/json")
    @Path("/ruleGroups/{spacename}/{clientid}/{appname}/{version}/{groupname}")
	Object getRuleGroupByName(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String groupname);
	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/ruleGroups/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.ADD_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.ADD_RULE_GROUP_METHOD_NAME)
    @POST
    @Path("/ruleGroups/{spacename}/{clientid}/{appname}/{version}")
	@Consumes("application/json")
	@Produces("application/json")
	Object addRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, SimpleConditionGroupInfo request );
	
	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/updateRuleGroup/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.UPDATE_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.UPDATE_RULE_GROUP_METHOD_NAME)
    @POST
    @Consumes("application/json")
	@Produces("application/json")
	@Path("/updateRuleGroup/{spacename}/{clientid}/{appname}/{version}")
	public Object updateRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, SimpleConditionGroupInfo request );

	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/multiRuleGroups/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.ADD_MULTI_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.ADD_MULTI_RULE_GROUP_METHOD_NAME)
	@POST
	@Path("/multiRuleGroups/{spacename}/{clientid}/{appname}/{version}")
	@Consumes("application/json")
	@Produces("application/json")
	Object addMultiRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, MultiConditionGroupInfo request );


	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/updateMultiRuleGroup/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.UPDATE_MULTI_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.UPDATE_MULTI_RULE_GROUP_METHOD_NAME)
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/updateMultiRuleGroup/{spacename}/{clientid}/{appname}/{version}")
	public Object updateMultiRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, MultiConditionGroupInfo request );

	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/multiNestedRuleGroups/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.ADD_MULTI_NESTED_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.ADD_MULTI_NESTED_RULE_GROUP_METHOD_NAME)
	@POST
	@Path("/multiNestedRuleGroups/{spacename}/{clientid}/{appname}/{version}")
	@Consumes("application/json")
	@Produces("application/json")
	Object addMultiNestedRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, MultiNestedConditionGroupInfo request );


	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/updateMultiNestedRuleGroup/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.UPDATE_MULTI_NESTED_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.UPDATE_MULTI_NESTED_RULE_GROUP_METHOD_NAME)
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/updateMultiNestedRuleGroup/{spacename}/{clientid}/{appname}/{version}")
	public Object updateMultiNestedRuleGroup(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, MultiNestedConditionGroupInfo request );

	@PraeceptaExposeAsRestServiceMethod(delete = true, functionPath = "/ruleGroups/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.DELETE_RULE_GROUP_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.DELETE_RULE_GROUP_METHOD_NAME)
    @DELETE
	@Path("/ruleGroups/{spacename}/{clientid}/{appname}/{version}/{groupname}")
	public Object deleteRuleGroup(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version, @PathParam(value = "groupname") String rulegroupname);
	
}
