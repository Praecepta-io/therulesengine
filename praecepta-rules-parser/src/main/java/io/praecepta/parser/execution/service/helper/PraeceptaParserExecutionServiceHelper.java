package io.praecepta.parser.execution.service.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import io.praecepta.parser.execution.enums.PARSER_REQUEST_KEYS;
import io.praecepta.parser.execution.enums.PARSER_REQUEST_TYPE;
import io.praecepta.parser.execution.request.dto.PraeceptaSidecarsQueryDto;
import io.praecepta.parser.execution.request.dto.QueryTokensDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.dto.*;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleLevelSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;
import io.praecepta.sql.antlr.parser.SimpleSQLLexer;
import io.praecepta.sql.antlr.parser.SimpleSQLParser;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ConstantContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Delete_statementContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ExecuteExpressionContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Execute_statmentContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.HavingConditionContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.IdentifierContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.InputExprContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Insert_statementContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Update_statementContext;
import io.praecepta.sql.antlr.parser.exceptions.ParserErrorListener;

public class PraeceptaParserExecutionServiceHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaParserExecutionServiceHelper.class);

	private static final String TABLE_NAME_SPLIT_REGIX = "\\.";
	private static final String COLUMN_VALUE_ENCLOSED_QUOTE = "'";

	/*method to get input details from query insert/update context */
	public static QueryTokensDto getInputTokensFromQuery(String query, String queryType) {

		QueryTokensDto queryTokensDto = new QueryTokensDto();

		LOG.debug("before parsing input query");
		SimpleSQLParser parser = getParser(query);

		if (!PraeceptaObjectHelper.isObjectNull(parser)) {

			PARSER_REQUEST_TYPE parserType = PARSER_REQUEST_TYPE.valueOf(queryType);

			switch (parserType) {

			case INSERT_RULE_GROUP:
			case INSERT_RULE_SPACE:
			case INSERT_SIDE_CARS:
				Insert_statementContext insertCtx = parser.insert_statement();
				if (!PraeceptaObjectHelper.isObjectNull(insertCtx)) {
					queryTokensDto.setTableNameIdentifier(insertCtx.tableIdentifier().getText());
					queryTokensDto.setInputExprCtx(insertCtx.inputExpr());
				}else {
					LOG.info("found sql parser insert context as null");
				}
				
				break;
			case UPDATE_RULE_GROUP:
			case UPDATE_RULE_SPACE:
			case UPDATE_SIDE_CARS:
				Update_statementContext updateCtx = parser.update_statement();
				if (!PraeceptaObjectHelper.isObjectNull(updateCtx)) {
					queryTokensDto.setTableNameIdentifier(updateCtx.tableIdentifier().getText());
					queryTokensDto.setInputExprCtx(updateCtx.inputExpr());
				}else {
					LOG.info("found sql parser update context as null");
				}
				break;
			case DELETE_RULE_SPACE:
			case DELETE_RULE_GROUP:
			case DELETE_SIDE_CARS:
				Delete_statementContext deleteCtx = parser.delete_statement();
				if (!PraeceptaObjectHelper.isObjectNull(deleteCtx)) {
					queryTokensDto.setTableNameIdentifier(deleteCtx.tableIdentifier().getText());
					queryTokensDto.setHavingKeyValExpr(deleteCtx.havingKeyValExpr());
				}else {
					LOG.info("found sql parser delete context as null");
				}
				break;	
			default:
				LOG.debug("Query type  not found :{}", queryType);
				break;
			}
		} else {
			LOG.info("found sql parser object as null ,check the given input query");
		}
		return queryTokensDto;
	}

	public static SimpleSQLParser getParser(String sqlQuery) {

		LOG.debug("inside getting parser for given input query");

		CharStream charStream = new ANTLRInputStream(sqlQuery);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);
		lexer.removeErrorListeners();
		lexer.addErrorListener(ParserErrorListener.INSTANCE);

		TokenStream tokens = new CommonTokenStream(lexer);

		return new SimpleSQLParser(tokens);
	}
    
	/*method to prepare RuleSpace request to insert/update*/
	public static RuleSpaceInfo prepareRuleSpaceRequest(String tableName, InputExprContext inputExpr) {
		
		LOG.debug("inside preparing rule space info request object");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnNames())
				&& !PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnValues())) {

			Map<String, String> columnNameWithValuesMap = buildColumnNamesWithValuesMap(inputExpr);

			// Getting Version from sql insert columnValues
			if (!PraeceptaObjectHelper.isObjectEmpty(columnNameWithValuesMap)) {

				ruleSpaceInfo.setVersion(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

			}

		} else if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.jsonExpression())) {
			// converting input json as map to get version
			Map<String, Object> ruleSpaceRequestMap = GsonHelper
					.toEntity(inputExpr.jsonExpression().getText(), Map.class);

			if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceRequestMap)) {
				ruleSpaceInfo.setVersion(
						(String) ruleSpaceRequestMap.getOrDefault(PARSER_REQUEST_KEYS.VERSION.name(), "V1"));
			}
		}
        //populating rule space key from table identifier 
		populateRuleSpaceInfo(tableName, ruleSpaceInfo);

		return ruleSpaceInfo;
	}
    
	/*method to prepare RuleGroup request to insert/update  */
	public static SimpleConditionGroupInfo prepareRuleGroupRequestForSimpleCondition(String tableName, InputExprContext inputExpr) {
		
		LOG.debug("inside preparing rule group info request object");
		
		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		SimpleConditionGroupInfo ruleGroupInfo = new SimpleConditionGroupInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnNames())
				&& !PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnValues())) {

			Map<String, String> columnNameWithValuesMap = buildColumnNamesWithValuesMap(inputExpr);

			// Getting Version,RuleGroupName and RulesCriteriaList from sql insert where clause
			if (!PraeceptaObjectHelper.isObjectEmpty(columnNameWithValuesMap)) {

				ruleSpaceInfo.setVersion(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

				ruleGroupInfo.setRuleGroupName(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name()));

				ruleGroupInfo.setSimpleConditionCriteriaInfos(
						GsonHelper.toEntity(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULES.name()),
								List.class, SimpleConditionCriteriaInfo.class));
				
			}
		} else if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.jsonExpression())) {
			
			// converting input ruleGroupJson as entity
			ruleGroupInfo = GsonHelper.toEntity(inputExpr.jsonExpression().getText(), SimpleConditionGroupInfo.class);
			
			// getting rule space info present inside input json -to get version
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo)
					&& !PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo.getRuleSpaceInfo())) {
				ruleSpaceInfo = ruleGroupInfo.getRuleSpaceInfo();
			}
		}
        
		//populating rule space key from table identifier 
		populateRuleSpaceInfo(tableName, ruleSpaceInfo);

		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;

	}


	public static MultiConditionGroupInfo prepareRuleGroupRequestForMultiCondition(String tableName, InputExprContext inputExpr) {

		LOG.debug("inside preparing rule group info request object");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		MultiConditionGroupInfo ruleGroupInfo = new MultiConditionGroupInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnNames())
				&& !PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnValues())) {

			Map<String, String> columnNameWithValuesMap = buildColumnNamesWithValuesMap(inputExpr);

			// Getting Version,RuleGroupName and RulesCriteriaList from sql insert where clause
			if (!PraeceptaObjectHelper.isObjectEmpty(columnNameWithValuesMap)) {

				ruleSpaceInfo.setVersion(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

				ruleGroupInfo.setRuleGroupName(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name()));

				ruleGroupInfo.setMultiConditionCriteriaInfos(
						GsonHelper.toEntity(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULES.name()),
								List.class, MultiConditionGroupInfo.class));

			}
		} else if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.jsonExpression())) {

			// converting input ruleGroupJson as entity
			ruleGroupInfo = GsonHelper.toEntity(inputExpr.jsonExpression().getText(), MultiConditionGroupInfo.class);

			// getting rule space info present inside input json -to get version
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo)
					&& !PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo.getRuleSpaceInfo())) {
				ruleSpaceInfo = ruleGroupInfo.getRuleSpaceInfo();
			}
		}

		//populating rule space key from table identifier
		populateRuleSpaceInfo(tableName, ruleSpaceInfo);

		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;

	}


	public static MultiNestedConditionGroupInfo prepareRuleGroupRequestForMultiNestedCondition(String tableName, InputExprContext inputExpr) {

		LOG.debug("inside preparing rule group info request object");

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		MultiNestedConditionGroupInfo ruleGroupInfo = new MultiNestedConditionGroupInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnNames())
				&& !PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnValues())) {

			Map<String, String> columnNameWithValuesMap = buildColumnNamesWithValuesMap(inputExpr);

			// Getting Version,RuleGroupName and RulesCriteriaList from sql insert where clause
			if (!PraeceptaObjectHelper.isObjectEmpty(columnNameWithValuesMap)) {

				ruleSpaceInfo.setVersion(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

				ruleGroupInfo.setRuleGroupName(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name()));

				ruleGroupInfo.setMultiNestedConditionCriteriaInfos(
						GsonHelper.toEntity(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULES.name()),
								List.class, MultiNestedConditionGroupInfo.class));

			}
		/*} else if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.jsonExpression())) {

			// converting input ruleGroupJson as entity
			ruleGroupInfo = GsonHelper.toEntity(inputExpr.jsonExpression().getText(), MultiNestedConditionGroupInfo.class);

			// getting rule space info present inside input json -to get version
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo)
					&& !PraeceptaObjectHelper.isObjectEmpty(ruleGroupInfo.getRuleSpaceInfo())) {
				ruleSpaceInfo = ruleGroupInfo.getRuleSpaceInfo();
			}*/
		}

		//populating rule space key from table identifier
		populateRuleSpaceInfo(tableName, ruleSpaceInfo);

		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;

	}

	/*method to prepare RuleGroup Sidecars request to insert/update*/
	@SuppressWarnings("unchecked")
	public static PraeceptaSidecarsQueryDto prepareSideCarsRequest(String tableName, InputExprContext inputExpr) {
        
		LOG.debug("inside preparing rule group sidecars request object");
		
		PraeceptaSidecarsQueryDto siderCarsQryDTO = new PraeceptaSidecarsQueryDto();

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

		if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnNames())
				&& !PraeceptaObjectHelper.isObjectEmpty(inputExpr.columnValues())) {

			prepareSideCarsRequestFromWhereCondition(inputExpr, siderCarsQryDTO);
			
		} else if (!PraeceptaObjectHelper.isObjectEmpty(inputExpr.jsonExpression())) {
			// converting input side cars json as entity
			siderCarsQryDTO = GsonHelper.toEntity(inputExpr.jsonExpression().getText(),
					PraeceptaSidecarsQueryDto.class);
		}

		// populating rule space info from tableIdentifier
		populateRuleSpaceInfo(tableName, ruleSpaceInfo);

		siderCarsQryDTO.setRuleSpaceInfo(ruleSpaceInfo);

		return siderCarsQryDTO;

	}
	/*method to prepare side cars request from where condition key/value pairs */
	@SuppressWarnings("unchecked")
	private static void prepareSideCarsRequestFromWhereCondition(InputExprContext inputExpr,PraeceptaSidecarsQueryDto siderCarsQryDTO) {
		
		LOG.debug("inside preparing rule group sidecars request from where condition");
		
		PraeceptaRuleGroupSideCarsInfo sideCarsInfo = new PraeceptaRuleGroupSideCarsInfo();
		
		Map<String, String> columnNameWithValuesMap = buildColumnNamesWithValuesMap(inputExpr);

		// Getting Version,RuleGroupName and sideCars info from sql insert columnValues
		if (!PraeceptaObjectHelper.isObjectEmpty(columnNameWithValuesMap)) {

			sideCarsInfo.setRuleGrpName(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULE_GROUP_NAME.name()));

			sideCarsInfo.setPreRuleGrpSideCars(GsonHelper.toEntity(
					columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.PRE_RULE_GRP_SIDECARS.name()), List.class,
					PraeceptaSideCarMetadata.class));
			sideCarsInfo.setPostRuleGrpSideCars(GsonHelper.toEntity(
					columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.POST_RULE_GRP_SIDECARS.name()), List.class,
					PraeceptaSideCarMetadata.class));
			sideCarsInfo.setRuleLevelSideCars(GsonHelper.toEntity(
					columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.RULE_LEVEL_SIDECARS.name()), List.class,
					PraeceptaRuleLevelSideCarsInfo.class));

			siderCarsQryDTO.setVersion(columnNameWithValuesMap.get(PARSER_REQUEST_KEYS.VERSION.name()));

			siderCarsQryDTO.setSideCarsInfo(sideCarsInfo);

		}
	}
    
	/* populating rule space key from table name identifier*/
	public static void populateRuleSpaceInfo(String tableName, RuleSpaceInfo ruleSpaceInfo) {
		String[] tableIdentiiferTuples = tableName.split(TABLE_NAME_SPLIT_REGIX);

		// adding/overriding space key info from INSERT statement tableIdentiifer
		if (!PraeceptaObjectHelper.isObjectEmpty(tableIdentiiferTuples) && tableIdentiiferTuples.length >= 3) {
			ruleSpaceInfo.setSpaceName(tableIdentiiferTuples[0]);
			ruleSpaceInfo.setAppName(tableIdentiiferTuples[1]);
			ruleSpaceInfo.setClientId(tableIdentiiferTuples[2]);
		}
	}
    
	/*preparing map from key/Value pairs from input expression like (key1,key2)  VALUES ('val1','val2')  */
	public static Map<String, String> buildColumnNamesWithValuesMap(InputExprContext inputExpr) {

		List<IdentifierContext> columnNames = inputExpr.columnNames().identifier();

		List<ConstantContext> columnValues = inputExpr.columnValues().constant();

		Map<String, String> columnNameWithValuesMap = new HashMap<>();

		if (!PraeceptaObjectHelper.isObjectEmpty(columnNames) && !PraeceptaObjectHelper.isObjectEmpty(columnValues)) {

			int counter = 0;

			for (ParseTree child : columnNames) {
				columnNameWithValuesMap.put(child.getText(), replaceQuotes(columnValues.get(counter).getText()));
				counter++;
			}
		}
		return columnNameWithValuesMap;
	}
    /* method to build rule engine execution input map*/
	public static Map<String, Object> buildExecutionInput(Execute_statmentContext ctx) {
		
		LOG.debug("before preparing execution input map");

		Map<String, Object> rulesEngineInputMap = new HashMap<>();

		populateHavingConditionalInputToMap(ctx, rulesEngineInputMap);

		prepareInputFromWhereCondContext(ctx, rulesEngineInputMap);

		return rulesEngineInputMap;
	}
	
    /*Method to populate key/val pairs from HAVING condition ex: HAVING key1=val1,key2=val2 ... */
	public static void populateHavingConditionalInputToMap(Execute_statmentContext ctx,
			Map<String, Object> rulesEngineInputMap) {
		
		LOG.debug("adding key/value pairs from havng conditions to input map");

		// Getting ruleGroupName, Version from having condition
		if (!PraeceptaObjectHelper.isObjectEmpty(ctx.havingKeyValExpr())
				&& !PraeceptaObjectHelper.isObjectEmpty(ctx.havingKeyValExpr().havingConditionList())) {
			
			List<HavingConditionContext> havingConditions = ctx.havingKeyValExpr().havingConditionList().havingCondition();
			
			prepareInputFromHavingCondContext(havingConditions, rulesEngineInputMap);
		}
	}
	
	public static void prepareInputFromHavingCondContext(List<HavingConditionContext> havingConditions,
			Map<String, Object> inputMap) {

		if (!PraeceptaObjectHelper.isObjectEmpty(havingConditions)) {
			Map<String, Object> rulGrpDetailsMap=new HashMap<>();
			for (ParseTree child : havingConditions) {
				if (child.getText().contains("=")) {
					rulGrpDetailsMap.putIfAbsent(child.getText().split("=")[0], child.getText().split("=")[1]);
				}
			}
			inputMap.put("rulGrpDetails", rulGrpDetailsMap);
		}
	}

	/*Method to populate key/val pairs from WHERE condition ex: WHERE key1=val1,key2=val2 ... */
	private static void prepareInputFromWhereCondContext(Execute_statmentContext ctx,
			Map<String, Object> rulesEngineInputMap) {

		LOG.debug("adding key/value pairs from where conditions to input map");
		Map<String, Object> rulesInputMap=new HashMap<>();
		// preparing input map from where/with condition
		if (!PraeceptaObjectHelper.isObjectEmpty(ctx.executeExprIdentifier().executeExpressionList())) {
			
			// getting input from WHERE clause as KeyValuePairs
			List<ExecuteExpressionContext> inputKeyValues = ctx.executeExprIdentifier().executeExpressionList()
					.executeExpression();

			if (!PraeceptaObjectHelper.isObjectEmpty(inputKeyValues)) {

				for (ParseTree child : inputKeyValues) {
					if (child.getText().contains("=")) {
						rulesInputMap.putIfAbsent(child.getText().split("=")[0], child.getText().split("=")[1]);
					}
				}
				rulesEngineInputMap.put("ruleInput", GsonHelper.toJson(rulesInputMap));
			}

		} else if (!PraeceptaObjectHelper.isObjectEmpty(ctx.executeExprIdentifier().jsonExpression())) {
			// getting input from WITH clause as JSON
			rulesEngineInputMap.put("ruleInput", ctx.executeExprIdentifier().jsonExpression().getText());
		}
		
	}
	
	public static Map<String, Object> prepareInputMapFromDeleteContext(QueryTokensDto queryTokensDto) {

		LOG.debug("preparing key/value pairs from having conditions");
		
		Map<String, Object> havingCondMap = new HashMap<>();

		if (!PraeceptaObjectHelper.isObjectNull(queryTokensDto.getHavingKeyValExpr())) {
			// Getting ruleGroupName, Version from having condition
			if (!PraeceptaObjectHelper.isObjectEmpty(queryTokensDto.getHavingKeyValExpr().havingConditionList())) {

				List<HavingConditionContext> havingConditions = queryTokensDto.getHavingKeyValExpr()
						.havingConditionList().havingCondition();

				prepareInputFromHavingCondContext(havingConditions, havingCondMap);
			}
		} else {
			LOG.debug("found sql parser object delete context expression as null");
		}
		LOG.debug("input form Having condition :{}", havingCondMap);
		return havingCondMap;
	}

	public static void upsertSpaceKeyInfo(String tableName, Map<String, Object> inputKeyWithValuesMap) {

		String[] tableIdentiiferTuples = tableName.split(TABLE_NAME_SPLIT_REGIX);
		
		Map<String, Object> spaceKeyDetailsMap = new HashMap<>();
		
		if (!PraeceptaObjectHelper.isObjectEmpty(tableIdentiiferTuples) && tableIdentiiferTuples.length >= 3) {
			spaceKeyDetailsMap.putIfAbsent(PARSER_REQUEST_KEYS.SPACE_NAME.name(), tableIdentiiferTuples[0]);
			spaceKeyDetailsMap.putIfAbsent(PARSER_REQUEST_KEYS.APP_NAME.name(), tableIdentiiferTuples[1]);
			spaceKeyDetailsMap.putIfAbsent(PARSER_REQUEST_KEYS.CLIENT_NAME.name(), tableIdentiiferTuples[2]);
			spaceKeyDetailsMap.putIfAbsent("spaceName", tableIdentiiferTuples[0]);
			spaceKeyDetailsMap.putIfAbsent("appName", tableIdentiiferTuples[1]);
			spaceKeyDetailsMap.putIfAbsent("clientId", tableIdentiiferTuples[2]);
		}
		inputKeyWithValuesMap.put("spaceKeyDetails", spaceKeyDetailsMap);
	}
	
	public static String replaceQuotes(String columnValue) {

		if (!PraeceptaObjectHelper.isObjectEmpty(columnValue) && columnValue.startsWith(COLUMN_VALUE_ENCLOSED_QUOTE)) {

			columnValue = columnValue.trim().substring(1, columnValue.length());
		}
		if (!PraeceptaObjectHelper.isObjectEmpty(columnValue) && columnValue.endsWith(COLUMN_VALUE_ENCLOSED_QUOTE)) {

			columnValue = columnValue.trim().substring(0, columnValue.length() - 1);
		}
		return columnValue;
	}
}
