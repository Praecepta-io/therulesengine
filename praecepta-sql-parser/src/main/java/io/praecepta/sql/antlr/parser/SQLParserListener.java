// Generated from SQLParser.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLParser}.
 */
public interface SQLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(SQLParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(SQLParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(SQLParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(SQLParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmlStatement(SQLParser.DmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmlStatement(SQLParser.DmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelect(SQLParser.SimpleSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelect(SQLParser.SimpleSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#adminStatement}.
	 * @param ctx the parse tree
	 */
	void enterAdminStatement(SQLParser.AdminStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#adminStatement}.
	 * @param ctx the parse tree
	 */
	void exitAdminStatement(SQLParser.AdminStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatement(SQLParser.ShowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#showStatement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatement(SQLParser.ShowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#describeStatement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeStatement(SQLParser.DescribeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#describeStatement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeStatement(SQLParser.DescribeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#columnFilter}.
	 * @param ctx the parse tree
	 */
	void enterColumnFilter(SQLParser.ColumnFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#columnFilter}.
	 * @param ctx the parse tree
	 */
	void exitColumnFilter(SQLParser.ColumnFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#tableFilter}.
	 * @param ctx the parse tree
	 */
	void enterTableFilter(SQLParser.TableFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#tableFilter}.
	 * @param ctx the parse tree
	 */
	void exitTableFilter(SQLParser.TableFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#showDescribePattern}.
	 * @param ctx the parse tree
	 */
	void enterShowDescribePattern(SQLParser.ShowDescribePatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#showDescribePattern}.
	 * @param ctx the parse tree
	 */
	void exitShowDescribePattern(SQLParser.ShowDescribePatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#compatibleID}.
	 * @param ctx the parse tree
	 */
	void enterCompatibleID(SQLParser.CompatibleIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#compatibleID}.
	 * @param ctx the parse tree
	 */
	void exitCompatibleID(SQLParser.CompatibleIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(SQLParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(SQLParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void enterSelectClause(SQLParser.SelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectClause}.
	 * @param ctx the parse tree
	 */
	void exitSelectClause(SQLParser.SelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpec(SQLParser.SelectSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectSpec}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpec(SQLParser.SelectSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void enterSelectElements(SQLParser.SelectElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectElements}.
	 * @param ctx the parse tree
	 */
	void exitSelectElements(SQLParser.SelectElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void enterSelectElement(SQLParser.SelectElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectElement}.
	 * @param ctx the parse tree
	 */
	void exitSelectElement(SQLParser.SelectElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(SQLParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(SQLParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterTableAsRelation(SQLParser.TableAsRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitTableAsRelation(SQLParser.TableAsRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryAsRelation(SQLParser.SubqueryAsRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryAsRelation(SQLParser.SubqueryAsRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#groupByElements}.
	 * @param ctx the parse tree
	 */
	void enterGroupByElements(SQLParser.GroupByElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#groupByElements}.
	 * @param ctx the parse tree
	 */
	void exitGroupByElements(SQLParser.GroupByElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#groupByElement}.
	 * @param ctx the parse tree
	 */
	void enterGroupByElement(SQLParser.GroupByElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#groupByElement}.
	 * @param ctx the parse tree
	 */
	void exitGroupByElement(SQLParser.GroupByElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#orderByElement}.
	 * @param ctx the parse tree
	 */
	void enterOrderByElement(SQLParser.OrderByElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#orderByElement}.
	 * @param ctx the parse tree
	 */
	void exitOrderByElement(SQLParser.OrderByElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#windowFunctionClause}.
	 * @param ctx the parse tree
	 */
	void enterWindowFunctionClause(SQLParser.WindowFunctionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#windowFunctionClause}.
	 * @param ctx the parse tree
	 */
	void exitWindowFunctionClause(SQLParser.WindowFunctionClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void enterScalarWindowFunction(SQLParser.ScalarWindowFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void exitScalarWindowFunction(SQLParser.ScalarWindowFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregateWindowFunction(SQLParser.AggregateWindowFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregateWindowFunction(SQLParser.AggregateWindowFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(SQLParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(SQLParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 */
	void enterPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 */
	void exitPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterString(SQLParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitString(SQLParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedDecimal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterSignedDecimal(SQLParser.SignedDecimalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedDecimal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitSignedDecimal(SQLParser.SignedDecimalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedReal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterSignedReal(SQLParser.SignedRealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedReal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitSignedReal(SQLParser.SignedRealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(SQLParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(SQLParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code datetime}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterDatetime(SQLParser.DatetimeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code datetime}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitDatetime(SQLParser.DatetimeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code interval}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterInterval(SQLParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code interval}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitInterval(SQLParser.IntervalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code null}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNull(SQLParser.NullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code null}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNull(SQLParser.NullContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(SQLParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(SQLParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#realLiteral}.
	 * @param ctx the parse tree
	 */
	void enterRealLiteral(SQLParser.RealLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#realLiteral}.
	 * @param ctx the parse tree
	 */
	void exitRealLiteral(SQLParser.RealLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterSign(SQLParser.SignContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitSign(SQLParser.SignContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#datetimeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDatetimeLiteral(SQLParser.DatetimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#datetimeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDatetimeLiteral(SQLParser.DatetimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#timeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterTimeLiteral(SQLParser.TimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#timeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitTimeLiteral(SQLParser.TimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#timestampLiteral}.
	 * @param ctx the parse tree
	 */
	void enterTimestampLiteral(SQLParser.TimestampLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#timestampLiteral}.
	 * @param ctx the parse tree
	 */
	void exitTimestampLiteral(SQLParser.TimestampLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#intervalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntervalLiteral(SQLParser.IntervalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#intervalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntervalLiteral(SQLParser.IntervalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void enterIntervalUnit(SQLParser.IntervalUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void exitIntervalUnit(SQLParser.IntervalUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(SQLParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(SQLParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(SQLParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(SQLParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(SQLParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(SQLParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPredicateExpression(SQLParser.PredicateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPredicateExpression(SQLParser.PredicateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAtomPredicate(SQLParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAtomPredicate(SQLParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryComparisonPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBinaryComparisonPredicate(SQLParser.BinaryComparisonPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryComparisonPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBinaryComparisonPredicate(SQLParser.BinaryComparisonPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterRegexpPredicate(SQLParser.RegexpPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitRegexpPredicate(SQLParser.RegexpPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(SQLParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(SQLParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(SQLParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(SQLParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(SQLParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(SQLParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterNestedExpressionAtom(SQLParser.NestedExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitNestedExpressionAtom(SQLParser.NestedExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void enterMathExpressionAtom(SQLParser.MathExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 */
	void exitMathExpressionAtom(SQLParser.MathExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void enterMathOperator(SQLParser.MathOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#mathOperator}.
	 * @param ctx the parse tree
	 */
	void exitMathOperator(SQLParser.MathOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void enterNullNotnull(SQLParser.NullNotnullContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#nullNotnull}.
	 * @param ctx the parse tree
	 */
	void exitNullNotnull(SQLParser.NullNotnullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionCall(SQLParser.ScalarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionCall(SQLParser.ScalarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterSpecificFunctionCall(SQLParser.SpecificFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitSpecificFunctionCall(SQLParser.SpecificFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code windowFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterWindowFunctionCall(SQLParser.WindowFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code windowFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitWindowFunctionCall(SQLParser.WindowFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterAggregateFunctionCall(SQLParser.AggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitAggregateFunctionCall(SQLParser.AggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code filteredAggregationFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFilteredAggregationFunctionCall(SQLParser.FilteredAggregationFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code filteredAggregationFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFilteredAggregationFunctionCall(SQLParser.FilteredAggregationFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterScalarFunctionName(SQLParser.ScalarFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitScalarFunctionName(SQLParser.ScalarFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterCaseFunctionCall(SQLParser.CaseFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitCaseFunctionCall(SQLParser.CaseFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeFunctionCall(SQLParser.DataTypeFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeFunctionCall(SQLParser.DataTypeFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void enterConvertedDataType(SQLParser.ConvertedDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#convertedDataType}.
	 * @param ctx the parse tree
	 */
	void exitConvertedDataType(SQLParser.ConvertedDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void enterCaseFuncAlternative(SQLParser.CaseFuncAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 */
	void exitCaseFuncAlternative(SQLParser.CaseFuncAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code regularAggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterRegularAggregateFunctionCall(SQLParser.RegularAggregateFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code regularAggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitRegularAggregateFunctionCall(SQLParser.RegularAggregateFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code countStarFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void enterCountStarFunctionCall(SQLParser.CountStarFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code countStarFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 */
	void exitCountStarFunctionCall(SQLParser.CountStarFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void enterFilterClause(SQLParser.FilterClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void exitFilterClause(SQLParser.FilterClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterAggregationFunctionName(SQLParser.AggregationFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitAggregationFunctionName(SQLParser.AggregationFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#mathematicalFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterMathematicalFunctionName(SQLParser.MathematicalFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#mathematicalFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitMathematicalFunctionName(SQLParser.MathematicalFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#trigonometricFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterTrigonometricFunctionName(SQLParser.TrigonometricFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#trigonometricFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitTrigonometricFunctionName(SQLParser.TrigonometricFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dateTimeFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeFunctionName(SQLParser.DateTimeFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dateTimeFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeFunctionName(SQLParser.DateTimeFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#textFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterTextFunctionName(SQLParser.TextFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#textFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitTextFunctionName(SQLParser.TextFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#flowControlFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterFlowControlFunctionName(SQLParser.FlowControlFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#flowControlFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitFlowControlFunctionName(SQLParser.FlowControlFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgs(SQLParser.FunctionArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#functionArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgs(SQLParser.FunctionArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(SQLParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(SQLParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SQLParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SQLParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(SQLParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(SQLParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SQLParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SQLParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(SQLParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(SQLParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(SQLParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(SQLParser.KeywordsCanBeIdContext ctx);
}