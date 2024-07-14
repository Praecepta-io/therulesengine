package io.praecepta.dao.elastic.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "praecepta_audit", createIndex = false)
public class PraeceptaAuditEntity implements IPraeceptaEsEntityModel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String spaceName;
	
	static final String SPACE_NAME = "spaceName";

	private String clientId;
	
	static final String CLIENT_ID = "clientId";

	private String appName;
	
	static final String APP_NAME = "appName";
	
	private String ruleGroupName;
	
	static final String RULE_GRP_NAME = "ruleGroupName";

	private String version = "V1";
	
	static final String VERSION = "version";

	private String createdBy;
	
	static final String CREATED_BY = "createdBy";
	
	private PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint;

	private String updatedBy;

	private Date createdDate;

	private Date updateDate;
	
	private Map<String, Object> entityFieldNameAndValue = new HashMap<>();

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public void setLastUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public void setLastUpdatedDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public String getLastUpdatedBy() {
		return updatedBy;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public Date getLastUpdatedDate() {
		return updateDate;
	}
	
	public String getRuleGroupName() {
		return ruleGroupName;
	}

	public void setRuleGroupName(String ruleGroupName) {
		this.ruleGroupName = ruleGroupName;
	}

	@Override
	public String[] getEntityFieldNames() {
		
		return new String[] {SPACE_NAME, CLIENT_ID, APP_NAME, VERSION, RULE_GRP_NAME, CREATED_BY};
	}
	
	public PraeceptaRuleGroupAuditPoint getRuleGroupAuditPoint() {
		return ruleGroupAuditPoint;
	}

	public void setRuleGroupAuditPoint(PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {
		this.ruleGroupAuditPoint = ruleGroupAuditPoint;
	}

	@Override
	public Map<String, Object> getEntityFieldNameAndValue() {
		
		entityFieldNameAndValue.clear();
		
		entityFieldNameAndValue.put(SPACE_NAME, spaceName);
		entityFieldNameAndValue.put(CLIENT_ID, clientId);
		entityFieldNameAndValue.put(APP_NAME, appName);
		entityFieldNameAndValue.put(VERSION, version);
		entityFieldNameAndValue.put(CREATED_BY, createdBy);
		
		return entityFieldNameAndValue;
	}

	@Override
	public String toString() {
		return "PraeceptaAuditEntity [id=" + id + ", spaceName=" + spaceName + ", clientId=" + clientId + ", appName="
				+ appName + ", version=" + version + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((spaceName == null) ? 0 : spaceName.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		PraeceptaAuditEntity other = (PraeceptaAuditEntity) obj;
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
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
