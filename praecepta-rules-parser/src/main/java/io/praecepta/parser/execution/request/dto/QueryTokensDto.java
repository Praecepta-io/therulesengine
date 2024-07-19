package io.praecepta.parser.execution.request.dto;

import io.praecepta.sql.antlr.parser.SimpleSQLParser.HavingKeyValExprContext;
import io.praecepta.sql.antlr.parser.SimpleSQLParser.InputExprContext;

public class QueryTokensDto {
	private String tableNameIdentifier;
	private InputExprContext inputExprCtx;
	private HavingKeyValExprContext havingKeyValExpr;

	public String getTableNameIdentifier() {
		return tableNameIdentifier;
	}

	public void setTableNameIdentifier(String tableNameIdentifier) {
		this.tableNameIdentifier = tableNameIdentifier;
	}

	public InputExprContext getInputExprCtx() {
		return inputExprCtx;
	}

	public void setInputExprCtx(InputExprContext inputExprCtx) {
		this.inputExprCtx = inputExprCtx;
	}

	public HavingKeyValExprContext getHavingKeyValExpr() {
		return havingKeyValExpr;
	}

	public void setHavingKeyValExpr(HavingKeyValExprContext havingKeyValExpr) {
		this.havingKeyValExpr = havingKeyValExpr;
	}
}
