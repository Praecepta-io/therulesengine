package io.praecepta.dao.criteria.enums;

/**
 * @author PunugotiR
 *
 */
public enum FilterConditionType {
	EQUAL("="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), LIKE("%"),
	LESS_THAN_OR_EQUAL("<="), GREATER_THAN_OR_EQUAL(">="), NOT_LIKE("!%"), BETWEEEN("between");
	; 
	
	FilterConditionType(String operator){
		this.operator = operator;
	}
	
	private String operator;
}
	
