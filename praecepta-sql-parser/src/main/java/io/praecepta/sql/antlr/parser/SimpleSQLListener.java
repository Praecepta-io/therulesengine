// Generated from SimpleSQL.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SimpleSQLParser}.
 */
public interface SimpleSQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#sql}.
	 * @param ctx the parse tree
	 */
	void enterSql(SimpleSQLParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#sql}.
	 * @param ctx the parse tree
	 */
	void exitSql(SimpleSQLParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#statementQuery}.
	 * @param ctx the parse tree
	 */
	void enterStatementQuery(SimpleSQLParser.StatementQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#statementQuery}.
	 * @param ctx the parse tree
	 */
	void exitStatementQuery(SimpleSQLParser.StatementQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void enterInsert_statement(SimpleSQLParser.Insert_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#insert_statement}.
	 * @param ctx the parse tree
	 */
	void exitInsert_statement(SimpleSQLParser.Insert_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_statement(SimpleSQLParser.Update_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#update_statement}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_statement(SimpleSQLParser.Update_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#execute_statment}.
	 * @param ctx the parse tree
	 */
	void enterExecute_statment(SimpleSQLParser.Execute_statmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#execute_statment}.
	 * @param ctx the parse tree
	 */
	void exitExecute_statment(SimpleSQLParser.Execute_statmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete_statement(SimpleSQLParser.Delete_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#delete_statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete_statement(SimpleSQLParser.Delete_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTableIdentifier(SimpleSQLParser.TableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTableIdentifier(SimpleSQLParser.TableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#inputExpr}.
	 * @param ctx the parse tree
	 */
	void enterInputExpr(SimpleSQLParser.InputExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#inputExpr}.
	 * @param ctx the parse tree
	 */
	void exitInputExpr(SimpleSQLParser.InputExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SimpleSQLParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SimpleSQLParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#columnNames}.
	 * @param ctx the parse tree
	 */
	void enterColumnNames(SimpleSQLParser.ColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#columnNames}.
	 * @param ctx the parse tree
	 */
	void exitColumnNames(SimpleSQLParser.ColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#columnValues}.
	 * @param ctx the parse tree
	 */
	void enterColumnValues(SimpleSQLParser.ColumnValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#columnValues}.
	 * @param ctx the parse tree
	 */
	void exitColumnValues(SimpleSQLParser.ColumnValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#havingKeyValExpr}.
	 * @param ctx the parse tree
	 */
	void enterHavingKeyValExpr(SimpleSQLParser.HavingKeyValExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#havingKeyValExpr}.
	 * @param ctx the parse tree
	 */
	void exitHavingKeyValExpr(SimpleSQLParser.HavingKeyValExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#havingConditionList}.
	 * @param ctx the parse tree
	 */
	void enterHavingConditionList(SimpleSQLParser.HavingConditionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#havingConditionList}.
	 * @param ctx the parse tree
	 */
	void exitHavingConditionList(SimpleSQLParser.HavingConditionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#havingCondition}.
	 * @param ctx the parse tree
	 */
	void enterHavingCondition(SimpleSQLParser.HavingConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#havingCondition}.
	 * @param ctx the parse tree
	 */
	void exitHavingCondition(SimpleSQLParser.HavingConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#executeExprIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExprIdentifier(SimpleSQLParser.ExecuteExprIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#executeExprIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExprIdentifier(SimpleSQLParser.ExecuteExprIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#executeExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExpressionList(SimpleSQLParser.ExecuteExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#executeExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExpressionList(SimpleSQLParser.ExecuteExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#executeExpression}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExpression(SimpleSQLParser.ExecuteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#executeExpression}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExpression(SimpleSQLParser.ExecuteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(SimpleSQLParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(SimpleSQLParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#jsonExpression}.
	 * @param ctx the parse tree
	 */
	void enterJsonExpression(SimpleSQLParser.JsonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#jsonExpression}.
	 * @param ctx the parse tree
	 */
	void exitJsonExpression(SimpleSQLParser.JsonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#xmlExpression}.
	 * @param ctx the parse tree
	 */
	void enterXmlExpression(SimpleSQLParser.XmlExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#xmlExpression}.
	 * @param ctx the parse tree
	 */
	void exitXmlExpression(SimpleSQLParser.XmlExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#xmlContent}.
	 * @param ctx the parse tree
	 */
	void enterXmlContent(SimpleSQLParser.XmlContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#xmlContent}.
	 * @param ctx the parse tree
	 */
	void exitXmlContent(SimpleSQLParser.XmlContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void enterJsonObject(SimpleSQLParser.JsonObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void exitJsonObject(SimpleSQLParser.JsonObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(SimpleSQLParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(SimpleSQLParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#jsonArr}.
	 * @param ctx the parse tree
	 */
	void enterJsonArr(SimpleSQLParser.JsonArrContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#jsonArr}.
	 * @param ctx the parse tree
	 */
	void exitJsonArr(SimpleSQLParser.JsonArrContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(SimpleSQLParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(SimpleSQLParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_statement(SimpleSQLParser.Select_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_statement(SimpleSQLParser.Select_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(SimpleSQLParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(SimpleSQLParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SimpleSQLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SimpleSQLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#conditionExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionExpression(SimpleSQLParser.ConditionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#conditionExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionExpression(SimpleSQLParser.ConditionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleSQLParser#compare}.
	 * @param ctx the parse tree
	 */
	void enterCompare(SimpleSQLParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleSQLParser#compare}.
	 * @param ctx the parse tree
	 */
	void exitCompare(SimpleSQLParser.CompareContext ctx);
}