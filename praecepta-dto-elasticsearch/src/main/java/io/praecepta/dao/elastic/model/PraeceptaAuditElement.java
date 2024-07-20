package io.praecepta.dao.elastic.model;

import io.praecepta.dao.elastic.enums.AUDIT_ELEMENT_TYPE;

public class PraeceptaAuditElement {
	
	private AUDIT_ELEMENT_TYPE elementType;
	
	private ValueHolder valueHolder;
	
	public PraeceptaAuditElement() {
	}
	
	public PraeceptaAuditElement(AUDIT_ELEMENT_TYPE elementType, ValueHolder valueHolder){
		this.elementType = elementType;
		this.valueHolder = valueHolder;
	}
	
	public AUDIT_ELEMENT_TYPE getElementType() {
		return elementType;
	}

	public void setElementType(AUDIT_ELEMENT_TYPE elementType) {
		this.elementType = elementType;
	}

	public ValueHolder getValueHolder() {
		return valueHolder;
	}

	public void setValueHolder(ValueHolder valueHolder) {
		this.valueHolder = valueHolder;
	}

	public static class ValueHolder {
		private String fromValue;

		private String toValue;
		
		public ValueHolder() {
		}
		
		public ValueHolder(String fromValue, String toValue) {
			this.fromValue = fromValue;
			this.toValue = toValue;
		}

		public String getFromValue() {
			return fromValue;
		}

		public void setFromValue(String fromValue) {
			this.fromValue = fromValue;
		}

		public String getToValue() {
			return toValue;
		}

		public void setToValue(String toValue) {
			this.toValue = toValue;
		}

		@Override
		public String toString() {
			return "ValueHolder [fromValue=" + fromValue + ", toValue=" + toValue + "]";
		}
		
	}

	@Override
	public String toString() {
		return "PraeceptaAuditElement [elementType=" + elementType + ", valueHolder=" + valueHolder + "]";
	}
	
	

}
