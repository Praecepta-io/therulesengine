package io.praecepta.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.praecepta.rules.dto.RuleSpaceInfo;

/**
 * Spring Boot REST Controller for Rule Space operations
 * Wraps the existing PraeceptaRuleSpaceController implementation
 */
@RestController
@RequestMapping("/ruleSpaceController")
public class PraeceptaRuleSpaceRestController {

    @Autowired
    private IPraeceptaRuleSpaceController ruleSpaceController;

    @PutMapping(value = "/ruleSpace", consumes = "application/json", produces = "application/json")
    public Object addRuleSpace(@RequestBody RuleSpaceInfo request) {
        return ruleSpaceController.addRuleSpace(request);
    }

    @DeleteMapping(value = "/ruleSpace/{spacename}/{clientid}/{appname}/{version}", produces = "application/json")
    public Object deleteRuleSpace(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version) {
        return ruleSpaceController.deleteRuleSpace(spaceName, clientId, appName, version);
    }

    @GetMapping(value = "/ruleSpace/{spacename}/{clientid}/{appname}/{version}", produces = "application/json")
    public Object getRuleSpace(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version) {
        return ruleSpaceController.getRuleSpace(spaceName, clientId, appName, version);
    }

    @GetMapping(value = "/ruleSpaceList", produces = "application/json")
    public Object getRuleSpaceList() {
        return ruleSpaceController.getRuleSpaceList();
    }
}
