package io.praecepta.rest.api.controller;

import javax.ws.rs.*;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.swagger.v3.oas.annotations.Operation;
@PraeceptaExposeAsRestService(serviceName = ServiceAndMethodNames.SIDE_CAR__CONTROLLER_NAME, servicePath = "/sidecarController")
@Path("/sidecarController")
public interface IPraeceptaSidecarController extends IPraeceptaApiService {

    @PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/getSidecars/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.SIDE_CARS_GET_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.SIDE_CARS_GET_METHOD_NAME)
    @GET
    @Produces("application/json")
    @Path("/getSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}")
    Object getSidecars(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, @PathParam(value = "groupname")String groupName);


    @PraeceptaExposeAsRestServiceMethod(put = true, functionPath = "/saveSidecars/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.SIDE_CARS_SAVE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.SIDE_CARS_SAVE_METHOD_NAME)
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/saveSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}")
    Object saveSidecars(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, @PathParam(value = "groupname")String groupName, PraeceptaRuleGroupSideCarsInfo request);



    @PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/updateSidecars/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.SIDE_CARS_UPDATE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.SIDE_CARS_UPDATE_METHOD_NAME)
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/updateSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}")
    Object updateSidecars(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, @PathParam(value = "groupname")String groupName, PraeceptaRuleGroupSideCarsInfo request);


    @PraeceptaExposeAsRestServiceMethod(delete = true, functionPath = "/deleteSidecars/:spacename/:clientid/:appname/:version/:groupname", methodName = ServiceAndMethodNames.SIDE_CARS_DELETE_METHOD_NAME)
    @Operation(operationId = ServiceAndMethodNames.SIDE_CARS_DELETE_METHOD_NAME)
    @DELETE
    @Produces("application/json")
    @Path("/deleteSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}")
    Object deleteSidecars(@PathParam(value = "spacename")String spaceName, @PathParam(value = "clientid")String clientId, @PathParam(value = "appname")String appName, @PathParam(value = "version")String version, @PathParam(value = "groupname")String groupName);


}
