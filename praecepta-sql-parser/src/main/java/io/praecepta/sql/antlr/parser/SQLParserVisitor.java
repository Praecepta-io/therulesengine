// Generated from SQLParser.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SQLParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(SQLParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlStatement(SQLParser.SqlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDmlStatement(SQLParser.DmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleSelect}
	 * labeled alternative in {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSelect(SQLParser.SimpleSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#adminStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdminStatement(SQLParser.AdminStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#showStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStatement(SQLParser.ShowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#describeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribeStatement(SQLParser.DescribeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#columnFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnFilter(SQLParser.ColumnFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#tableFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableFilter(SQLParser.TableFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#showDescribePattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDescribePattern(SQLParser.ShowDescribePatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#compatibleID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompatibleID(SQLParser.CompatibleIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#querySpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuerySpecification(SQLParser.QuerySpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectClause(SQLParser.SelectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectSpec(SQLParser.SelectSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectElements(SQLParser.SelectElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectElement(SQLParser.SelectElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(SQLParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tableAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableAsRelation(SQLParser.TableAsRelationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subqueryAsRelation}
	 * labeled alternative in {@link SQLParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryAsRelation(SQLParser.SubqueryAsRelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#groupByElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByElements(SQLParser.GroupByElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#groupByElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByElement(SQLParser.GroupByElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#orderByElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByElement(SQLParser.OrderByElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#windowFunctionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowFunctionClause(SQLParser.WindowFunctionClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarWindowFunction(SQLParser.ScalarWindowFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggregateWindowFunction}
	 * labeled alternative in {@link SQLParser#windowFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateWindowFunction(SQLParser.AggregateWindowFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#overClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverClause(SQLParser.OverClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(SQLParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedDecimal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedDecimal(SQLParser.SignedDecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedReal}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedReal(SQLParser.SignedRealContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(SQLParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code datetime}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetime(SQLParser.DatetimeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interval}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval(SQLParser.IntervalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code null}
	 * labeled alternative in {@link SQLParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(SQLParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(SQLParser.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#realLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(SQLParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSign(SQLParser.SignContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#datetimeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetimeLiteral(SQLParser.DatetimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#timeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeLiteral(SQLParser.TimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#timestampLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestampLiteral(SQLParser.TimestampLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#intervalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalLiteral(SQLParser.IntervalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#intervalUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalUnit(SQLParser.IntervalUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(SQLParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(SQLParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(SQLParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateExpression}
	 * labeled alternative in {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateExpression(SQLParser.PredicateExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionAtomPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionAtomPredicate(SQLParser.ExpressionAtomPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryComparisonPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryComparisonPredicate(SQLParser.BinaryComparisonPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isNullPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code likePredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regexpPredicate}
	 * labeled alternative in {@link SQLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexpPredicate(SQLParser.RegexpPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpressionAtom(SQLParser.ConstantExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpressionAtom(SQLParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnNameExpressionAtom(SQLParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedExpressionAtom(SQLParser.NestedExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathExpressionAtom}
	 * labeled alternative in {@link SQLParser#expressionAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExpressionAtom(SQLParser.MathExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#mathOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathOperator(SQLParser.MathOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#nullNotnull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullNotnull(SQLParser.NullNotnullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFunctionCall(SQLParser.ScalarFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code specificFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecificFunctionCall(SQLParser.SpecificFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code windowFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowFunctionCall(SQLParser.WindowFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateFunctionCall(SQLParser.AggregateFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code filteredAggregationFunctionCall}
	 * labeled alternative in {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilteredAggregationFunctionCall(SQLParser.FilteredAggregationFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#scalarFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarFunctionName(SQLParser.ScalarFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code caseFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseFunctionCall(SQLParser.CaseFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dataTypeFunctionCall}
	 * labeled alternative in {@link SQLParser#specificFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeFunctionCall(SQLParser.DataTypeFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#convertedDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvertedDataType(SQLParser.ConvertedDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#caseFuncAlternative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseFuncAlternative(SQLParser.CaseFuncAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regularAggregateFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularAggregateFunctionCall(SQLParser.RegularAggregateFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code countStarFunctionCall}
	 * labeled alternative in {@link SQLParser#aggregateFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountStarFunctionCall(SQLParser.CountStarFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#filterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterClause(SQLParser.FilterClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregationFunctionName(SQLParser.AggregationFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#mathematicalFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathematicalFunctionName(SQLParser.MathematicalFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#trigonometricFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigonometricFunctionName(SQLParser.TrigonometricFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dateTimeFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimeFunctionName(SQLParser.DateTimeFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#textFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextFunctionName(SQLParser.TextFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#flowControlFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowControlFunctionName(SQLParser.FlowControlFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#functionArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgs(SQLParser.FunctionArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(SQLParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SQLParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(SQLParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(SQLParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(SQLParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordsCanBeId(SQLParser.KeywordsCanBeIdContext ctx);
}