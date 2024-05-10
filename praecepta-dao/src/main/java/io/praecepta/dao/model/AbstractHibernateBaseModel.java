package io.praecepta.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.util.ObjectUtils;

import io.praecepta.core.data.intf.IBaseModel;

@MappedSuperclass
public abstract class AbstractHibernateBaseModel<KEY extends Serializable> implements IBaseModel<KEY>{

	private static final String DEFAULT_USER = "XPLORE_TECH_USER"; 
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	public void assignDefaultValues(){
		
		if(ObjectUtils.isEmpty(createdBy)){
			createdBy = DEFAULT_USER;
		}
		if(ObjectUtils.isEmpty(lastUpdatedBy)){
			lastUpdatedBy = DEFAULT_USER;
		}
		if(ObjectUtils.isEmpty(createdDate)){
			createdDate = new Date();
		}
		if(ObjectUtils.isEmpty(lastUpdatedDate)){
			lastUpdatedDate = new Date();
		}
	}
	
}
