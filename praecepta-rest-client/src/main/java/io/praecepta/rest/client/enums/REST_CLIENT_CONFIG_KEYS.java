package io.praecepta.rest.client.enums;

public enum REST_CLIENT_CONFIG_KEYS {
	CLIENT_TYPE("clientType"), 
	REQUEST_TYPE("requestType"), 
	ENDPOINT_URL("endPointURL"),
	CERT_PATH("certPath"),
	CERT_PASSWORD("certPassword"),
	QUERY_PARAMS("queryParams"),
	PATH_PARAMS("pathParams"),
	IN_HEADER_PARAMS("inHeaderParams"),
	AUTH_PARAMS("authParams"),
	OUT_HEADER_PARAMS("outHeaderParams"),
	PARAM_NAME_IN_THE_ENDPOINT("parameterNameInTheEndpoint"),
	PARAM_VALUE_IN_SOURCE("parameterValueInSource"),
	PARAM_VALUE_STRATEGY("parameterValueStrategy"),
	PARAM_VAL_SPLIT_REGEX("\\.");

	public final String key;

	REST_CLIENT_CONFIG_KEYS(String key) {
		this.key = key;
	}
}
