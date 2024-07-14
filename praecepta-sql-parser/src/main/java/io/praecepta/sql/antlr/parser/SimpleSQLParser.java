// Generated from SimpleSQL.g4 by ANTLR 4.5
package io.praecepta.sql.antlr.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SimpleSQLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, STRING=11, CREATE=12, SELECT=13, FROM=14, INSERT=15, UPDATE=16, 
		EXECUTE=17, INTO=18, VALUES=19, WHERE=20, NULL=21, WITH=22, HAVING=23, 
		DELETE=24, DOT=25, COMMA=26, ASTERISK=27, LEFT_PARENTHESIS=28, RIGHT_PARENTHESIS=29, 
		EQUALS=30, NOT=31, MINUS=32, PLUS=33, GT=34, GE=35, LT=36, LE=37, NE=38, 
		AND=39, OR=40, QUOTED_STRING=41, INTEGER_VALUE=42, DECIMAL_VALUE=43, IDENTIFIER=44, 
		XMLATTR=45, NUMBER=46, WS=47;
	public static final int
		RULE_sql = 0, RULE_statementQuery = 1, RULE_insert_statement = 2, RULE_update_statement = 3, 
		RULE_execute_statment = 4, RULE_delete_statement = 5, RULE_tableIdentifier = 6, 
		RULE_inputExpr = 7, RULE_identifier = 8, RULE_columnNames = 9, RULE_columnValues = 10, 
		RULE_havingKeyValExpr = 11, RULE_havingConditionList = 12, RULE_havingCondition = 13, 
		RULE_executeExprIdentifier = 14, RULE_executeExpressionList = 15, RULE_executeExpression = 16, 
		RULE_constant = 17, RULE_jsonExpression = 18, RULE_xmlExpression = 19, 
		RULE_xmlContent = 20, RULE_jsonObject = 21, RULE_pair = 22, RULE_jsonArr = 23, 
		RULE_value = 24, RULE_select_statement = 25, RULE_expressionList = 26, 
		RULE_expression = 27, RULE_conditionExpression = 28, RULE_compare = 29;
	public static final String[] ruleNames = {
		"sql", "statementQuery", "insert_statement", "update_statement", "execute_statment", 
		"delete_statement", "tableIdentifier", "inputExpr", "identifier", "columnNames", 
		"columnValues", "havingKeyValExpr", "havingConditionList", "havingCondition", 
		"executeExprIdentifier", "executeExpressionList", "executeExpression", 
		"constant", "jsonExpression", "xmlExpression", "xmlContent", "jsonObject", 
		"pair", "jsonArr", "value", "select_statement", "expressionList", "expression", 
		"conditionExpression", "compare"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'/'", "'true'", "'false'", "'null'", "'{'", "'}'", "':'", 
		"'['", "']'", null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "'.'", "','", "'*'", "'('", "')'", "'='", "'!'", 
		"'-'", "'+'", "'>'", "'>='", "'<'", "'<='", "'!='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, "STRING", 
		"CREATE", "SELECT", "FROM", "INSERT", "UPDATE", "EXECUTE", "INTO", "VALUES", 
		"WHERE", "NULL", "WITH", "HAVING", "DELETE", "DOT", "COMMA", "ASTERISK", 
		"LEFT_PARENTHESIS", "RIGHT_PARENTHESIS", "EQUALS", "NOT", "MINUS", "PLUS", 
		"GT", "GE", "LT", "LE", "NE", "AND", "OR", "QUOTED_STRING", "INTEGER_VALUE", 
		"DECIMAL_VALUE", "IDENTIFIER", "XMLATTR", "NUMBER", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SimpleSQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SimpleSQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SqlContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SimpleSQLParser.EOF, 0); }
		public List<StatementQueryContext> statementQuery() {
			return getRuleContexts(StatementQueryContext.class);
		}
		public StatementQueryContext statementQuery(int i) {
			return getRuleContext(StatementQueryContext.class,i);
		}
		public SqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterSql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitSql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitSql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlContext sql() throws RecognitionException {
		SqlContext _localctx = new SqlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_sql);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(62);
				switch (_input.LA(1)) {
				case SELECT:
				case INSERT:
				case UPDATE:
				case EXECUTE:
				case DELETE:
					{
					setState(60);
					statementQuery();
					}
					break;
				case T__0:
					{
					setState(61);
					match(T__0);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(64); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << SELECT) | (1L << INSERT) | (1L << UPDATE) | (1L << EXECUTE) | (1L << DELETE))) != 0) );
			setState(66);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementQueryContext extends ParserRuleContext {
		public Insert_statementContext insert_statement() {
			return getRuleContext(Insert_statementContext.class,0);
		}
		public Update_statementContext update_statement() {
			return getRuleContext(Update_statementContext.class,0);
		}
		public Select_statementContext select_statement() {
			return getRuleContext(Select_statementContext.class,0);
		}
		public Execute_statmentContext execute_statment() {
			return getRuleContext(Execute_statmentContext.class,0);
		}
		public Delete_statementContext delete_statement() {
			return getRuleContext(Delete_statementContext.class,0);
		}
		public StatementQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterStatementQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitStatementQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitStatementQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementQueryContext statementQuery() throws RecognitionException {
		StatementQueryContext _localctx = new StatementQueryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statementQuery);
		try {
			setState(73);
			switch (_input.LA(1)) {
			case INSERT:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				insert_statement();
				}
				break;
			case UPDATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				update_statement();
				}
				break;
			case SELECT:
				enterOuterAlt(_localctx, 3);
				{
				setState(70);
				select_statement();
				}
				break;
			case EXECUTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(71);
				execute_statment();
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 5);
				{
				setState(72);
				delete_statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Insert_statementContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(SimpleSQLParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(SimpleSQLParser.INTO, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public InputExprContext inputExpr() {
			return getRuleContext(InputExprContext.class,0);
		}
		public Insert_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterInsert_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitInsert_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitInsert_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Insert_statementContext insert_statement() throws RecognitionException {
		Insert_statementContext _localctx = new Insert_statementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_insert_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(INSERT);
			setState(76);
			match(INTO);
			setState(77);
			tableIdentifier();
			setState(78);
			inputExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Update_statementContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(SimpleSQLParser.UPDATE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public InputExprContext inputExpr() {
			return getRuleContext(InputExprContext.class,0);
		}
		public Update_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterUpdate_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitUpdate_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitUpdate_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Update_statementContext update_statement() throws RecognitionException {
		Update_statementContext _localctx = new Update_statementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_update_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(UPDATE);
			setState(81);
			tableIdentifier();
			setState(82);
			inputExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Execute_statmentContext extends ParserRuleContext {
		public TerminalNode EXECUTE() { return getToken(SimpleSQLParser.EXECUTE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public HavingKeyValExprContext havingKeyValExpr() {
			return getRuleContext(HavingKeyValExprContext.class,0);
		}
		public ExecuteExprIdentifierContext executeExprIdentifier() {
			return getRuleContext(ExecuteExprIdentifierContext.class,0);
		}
		public Execute_statmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_execute_statment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExecute_statment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExecute_statment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExecute_statment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Execute_statmentContext execute_statment() throws RecognitionException {
		Execute_statmentContext _localctx = new Execute_statmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_execute_statment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(EXECUTE);
			setState(85);
			tableIdentifier();
			setState(86);
			havingKeyValExpr();
			setState(87);
			executeExprIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Delete_statementContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(SimpleSQLParser.DELETE, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public HavingKeyValExprContext havingKeyValExpr() {
			return getRuleContext(HavingKeyValExprContext.class,0);
		}
		public Delete_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterDelete_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitDelete_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitDelete_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Delete_statementContext delete_statement() throws RecognitionException {
		Delete_statementContext _localctx = new Delete_statementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_delete_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(DELETE);
			setState(90);
			tableIdentifier();
			setState(91);
			havingKeyValExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TableIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterTableIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitTableIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitTableIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableIdentifierContext tableIdentifier() throws RecognitionException {
		TableIdentifierContext _localctx = new TableIdentifierContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_tableIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputExprContext extends ParserRuleContext {
		public ColumnNamesContext columnNames() {
			return getRuleContext(ColumnNamesContext.class,0);
		}
		public TerminalNode VALUES() { return getToken(SimpleSQLParser.VALUES, 0); }
		public ColumnValuesContext columnValues() {
			return getRuleContext(ColumnValuesContext.class,0);
		}
		public TerminalNode WITH() { return getToken(SimpleSQLParser.WITH, 0); }
		public TerminalNode LEFT_PARENTHESIS() { return getToken(SimpleSQLParser.LEFT_PARENTHESIS, 0); }
		public JsonExpressionContext jsonExpression() {
			return getRuleContext(JsonExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PARENTHESIS() { return getToken(SimpleSQLParser.RIGHT_PARENTHESIS, 0); }
		public InputExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterInputExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitInputExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitInputExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputExprContext inputExpr() throws RecognitionException {
		InputExprContext _localctx = new InputExprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_inputExpr);
		try {
			setState(104);
			switch (_input.LA(1)) {
			case LEFT_PARENTHESIS:
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				columnNames();
				setState(96);
				match(VALUES);
				setState(97);
				columnValues();
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
				match(WITH);
				setState(100);
				match(LEFT_PARENTHESIS);
				setState(101);
				jsonExpression();
				setState(102);
				match(RIGHT_PARENTHESIS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(SimpleSQLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SimpleSQLParser.IDENTIFIER, i);
		}
		public List<TerminalNode> DOT() { return getTokens(SimpleSQLParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SimpleSQLParser.DOT, i);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_identifier);
		try {
			setState(115);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				match(IDENTIFIER);
				setState(107);
				match(DOT);
				setState(108);
				match(IDENTIFIER);
				setState(109);
				match(DOT);
				setState(110);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(111);
				match(IDENTIFIER);
				setState(112);
				match(DOT);
				setState(113);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnNamesContext extends ParserRuleContext {
		public TerminalNode LEFT_PARENTHESIS() { return getToken(SimpleSQLParser.LEFT_PARENTHESIS, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode RIGHT_PARENTHESIS() { return getToken(SimpleSQLParser.RIGHT_PARENTHESIS, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SimpleSQLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SimpleSQLParser.COMMA, i);
		}
		public ColumnNamesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnNames; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterColumnNames(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitColumnNames(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitColumnNames(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnNamesContext columnNames() throws RecognitionException {
		ColumnNamesContext _localctx = new ColumnNamesContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_columnNames);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(LEFT_PARENTHESIS);
			setState(118);
			identifier();
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(119);
				match(COMMA);
				setState(120);
				identifier();
				}
				}
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(126);
			match(RIGHT_PARENTHESIS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnValuesContext extends ParserRuleContext {
		public TerminalNode LEFT_PARENTHESIS() { return getToken(SimpleSQLParser.LEFT_PARENTHESIS, 0); }
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public TerminalNode RIGHT_PARENTHESIS() { return getToken(SimpleSQLParser.RIGHT_PARENTHESIS, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SimpleSQLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SimpleSQLParser.COMMA, i);
		}
		public ColumnValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterColumnValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitColumnValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitColumnValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnValuesContext columnValues() throws RecognitionException {
		ColumnValuesContext _localctx = new ColumnValuesContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_columnValues);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(LEFT_PARENTHESIS);
			setState(129);
			constant();
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(130);
				match(COMMA);
				setState(131);
				constant();
				}
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(137);
			match(RIGHT_PARENTHESIS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HavingKeyValExprContext extends ParserRuleContext {
		public TerminalNode HAVING() { return getToken(SimpleSQLParser.HAVING, 0); }
		public HavingConditionListContext havingConditionList() {
			return getRuleContext(HavingConditionListContext.class,0);
		}
		public HavingKeyValExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingKeyValExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterHavingKeyValExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitHavingKeyValExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitHavingKeyValExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingKeyValExprContext havingKeyValExpr() throws RecognitionException {
		HavingKeyValExprContext _localctx = new HavingKeyValExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_havingKeyValExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(HAVING);
			setState(140);
			havingConditionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HavingConditionListContext extends ParserRuleContext {
		public List<HavingConditionContext> havingCondition() {
			return getRuleContexts(HavingConditionContext.class);
		}
		public HavingConditionContext havingCondition(int i) {
			return getRuleContext(HavingConditionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SimpleSQLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SimpleSQLParser.COMMA, i);
		}
		public HavingConditionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingConditionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterHavingConditionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitHavingConditionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitHavingConditionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingConditionListContext havingConditionList() throws RecognitionException {
		HavingConditionListContext _localctx = new HavingConditionListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_havingConditionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			havingCondition();
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(143);
				match(COMMA);
				setState(144);
				havingCondition();
				}
				}
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HavingConditionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CompareContext compare() {
			return getRuleContext(CompareContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public HavingConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterHavingCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitHavingCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitHavingCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingConditionContext havingCondition() throws RecognitionException {
		HavingConditionContext _localctx = new HavingConditionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_havingCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			identifier();
			setState(151);
			compare();
			setState(152);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExecuteExprIdentifierContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(SimpleSQLParser.WHERE, 0); }
		public ExecuteExpressionListContext executeExpressionList() {
			return getRuleContext(ExecuteExpressionListContext.class,0);
		}
		public TerminalNode WITH() { return getToken(SimpleSQLParser.WITH, 0); }
		public TerminalNode LEFT_PARENTHESIS() { return getToken(SimpleSQLParser.LEFT_PARENTHESIS, 0); }
		public JsonExpressionContext jsonExpression() {
			return getRuleContext(JsonExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PARENTHESIS() { return getToken(SimpleSQLParser.RIGHT_PARENTHESIS, 0); }
		public ExecuteExprIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_executeExprIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExecuteExprIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExecuteExprIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExecuteExprIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExecuteExprIdentifierContext executeExprIdentifier() throws RecognitionException {
		ExecuteExprIdentifierContext _localctx = new ExecuteExprIdentifierContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_executeExprIdentifier);
		try {
			setState(161);
			switch (_input.LA(1)) {
			case WHERE:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				match(WHERE);
				setState(155);
				executeExpressionList();
				}
				break;
			case WITH:
				enterOuterAlt(_localctx, 2);
				{
				setState(156);
				match(WITH);
				setState(157);
				match(LEFT_PARENTHESIS);
				setState(158);
				jsonExpression();
				setState(159);
				match(RIGHT_PARENTHESIS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExecuteExpressionListContext extends ParserRuleContext {
		public List<ExecuteExpressionContext> executeExpression() {
			return getRuleContexts(ExecuteExpressionContext.class);
		}
		public ExecuteExpressionContext executeExpression(int i) {
			return getRuleContext(ExecuteExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SimpleSQLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SimpleSQLParser.COMMA, i);
		}
		public ExecuteExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_executeExpressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExecuteExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExecuteExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExecuteExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExecuteExpressionListContext executeExpressionList() throws RecognitionException {
		ExecuteExpressionListContext _localctx = new ExecuteExpressionListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_executeExpressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			executeExpression();
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(164);
				match(COMMA);
				setState(165);
				executeExpression();
				}
				}
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExecuteExpressionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CompareContext compare() {
			return getRuleContext(CompareContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExecuteExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_executeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExecuteExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExecuteExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExecuteExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExecuteExpressionContext executeExpression() throws RecognitionException {
		ExecuteExpressionContext _localctx = new ExecuteExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_executeExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			identifier();
			setState(172);
			compare();
			setState(173);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(SimpleSQLParser.NULL, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode INTEGER_VALUE() { return getToken(SimpleSQLParser.INTEGER_VALUE, 0); }
		public TerminalNode MINUS() { return getToken(SimpleSQLParser.MINUS, 0); }
		public TerminalNode PLUS() { return getToken(SimpleSQLParser.PLUS, 0); }
		public TerminalNode DECIMAL_VALUE() { return getToken(SimpleSQLParser.DECIMAL_VALUE, 0); }
		public List<TerminalNode> QUOTED_STRING() { return getTokens(SimpleSQLParser.QUOTED_STRING); }
		public TerminalNode QUOTED_STRING(int i) {
			return getToken(SimpleSQLParser.QUOTED_STRING, i);
		}
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constant);
		int _la;
		try {
			int _alt;
			setState(190);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				match(NULL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				identifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(178);
				_la = _input.LA(1);
				if (_la==MINUS || _la==PLUS) {
					{
					setState(177);
					_la = _input.LA(1);
					if ( !(_la==MINUS || _la==PLUS) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(180);
				match(INTEGER_VALUE);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(182);
				_la = _input.LA(1);
				if (_la==MINUS || _la==PLUS) {
					{
					setState(181);
					_la = _input.LA(1);
					if ( !(_la==MINUS || _la==PLUS) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(184);
				match(DECIMAL_VALUE);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(186); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(185);
						match(QUOTED_STRING);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(188); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsonExpressionContext extends ParserRuleContext {
		public JsonObjectContext jsonObject() {
			return getRuleContext(JsonObjectContext.class,0);
		}
		public JsonArrContext jsonArr() {
			return getRuleContext(JsonArrContext.class,0);
		}
		public XmlExpressionContext xmlExpression() {
			return getRuleContext(XmlExpressionContext.class,0);
		}
		public JsonExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterJsonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitJsonExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitJsonExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonExpressionContext jsonExpression() throws RecognitionException {
		JsonExpressionContext _localctx = new JsonExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_jsonExpression);
		try {
			setState(195);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(192);
				jsonObject();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(193);
				jsonArr();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 3);
				{
				setState(194);
				xmlExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XmlExpressionContext extends ParserRuleContext {
		public List<TerminalNode> XMLATTR() { return getTokens(SimpleSQLParser.XMLATTR); }
		public TerminalNode XMLATTR(int i) {
			return getToken(SimpleSQLParser.XMLATTR, i);
		}
		public List<XmlContentContext> xmlContent() {
			return getRuleContexts(XmlContentContext.class);
		}
		public XmlContentContext xmlContent(int i) {
			return getRuleContext(XmlContentContext.class,i);
		}
		public XmlExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterXmlExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitXmlExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitXmlExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlExpressionContext xmlExpression() throws RecognitionException {
		XmlExpressionContext _localctx = new XmlExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_xmlExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(LT);
			setState(198);
			match(XMLATTR);
			setState(199);
			match(GT);
			setState(203);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(200);
					xmlContent();
					}
					} 
				}
				setState(205);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(206);
			match(LT);
			setState(207);
			match(T__1);
			setState(208);
			match(XMLATTR);
			setState(209);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XmlContentContext extends ParserRuleContext {
		public TerminalNode XMLATTR() { return getToken(SimpleSQLParser.XMLATTR, 0); }
		public TerminalNode NUMBER() { return getToken(SimpleSQLParser.NUMBER, 0); }
		public XmlExpressionContext xmlExpression() {
			return getRuleContext(XmlExpressionContext.class,0);
		}
		public XmlContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xmlContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterXmlContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitXmlContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitXmlContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XmlContentContext xmlContent() throws RecognitionException {
		XmlContentContext _localctx = new XmlContentContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_xmlContent);
		try {
			setState(217);
			switch (_input.LA(1)) {
			case XMLATTR:
				enterOuterAlt(_localctx, 1);
				{
				setState(211);
				match(XMLATTR);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(212);
				match(NUMBER);
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 3);
				{
				setState(213);
				xmlExpression();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 4);
				{
				setState(214);
				match(T__2);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 5);
				{
				setState(215);
				match(T__3);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 6);
				{
				setState(216);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsonObjectContext extends ParserRuleContext {
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public JsonObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonObject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterJsonObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitJsonObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitJsonObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonObjectContext jsonObject() throws RecognitionException {
		JsonObjectContext _localctx = new JsonObjectContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_jsonObject);
		int _la;
		try {
			setState(232);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(219);
				match(T__5);
				setState(220);
				pair();
				setState(225);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(221);
					match(COMMA);
					setState(222);
					pair();
					}
					}
					setState(227);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(228);
				match(T__6);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				match(T__5);
				setState(231);
				match(T__6);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SimpleSQLParser.STRING, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(STRING);
			setState(235);
			match(T__7);
			setState(236);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsonArrContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public JsonArrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonArr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterJsonArr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitJsonArr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitJsonArr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonArrContext jsonArr() throws RecognitionException {
		JsonArrContext _localctx = new JsonArrContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_jsonArr);
		int _la;
		try {
			setState(251);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				match(T__8);
				setState(239);
				value();
				setState(244);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(240);
					match(COMMA);
					setState(241);
					value();
					}
					}
					setState(246);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(247);
				match(T__9);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(249);
				match(T__8);
				setState(250);
				match(T__9);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SimpleSQLParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(SimpleSQLParser.NUMBER, 0); }
		public JsonObjectContext jsonObject() {
			return getRuleContext(JsonObjectContext.class,0);
		}
		public JsonArrContext jsonArr() {
			return getRuleContext(JsonArrContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_value);
		try {
			setState(260);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(253);
				match(STRING);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(254);
				match(NUMBER);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 3);
				{
				setState(255);
				jsonObject();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(256);
				jsonArr();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 5);
				{
				setState(257);
				match(T__2);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 6);
				{
				setState(258);
				match(T__3);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 7);
				{
				setState(259);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_statementContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(SimpleSQLParser.SELECT, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode FROM() { return getToken(SimpleSQLParser.FROM, 0); }
		public TableIdentifierContext tableIdentifier() {
			return getRuleContext(TableIdentifierContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(SimpleSQLParser.WHERE, 0); }
		public ConditionExpressionContext conditionExpression() {
			return getRuleContext(ConditionExpressionContext.class,0);
		}
		public Select_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterSelect_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitSelect_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitSelect_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_statementContext select_statement() throws RecognitionException {
		Select_statementContext _localctx = new Select_statementContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_select_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(SELECT);
			setState(263);
			expressionList();
			setState(264);
			match(FROM);
			setState(265);
			tableIdentifier();
			setState(268);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(266);
				match(WHERE);
				setState(267);
				conditionExpression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SimpleSQLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SimpleSQLParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			expression();
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(271);
				match(COMMA);
				setState(272);
				expression();
				}
				}
				setState(277);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(SimpleSQLParser.ASTERISK, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_expression);
		try {
			setState(281);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(278);
				match(ASTERISK);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(279);
				identifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(280);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionExpressionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CompareContext compare() {
			return getRuleContext(CompareContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ConditionExpressionContext> conditionExpression() {
			return getRuleContexts(ConditionExpressionContext.class);
		}
		public ConditionExpressionContext conditionExpression(int i) {
			return getRuleContext(ConditionExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(SimpleSQLParser.AND, 0); }
		public TerminalNode OR() { return getToken(SimpleSQLParser.OR, 0); }
		public ConditionExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterConditionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitConditionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitConditionExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionExpressionContext conditionExpression() throws RecognitionException {
		return conditionExpression(0);
	}

	private ConditionExpressionContext conditionExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionExpressionContext _localctx = new ConditionExpressionContext(_ctx, _parentState);
		ConditionExpressionContext _prevctx = _localctx;
		int _startState = 56;
		enterRecursionRule(_localctx, 56, RULE_conditionExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(284);
			identifier();
			setState(285);
			compare();
			setState(286);
			expression();
			}
			_ctx.stop = _input.LT(-1);
			setState(296);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(294);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						_localctx = new ConditionExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_conditionExpression);
						setState(288);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(289);
						match(AND);
						setState(290);
						conditionExpression(3);
						}
						break;
					case 2:
						{
						_localctx = new ConditionExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_conditionExpression);
						setState(291);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(292);
						match(OR);
						setState(293);
						conditionExpression(2);
						}
						break;
					}
					} 
				}
				setState(298);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CompareContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(SimpleSQLParser.EQUALS, 0); }
		public TerminalNode GT() { return getToken(SimpleSQLParser.GT, 0); }
		public TerminalNode GE() { return getToken(SimpleSQLParser.GE, 0); }
		public TerminalNode LT() { return getToken(SimpleSQLParser.LT, 0); }
		public TerminalNode LE() { return getToken(SimpleSQLParser.LE, 0); }
		public TerminalNode NE() { return getToken(SimpleSQLParser.NE, 0); }
		public CompareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).enterCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleSQLListener ) ((SimpleSQLListener)listener).exitCompare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleSQLVisitor ) return ((SimpleSQLVisitor<? extends T>)visitor).visitCompare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompareContext compare() throws RecognitionException {
		CompareContext _localctx = new CompareContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_compare);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << GT) | (1L << GE) | (1L << LT) | (1L << LE) | (1L << NE))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 28:
			return conditionExpression_sempred((ConditionExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean conditionExpression_sempred(ConditionExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\61\u0130\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2\3\2\6"+
		"\2A\n\2\r\2\16\2B\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3L\n\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tk\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\5\nv\n\n\3\13\3\13\3\13\3\13\7\13|\n\13\f\13\16\13\177\13\13\3"+
		"\13\3\13\3\f\3\f\3\f\3\f\7\f\u0087\n\f\f\f\16\f\u008a\13\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\16\3\16\3\16\7\16\u0094\n\16\f\16\16\16\u0097\13\16\3\17\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00a4\n\20\3\21"+
		"\3\21\3\21\7\21\u00a9\n\21\f\21\16\21\u00ac\13\21\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\5\23\u00b5\n\23\3\23\3\23\5\23\u00b9\n\23\3\23\3\23\6"+
		"\23\u00bd\n\23\r\23\16\23\u00be\5\23\u00c1\n\23\3\24\3\24\3\24\5\24\u00c6"+
		"\n\24\3\25\3\25\3\25\3\25\7\25\u00cc\n\25\f\25\16\25\u00cf\13\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u00dc\n\26\3\27"+
		"\3\27\3\27\3\27\7\27\u00e2\n\27\f\27\16\27\u00e5\13\27\3\27\3\27\3\27"+
		"\3\27\5\27\u00eb\n\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\7\31\u00f5"+
		"\n\31\f\31\16\31\u00f8\13\31\3\31\3\31\3\31\3\31\5\31\u00fe\n\31\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u0107\n\32\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\5\33\u010f\n\33\3\34\3\34\3\34\7\34\u0114\n\34\f\34\16\34\u0117"+
		"\13\34\3\35\3\35\3\35\5\35\u011c\n\35\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\7\36\u0129\n\36\f\36\16\36\u012c\13\36\3\37\3"+
		"\37\3\37\2\3: \2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:<\2\4\3\2\"#\4\2  $(\u013e\2@\3\2\2\2\4K\3\2\2\2\6M\3\2\2\2\bR\3"+
		"\2\2\2\nV\3\2\2\2\f[\3\2\2\2\16_\3\2\2\2\20j\3\2\2\2\22u\3\2\2\2\24w\3"+
		"\2\2\2\26\u0082\3\2\2\2\30\u008d\3\2\2\2\32\u0090\3\2\2\2\34\u0098\3\2"+
		"\2\2\36\u00a3\3\2\2\2 \u00a5\3\2\2\2\"\u00ad\3\2\2\2$\u00c0\3\2\2\2&\u00c5"+
		"\3\2\2\2(\u00c7\3\2\2\2*\u00db\3\2\2\2,\u00ea\3\2\2\2.\u00ec\3\2\2\2\60"+
		"\u00fd\3\2\2\2\62\u0106\3\2\2\2\64\u0108\3\2\2\2\66\u0110\3\2\2\28\u011b"+
		"\3\2\2\2:\u011d\3\2\2\2<\u012d\3\2\2\2>A\5\4\3\2?A\7\3\2\2@>\3\2\2\2@"+
		"?\3\2\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2CD\3\2\2\2DE\7\2\2\3E\3\3\2\2\2"+
		"FL\5\6\4\2GL\5\b\5\2HL\5\64\33\2IL\5\n\6\2JL\5\f\7\2KF\3\2\2\2KG\3\2\2"+
		"\2KH\3\2\2\2KI\3\2\2\2KJ\3\2\2\2L\5\3\2\2\2MN\7\21\2\2NO\7\24\2\2OP\5"+
		"\16\b\2PQ\5\20\t\2Q\7\3\2\2\2RS\7\22\2\2ST\5\16\b\2TU\5\20\t\2U\t\3\2"+
		"\2\2VW\7\23\2\2WX\5\16\b\2XY\5\30\r\2YZ\5\36\20\2Z\13\3\2\2\2[\\\7\32"+
		"\2\2\\]\5\16\b\2]^\5\30\r\2^\r\3\2\2\2_`\5\22\n\2`\17\3\2\2\2ab\5\24\13"+
		"\2bc\7\25\2\2cd\5\26\f\2dk\3\2\2\2ef\7\30\2\2fg\7\36\2\2gh\5&\24\2hi\7"+
		"\37\2\2ik\3\2\2\2ja\3\2\2\2je\3\2\2\2k\21\3\2\2\2lm\7.\2\2mn\7\33\2\2"+
		"no\7.\2\2op\7\33\2\2pv\7.\2\2qr\7.\2\2rs\7\33\2\2sv\7.\2\2tv\7.\2\2ul"+
		"\3\2\2\2uq\3\2\2\2ut\3\2\2\2v\23\3\2\2\2wx\7\36\2\2x}\5\22\n\2yz\7\34"+
		"\2\2z|\5\22\n\2{y\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0080\3\2"+
		"\2\2\177}\3\2\2\2\u0080\u0081\7\37\2\2\u0081\25\3\2\2\2\u0082\u0083\7"+
		"\36\2\2\u0083\u0088\5$\23\2\u0084\u0085\7\34\2\2\u0085\u0087\5$\23\2\u0086"+
		"\u0084\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2"+
		"\2\2\u0089\u008b\3\2\2\2\u008a\u0088\3\2\2\2\u008b\u008c\7\37\2\2\u008c"+
		"\27\3\2\2\2\u008d\u008e\7\31\2\2\u008e\u008f\5\32\16\2\u008f\31\3\2\2"+
		"\2\u0090\u0095\5\34\17\2\u0091\u0092\7\34\2\2\u0092\u0094\5\34\17\2\u0093"+
		"\u0091\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2"+
		"\2\2\u0096\33\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u0099\5\22\n\2\u0099\u009a"+
		"\5<\37\2\u009a\u009b\58\35\2\u009b\35\3\2\2\2\u009c\u009d\7\26\2\2\u009d"+
		"\u00a4\5 \21\2\u009e\u009f\7\30\2\2\u009f\u00a0\7\36\2\2\u00a0\u00a1\5"+
		"&\24\2\u00a1\u00a2\7\37\2\2\u00a2\u00a4\3\2\2\2\u00a3\u009c\3\2\2\2\u00a3"+
		"\u009e\3\2\2\2\u00a4\37\3\2\2\2\u00a5\u00aa\5\"\22\2\u00a6\u00a7\7\34"+
		"\2\2\u00a7\u00a9\5\"\22\2\u00a8\u00a6\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa"+
		"\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab!\3\2\2\2\u00ac\u00aa\3\2\2\2"+
		"\u00ad\u00ae\5\22\n\2\u00ae\u00af\5<\37\2\u00af\u00b0\58\35\2\u00b0#\3"+
		"\2\2\2\u00b1\u00c1\7\27\2\2\u00b2\u00c1\5\22\n\2\u00b3\u00b5\t\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00c1\7,"+
		"\2\2\u00b7\u00b9\t\2\2\2\u00b8\u00b7\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9"+
		"\u00ba\3\2\2\2\u00ba\u00c1\7-\2\2\u00bb\u00bd\7+\2\2\u00bc\u00bb\3\2\2"+
		"\2\u00bd\u00be\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1"+
		"\3\2\2\2\u00c0\u00b1\3\2\2\2\u00c0\u00b2\3\2\2\2\u00c0\u00b4\3\2\2\2\u00c0"+
		"\u00b8\3\2\2\2\u00c0\u00bc\3\2\2\2\u00c1%\3\2\2\2\u00c2\u00c6\5,\27\2"+
		"\u00c3\u00c6\5\60\31\2\u00c4\u00c6\5(\25\2\u00c5\u00c2\3\2\2\2\u00c5\u00c3"+
		"\3\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\'\3\2\2\2\u00c7\u00c8\7&\2\2\u00c8"+
		"\u00c9\7/\2\2\u00c9\u00cd\7$\2\2\u00ca\u00cc\5*\26\2\u00cb\u00ca\3\2\2"+
		"\2\u00cc\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00d0"+
		"\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d1\7&\2\2\u00d1\u00d2\7\4\2\2\u00d2"+
		"\u00d3\7/\2\2\u00d3\u00d4\7$\2\2\u00d4)\3\2\2\2\u00d5\u00dc\7/\2\2\u00d6"+
		"\u00dc\7\60\2\2\u00d7\u00dc\5(\25\2\u00d8\u00dc\7\5\2\2\u00d9\u00dc\7"+
		"\6\2\2\u00da\u00dc\7\7\2\2\u00db\u00d5\3\2\2\2\u00db\u00d6\3\2\2\2\u00db"+
		"\u00d7\3\2\2\2\u00db\u00d8\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00da\3\2"+
		"\2\2\u00dc+\3\2\2\2\u00dd\u00de\7\b\2\2\u00de\u00e3\5.\30\2\u00df\u00e0"+
		"\7\34\2\2\u00e0\u00e2\5.\30\2\u00e1\u00df\3\2\2\2\u00e2\u00e5\3\2\2\2"+
		"\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e6\3\2\2\2\u00e5\u00e3"+
		"\3\2\2\2\u00e6\u00e7\7\t\2\2\u00e7\u00eb\3\2\2\2\u00e8\u00e9\7\b\2\2\u00e9"+
		"\u00eb\7\t\2\2\u00ea\u00dd\3\2\2\2\u00ea\u00e8\3\2\2\2\u00eb-\3\2\2\2"+
		"\u00ec\u00ed\7\r\2\2\u00ed\u00ee\7\n\2\2\u00ee\u00ef\5\62\32\2\u00ef/"+
		"\3\2\2\2\u00f0\u00f1\7\13\2\2\u00f1\u00f6\5\62\32\2\u00f2\u00f3\7\34\2"+
		"\2\u00f3\u00f5\5\62\32\2\u00f4\u00f2\3\2\2\2\u00f5\u00f8\3\2\2\2\u00f6"+
		"\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f9\3\2\2\2\u00f8\u00f6\3\2"+
		"\2\2\u00f9\u00fa\7\f\2\2\u00fa\u00fe\3\2\2\2\u00fb\u00fc\7\13\2\2\u00fc"+
		"\u00fe\7\f\2\2\u00fd\u00f0\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fe\61\3\2\2"+
		"\2\u00ff\u0107\7\r\2\2\u0100\u0107\7\60\2\2\u0101\u0107\5,\27\2\u0102"+
		"\u0107\5\60\31\2\u0103\u0107\7\5\2\2\u0104\u0107\7\6\2\2\u0105\u0107\7"+
		"\7\2\2\u0106\u00ff\3\2\2\2\u0106\u0100\3\2\2\2\u0106\u0101\3\2\2\2\u0106"+
		"\u0102\3\2\2\2\u0106\u0103\3\2\2\2\u0106\u0104\3\2\2\2\u0106\u0105\3\2"+
		"\2\2\u0107\63\3\2\2\2\u0108\u0109\7\17\2\2\u0109\u010a\5\66\34\2\u010a"+
		"\u010b\7\20\2\2\u010b\u010e\5\16\b\2\u010c\u010d\7\26\2\2\u010d\u010f"+
		"\5:\36\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f\65\3\2\2\2\u0110"+
		"\u0115\58\35\2\u0111\u0112\7\34\2\2\u0112\u0114\58\35\2\u0113\u0111\3"+
		"\2\2\2\u0114\u0117\3\2\2\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116"+
		"\67\3\2\2\2\u0117\u0115\3\2\2\2\u0118\u011c\7\35\2\2\u0119\u011c\5\22"+
		"\n\2\u011a\u011c\5$\23\2\u011b\u0118\3\2\2\2\u011b\u0119\3\2\2\2\u011b"+
		"\u011a\3\2\2\2\u011c9\3\2\2\2\u011d\u011e\b\36\1\2\u011e\u011f\5\22\n"+
		"\2\u011f\u0120\5<\37\2\u0120\u0121\58\35\2\u0121\u012a\3\2\2\2\u0122\u0123"+
		"\f\4\2\2\u0123\u0124\7)\2\2\u0124\u0129\5:\36\5\u0125\u0126\f\3\2\2\u0126"+
		"\u0127\7*\2\2\u0127\u0129\5:\36\4\u0128\u0122\3\2\2\2\u0128\u0125\3\2"+
		"\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b"+
		";\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012e\t\3\2\2\u012e=\3\2\2\2\35@B"+
		"Kju}\u0088\u0095\u00a3\u00aa\u00b4\u00b8\u00be\u00c0\u00c5\u00cd\u00db"+
		"\u00e3\u00ea\u00f6\u00fd\u0106\u010e\u0115\u011b\u0128\u012a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}