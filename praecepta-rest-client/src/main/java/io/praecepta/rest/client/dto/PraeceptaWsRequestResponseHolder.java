package io.praecepta.rest.client.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.client.enums.PraeceptaEnricherAttributeType;

public class PraeceptaWsRequestResponseHolder {
	
	private HttpMethod httpMethod;
	
	private Map<String, String> queryParams;
	
	private Map<String, String> pathParams;
	
	private Map<String, String> inHeaderParams;
	
	private Map<String, String> outHeaderParams = new HashMap<>();
	
	private Map<String, String> authParams;
	
	private String input;
	
	private String output;
	
	private HttpStatus httpStatus;
	
	private PraeceptaHttpCallStatus callStatus;

	public PraeceptaWsRequestResponseHolder(PraeceptaWsOperationType httpOperationType) {
		
		captureHttpMethod(httpOperationType);
	}

	private void captureHttpMethod(PraeceptaWsOperationType httpOperationType) {
		if(httpOperationType != null) {
			httpMethod = HttpMethod.resolve(httpOperationType.name());
		}
	}
	
	public PraeceptaWsRequestResponseHolder(PraeceptaWsOperationType httpOperationType, String input, 
			Map<String, String> queryParams, Map<String, String> pathParams, Map<String, String> inHeaderParams) {
		
		this(httpOperationType);
		
		this.queryParams = queryParams;
		this.pathParams = pathParams;
		this.inHeaderParams = inHeaderParams;
		this.input = input;
	}
	
	public PraeceptaWsRequestResponseHolder(PraeceptaWsOperationType httpOperationType, String input, 
			Map<String, String> queryParams, Map<String, String> pathParams, Map<String, String> inHeaderParams, Map<String, String> authParams) {
		
		this(httpOperationType, input, queryParams, pathParams, inHeaderParams);
		
		this.authParams = authParams;
		
		if(PraeceptaObjectHelper.isObjectEmpty(this.inHeaderParams)) {
			this.inHeaderParams = new HashMap<String, String>();
		}
		
		if(!PraeceptaObjectHelper.isObjectEmpty(this.authParams)) {
			this.inHeaderParams.putAll(authParams);
		}
		if(!PraeceptaObjectHelper.isObjectEmpty(this.inHeaderParams)) {
			outHeaderParams.putAll(inHeaderParams);
		}
		
	}
	
	public PraeceptaWsRequestResponseHolder(Map<PraeceptaEnricherAttributeType, Object> requiredInfo) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(requiredInfo)) {
			
			captureHttpMethod((PraeceptaWsOperationType) requiredInfo.get(PraeceptaEnricherAttributeType.HTTP_OPERATION_TYPE));
		}
		
	}
	
	public enum PraeceptaWsOperationType{
		
		GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
	}
	
	public enum PraeceptaHttpCallStatus{
		SUCCESS, FAILURE,  FAILURE_WITH_EXCPETION, FATAL;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public Map<String, String> getInHeaderParams() {
		return inHeaderParams;
	}

	public Map<String, String> getOutHeaderParams() {
		return outHeaderParams;
	}

	public Map<String, String> getAuthParams() {
		return authParams;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public PraeceptaHttpCallStatus getCallStatus() {
		return callStatus;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}

	public void setInHeaderParams(Map<String, String> inHeaderParams) {
		this.inHeaderParams = inHeaderParams;
	}

	public void setOutHeaderParams(Map<String, String> outHeaderParams) {
		this.outHeaderParams = outHeaderParams;
	}

	public void setAuthParams(Map<String, String> authParams) {
		this.authParams = authParams;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setCallStatus(PraeceptaHttpCallStatus callStatus) {
		this.callStatus = callStatus;
	}

	@Override
	public String toString() {
		return "PraeceptaWsRequestResponseHolder [httpMethod=" + httpMethod + ", queryParams=" + queryParams
				+ ", pathParams=" + pathParams + ", inHeaderParams=" + inHeaderParams + ", outHeaderParams="
				+ outHeaderParams + ", authParams=" + authParams + ", input=" + input + ", output=" + output
				+ ", httpStatus=" + httpStatus + ", callStatus=" + callStatus + "]";
	}
	
}
