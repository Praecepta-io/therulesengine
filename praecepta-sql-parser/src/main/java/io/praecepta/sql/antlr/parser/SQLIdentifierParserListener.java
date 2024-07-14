// Generated from SQLIdentifierParser.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLIdentifierParser}.
 */
public interface SQLIdentifierParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SQLIdentifierParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SQLIdentifierParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(SQLIdentifierParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(SQLIdentifierParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(SQLIdentifierParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(SQLIdentifierParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SQLIdentifierParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SQLIdentifierParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(SQLIdentifierParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(SQLIdentifierParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLIdentifierParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(SQLIdentifierParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLIdentifierParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(SQLIdentifierParser.KeywordsCanBeIdContext ctx);
}