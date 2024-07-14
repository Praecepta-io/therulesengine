package io.praecepta.sql.antlr.parser;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import io.praecepta.sql.antlr.parser.SimpleSQLParser;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.ConstantContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.IdentifierContext;

public class TestListener extends SimpleSQLBaseListener{
	
	@Override
	public void enterSql(SimpleSQLParser.SqlContext ctx) {

		try {
			super.enterSql(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing given input");
		}

	}

	@Override
	public void exitInsert_statement(SimpleSQLParser.Insert_statementContext ctx) {
		try {

			String tableName = ctx.tableIdentifier().getText();

			System.out.println("*****" + tableName);
			
			String[] tableIdentiiferTuples=tableName.split(".");

			List<IdentifierContext> columnNames = ctx.inputExpr().columnNames().identifier();
			List<ConstantContext> columnValues = ctx.inputExpr().columnValues().constant();
			
			
			System.out.println(columnNames.size());
			System.out.println(columnValues.size());


			for (ParseTree child : columnNames) {
				System.out.println(child.getText());
			}
			
			for (ParseTree child : columnValues) {
				System.out.println(child.getText());
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("Error while executing parser for insert statement");
		}
	}

}
