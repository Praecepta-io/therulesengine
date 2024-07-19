package io.praecepta.dao.elastic.model;

import java.io.Serializable;
import java.util.Map;

import io.praecepta.core.data.intf.IBaseModel;

public interface IPraeceptaEsEntityModel<ID extends Serializable> extends IBaseModel<ID>{

	String[] getEntityFieldNames();
	
	Map<String, Object> getEntityFieldNameAndValue();
}
