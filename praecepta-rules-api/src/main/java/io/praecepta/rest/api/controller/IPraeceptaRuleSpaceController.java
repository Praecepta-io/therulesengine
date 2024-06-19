package io.praecepta.rest.api.controller;

import javax.ws.rs.*;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.swagger.v3.oas.annotations.Operation;

@PraeceptaExposeAsRestService(serviceName = ServiceAndMethodNames.RULE_SPACE_CONTROLLER_NAME, servicePath = "/ruleSpaceController")
@Path("/ruleSpaceController")
public interface IPraeceptaRuleSpaceController extends IPraeceptaApiService {

    @PraeceptaExposeAsRestServiceMethod(put = true, functionPath = "/ruleSpace", methodName = ServiceAndMethodNames.ADD_RULE_SPACE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.ADD_RULE_SPACE_METHOD_NAME)
    @PUT
    @Path("/ruleSpace")
    @Consumes("application/json")
    @Produces("application/json")
    public Object addRuleSpace(RuleSpaceInfo request);


    @PraeceptaExposeAsRestServiceMethod(delete = true, functionPath = "/ruleSpace/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.DELETE_RULE_SPACE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.DELETE_RULE_SPACE_METHOD_NAME)
    @DELETE
    @Path("/ruleSpace/{spacename}/{clientid}/{appname}/{version}")
    @Produces("application/json")
    public Object deleteRuleSpace(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appName,@PathParam(value = "version")String version);


    @PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/ruleSpace/:spacename/:clientid/:appname/:version", methodName = ServiceAndMethodNames.GET_RULE_SPACE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.GET_RULE_SPACE_METHOD_NAME)
    @GET
    @Path("/ruleSpace/{spacename}/{clientid}/{appname}/{version}")
    @Produces("application/json")
    public Object getRuleSpace(@PathParam(value = "spacename")String spaceName,@PathParam(value = "clientid")String clientId,@PathParam(value = "appname")String appame,@PathParam(value = "version")String version);


    @PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/ruleSpaceList", methodName = ServiceAndMethodNames.GET_RULE_SPACE_LIST_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.GET_RULE_SPACE_LIST_METHOD_NAME)
    @GET
    @Path("/ruleSpaceList")
    @Produces("application/json")
    public Object getRuleSpaceList();


}
