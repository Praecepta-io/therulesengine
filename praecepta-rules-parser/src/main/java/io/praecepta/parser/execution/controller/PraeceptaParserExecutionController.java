package io.praecepta.parser.execution.controller;

import io.praecepta.parser.execution.constants.ServiceAndMethodNames;
import io.praecepta.parser.execution.request.dto.PraeceptaParserRequst;
import io.praecepta.parser.execution.service.IPraeceptaParserExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;

public class PraeceptaParserExecutionController implements IPraeceptaSQLParserExecutionController {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaParserExecutionController.class);

	@Autowired
	IPraeceptaParserExecutionService parserExecutionService;

	@Override
	public void execute(PraeceptaRequestStore requestStore) {
		String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);

		LOG.info("Operation Name: {}", operation);

		switch (operation) {

		case ServiceAndMethodNames.PARSER_EXECUTOR_METHOD_NAME:

			PraeceptaParserRequst parserRequest = GsonHelper.toEntity(
					(String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT),
					PraeceptaParserRequst.class);

			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT,
					GsonHelper.toJson(executeParser(parserRequest)));
			break;
		}
	}

	@Override
	public Object executeParser(PraeceptaParserRequst request) {

		return parserExecutionService.executeSQLParser(request);

	}

}
