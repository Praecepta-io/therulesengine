package io.praecepta.sql.antlr.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.praecepta.sql.antlr.parser.SimpleSQLLexer;
import io.praecepta.sql.antlr.parser.SimpleSQLParser;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ConstantContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ExecuteExpressionContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Execute_statmentContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.IdentifierContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Insert_statementContext;

public class SimpleSQLParserTest {

	@Before
	public void init() {

	}

	@Test
	public void testParser_insert() {
		// String query = "INSERT INTO praecepta.rule_group_status_info (ID,
		// RULE_GROUP_ID) VALUES (1 ,'{\"spaceName\": \"test1\",\"clientId\":
		// \"CLNT1\",\"appName\": \"praecepta\", \"ruleGroupName\" :\"RuleGroup1\",
		// \"email\": \"test@gmail.com\",\"email2\": \"test@gmail.com\",\"mobile\":
		// \"1234567890\",\"version\":\"V1\",\"test\":{\"userName\":\"app1234\",\"age\":\"23\"}}');";

		String query = "INSERT INTO test.praecepta.CLNT1 (RULES) VALUES ('[{     \"ruleName\": \"ruleName1\",     \"actionList\": [{       \"actionStrategy\": \"ADD_TO_PAYLOAD\",       \"actionAttributeName\": \"attribute1\",       \"valueToAssign\": \"10\",       \"sourceValueAttributeName\": \"attribute2\"     }],     \"conditions\": {       \"conditionInfo\": {         \"attributeName\": \"emp.name\",         \"operatorType\": \"EQUAL_CHARS\",         \"valueToCompare\": \"vara\",         \"joinOperatorType\": \"AND\",         \"nextConditionInfo\": {           \"attributeName\": \"emp.sql\",           \"operatorType\": \"GREATER_THAN_EQUAL_NUMBER\",           \"valueToCompare\": 123456.78         }       },       \"joinOperatorType\": \"AND\",       \"nextNestedCondition\": {         \"conditionInfo\": {           \"attributeName\": \"emp.email\",           \"operatorType\": \"EQUAL_CHARS\",           \"valueToCompare\": \"varam.kotapati@gmail.com\",           \"joinOperatorType\": \"AND\",           \"nextConditionInfo\": {             \"attributeName\": \"emp.dob\",             \"operatorType\": \"EQUAL_DATE\",             \"valueToCompare\": \"2020-10-19\",             \"additionalParameters\": {               \"fromDateFormat\": \"yyyy-MM-dd\"             }           }         }       }     },     \"failureActionList\": [{       \"actionStrategy\": \"ADD_TO_PAYLOAD\",       \"actionAttributeName\": \"attribute_new\",       \"valueToAssign\": \"50\"     }]   }]');";

		CharStream charStream = new ANTLRInputStream(query);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);

		TokenStream tokens = new CommonTokenStream(lexer);

		SimpleSQLParser parser = new SimpleSQLParser(tokens);

		Insert_statementContext insertCtx = parser.insert_statement();

		String tableNameToken = insertCtx.tableIdentifier().getText();

		Assert.assertEquals("test.praecepta.CLNT1", tableNameToken);

		List<ConstantContext> columnValues = insertCtx.inputExpr().columnValues().constant();
		List<IdentifierContext> columnNames = insertCtx.inputExpr().columnNames().identifier();

		for (ParseTree child : columnNames) {
			System.out.println(child.getText());
		}

		for (ParseTree child : columnValues) {
			System.out.println(child.getText());
		}

