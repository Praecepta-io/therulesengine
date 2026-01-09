package io.praecepta.rest.api.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adapter class to convert HttpServletRequest/Response to PraeceptaRequestStore
 * Replaces Spark Request/Response handling
 */
public class PraeceptaControllerAdapter {

private static final Logger LOG = LoggerFactory.getLogger(PraeceptaControllerAdapter.class);

public static Object executeService(IPraeceptaApiService service, HttpServletRequest request, 
HttpServletResponse response, String operationName) {

PraeceptaRequestStore requestStore = buildRequestStore(request, response);

requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION, operationName);

service.execute(requestStore);

return requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT);
}

private static PraeceptaRequestStore buildRequestStore(HttpServletRequest request, HttpServletResponse response) {
PraeceptaRequestStore requestStore = new PraeceptaRequestStore();

try {
// Store raw request and response
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_RAW_INPUT, request);
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_RAW_OUTPUT, response);

// Store request body
String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT, body);

// Store content type
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_CONTENT_TYPE, request.getContentType());

// Store query parameters
Map<String, String[]> queryParams = request.getParameterMap();
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_QUERY_PARAMS, queryParams);

// Store path parameters (will be populated by controllers)
Map<String, String> pathParams = new HashMap<>();
requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS, pathParams);

} catch (IOException e) {
LOG.error("Error reading request body", e);
}

return requestStore;
}
}
