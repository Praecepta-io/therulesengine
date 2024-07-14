// Generated from SQLIdentifierParser.g4 by ANTLR 4.5
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
public class SQLIdentifierParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, SPEC_SQL_COMMENT=2, COMMENT_INPUT=3, LINE_COMMENT=4, ALL=5, AND=6, 
		AS=7, ASC=8, BOOLEAN=9, BETWEEN=10, BY=11, CASE=12, CAST=13, CROSS=14, 
		COLUMNS=15, DATETIME=16, DELETE=17, DESC=18, DESCRIBE=19, DISTINCT=20, 
		DOUBLE=21, ELSE=22, EXISTS=23, FALSE=24, FLOAT=25, FIRST=26, FROM=27, 
		GROUP=28, HAVING=29, IN=30, INNER=31, INT=32, INTEGER=33, IS=34, JOIN=35, 
		LAST=36, LEFT=37, LIKE=38, LIMIT=39, LONG=40, MATCH=41, NATURAL=42, MISSING_LITERAL=43, 
		NOT=44, NULL_LITERAL=45, NULLS=46, ON=47, OR=48, ORDER=49, OUTER=50, OVER=51, 
		PARTITION=52, REGEXP=53, RIGHT=54, SELECT=55, SHOW=56, STRING=57, THEN=58, 
		TRUE=59, UNION=60, USING=61, WHEN=62, WHERE=63, MISSING=64, EXCEPT=65, 
		AVG=66, COUNT=67, MAX=68, MIN=69, SUM=70, SUBSTRING=71, TRIM=72, END=73, 
		FULL=74, OFFSET=75, INTERVAL=76, MICROSECOND=77, SECOND=78, MINUTE=79, 
		HOUR=80, DAY=81, WEEK=82, MONTH=83, QUARTER=84, YEAR=85, SECOND_MICROSECOND=86, 
		MINUTE_MICROSECOND=87, MINUTE_SECOND=88, HOUR_MICROSECOND=89, HOUR_SECOND=90, 
		HOUR_MINUTE=91, DAY_MICROSECOND=92, DAY_SECOND=93, DAY_MINUTE=94, DAY_HOUR=95, 
		YEAR_MONTH=96, TABLES=97, ABS=98, ACOS=99, ADD=100, ASCII=101, ASIN=102, 
		ATAN=103, ATAN2=104, CBRT=105, CEIL=106, CEILING=107, CONCAT=108, CONCAT_WS=109, 
		CONV=110, COS=111, COSH=112, COT=113, CRC32=114, CURDATE=115, DATE=116, 
		DATE_FORMAT=117, DATE_ADD=118, DATE_SUB=119, DAYOFMONTH=120, DAYOFWEEK=121, 
		DAYOFYEAR=122, DAYNAME=123, DEGREES=124, E=125, EXP=126, EXPM1=127, FLOOR=128, 
		FROM_DAYS=129, IF=130, IFNULL=131, ISNULL=132, LENGTH=133, LN=134, LOCATE=135, 
		LOG=136, LOG10=137, LOG2=138, LOWER=139, LTRIM=140, MAKETIME=141, MODULUS=142, 
		MONTHNAME=143, MULTIPLY=144, NOW=145, NULLIF=146, PI=147, POW=148, POWER=149, 
		RADIANS=150, RAND=151, REPLACE=152, RINT=153, ROUND=154, RTRIM=155, SIGN=156, 
		SIGNUM=157, SIN=158, SINH=159, SQRT=160, SUBDATE=161, SUBTRACT=162, TAN=163, 
		TIME=164, TIME_TO_SEC=165, TIMESTAMP=166, TRUNCATE=167, TO_DAYS=168, UPPER=169, 
		D=170, T=171, TS=172, LEFT_BRACE=173, RIGHT_BRACE=174, DENSE_RANK=175, 
		RANK=176, ROW_NUMBER=177, DATE_HISTOGRAM=178, DAY_OF_MONTH=179, DAY_OF_YEAR=180, 
		DAY_OF_WEEK=181, EXCLUDE=182, EXTENDED_STATS=183, FIELD=184, FILTER=185, 
		GEO_BOUNDING_BOX=186, GEO_CELL=187, GEO_DISTANCE=188, GEO_DISTANCE_RANGE=189, 
		GEO_INTERSECTS=190, GEO_POLYGON=191, HISTOGRAM=192, HOUR_OF_DAY=193, INCLUDE=194, 
		IN_TERMS=195, MATCHPHRASE=196, MATCH_PHRASE=197, MATCHQUERY=198, MATCH_QUERY=199, 
		MINUTE_OF_DAY=200, MINUTE_OF_HOUR=201, MONTH_OF_YEAR=202, MULTIMATCH=203, 
		MULTI_MATCH=204, NESTED=205, PERCENTILES=206, REGEXP_QUERY=207, REVERSE_NESTED=208, 
		QUERY=209, RANGE=210, SCORE=211, SECOND_OF_MINUTE=212, STATS=213, TERM=214, 
		TERMS=215, TOPHITS=216, WEEK_OF_YEAR=217, WILDCARDQUERY=218, WILDCARD_QUERY=219, 
		SUBSTR=220, STRCMP=221, ADDDATE=222, STAR=223, DIVIDE=224, MODULE=225, 
		PLUS=226, MINUS=227, DIV=228, MOD=229, EQUAL_SYMBOL=230, GREATER_SYMBOL=231, 
		LESS_SYMBOL=232, EXCLAMATION_SYMBOL=233, BIT_NOT_OP=234, BIT_OR_OP=235, 
		BIT_AND_OP=236, BIT_XOR_OP=237, DOT=238, LR_BRACKET=239, RR_BRACKET=240, 
		COMMA=241, SEMI=242, AT_SIGN=243, ZERO_DECIMAL=244, ONE_DECIMAL=245, TWO_DECIMAL=246, 
		SINGLE_QUOTE_SYMB=247, DOUBLE_QUOTE_SYMB=248, REVERSE_QUOTE_SYMB=249, 
		COLON_SYMB=250, START_NATIONAL_STRING_LITERAL=251, STRING_LITERAL=252, 
		DECIMAL_LITERAL=253, HEXADECIMAL_LITERAL=254, REAL_LITERAL=255, NULL_SPEC_LITERAL=256, 
		BIT_STRING=257, ID=258, DOUBLE_QUOTE_ID=259, BACKTICK_QUOTE_ID=260, ERROR_RECOGNITION=261;
	public static final int
		RULE_tableName = 0, RULE_columnName = 1, RULE_alias = 2, RULE_qualifiedName = 3, 
		RULE_ident = 4, RULE_keywordsCanBeId = 5;
	public static final String[] ruleNames = {
		"tableName", "columnName", "alias", "qualifiedName", "ident", "keywordsCanBeId"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, "'ALL'", "'AND'", "'AS'", "'ASC'", "'BOOLEAN'", 
		"'BETWEEN'", "'BY'", "'CASE'", "'CAST'", "'CROSS'", "'COLUMNS'", "'DATETIME'", 
		"'DELETE'", "'DESC'", "'DESCRIBE'", "'DISTINCT'", "'DOUBLE'", "'ELSE'", 
		"'EXISTS'", "'FALSE'", "'FLOAT'", "'FIRST'", "'FROM'", "'GROUP'", "'HAVING'", 
		"'IN'", "'INNER'", "'INT'", "'INTEGER'", "'IS'", "'JOIN'", "'LAST'", "'LEFT'", 
		"'LIKE'", "'LIMIT'", "'LONG'", "'MATCH'", "'NATURAL'", null, "'NOT'", 
		"'NULL'", "'NULLS'", "'ON'", "'OR'", "'ORDER'", "'OUTER'", "'OVER'", "'PARTITION'", 
		"'REGEXP'", "'RIGHT'", "'SELECT'", "'SHOW'", "'STRING'", "'THEN'", "'TRUE'", 
		"'UNION'", "'USING'", "'WHEN'", "'WHERE'", null, "'MINUS'", "'AVG'", "'COUNT'", 
		"'MAX'", "'MIN'", "'SUM'", "'SUBSTRING'", "'TRIM'", "'END'", "'FULL'", 
		"'OFFSET'", "'INTERVAL'", "'MICROSECOND'", "'SECOND'", "'MINUTE'", "'HOUR'", 
		"'DAY'", "'WEEK'", "'MONTH'", "'QUARTER'", "'YEAR'", "'SECOND_MICROSECOND'", 
		"'MINUTE_MICROSECOND'", "'MINUTE_SECOND'", "'HOUR_MICROSECOND'", "'HOUR_SECOND'", 
		"'HOUR_MINUTE'", "'DAY_MICROSECOND'", "'DAY_SECOND'", "'DAY_MINUTE'", 
		"'DAY_HOUR'", "'YEAR_MONTH'", "'TABLES'", "'ABS'", "'ACOS'", "'ADD'", 
		"'ASCII'", "'ASIN'", "'ATAN'", "'ATAN2'", "'CBRT'", "'CEIL'", "'CEILING'", 
		"'CONCAT'", "'CONCAT_WS'", "'CONV'", "'COS'", "'COSH'", "'COT'", "'CRC32'", 
		"'CURDATE'", "'DATE'", "'DATE_FORMAT'", "'DATE_ADD'", "'DATE_SUB'", "'DAYOFMONTH'", 
		"'DAYOFWEEK'", "'DAYOFYEAR'", "'DAYNAME'", "'DEGREES'", "'E'", "'EXP'", 
		"'EXPM1'", "'FLOOR'", "'FROM_DAYS'", "'IF'", "'IFNULL'", "'ISNULL'", "'LENGTH'", 
		"'LN'", "'LOCATE'", "'LOG'", "'LOG10'", "'LOG2'", "'LOWER'", "'LTRIM'", 
		"'MAKETIME'", "'MODULUS'", "'MONTHNAME'", "'MULTIPLY'", "'NOW'", "'NULLIF'", 
		"'PI'", "'POW'", "'POWER'", "'RADIANS'", "'RAND'", "'REPLACE'", "'RINT'", 
		"'ROUND'", "'RTRIM'", "'SIGN'", "'SIGNUM'", "'SIN'", "'SINH'", "'SQRT'", 
		"'SUBDATE'", "'SUBTRACT'", "'TAN'", "'TIME'", "'TIME_TO_SEC'", "'TIMESTAMP'", 
		"'TRUNCATE'", "'TO_DAYS'", "'UPPER'", "'D'", "'T'", "'TS'", "'{'", "'}'", 
		"'DENSE_RANK'", "'RANK'", "'ROW_NUMBER'", "'DATE_HISTOGRAM'", "'DAY_OF_MONTH'", 
		"'DAY_OF_YEAR'", "'DAY_OF_WEEK'", "'EXCLUDE'", "'EXTENDED_STATS'", "'FIELD'", 
		"'FILTER'", "'GEO_BOUNDING_BOX'", "'GEO_CELL'", "'GEO_DISTANCE'", "'GEO_DISTANCE_RANGE'", 
		"'GEO_INTERSECTS'", "'GEO_POLYGON'", "'HISTOGRAM'", "'HOUR_OF_DAY'", "'INCLUDE'", 
		"'IN_TERMS'", "'MATCHPHRASE'", "'MATCH_PHRASE'", "'MATCHQUERY'", "'MATCH_QUERY'", 
		"'MINUTE_OF_DAY'", "'MINUTE_OF_HOUR'", "'MONTH_OF_YEAR'", "'MULTIMATCH'", 
		"'MULTI_MATCH'", "'NESTED'", "'PERCENTILES'", "'REGEXP_QUERY'", "'REVERSE_NESTED'", 
		"'QUERY'", "'RANGE'", "'SCORE'", "'SECOND_OF_MINUTE'", "'STATS'", "'TERM'", 
		"'TERMS'", "'TOPHITS'", "'WEEK_OF_YEAR'", "'WILDCARDQUERY'", "'WILDCARD_QUERY'", 
		"'SUBSTR'", "'STRCMP'", "'ADDDATE'", "'*'", "'/'", "'%'", "'+'", "'-'", 
		"'DIV'", "'MOD'", "'='", "'>'", "'<'", "'!'", "'~'", "'|'", "'&'", "'^'", 
		"'.'", "'('", "')'", "','", "';'", "'@'", "'0'", "'1'", "'2'", "'''", 
		"'\"'", "'`'", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SPACE", "SPEC_SQL_COMMENT", "COMMENT_INPUT", "LINE_COMMENT", "ALL", 
		"AND", "AS", "ASC", "BOOLEAN", "BETWEEN", "BY", "CASE", "CAST", "CROSS", 
		"COLUMNS", "DATETIME", "DELETE", "DESC", "DESCRIBE", "DISTINCT", "DOUBLE", 
		"ELSE", "EXISTS", "FALSE", "FLOAT", "FIRST", "FROM", "GROUP", "HAVING", 
		"IN", "INNER", "INT", "INTEGER", "IS", "JOIN", "LAST", "LEFT", "LIKE", 
		"LIMIT", "LONG", "MATCH", "NATURAL", "MISSING_LITERAL", "NOT", "NULL_LITERAL", 
		"NULLS", "ON", "OR", "ORDER", "OUTER", "OVER", "PARTITION", "REGEXP", 
		"RIGHT", "SELECT", "SHOW", "STRING", "THEN", "TRUE", "UNION", "USING", 
		"WHEN", "WHERE", "MISSING", "EXCEPT", "AVG", "COUNT", "MAX", "MIN", "SUM", 
		"SUBSTRING", "TRIM", "END", "FULL", "OFFSET", "INTERVAL", "MICROSECOND", 
		"SECOND", "MINUTE", "HOUR", "DAY", "WEEK", "MONTH", "QUARTER", "YEAR", 
		"SECOND_MICROSECOND", "MINUTE_MICROSECOND", "MINUTE_SECOND", "HOUR_MICROSECOND", 
		"HOUR_SECOND", "HOUR_MINUTE", "DAY_MICROSECOND", "DAY_SECOND", "DAY_MINUTE", 
		"DAY_HOUR", "YEAR_MONTH", "TABLES", "ABS", "ACOS", "ADD", "ASCII", "ASIN", 
		"ATAN", "ATAN2", "CBRT", "CEIL", "CEILING", "CONCAT", "CONCAT_WS", "CONV", 
		"COS", "COSH", "COT", "CRC32", "CURDATE", "DATE", "DATE_FORMAT", "DATE_ADD", 
		"DATE_SUB", "DAYOFMONTH", "DAYOFWEEK", "DAYOFYEAR", "DAYNAME", "DEGREES", 
		"E", "EXP", "EXPM1", "FLOOR", "FROM_DAYS", "IF", "IFNULL", "ISNULL", "LENGTH", 
		"LN", "LOCATE", "LOG", "LOG10", "LOG2", "LOWER", "LTRIM", "MAKETIME", 
		"MODULUS", "MONTHNAME", "MULTIPLY", "NOW", "NULLIF", "PI", "POW", "POWER", 
		"RADIANS", "RAND", "REPLACE", "RINT", "ROUND", "RTRIM", "SIGN", "SIGNUM", 
		"SIN", "SINH", "SQRT", "SUBDATE", "SUBTRACT", "TAN", "TIME", "TIME_TO_SEC", 
		"TIMESTAMP", "TRUNCATE", "TO_DAYS", "UPPER", "D", "T", "TS", "LEFT_BRACE", 
		"RIGHT_BRACE", "DENSE_RANK", "RANK", "ROW_NUMBER", "DATE_HISTOGRAM", "DAY_OF_MONTH", 
		"DAY_OF_YEAR", "DAY_OF_WEEK", "EXCLUDE", "EXTENDED_STATS", "FIELD", "FILTER", 
		"GEO_BOUNDING_BOX", "GEO_CELL", "GEO_DISTANCE", "GEO_DISTANCE_RANGE", 
		"GEO_INTERSECTS", "GEO_POLYGON", "HISTOGRAM", "HOUR_OF_DAY", "INCLUDE", 
		"IN_TERMS", "MATCHPHRASE", "MATCH_PHRASE", "MATCHQUERY", "MATCH_QUERY", 
		"MINUTE_OF_DAY", "MINUTE_OF_HOUR", "MONTH_OF_YEAR", "MULTIMATCH", "MULTI_MATCH", 
		"NESTED", "PERCENTILES", "REGEXP_QUERY", "REVERSE_NESTED", "QUERY", "RANGE", 
		"SCORE", "SECOND_OF_MINUTE", "STATS", "TERM", "TERMS", "TOPHITS", "WEEK_OF_YEAR", 
		"WILDCARDQUERY", "WILDCARD_QUERY", "SUBSTR", "STRCMP", "ADDDATE", "STAR", 
		"DIVIDE", "MODULE", "PLUS", "MINUS", "DIV", "MOD", "EQUAL_SYMBOL", "GREATER_SYMBOL", 
		"LESS_SYMBOL", "EXCLAMATION_SYMBOL", "BIT_NOT_OP", "BIT_OR_OP", "BIT_AND_OP", 
		"BIT_XOR_OP", "DOT", "LR_BRACKET", "RR_BRACKET", "COMMA", "SEMI", "AT_SIGN", 
		"ZERO_DECIMAL", "ONE_DECIMAL", "TWO_DECIMAL", "SINGLE_QUOTE_SYMB", "DOUBLE_QUOTE_SYMB", 
		"REVERSE_QUOTE_SYMB", "COLON_SYMB", "START_NATIONAL_STRING_LITERAL", "STRING_LITERAL", 
		"DECIMAL_LITERAL", "HEXADECIMAL_LITERAL", "REAL_LITERAL", "NULL_SPEC_LITERAL", 
		"BIT_STRING", "ID", "DOUBLE_QUOTE_ID", "BACKTICK_QUOTE_ID", "ERROR_RECOGNITION"
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
	public String getGrammarFileName() { return "SQLIdentifierParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SQLIdentifierParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TableNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitTableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			qualifiedName();
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

	public static class ColumnNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ColumnNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterColumnName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitColumnName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitColumnName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnNameContext columnName() throws RecognitionException {
		ColumnNameContext _localctx = new ColumnNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_columnName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			qualifiedName();
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

	public static class AliasContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			ident();
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

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<IdentContext> ident() {
			return getRuleContexts(IdentContext.class);
		}
		public IdentContext ident(int i) {
			return getRuleContext(IdentContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(SQLIdentifierParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SQLIdentifierParser.DOT, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitQualifiedName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			ident();
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(19);
				match(DOT);
				setState(20);
				ident();
				}
				}
				setState(25);
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

	public static class IdentContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SQLIdentifierParser.ID, 0); }
		public TerminalNode DOT() { return getToken(SQLIdentifierParser.DOT, 0); }
		public TerminalNode BACKTICK_QUOTE_ID() { return getToken(SQLIdentifierParser.BACKTICK_QUOTE_ID, 0); }
		public KeywordsCanBeIdContext keywordsCanBeId() {
			return getRuleContext(KeywordsCanBeIdContext.class,0);
		}
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterIdent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitIdent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ident);
		int _la;
		try {
			setState(32);
			switch (_input.LA(1)) {
			case DOT:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(27);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(26);
					match(DOT);
					}
				}

				setState(29);
				match(ID);
				}
				break;
			case BACKTICK_QUOTE_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(30);
				match(BACKTICK_QUOTE_ID);
				}
				break;
			case FIRST:
			case LAST:
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
			case FULL:
			case DATE:
			case DAYOFWEEK:
			case TIME:
			case TIMESTAMP:
			case D:
			case T:
			case TS:
			case FIELD:
				enterOuterAlt(_localctx, 3);
				{
				setState(31);
				keywordsCanBeId();
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

	public static class KeywordsCanBeIdContext extends ParserRuleContext {
		public TerminalNode FULL() { return getToken(SQLIdentifierParser.FULL, 0); }
		public TerminalNode FIELD() { return getToken(SQLIdentifierParser.FIELD, 0); }
		public TerminalNode D() { return getToken(SQLIdentifierParser.D, 0); }
		public TerminalNode T() { return getToken(SQLIdentifierParser.T, 0); }
		public TerminalNode TS() { return getToken(SQLIdentifierParser.TS, 0); }
		public TerminalNode COUNT() { return getToken(SQLIdentifierParser.COUNT, 0); }
		public TerminalNode SUM() { return getToken(SQLIdentifierParser.SUM, 0); }
		public TerminalNode AVG() { return getToken(SQLIdentifierParser.AVG, 0); }
		public TerminalNode MAX() { return getToken(SQLIdentifierParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(SQLIdentifierParser.MIN, 0); }
		public TerminalNode TIMESTAMP() { return getToken(SQLIdentifierParser.TIMESTAMP, 0); }
		public TerminalNode DATE() { return getToken(SQLIdentifierParser.DATE, 0); }
		public TerminalNode TIME() { return getToken(SQLIdentifierParser.TIME, 0); }
		public TerminalNode DAYOFWEEK() { return getToken(SQLIdentifierParser.DAYOFWEEK, 0); }
		public TerminalNode FIRST() { return getToken(SQLIdentifierParser.FIRST, 0); }
		public TerminalNode LAST() { return getToken(SQLIdentifierParser.LAST, 0); }
		public KeywordsCanBeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordsCanBeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).enterKeywordsCanBeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLIdentifierParserListener ) ((SQLIdentifierParserListener)listener).exitKeywordsCanBeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLIdentifierParserVisitor ) return ((SQLIdentifierParserVisitor<? extends T>)visitor).visitKeywordsCanBeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordsCanBeIdContext keywordsCanBeId() throws RecognitionException {
		KeywordsCanBeIdContext _localctx = new KeywordsCanBeIdContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_keywordsCanBeId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			_la = _input.LA(1);
			if ( !(_la==FIRST || _la==LAST || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (AVG - 66)) | (1L << (COUNT - 66)) | (1L << (MAX - 66)) | (1L << (MIN - 66)) | (1L << (SUM - 66)) | (1L << (FULL - 66)) | (1L << (DATE - 66)) | (1L << (DAYOFWEEK - 66)))) != 0) || ((((_la - 164)) & ~0x3f) == 0 && ((1L << (_la - 164)) & ((1L << (TIME - 164)) | (1L << (TIMESTAMP - 164)) | (1L << (D - 164)) | (1L << (T - 164)) | (1L << (TS - 164)) | (1L << (FIELD - 164)))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u0107\'\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5"+
		"\3\5\7\5\30\n\5\f\5\16\5\33\13\5\3\6\5\6\36\n\6\3\6\3\6\3\6\5\6#\n\6\3"+
		"\7\3\7\3\7\2\2\b\2\4\6\b\n\f\2\3\f\2\34\34&&DHLLvv{{\u00a6\u00a6\u00a8"+
		"\u00a8\u00ac\u00ae\u00ba\u00ba$\2\16\3\2\2\2\4\20\3\2\2\2\6\22\3\2\2\2"+
		"\b\24\3\2\2\2\n\"\3\2\2\2\f$\3\2\2\2\16\17\5\b\5\2\17\3\3\2\2\2\20\21"+
		"\5\b\5\2\21\5\3\2\2\2\22\23\5\n\6\2\23\7\3\2\2\2\24\31\5\n\6\2\25\26\7"+
		"\u00f0\2\2\26\30\5\n\6\2\27\25\3\2\2\2\30\33\3\2\2\2\31\27\3\2\2\2\31"+
		"\32\3\2\2\2\32\t\3\2\2\2\33\31\3\2\2\2\34\36\7\u00f0\2\2\35\34\3\2\2\2"+
		"\35\36\3\2\2\2\36\37\3\2\2\2\37#\7\u0104\2\2 #\7\u0106\2\2!#\5\f\7\2\""+
		"\35\3\2\2\2\" \3\2\2\2\"!\3\2\2\2#\13\3\2\2\2$%\t\2\2\2%\r\3\2\2\2\5\31"+
		"\35\"";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}