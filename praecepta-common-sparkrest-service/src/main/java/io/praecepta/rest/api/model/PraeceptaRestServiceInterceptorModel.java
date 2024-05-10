package io.praecepta.rest.api.model;

import spark.route.HttpMethod;

public class PraeceptaRestServiceInterceptorModel {

	private String serviceName;
	
	private String methodName;
	
	private HttpMethod methodProtocol;
	
	public PraeceptaRestServiceInterceptorModel(String serviceName) {
		this(serviceName, null, null);
	}
	
	public PraeceptaRestServiceInterceptorModel(String serviceName, String methodName, HttpMethod methodProtocol) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.methodProtocol = methodProtocol;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public HttpMethod getMethodProtocol() {
		return methodProtocol;
	}

	public void setMethodProtocol(HttpMethod methodProtocol) {
		this.methodProtocol = methodProtocol;
	}

	@Override
	public String toString() {
		return "PraeceptaRestServiceInterceptorModel [serviceName=" + serviceName + ", methodName=" + methodName
				+ ", methodProtocol=" + methodProtocol + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((methodProtocol == null) ? 0 : methodProtocol.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PraeceptaRestServiceInterceptorModel other = (PraeceptaRestServiceInterceptorModel) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (methodProtocol == null) {
			if (other.methodProtocol != null)
				return false;
		} else if (!methodProtocol.equals(other.methodProtocol))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}
	
}
