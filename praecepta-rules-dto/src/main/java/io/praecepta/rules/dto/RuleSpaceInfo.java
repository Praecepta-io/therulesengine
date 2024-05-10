package io.praecepta.rules.dto;

import java.util.Objects;

public class RuleSpaceInfo {

    private String spaceName;

    private String clientId;

    private String appName;

    private String version;

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
		return Objects.hash(appName, clientId, spaceName, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleSpaceInfo other = (RuleSpaceInfo) obj;
		return Objects.equals(appName, other.appName) && Objects.equals(clientId, other.clientId)
				&& Objects.equals(spaceName, other.spaceName) && Objects.equals(version, other.version);
	}
}
