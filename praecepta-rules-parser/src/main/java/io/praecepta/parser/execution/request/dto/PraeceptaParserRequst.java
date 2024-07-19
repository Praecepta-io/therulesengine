package io.praecepta.parser.execution.request.dto;

public class PraeceptaParserRequst {
	private String parserType;
	private String query;
	private String ruleInputType;

	public String getQuery() {
		return query;
	}

	public void setQuery(String sqlQuery) {
		this.query = sqlQuery;
	}

	public String getParserType() {
		return parserType;
	}

	public void setParserType(String type) {
		this.parserType = type;
	}
	

	public String getRuleInputType() {
		return ruleInputType;
	}

	public void setRuleInputType(String ruleInputType) {
		this.ruleInputType = ruleInputType;
	}

	@Override
	public String toString() {
		return "PraeceptaParserRequst [parserType=" + parserType + ", query=" + query + ", ruleInputType="
				+ ruleInputType + "]";
	}

}
