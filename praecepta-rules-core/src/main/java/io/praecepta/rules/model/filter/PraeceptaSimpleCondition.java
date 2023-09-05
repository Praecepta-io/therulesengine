package io.praecepta.rules.model.filter;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import io.praecepta.rules.model.PraeceptaConditionResult;

/**
 * 
 * @author rajasrikar
 *
 *Example : {"subjectName":"attr1","conditionOperator":"EQUAL_CHARS","conditionValueHolder":{"fromValue":"Raja","toValue":"Srikar"}}
 */
public class PraeceptaSimpleCondition implements Serializable{

	// Attribute Name
	private String subjectName;
	
	// EQUALS/NoT EQUALS etc
	private ConditionOperatorType conditionOperator;
	
	private ConditionValueHolder<?> conditionValueHolder;
	
	private String definedAttributeToCompare;

	private Object valueToCompare;
	
	private String comparingFormat;
	
	private PraeceptaConditionResult conditionResult;
	
	private Map<String,Object> parameters = Collections.emptyMap();
	
	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
	}	
	
	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder,Map<String,Object> parameters) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
		this.parameters = parameters;
	}

	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder, String definedAttributeToCompare) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
		this.definedAttributeToCompare = definedAttributeToCompare;
	}

	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder, String definedAttributeToCompare, Object valueToCompare) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
		this.definedAttributeToCompare = definedAttributeToCompare;
		this.valueToCompare = valueToCompare;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public ConditionOperatorType getConditionOperator() {
		return conditionOperator;
	}

	public void setConditionOperator(ConditionOperatorType conditionOperator) {
		this.conditionOperator = conditionOperator;
	}

	public String getDefinedAttributeToCompare() {
		return definedAttributeToCompare;
	}

	public void setDefinedAttributeToCompare(String definedAttributeToCompare) {
		this.definedAttributeToCompare = definedAttributeToCompare;
	}

	public ConditionValueHolder<?> getConditionValueHolder() {
		return conditionValueHolder;
	}

	public void setConditionValueHolder(ConditionValueHolder<?> conditionValueHolder) {
		this.conditionValueHolder = conditionValueHolder;
	}
	
	public String getComparingFormat() {
		return comparingFormat;
	}

	public void setComparingFormat(String comparingFormat) {
		this.comparingFormat = comparingFormat;
	}

	public PraeceptaConditionResult getConditionResult() {
		return conditionResult;
	}

	public void setConditionResult(PraeceptaConditionResult conditionResult) {
		this.conditionResult = conditionResult;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Object getValueToCompare() {
		return valueToCompare;
	}

	public void setValueToCompare(Object valueToCompare) {
		this.valueToCompare = valueToCompare;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PraeceptaSimpleCondition other = (PraeceptaSimpleCondition) obj;
		if (comparingFormat == null) {
			if (other.comparingFormat != null)
				return false;
		} else if (!comparingFormat.equals(other.comparingFormat))
			return false;
		if (conditionOperator != other.conditionOperator)
			return false;
		if (conditionResult == null) {
			if (other.conditionResult != null)
				return false;
		} else if (!conditionResult.equals(other.conditionResult))
			return false;
		if (conditionValueHolder == null) {
			if (other.conditionValueHolder != null)
				return false;
		} else if (!conditionValueHolder.equals(other.conditionValueHolder))
			return false;
		if (definedAttributeToCompare == null) {
			if (other.definedAttributeToCompare != null)
				return false;
		} else if (!definedAttributeToCompare.equals(other.definedAttributeToCompare))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (subjectName == null) {
			if (other.subjectName != null)
				return false;
		} else if (!subjectName.equals(other.subjectName))
			return false;
		if (valueToCompare == null) {
			if (other.valueToCompare != null)
				return false;
		} else if (!valueToCompare.equals(other.valueToCompare))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaSimpleCondition [subjectName=" + subjectName + ", valueToCompare=" + valueToCompare+", conditionOperator=" + conditionOperator
				+ ", conditionValueHolder=" + conditionValueHolder
				+ ", definedAttributeToCompare=" + definedAttributeToCompare + ", comparingFormat=" + comparingFormat
				+ ", conditionResult=" + conditionResult + ", parameters=" + parameters + "]";
	}

	public static class ConditionValueHolder<T>{
		
		private T fromValue;
		
		private T toValue;
		
		public ConditionValueHolder(T fromValue, T toValue) {
			this.fromValue = fromValue;
			this.toValue = toValue;
		}
		
		public T getFromValue() {
			return fromValue;
		}

		public void setFromValue(T fromValue) {
			this.fromValue = fromValue;
		}

		public T getToValue() {
			return toValue;
		}

		public void setToValue(T toValue) {
			this.toValue = toValue;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fromValue == null) ? 0 : fromValue.hashCode());
			result = prime * result + ((toValue == null) ? 0 : toValue.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConditionValueHolder<T> other = (ConditionValueHolder<T>) obj;
			if (fromValue == null) {
				if (other.fromValue != null)
					return false;
			} else if (!fromValue.equals(other.fromValue))
				return false;
			if (toValue == null) {
				if (other.toValue != null)
					return false;
			} else if (!toValue.equals(other.toValue))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "ConditionValueHolder [fromValue=" + fromValue + ", toValue=" + toValue + "]";
		}
	}
}

