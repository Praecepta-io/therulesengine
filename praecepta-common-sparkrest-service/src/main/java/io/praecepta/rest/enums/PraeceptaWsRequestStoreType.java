package io.praecepta.rest.enums;

import io.praecepta.core.data.enums.IPraeceptaRequestStoreType;

public enum PraeceptaWsRequestStoreType implements IPraeceptaRequestStoreType{
	
	
	WS_RAW_INPUT("wsRawInput"), 
	WS_INPUT("wsInput"), 
	WS_INPUT_CONTENT_TYPE("wsInputContentType"), 
	WS_INOUT_HEADERS("wsInputHeaders"),
	WS_INPUT_QUERY_PARAMS("wsInputQueryParams"),
	WS_INPUT_PATH_PARAMS("wsInputPathParams"),

	WS_SESSION_DATA("wsSessionData"),
	
	WS_RAW_OUTPUT("wsRawOutput"),
	WS_OPERATION("wsOperation"),
	WS_OUTPUT("wsOutput"),
	WS_OUTPUT_CONTENT_TYPE("wsOutputContentType"), 
	WS_OUTPUT_HEADERS("wsOutputHeasders"),
	WS_OUTPUT_QUERY_PARAMS("wsOutputQueryParams"),
	WS_OUTPUT_PATH_PARAMS("wsOutputPathParams"),
	
	;

	private PraeceptaWsRequestStoreType(String wsProcessingAttributeName) {
		this.wsProcessingAttributeName = wsProcessingAttributeName;
	}
	
	private String wsProcessingAttributeName;
	
	@Override
	public String getStoringAttributeName() {
		return wsProcessingAttributeName;
	}
}
