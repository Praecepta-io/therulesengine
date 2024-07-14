// Generated from SQLIdentifierParser.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLIdentifierParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLIdentifierParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SQLIdentifierParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#columnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnName(SQLIdentifierParser.ColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(SQLIdentifierParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(SQLIdentifierParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(SQLIdentifierParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLIdentifierParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordsCanBeId(SQLIdentifierParser.KeywordsCanBeIdContext ctx);
}