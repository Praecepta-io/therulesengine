package io.praecepta.rules.model.filter;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import io.praecepta.core.helper.PraeceptaObjectHelper;
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
	
	private Object subjectValue;
	
	// EQUALS/NoT EQUALS etc
	private ConditionOperatorType conditionOperator;
	
	private ConditionValueHolder<?> conditionValueHolder;
	
	private String definedAttributeToCompare;

	private Object valueToCompare;
	
	private String comparingFormat;
	
	private PraeceptaConditionResult conditionResult;
	
	private Map<String,Object> parameters = Collections.emptyMap();
	
	private JoinOperatorType nextConditionJoinOperator;

	public PraeceptaSimpleCondition(){

	}
	public JoinOperatorType getNextConditionJoinOperator() {
		return nextConditionJoinOperator;
	}

	public void setNextConditionJoinOperator(JoinOperatorType nextConditionJoinOperator) {
		this.nextConditionJoinOperator = nextConditionJoinOperator;
	}

	private PraeceptaSimpleCondition nextCondition;
	
	public PraeceptaSimpleCondition getNextCondition() {
		return nextCondition;
	}

	public void setNextCondition(PraeceptaSimpleCondition nextCondition) {
		this.nextCondition = nextCondition;
	}
	
	public void setNextConditionInfo(JoinOperatorType nextConditionJoinOperator, PraeceptaSimpleCondition nextCondition) {
		setNextConditionJoinOperator(nextConditionJoinOperator);
		setNextCondition(nextCondition);
	}

	public PraeceptaSimpleCondition(String subjectName, Map<ConditionValueHolderType, Object> holderValues, ConditionOperatorType conditionOperator) {
		if(!PraeceptaObjectHelper.isObjectEmpty(holderValues)) {
			this.subjectName = subjectName;
			this.conditionOperator = conditionOperator;
			this.conditionValueHolder = new ConditionValueHolder<>(holderValues.get(ConditionValueHolderType.LHS_VALUE), holderValues.get(ConditionValueHolderType.RHS_VALUE));
		}
	}	
	
	public PraeceptaSimpleCondition(String subjectName, String definedAttributeToCompare, Map<ConditionValueHolderType, Object> holderValues, ConditionOperatorType conditionOperator) {
		this(subjectName, holderValues, conditionOperator);
		this.definedAttributeToCompare = definedAttributeToCompare;
	}	
	
	public PraeceptaSimpleCondition(String subjectName, Map<ConditionValueHolderType, Object> holderValues, ConditionOperatorType conditionOperator, Map<String,Object> parameters) {
		this(subjectName, holderValues, conditionOperator);
		this.parameters = parameters;
	}	
	
	public PraeceptaSimpleCondition(ConditionOperatorType conditionOperator, Map<ConditionValueHolderType, Object> holderValues) {
		if(!PraeceptaObjectHelper.isObjectEmpty(holderValues)) {
			this.conditionOperator = conditionOperator;
			this.conditionValueHolder = new ConditionValueHolder<>(holderValues.get(ConditionValueHolderType.LHS_VALUE), holderValues.get(ConditionValueHolderType.RHS_VALUE));
		}
	}	
	
	public PraeceptaSimpleCondition(ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder) {
			this.conditionOperator = conditionOperator;
			this.conditionValueHolder = conditionValueHolder;
	}	
	
	public PraeceptaSimpleCondition(ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder, Map<String,Object> parameters) {
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
		this.parameters = parameters;
	}	
	
	public PraeceptaSimpleCondition(ConditionOperatorType conditionOperator, Map<ConditionValueHolderType, Object> holderValues, Map<String,Object> parameters) {
		this(conditionOperator, holderValues);
		this.parameters = parameters;
	}	
	
	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.conditionValueHolder = conditionValueHolder;
	}	
	
	public PraeceptaSimpleCondition(String subjectName, ConditionOperatorType conditionOperator, ConditionValueHolder<?> conditionValueHolder, Map<String,Object> parameters) {
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
	
	public PraeceptaSimpleCondition(String subjectName, Object subjectValue, ConditionOperatorType conditionOperator, String definedAttributeToCompare, Object valueToCompare) {
		this.subjectName = subjectName;
		this.conditionOperator = conditionOperator;
		this.subjectValue = subjectValue;
		this.definedAttributeToCompare = definedAttributeToCompare;
		this.valueToCompare = valueToCompare;
		this.conditionValueHolder = new ConditionValueHolder<>(subjectValue, valueToCompare);
	}
	
	public PraeceptaSimpleCondition(String subjectName, Object subjectValue, ConditionOperatorType conditionOperator, String definedAttributeToCompare, Object valueToCompare, Map<String,Object> parameters) {
		this(subjectName, subjectValue, conditionOperator, definedAttributeToCompare, valueToCompare);
		this.parameters = parameters;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public Object getSubjectValue() {
		return subjectValue;
	}

	public void setSubjectValue(Object subjectValue) {
		this.subjectValue = subjectValue;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparingFormat == null) ? 0 : comparingFormat.hashCode());
		result = prime * result + ((conditionOperator == null) ? 0 : conditionOperator.hashCode());
		result = prime * result + ((conditionResult == null) ? 0 : conditionResult.hashCode());
		result = prime * result + ((conditionValueHolder == null) ? 0 : conditionValueHolder.hashCode());
		result = prime * result + ((definedAttributeToCompare == null) ? 0 : definedAttributeToCompare.hashCode());
		result = prime * result + ((nextCondition == null) ? 0 : nextCondition.hashCode());
		result = prime * result + ((nextConditionJoinOperator == null) ? 0 : nextConditionJoinOperator.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
		result = prime * result + ((subjectValue == null) ? 0 : subjectValue.hashCode());
		result = prime * result + ((valueToCompare == null) ? 0 : valueToCompare.hashCode());
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
		if (nextCondition == null) {
			if (other.nextCondition != null)
				return false;
		} else if (!nextCondition.equals(other.nextCondition))
			return false;
		if (nextConditionJoinOperator != other.nextConditionJoinOperator)
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
		if (subjectValue == null) {
			if (other.subjectValue != null)
				return false;
		} else if (!subjectValue.equals(other.subjectValue))
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
		return "PraeceptaSimpleCondition [subjectName=" + subjectName + ", subjectValue=" + subjectValue
				+ ", conditionOperator=" + conditionOperator + ", conditionValueHolder=" + conditionValueHolder
				+ ", definedAttributeToCompare=" + definedAttributeToCompare + ", valueToCompare=" + valueToCompare
				+ ", comparingFormat=" + comparingFormat + ", conditionResult=" + conditionResult + ", parameters="
				+ parameters + ", nextConditionJoinOperator=" + nextConditionJoinOperator + ", nextCondition="
				+ nextCondition + "]";
	}

	public enum ConditionValueHolderType{
		LHS_VALUE, RHS_VALUE;
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

