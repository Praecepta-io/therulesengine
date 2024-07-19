package io.praecepta.parser.execution.service.impl;

import static io.praecepta.parser.execution.service.helper.PraeceptaParserExecutionServiceHelper.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.praecepta.parser.execution.enums.PARSER_REQUEST_KEYS;
import io.praecepta.parser.execution.enums.PARSER_REQUEST_TYPE;
import io.praecepta.parser.execution.request.dto.PraeceptaParserRequst;
import io.praecepta.parser.execution.request.dto.PraeceptaSidecarsQueryDto;
import io.praecepta.parser.execution.request.dto.QueryTokensDto;
import io.praecepta.parser.execution.service.IPraeceptaParserExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rest.api.service.IPraeceptaSidecarService;
import io.praecepta.rules.dto.*;
import io.praecepta.rules.engine.execution.IPraeceptaRuleExecutionEngine;
import io.praecepta.rules.engine.execution.PraeceptaPipesAndFiltersRuleExecutionEngine;
import io.praecepta.rules.engine.helper.PraeceptaRuleExecutionEngineHelper;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.praecepta.sql.antlr.parser.SimpleSQLParser;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Execute_statmentContext;

public class PraeceptaParserExecutionService implements IPraeceptaParserExecutionService {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaParserExecutionService.class);
	
	@Autowired
	IPraeceptaRulesGroupService rulesGroupService;
	
	@Autowired
	IPraeceptaSidecarService sideCarsService;
	
	@Autowired
	IPraeceptaPivotalRulesHubManager pivotalRulesHubManager;
	
	private static final String RULE_INPUT_TYPE="ruleInputType";

	@Override
	public Object executeSQLParser(PraeceptaParserRequst request) {

		LOG.debug("Started executing SQL Parser");

		if (PraeceptaObjectHelper.isObjectNull(request) || PraeceptaObjectHelper.isObjectEmpty(request.getQuery())) {
			LOG.debug("Parser execution request found null");
		}

		LOG.debug("Started executing SQL Parser for the type :{}", request.getParserType());

		try {
			PARSER_REQUEST_TYPE parserType = PARSER_REQUEST_TYPE.valueOf(request.getParserType());

			switch (parserType) {

			case INSERT_RULE_GROUP:
			case UPDATE_RULE_GROUP:	

				return executeQueryInsertOrUpdateSimpleConditionRuleGroup(request);
			case INSERT_MULTI_RULE_GROUP:
			case UPDATE_MULTI_RULE_GROUP:

				return executeQueryInsertOrUpdateMultiConditionRuleGroup(request);
			case INSERT_MULTI_NESTED_RULE_GROUP:
			case UPDATE_MULTI_NESTED_RULE_GROUP:

				return executeQueryInsertOrUpdateMultiNestedConditionRuleGroup(request);

			case INSERT_RULE_SPACE:
			case UPDATE_RULE_SPACE:

				return executeQueryInsertOrUpdateRuleSpace(request);

			case INSERT_SIDE_CARS:
			case UPDATE_SIDE_CARS:

				return executeQueryInsertOrUpdateSideCars(request);

			case EXECUTE_RULES:

				return executeQueryRuleEngineExecution(request);
		    
			case DELETE_RULE_SPACE:
				
				return executeQueryDeleteRuleSpace(request);
				
			case DELETE_RULE_GROUP:
				
				return executeQueryDeleteRuleGroup(request);
				
			case DELETE_SIDE_CARS:
				
				return executeQueryDeleteSideCars(request);

			default:
				return null;
			}

		} catch (Exception e) {
			LOG.error("error while executing the parser", e);
			throw e;
		}
	}
    
	/*method to execute query of insert/update Rule space*/
	private Object executeQueryInsertOrUpdateRuleSpace(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given insert/update rule space query");

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())
				&& !PraeceptaObjectHelper.isObjectNull(queryTokensDto.getInputExprCtx())) {

			LOG.info("tableName from input : {}", queryTokensDto.getTableNameIdentifier());

			RuleSpaceInfo ruleSpaceInfo = prepareRuleSpaceRequest(queryTokensDto.getTableNameIdentifier(),
					queryTokensDto.getInputExprCtx());

			return addRuleSpace(ruleSpaceInfo);

		} else {
			LOG.debug("found sql parser inputExpr object from insert/update context as null");
		}

		return null;
	}
	
	/*method to execute query of insert/update Rule group*/
	private Object executeQueryInsertOrUpdateSimpleConditionRuleGroup(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given add/update rule group query");

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());
		
		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())
				&& !PraeceptaObjectHelper.isObjectNull(queryTokensDto.getInputExprCtx())) {

			LOG.info("tableName from input : {}", queryTokensDto.getTableNameIdentifier());

			SimpleConditionGroupInfo ruleGroupInfo = prepareRuleGroupRequestForSimpleCondition(queryTokensDto.getTableNameIdentifier(),
					queryTokensDto.getInputExprCtx());

			return addOrUpdateRuleGroup(ruleGroupInfo.getRuleSpaceInfo().getSpaceName(),
					ruleGroupInfo.getRuleSpaceInfo().getClientId(), ruleGroupInfo.getRuleSpaceInfo().getAppName(),
					ruleGroupInfo.getRuleSpaceInfo().getVersion(), ruleGroupInfo);

		}

		return null;
	}

	/*method to execute query of insert/update Rule group*/
	private Object executeQueryInsertOrUpdateMultiConditionRuleGroup(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given add/update rule group query");

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())
				&& !PraeceptaObjectHelper.isObjectNull(queryTokensDto.getInputExprCtx())) {

			LOG.info("tableName from input : {}", queryTokensDto.getTableNameIdentifier());

			MultiConditionGroupInfo ruleGroupInfo = prepareRuleGroupRequestForMultiCondition(queryTokensDto.getTableNameIdentifier(),
					queryTokensDto.getInputExprCtx());

			return addOrUpdateRuleGroup(ruleGroupInfo.getRuleSpaceInfo().getSpaceName(),
					ruleGroupInfo.getRuleSpaceInfo().getClientId(), ruleGroupInfo.getRuleSpaceInfo().getAppName(),
					ruleGroupInfo.getRuleSpaceInfo().getVersion(), ruleGroupInfo);

		}

		return null;
	}

	/*method to execute query of insert/update Rule group*/
	private Object executeQueryInsertOrUpdateMultiNestedConditionRuleGroup(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given add/update rule group query");

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())
				&& !PraeceptaObjectHelper.isObjectNull(queryTokensDto.getInputExprCtx())) {

			LOG.info("tableName from input : {}", queryTokensDto.getTableNameIdentifier());

			MultiNestedConditionGroupInfo ruleGroupInfo = prepareRuleGroupRequestForMultiNestedCondition(queryTokensDto.getTableNameIdentifier(),
					queryTokensDto.getInputExprCtx());

			return addOrUpdateRuleGroup(ruleGroupInfo.getRuleSpaceInfo().getSpaceName(),
					ruleGroupInfo.getRuleSpaceInfo().getClientId(), ruleGroupInfo.getRuleSpaceInfo().getAppName(),
					ruleGroupInfo.getRuleSpaceInfo().getVersion(), ruleGroupInfo);

		}

		return null;
	}

	/*method to execute query of insert/update Rule group side cars*/
	private Object executeQueryInsertOrUpdateSideCars(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given insert/update rule group sidecars query");

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())
				&& !PraeceptaObjectHelper.isObjectNull(queryTokensDto.getInputExprCtx())) {

			LOG.info("tableName from input : {}", queryTokensDto.getTableNameIdentifier());

			PraeceptaSidecarsQueryDto ruleGroupInfo = prepareSideCarsRequest(queryTokensDto.getTableNameIdentifier(),
					queryTokensDto.getInputExprCtx());

			return addOrUpdateSidecars(ruleGroupInfo.getRuleSpaceInfo().getSpaceName(),
					ruleGroupInfo.getRuleSpaceInfo().getClientId(), ruleGroupInfo.getRuleSpaceInfo().getAppName(),
					ruleGroupInfo.getVersion(), ruleGroupInfo.getSideCarsInfo().getRuleGrpName(),
					ruleGroupInfo.getSideCarsInfo());

		} else {
			LOG.debug("found sql parser object/insert context as null");
		}

		return null;
	}
    
	/*method to execute query to trigger rules engine*/
	private Object executeQueryRuleEngineExecution(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given input query to trigger rules engine");

		SimpleSQLParser parser = getParser(request.getQuery());

		if (!PraeceptaObjectHelper.isObjectNull(parser)) {

			Execute_statmentContext ctx = parser.execute_statment();

			if (!PraeceptaObjectHelper.isObjectEmpty(ctx)) {

				String tableName = ctx.tableIdentifier().getText();

				LOG.info("tableName from input : {}", tableName);

				Map<String, Object> rulesEngineInputMap = buildExecutionInput(ctx);
				
				rulesEngineInputMap.put(RULE_INPUT_TYPE, request.getRuleInputType());

				upsertSpaceKeyInfo(tableName, rulesEngineInputMap);

				return triggerRuleEngine(rulesEngineInputMap);
			}

		} else {
			LOG.debug("found sql parser object/insert context as null");
		}

		return null;
	}

	public Object triggerRuleEngine(Map<String, Object> request) {

		PraeceptaRequestStore ruleRequestStore = PraeceptaRuleExecutionEngineHelper
				.createRuleStore(GsonHelper.toJson(request), new HashMap<>());

		// Convert Side Car Meta Data to Actual Side Cars Depending on the Type and
		// SubType
		if (ruleRequestStore != null) {
			PraeceptaSideCarHelper.convertSideCarMetadataToSideCarsForAStore(ruleRequestStore);
		}

		IPraeceptaRuleExecutionEngine ruleExecutionEngine = new PraeceptaPipesAndFiltersRuleExecutionEngine();

		ruleExecutionEngine.performRuleEngineExecution(ruleRequestStore);

		return ((PraeceptaRequestStore) ((ArrayList) ruleRequestStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.CRITERIA_RULE_STORES)).get(0))
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST);
	}
	
	/*method to execute query of delete Rule space*/
	private Object executeQueryDeleteRuleSpace(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given delete rule space query");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())) {

			LOG.info("Space Key  to delete : {}", queryTokensDto.getTableNameIdentifier());
			// populating rule space key from table identifier
			populateRuleSpaceInfo(queryTokensDto.getTableNameIdentifier(), ruleSpaceInfo);

			Map<String, Object> havingCondMap = prepareInputMapFromDeleteContext(queryTokensDto);

			if (!PraeceptaObjectHelper.isObjectNull(havingCondMap)) {
				// populating rule space version from havingclaus
				ruleSpaceInfo.setVersion((String) havingCondMap.get(PARSER_REQUEST_KEYS.VERSION.name()));
			}
			return deleteRuleSpace(ruleSpaceInfo);
		}
		return "Issue while deleting Rule Space";
	}
	
	/* method to execute query of delete Rule Group */
	private Object executeQueryDeleteRuleGroup(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given delete rule group query");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())) {
			
			LOG.info("Space Key to delete : {}", queryTokensDto.getTableNameIdentifier());
			// populating rule space key from table identifier
			populateRuleSpaceInfo(queryTokensDto.getTableNameIdentifier(), ruleSpaceInfo);

			Map<String, Object> havingCondMap = prepareInputMapFromDeleteContext(queryTokensDto);

			if (!PraeceptaObjectHelper.isObjectNull(havingCondMap)) {

				// populating rule space version,rule group name from havingclaus
				ruleSpaceInfo.setVersion((String) havingCondMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

				String ruleGrpName = (String) havingCondMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name());

				LOG.info("RuleGrpName to delete : {}", ruleGrpName);

				return deleteRuleGroup(ruleSpaceInfo, ruleGrpName);
			}
		}
		return "Issue while deleting Rule Group";
	}
	
	/* method to execute query of delete Rule Group */
	private Object executeQueryDeleteSideCars(PraeceptaParserRequst request) {

		LOG.debug("started parse and executing given delete rule group side cars query");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		QueryTokensDto queryTokensDto = getInputTokensFromQuery(request.getQuery(), request.getParserType());

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getTableNameIdentifier())) {

			LOG.info("Space Key to delete : {}", queryTokensDto.getTableNameIdentifier());
			// populating rule space key from table identifier
			populateRuleSpaceInfo(queryTokensDto.getTableNameIdentifier(), ruleSpaceInfo);

			Map<String, Object> havingCondMap = prepareInputMapFromDeleteContext(queryTokensDto);

			if (!PraeceptaObjectHelper.isObjectNull(havingCondMap)) {

				// populating rule space version,rule group name from havingclaus
				ruleSpaceInfo.setVersion((String) havingCondMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

				String ruleGrpName = (String) havingCondMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name());

				LOG.info("Sidecars to be deleted for RuleGrpName {}", ruleGrpName);

				return deleteSideCars(ruleSpaceInfo, ruleGrpName);
			}
		}
		return "Issue while deleting Rule Group";
	}
    
	public Object addRuleSpace(RuleSpaceInfo request) {
		
		LOG.debug("before calling add rule space");
		
		return rulesGroupService.addRuleSpace(request);
	}
	
	public Object deleteRuleSpace(RuleSpaceInfo request) {

		LOG.debug("before calling delete rule space");

		return rulesGroupService.deleteRuleSpace(request.getSpaceName(), request.getClientId(), request.getAppName(),
				request.getVersion());
	}
	
	public Object deleteRuleGroup(RuleSpaceInfo request,String ruleGroupName) {

		LOG.debug("before calling delete rule group for rulegrpName:{}",ruleGroupName);

		return rulesGroupService.deleteRuleGroup(request.getSpaceName(), request.getClientId(), request.getAppName(),
				request.getVersion(), ruleGroupName);
	}
	
	public Object deleteSideCars(RuleSpaceInfo request,String ruleGroupName) {

		LOG.debug("before calling delete side cars for rulegrpName:{}", ruleGroupName);

		try {
			sideCarsService.deleteSideCars(request.getSpaceName(), request.getClientId(), request.getAppName(),
					request.getVersion(), ruleGroupName);
		} catch (Exception e) {
			LOG.error("error while deleting side cars", e);
			return "error while deleting side cars";
		}

		return "Side cars deleted successfully";
	}
	
	public Object addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version,
			SimpleConditionGroupInfo request) {
		LOG.debug("before calling add/update rule group");
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}

	public Object addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version,
									   MultiConditionGroupInfo request) {
		LOG.debug("before calling add/update rule group");
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}

	public Object addOrUpdateRuleGroup(String spaceName, String clientId, String appName, String version,
									   MultiNestedConditionGroupInfo request) {
		LOG.debug("before calling add/update rule group");
		return rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
	}
	
	public Object addOrUpdateSidecars(String spaceName, String clientId, String appName, String version,
			String ruleGrpName, PraeceptaRuleGroupSideCarsInfo request) {

		LOG.debug("before calling add/update rule group side cars");
		try {
			sideCarsService.saveSideCars(spaceName, clientId, appName, version, ruleGrpName, request);
		} catch (Exception e) {
			LOG.error("error while add/update side cars", e);
			return "error while add/update side cars";
		}
		return "Sidecars added successfully";
	}
}