		Assert.assertEquals(1, columnNames.size());
		Assert.assertEquals(1, columnValues.size());

//		TestListener listener = new TestListener();
//
//		ParseTree tree = parser.sql();
//
//		ParseTreeWalker walker = new ParseTreeWalker();
//
//		walker.walk(listener, tree);
	}
	
	@Test
	public void testParser_Insert_test2() {
		

		String query ="INSERT INTO test1.praecepta.CLNT1 (RULE_GROUP_NAME,VERSION,RULES) VALUES ('tesRuleGroup','V1','[{     \"ruleName\": \"ruleName1\",     \"actionList\": [{       \"actionStrategy\": \"ADD_TO_PAYLOAD\",       \"actionAttributeName\": \"attribute1\",       \"valueToAssign\": \"10\",       \"sourceValueAttributeName\": \"attribute2\"     }],     \"conditions\": {       \"conditionInfo\": {         \"attributeName\": \"emp.name\",         \"operatorType\": \"EQUAL_CHARS\",         \"valueToCompare\": \"vara\",         \"joinOperatorType\": \"AND\",         \"nextConditionInfo\": {           \"attributeName\": \"emp.sql\",           \"operatorType\": \"GREATER_THAN_EQUAL_NUMBER\",           \"valueToCompare\": 123456.78         }       },       \"joinOperatorType\": \"AND\",       \"nextNestedCondition\": {         \"conditionInfo\": {           \"attributeName\": \"emp.email\",           \"operatorType\": \"EQUAL_CHARS\",           \"valueToCompare\": \"varam.kotapati@gmail.com\",           \"joinOperatorType\": \"AND\",           \"nextConditionInfo\": {             \"attributeName\": \"emp.dob\",             \"operatorType\": \"EQUAL_DATE\",             \"valueToCompare\": \"2020-10-19\",             \"additionalParameters\": {               \"fromDateFormat\": \"yyyy-MM-dd\"             }           }         }       }     },     \"failureActionList\": [{       \"actionStrategy\": \"ADD_TO_PAYLOAD\",       \"actionAttributeName\": \"attribute_new\",       \"valueToAssign\": \"50\"     }]   }]');";

		CharStream charStream = new ANTLRInputStream(query);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);

		TokenStream tokens = new CommonTokenStream(lexer);

		SimpleSQLParser parser = new SimpleSQLParser(tokens);

		Insert_statementContext insertCtx = parser.insert_statement();

		String tableNameToken = insertCtx.tableIdentifier().getText();

		Assert.assertEquals("test1.praecepta.CLNT1", tableNameToken);

		List<ConstantContext> columnValues = insertCtx.inputExpr().columnValues().constant();
		List<IdentifierContext> columnNames = insertCtx.inputExpr().columnNames().identifier();

		for (ParseTree child : columnNames) {
			System.out.println(child.getText());
		}

		for (ParseTree child : columnValues) {
			System.out.println(child.getText());
		}

		Assert.assertEquals(3, columnNames.size());
		Assert.assertEquals(3, columnValues.size());

//		TestListener listener = new TestListener();
//
//		ParseTree tree = parser.sql();
//
//		ParseTreeWalker walker = new ParseTreeWalker();
//
//		walker.walk(listener, tree);
	}
	
	@Test
	public void testParser_Insert_WITH_expression() {
		

		String query ="INSERT INTO test1.praecepta.CLNT1 WITH ({\"test\":\"123\"})";

		CharStream charStream = new ANTLRInputStream(query);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);

		TokenStream tokens = new CommonTokenStream(lexer);

		SimpleSQLParser parser = new SimpleSQLParser(tokens);

		Insert_statementContext insertCtx = parser.insert_statement();

		String tableNameToken = insertCtx.tableIdentifier().getText();

		Assert.assertEquals("test1.praecepta.CLNT1", tableNameToken);
		
		insertCtx.inputExpr().columnValues();
		
		String jsonExpr=	insertCtx.inputExpr().jsonExpression().getText();
		System.out.println(insertCtx.inputExpr().jsonExpression());
	
		Assert.assertNotNull(jsonExpr);
	}
	
	@Test
	public void testParser_executeTest() {
		

		String query =" EXECUTE test1.praecepta.CLNT1 WHERE RULE_GROUP=tesRuleGroup,Version=V1,emp.name=Surekha";

		CharStream charStream = new ANTLRInputStream(query);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);

		TokenStream tokens = new CommonTokenStream(lexer);

		SimpleSQLParser parser = new SimpleSQLParser(tokens);

		Execute_statmentContext ctx = parser.execute_statment();

		String tableNameToken = ctx.tableIdentifier().getText();

		Assert.assertEquals("test1.praecepta.CLNT1", tableNameToken);

		List<ExecuteExpressionContext> columnValues = ctx.executeExprIdentifier().executeExpressionList().executeExpression();
		
		Map<String, Object> inputKeyWithValuesMap = new HashMap<>();

		for (ParseTree child : columnValues) {
			if(child.getText().contains("=")) {
				inputKeyWithValuesMap.putIfAbsent(child.getText().split("=")[0], child.getText().split("=")[1]);
			}
		}
		
		Assert.assertEquals(3, inputKeyWithValuesMap.size());
	}
	
	@Test
	public void testParser_executeTest_withExpression() {
		

		String query =" EXECUTE test1.praecepta.CLNT1 WITH ({\"emp.name\": \"alex\",\"emp.age\": \"15\",\"mobile\": \"1234567890\",\"test\":{\"userName\":\"app1234\",\"age\":\"23\"}})";

		CharStream charStream = new ANTLRInputStream(query);

		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);

		TokenStream tokens = new CommonTokenStream(lexer);

		SimpleSQLParser parser = new SimpleSQLParser(tokens);

		Execute_statmentContext ctx = parser.execute_statment();

		String tableNameToken = ctx.tableIdentifier().getText();

		Assert.assertEquals("test1.praecepta.CLNT1", tableNameToken);

		
		String jsonExpr=	ctx.executeExprIdentifier().jsonExpression().getText();
		System.out.println(jsonExpr);
		Assert.assertNotNull(jsonExpr);
	}
}
