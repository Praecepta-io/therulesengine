package io.praecepta.rules.sidecars.model;

import java.io.Serializable;
import java.util.Map;

public class PraeceptaSideCarMetadata implements Serializable {
	private int order;
	private String sideCarType;
	private String type;
	private Map<String, Object> sideCarConfigs;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getSideCarType() {
		return sideCarType;
	}

	public void setSideCarType(String sideCarType) {
		this.sideCarType = sideCarType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getSideCarConfigs() {
		return sideCarConfigs;
	}

	public void setSideCarConfigs(Map<String, Object> sideCarConfigs) {
		this.sideCarConfigs = sideCarConfigs;
	}

	@Override
	public String toString() {
		return "PraeceptaSideCarMetadata [order=" + order + ", sideCarType=" + sideCarType + ", type=" + type
				+ ", sideCarConfigs=" + sideCarConfigs + "]";
	}
}
