package io.praecepta.sql.parser.listener.impl;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.beans.factory.annotation.Autowired;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.sql.antlr.parser.SimpleSQLBaseListener;
import io.praecepta.sql.antlr.parser.SimpleSQLLexer;
import io.praecepta.sql.antlr.parser.SimpleSQLParser;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ConstantContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.IdentifierContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.Insert_statementContext;
import io.praecepta.sql.antlr.parser.exceptions.ParserErrorListener;

public class PraeceptaSQLParserListener extends SimpleSQLBaseListener {

//	@Autowired
//	IPraeceptaRulesGroupService rulesGroupService;
//
//	public void executeParser(String sqlQuery) throws Exception {
//
//		CharStream charStream = new ANTLRInputStream(sqlQuery);
//
//		SimpleSQLLexer lexer = new SimpleSQLLexer(charStream);
//		lexer.removeErrorListeners();
//		lexer.addErrorListener(ParserErrorListener.INSTANCE);
//
//		TokenStream tokens = new CommonTokenStream(lexer);
//
//		SimpleSQLParser parser = new SimpleSQLParser(tokens);
//		Insert_statementContext insertCtx=parser.insert_statement();
//		List<ConstantContext> columnValues = insertCtx.columnValues().constant();
//		List<IdentifierContext> columnNames = insertCtx.columnNames().identifier();
//		System.out.println(columnNames.size());
//		
//		for (ParseTree child : columnNames) {
//			System.out.println(child.getText());
//		}
//		
//		for (ParseTree child : columnValues) {
//			System.out.println(child.getText());
//		}
//		
//		
//		
//		parser.removeErrorListeners();
//		parser.addErrorListener(ParserErrorListener.INSTANCE);
//
//		ParseTree tree = parser.sql();
//
//		ParseTreeWalker walker = new ParseTreeWalker();
//
//		walker.walk(this, tree);
//
//	}
//
//	@Override
//	public void enterSql(SimpleSQLParser.SqlContext ctx) {
//
//		try {
//			super.enterSql(ctx);
//		} catch (Exception e) {
//			throw new RuntimeException("Error while parsing given input");
//		}
//
//	}
//
//	@Override
//	public void exitInsert_statement(SimpleSQLParser.Insert_statementContext ctx) {
//		try {
//
//			String tableName = ctx.tableIdentifier().getText();
//
//			System.out.println("*****" + tableName);
//			
//			String[] tableIdentiiferTuples=tableName.split(".");
//
//			List<ParseTree> columnNames = ctx.columnNames().children;
//			List<ParseTree> columnValues = ctx.columnValues().children;
//
//			for (ParseTree child : columnNames) {
//				System.out.println(child.toString());
//			}
//			addRuleGroup(tableIdentiiferTuples[0], tableIdentiiferTuples[1], tableIdentiiferTuples[2], tableIdentiiferTuples[3], null);
//			
//		} catch (Exception e) {
//			throw new RuntimeException("Error while executing parser for insert statement");
//		}
//	}
//
//	public void addRuleGroup(String spaceName, String clientId, String appName, String version,
//			RuleGroupInfo request) {
//
//		rulesGroupService.addOrUpdateRuleGroup(spaceName, clientId, appName, version, request);
//	}
//	
//	private RuleGroupInfo prepareRuleGroupRequest()
//	

}
