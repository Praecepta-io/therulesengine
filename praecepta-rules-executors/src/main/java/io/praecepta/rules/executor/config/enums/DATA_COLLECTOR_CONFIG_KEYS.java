package io.praecepta.rules.executor.config.enums;

public enum DATA_COLLECTOR_CONFIG_KEYS {
	DATA_COLLECTOR_TYPE("data_collector_type"), CONNECTION_PROPS("connection_props"), CONN_PROPS_PREFIX("conn_prefix");

	public final String value;

	DATA_COLLECTOR_CONFIG_KEYS(String value) {
		this.value = value;
	}
}
