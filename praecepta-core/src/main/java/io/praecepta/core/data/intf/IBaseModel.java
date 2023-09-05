package io.praecepta.core.data.intf;

import java.io.Serializable;
import java.util.Date;

public interface IBaseModel<KEY extends Serializable> extends IModel{

	KEY getId();
	void setId(KEY id);
	void setCreatedBy(String createdBy);
	void setCreatedDate(Date createdDate);
	void setLastUpdatedBy(String updatedBy);
	void setLastUpdatedDate(Date updateDate);
	String getCreatedBy();
	String getLastUpdatedBy();
	Date getCreatedDate();
	Date getLastUpdatedDate();
}
