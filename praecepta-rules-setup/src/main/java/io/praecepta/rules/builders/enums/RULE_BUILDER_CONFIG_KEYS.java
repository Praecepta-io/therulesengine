package io.praecepta.rules.builders.enums;

public enum RULE_BUILDER_CONFIG_KEYS {
	RULE_LOADER_TYPE("rule_loader_type"), TYPE("type"), CONNECTION_PROPS("connection_props"), 
	RULE_SPACE_NAME("rule_space_name"), RULE_CLIENT_ID("rule_client_id"), RULE_APP_NAME("rule_app_name"), RULE_VERSION("rule_version"),
	RULE_CONNECTIOS_PREFIX("rule_conn_prefix");

	public final String value;

	RULE_BUILDER_CONFIG_KEYS(String value) {
		this.value = value;
	}
}
