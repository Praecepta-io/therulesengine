package io.praecepta.rest.client.config;

public class PraeceptaSecureWebServiceClientConfig extends PraeceptaWebServiceClientConfig{

	private String certPath;
	
	private String certPassword;

	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}

	@Override
	public String toString() {
		return "PraeceptaSecureWebServiceClientConfig [certPath=" + certPath + ", certPassword=" + certPassword + "]";
	}
	
}
