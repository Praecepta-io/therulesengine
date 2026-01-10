package io.praecepta.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

/**
 * Spring Boot REST Controller for Sidecar operations
 * Wraps the existing PraeceptaSidecarController implementation
 */
@RestController
@RequestMapping("/sidecarController")
public class PraeceptaSidecarRestController {

    @Autowired
    private IPraeceptaSidecarController sidecarController;

    @GetMapping(value = "/getSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}", produces = "application/json")
    public Object getSidecars(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String groupName) {
        return sidecarController.getSidecars(spaceName, clientId, appName, version, groupName);
    }

    @PutMapping(value = "/saveSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}", consumes = "application/json", produces = "application/json")
    public Object saveSidecars(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String groupName,
            @RequestBody PraeceptaRuleGroupSideCarsInfo request) {
        return sidecarController.saveSidecars(spaceName, clientId, appName, version, groupName, request);
    }

    @PostMapping(value = "/updateSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}", consumes = "application/json", produces = "application/json")
    public Object updateSidecars(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String groupName,
            @RequestBody PraeceptaRuleGroupSideCarsInfo request) {
        return sidecarController.updateSidecars(spaceName, clientId, appName, version, groupName, request);
    }

    @DeleteMapping(value = "/deleteSidecars/{spacename}/{clientid}/{appname}/{version}/{groupname}", produces = "application/json")
    public Object deleteSidecars(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String groupName) {
        return sidecarController.deleteSidecars(spaceName, clientId, appName, version, groupName);
    }
}
