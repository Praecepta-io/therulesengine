// Generated from SimpleSQL.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpleSQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpleSQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#sql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql(SimpleSQLParser.SqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#statementQuery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementQuery(SimpleSQLParser.StatementQueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#insert_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_statement(SimpleSQLParser.Insert_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#update_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_statement(SimpleSQLParser.Update_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#execute_statment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_statment(SimpleSQLParser.Execute_statmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#delete_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_statement(SimpleSQLParser.Delete_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#tableIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIdentifier(SimpleSQLParser.TableIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#inputExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputExpr(SimpleSQLParser.InputExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(SimpleSQLParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#columnNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnNames(SimpleSQLParser.ColumnNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#columnValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnValues(SimpleSQLParser.ColumnValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#havingKeyValExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingKeyValExpr(SimpleSQLParser.HavingKeyValExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#havingConditionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingConditionList(SimpleSQLParser.HavingConditionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#havingCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingCondition(SimpleSQLParser.HavingConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#executeExprIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteExprIdentifier(SimpleSQLParser.ExecuteExprIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#executeExpressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteExpressionList(SimpleSQLParser.ExecuteExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#executeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteExpression(SimpleSQLParser.ExecuteExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(SimpleSQLParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#jsonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonExpression(SimpleSQLParser.JsonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#xmlExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlExpression(SimpleSQLParser.XmlExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#xmlContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXmlContent(SimpleSQLParser.XmlContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#jsonObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonObject(SimpleSQLParser.JsonObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(SimpleSQLParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#jsonArr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonArr(SimpleSQLParser.JsonArrContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(SimpleSQLParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#select_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_statement(SimpleSQLParser.Select_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SimpleSQLParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SimpleSQLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#conditionExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionExpression(SimpleSQLParser.ConditionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleSQLParser#compare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare(SimpleSQLParser.CompareContext ctx);
}