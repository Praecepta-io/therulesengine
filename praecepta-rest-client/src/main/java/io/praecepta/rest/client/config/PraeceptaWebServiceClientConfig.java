package io.praecepta.rest.client.config;

public class PraeceptaWebServiceClientConfig {
	
	private String endpointUrl;
	
	private String requestType;
	
	private Long connectionTimeOutInMilliSecs = 15000L;

	private Long readTimeOutInMilliSecs = 15000L;

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public Long getConnectionTimeOut() {
		return connectionTimeOutInMilliSecs;
	}

	public void setConnectionTimeOut(Long connectionTimeOutInMilliSecs) {
		this.connectionTimeOutInMilliSecs = connectionTimeOutInMilliSecs;
	}
	
	public Long getReadTimeOut() {
		return readTimeOutInMilliSecs;
	}
	
	public void setReadTimeOut(Long readTimeOutInMilliSecs) {
		this.readTimeOutInMilliSecs = readTimeOutInMilliSecs;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return "PraeceptaWebServiceClientConfig [endpointUrl=" + endpointUrl + ", requestType=" + requestType
				+ ", connectionTimeOutInMilliSecs=" + connectionTimeOutInMilliSecs + ", readTimeOutInMilliSecs="
				+ readTimeOutInMilliSecs + "]";
	}
	
}
