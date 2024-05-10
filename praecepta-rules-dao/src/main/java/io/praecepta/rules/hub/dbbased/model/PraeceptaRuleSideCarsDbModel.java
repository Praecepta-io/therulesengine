package io.praecepta.rules.hub.dbbased.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import io.praecepta.dao.model.AbstractHibernateBaseModel;

@Entity
@Table(name = "RULE_SIDE_CARS_INFO")
public class PraeceptaRuleSideCarsDbModel  extends AbstractHibernateBaseModel<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "RULE_GROUP_NAME")
	private String ruleGroupName;

	@Column(name = "RULE_SPACE_NAME")
	private String spaceName;

	@Column(name = "CLIENT_NAME")
	private String clientName;

	@Column(name = "APP_NAME")
	private String appName;
	
	@Column(name = "VERSION",nullable = false)
	private String version;

	@Lob
	@Column(name = "METADATA")
	private String metaData;
	
	@Column(name = "ACTIVE", nullable = false,columnDefinition = "TINYINT(1)")
	private boolean active;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getRuleGroupName() {
		return ruleGroupName;
	}

	public void setRuleGroupName(String ruleGroupName) {
		this.ruleGroupName = ruleGroupName;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	@Override
	public String toString() {
		return "PraeceptaRuleSideCarsDbModel [id=" + id + ", ruleGroupName=" + ruleGroupName + ", spaceName="
				+ spaceName + ", clientName=" + clientName + ", appName=" + appName + ", version=" + version
				+ ", metaData=" + metaData + ", active=" + active + "]";
	}
}
