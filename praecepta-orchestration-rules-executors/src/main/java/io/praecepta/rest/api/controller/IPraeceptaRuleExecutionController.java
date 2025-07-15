package io.praecepta.rest.api.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.swagger.v3.oas.annotations.Operation;

@PraeceptaExposeAsRestService(serviceName = ServiceAndMethodNames.RULE_EXECUTION_CONTROLLER_NAME, servicePath = "/ruleExecutionController")
@Path("/ruleExecutionController")
public interface IPraeceptaRuleExecutionController extends IPraeceptaApiService{



	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.RULE_EXECUTOR_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.RULE_EXECUTOR_METHOD_NAME)
    @POST
    @Path("/{spacename}/{clientid}/{appname}/{version}/{groupname}")
	@Consumes("application/json")
	@Produces("application/json")
	Object executeRule(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version,@PathParam(value = "groupname")String groupName,
					   Object request );

	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/disableRuleExecutor", methodName = ServiceAndMethodNames.RULE_EXECUTOR_DISABLE_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.RULE_EXECUTOR_DISABLE_METHOD_NAME)
	@GET
	@Path("/disableRuleExecutor")
	@Produces("application/json")
	Object disableRuleExecutor();

	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/enableRuleExecutor", methodName = ServiceAndMethodNames.RULE_EXECUTOR_ENABLE_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.RULE_EXECUTOR_ENABLE_METHOD_NAME)
	@GET
	@Path("/enableRuleExecutor")
	@Produces("application/json")
	Object enableRuleExecutor();

}
