package io.praecepta.parser.execution.service;

import io.praecepta.parser.execution.request.dto.PraeceptaParserRequst;

public interface IPraeceptaParserExecutionService {

	public Object executeSQLParser(PraeceptaParserRequst request);

}
