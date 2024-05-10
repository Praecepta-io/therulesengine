package io.praecepta.rules.hub.dbbased.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.praecepta.dao.model.AbstractHibernateBaseModel;

@Entity
@Table(name = "RULE_SPACE_INFO")
public class PraeceptaRuleSpaceDbModel extends AbstractHibernateBaseModel<Long>{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RULE_SPACE_ID")
	private Long id;
    
	@Column(name = "RULE_SPACE_NAME" ,nullable = false)
	private String spaceName;
	
	@Column(name = "APP_NAME",nullable = false)
	private String appName;
    
	@Column(name = "CLIENT_ID",nullable = false)
	private String clientId;
	
	@Column(name = "VERSION",nullable = false)
	private String version;

	@Column(name = "ACTIVE", nullable = false,columnDefinition = "TINYINT(1)")
	private boolean active;
	
//	@OneToMany(targetEntity = PraeceptaRuleGroupDbModel.class,cascade = CascadeType.ALL,orphanRemoval = true)
//	@JoinColumn(name="RULE_SPACE_ID")
//	private List<PraeceptaRuleGroupDbModel> praeceptaRuleGrps;
	
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

//	public List getPraeceptaRuleGrps() {
//		return praeceptaRuleGrps;
//	}
//
//	public void setPraeceptaRuleGrps(List praeceptaRuleGrps) {
//		this.praeceptaRuleGrps = praeceptaRuleGrps;
//	}

	@Override
	public String toString() {
		return "PraeceptaRuleSpaceDbModel [id=" + id + ", ruleSpaceName=" + spaceName + ", appName=" + appName
				+ ", clientId=" + clientId + ", version=" + version + ", active=" + active + "]";
	}

}
