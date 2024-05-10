package io.praecepta.rules.hub.dao.models;

import java.io.Serializable;

public class PraeceptaRuleSpaceCompositeKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Entity Name - BR
	private String spaceName;
	
	// Client Id - GTO/ICS/003/005
	private String clientId;
	
	// System names
	private String appName;
	
	// V1/V2/V1M2P23 [Version 1 and Path 1]
	private String version = "V1";

	public PraeceptaRuleSpaceCompositeKey(){}
	public PraeceptaRuleSpaceCompositeKey(String spaceName) {
		this(spaceName, null, null);
	}
	
	public PraeceptaRuleSpaceCompositeKey(String spaceName, String clientId, String appName) {
		this.spaceName = spaceName;
		this.clientId = clientId;
		this.appName = appName;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((spaceName == null) ? 0 : spaceName.hashCode());
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
		PraeceptaRuleSpaceCompositeKey other = (PraeceptaRuleSpaceCompositeKey) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSpaceCompositeKey [spaceName=" + spaceName + ", clientId=" + clientId + ", appName="
				+ appName + ", version=" + version + "]";
	}
	
}
