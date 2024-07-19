package io.praecepta.parser.execution.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.praecepta.parser.execution.constants.ServiceAndMethodNames;
import io.praecepta.parser.execution.request.dto.PraeceptaParserRequst;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;

import io.swagger.v3.oas.annotations.Operation;

@PraeceptaExposeAsRestService(serviceName = ServiceAndMethodNames.PARSER_EXECUTION_CONTROLLER_NAME, servicePath = "/parserExecutionController")
@Path("/parserExecutionController")
public interface IPraeceptaSQLParserExecutionController extends IPraeceptaApiService{

	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/executeParser", methodName = ServiceAndMethodNames.PARSER_EXECUTOR_METHOD_NAME)
	@Operation(operationId = ServiceAndMethodNames.PARSER_EXECUTOR_METHOD_NAME)
	@POST
	@Path("/executeParser")
	@Consumes("application/json")
	@Produces("application/json")
	Object executeParser(PraeceptaParserRequst request);

}
