package io.praecepta.rules.model.filter;

import java.io.Serializable;

/**
 * 
 * @author rajasrikar
 * 
 * Sample JSON :
 * {"condition":{"subjectName":"attr1","conditionOperator":"EQUAL_CHARS","conditionValueHolder":{"fromValue":"Raja","toValue":"Srikar"}},
 * "nextConditionJoinOperator":"OR","nextCondition":{"condition":{"subjectName":"attr2","conditionOperator":"LESS_THAN_NUMBER","conditionValueHolder":{"fromValue":10,"toValue":20}},
 * "nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"attr3","conditionOperator":"NOT_EMPTY","conditionValueHolder":{"fromValue":"Rao"}},"nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"attr4","conditionOperator":"MATCHING_COLLECTION","conditionValueHolder":{"fromValue":"[2, 3,4]","toValue":"[1, 2, 3,4]"}}}}}}
 *
 */
public class PraeceptaMultiCondition implements Serializable{
  
	private PraeceptaSimpleCondition condition;

	private JoinOperatorType nextConditionJoinOperator;
	
	private PraeceptaMultiCondition nextMultiCondition;

	public PraeceptaMultiCondition() {
	}
	public PraeceptaMultiCondition(PraeceptaSimpleCondition condition) {
		this.condition = condition;
	}

	public PraeceptaMultiCondition(PraeceptaSimpleCondition condition, JoinOperatorType nextConditionJoinOperator, PraeceptaMultiCondition nextMultiCondition) {
		this.condition = condition;
		this.nextMultiCondition = nextMultiCondition;
		this.nextConditionJoinOperator = nextConditionJoinOperator;
	}

	public PraeceptaSimpleCondition getCondition() {
		return condition;
	}

	public void setCondition(PraeceptaSimpleCondition condition) {
		this.condition = condition;
	}

	public JoinOperatorType getNextConditionJoinOperator() {
		return nextConditionJoinOperator;
	}

	public void setNextConditionJoinOperator(JoinOperatorType nextConditionJoinOperator) {
		this.nextConditionJoinOperator = nextConditionJoinOperator;
	}

	public PraeceptaMultiCondition getNextMultiCondition() {
		return nextMultiCondition;
	}

	public void setNextMultiCondition(PraeceptaMultiCondition nextCondition) {
		this.nextMultiCondition = nextCondition;
	}
	
	public void setNextMultiConditionInfo(JoinOperatorType nextConditionJoinOperator, PraeceptaMultiCondition nextCondition) {
		setNextConditionJoinOperator(nextConditionJoinOperator);
		setNextMultiCondition(nextCondition);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((nextMultiCondition == null) ? 0 : nextMultiCondition.hashCode());
		result = prime * result + ((nextConditionJoinOperator == null) ? 0 : nextConditionJoinOperator.hashCode());
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
		PraeceptaMultiCondition other = (PraeceptaMultiCondition) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (nextMultiCondition == null) {
			if (other.nextMultiCondition != null)
				return false;
		} else if (!nextMultiCondition.equals(other.nextMultiCondition))
			return false;
		if (nextConditionJoinOperator != other.nextConditionJoinOperator)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaMultiCondition [condition=" + condition + ", nextConditionJoinOperator="
				+ nextConditionJoinOperator + ", nextCondition=" + nextMultiCondition + "]";
	}
	
}

