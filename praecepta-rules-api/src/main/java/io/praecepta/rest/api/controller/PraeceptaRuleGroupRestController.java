package io.praecepta.rest.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.praecepta.rules.dto.MultiConditionGroupInfo;
import io.praecepta.rules.dto.MultiNestedConditionGroupInfo;
import io.praecepta.rules.dto.SimpleConditionGroupInfo;

/**
 * Spring Boot REST Controller for Rule Group operations
 * Wraps the existing PraeceptaRuleGroupController implementation
 */
@RestController
@RequestMapping("/ruleGroupController")
public class PraeceptaRuleGroupRestController {

    @Autowired
    private IPraeceptaRuleGroupController ruleGroupController;

    @GetMapping(value = "/getRuleGroups/{spacename}/{clientid}/{appname}/{version}", produces = "application/json")
    public Object getRuleGroups(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version) {
        return ruleGroupController.getRuleGroups(spaceName, clientId, appName, version);
    }

    @GetMapping(value = "/ruleGroups/{spacename}/{clientid}/{appname}/{version}/{groupname}", produces = "application/json")
    public Object getRuleGroupByName(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String groupname) {
        return ruleGroupController.getRuleGroupByName(spaceName, clientId, appName, version, groupname);
    }

    @PutMapping(value = "/ruleGroups/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object addRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody SimpleConditionGroupInfo request) {
        return ruleGroupController.addRuleGroup(spaceName, clientId, appName, version, request);
    }

    @PostMapping(value = "/updateRuleGroup/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object updateRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody SimpleConditionGroupInfo request) {
        return ruleGroupController.updateRuleGroup(spaceName, clientId, appName, version, request);
    }

    @PutMapping(value = "/multiRuleGroups/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object addMultiRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody MultiConditionGroupInfo request) {
        return ruleGroupController.addMultiRuleGroup(spaceName, clientId, appName, version, request);
    }

    @PostMapping(value = "/updateMultiRuleGroup/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object updateMultiRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody MultiConditionGroupInfo request) {
        return ruleGroupController.updateMultiRuleGroup(spaceName, clientId, appName, version, request);
    }

    @PutMapping(value = "/multiNestedRuleGroups/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object addMultiNestedRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody MultiNestedConditionGroupInfo request) {
        return ruleGroupController.addMultiNestedRuleGroup(spaceName, clientId, appName, version, request);
    }

    @PostMapping(value = "/updateMultiNestedRuleGroup/{spacename}/{clientid}/{appname}/{version}", consumes = "application/json", produces = "application/json")
    public Object updateMultiNestedRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @RequestBody MultiNestedConditionGroupInfo request) {
        return ruleGroupController.updateMultiNestedRuleGroup(spaceName, clientId, appName, version, request);
    }

    @DeleteMapping(value = "/ruleGroups/{spacename}/{clientid}/{appname}/{version}/{groupname}", produces = "application/json")
    public Object deleteRuleGroup(
            @PathVariable("spacename") String spaceName,
            @PathVariable("clientid") String clientId,
            @PathVariable("appname") String appName,
            @PathVariable("version") String version,
            @PathVariable("groupname") String rulegroupname) {
        return ruleGroupController.deleteRuleGroup(spaceName, clientId, appName, version, rulegroupname);
    }
}
