package io.praecepta.rules.model.filter;

import java.io.Serializable;

/**
 * 
 * @author rajasrikar
 *
 *{"multiCondition":{"condition":{"subjectName":"n1Attr1","conditionOperator":"EQUAL_CHARS","conditionValueHolder":{"fromValue":"Raja","toValue":"Srikar"}},"nextConditionJoinOperator":"OR","nextCondition":{"condition":{"subjectName":"n1Attr2","conditionOperator":"LESS_THAN_NUMBER","conditionValueHolder":{"fromValue":10,"toValue":20}},"nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"n1Attr3","conditionOperator":"NOT_EMPTY","conditionValueHolder":{"fromValue":"Rao"}},"nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"n1Attr4","conditionOperator":"MATCHING_COLLECTION","conditionValueHolder":{"fromValue":"[2, 3,4]","toValue":"[2, 3,4]"}}}}}},"nextConditionJoinOperator":"AND","nextMultiCondition":{"multiCondition":{"condition":{"subjectName":"n2Attr1","conditionOperator":"GREATER_THAN_DATE","conditionValueHolder":{"fromValue":"10-MAR-2021","toValue":"09-MAY-2022"},"comparingFormat":"dd-MMM-yyyy"},"nextConditionJoinOperator":"OR","nextCondition":{"condition":{"subjectName":"n2Attr2","conditionOperator":"NOT_MATCHING_COLLECTION","conditionValueHolder":{"fromValue":"[2, 3,4]","toValue":"[11, 100]"}},"nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"n2Attr3","conditionOperator":"EMPTY","conditionValueHolder":{}}}}},"nextConditionJoinOperator":"OR","nextMultiCondition":{"multiCondition":{"condition":{"subjectName":"n3Attr1","conditionOperator":"EQUAL_DATE","conditionValueHolder":{"fromValue":"10-17-2021","toValue":"06-23-2022"},"comparingFormat":"MM-dd-yyyy"},"nextConditionJoinOperator":"AND","nextCondition":{"condition":{"subjectName":"n3Attr2","conditionOperator":"EQUAL_NUMBER","conditionValueHolder":{"fromValue":10,"toValue":10}},"nextConditionJoinOperator":"OR","nextCondition":{"condition":{"subjectName":"n3Attr3","conditionOperator":"EQUAL_CHARS","conditionValueHolder":{"fromValue":"XYZ","toValue":"XYZ"}},"nextConditionJoinOperator":"OR","nextCondition":{"condition":{"subjectName":"n3Attr4","conditionOperator":"NOT_EQUAL_NUMBER","conditionValueHolder":{"fromValue":5,"toValue":1}}}}}}}}}
 */

public class PraeceptaMultiNestedCondition implements Serializable{
  
	private PraeceptaMultiCondition multiCondition;

	private JoinOperatorType nextConditionJoinOperator;
	
	private PraeceptaMultiNestedCondition nextMultiNestedCondition;

	public PraeceptaMultiNestedCondition() {
	}
	public PraeceptaMultiNestedCondition(PraeceptaMultiCondition multiCondition) {
		this.multiCondition = multiCondition;
	}

	public PraeceptaMultiNestedCondition(PraeceptaMultiCondition multiCondition, JoinOperatorType nextConditionJoinOperator, PraeceptaMultiNestedCondition nextMultiNestedCondition) {
		this.multiCondition = multiCondition;
		this.nextMultiNestedCondition = nextMultiNestedCondition;
		this.nextConditionJoinOperator = nextConditionJoinOperator;
	}

	public PraeceptaMultiCondition getMultiCondition() {
		return multiCondition;
	}

	public void setMultiCondition(PraeceptaMultiCondition multiCondition) {
		this.multiCondition = multiCondition;
	}

	public JoinOperatorType getNextConditionJoinOperator() {
		return nextConditionJoinOperator;
	}

	public void setNextConditionJoinOperator(JoinOperatorType nextConditionJoinOperator) {
		this.nextConditionJoinOperator = nextConditionJoinOperator;
	}

	public PraeceptaMultiNestedCondition getNextMultiNestedCondition() {
		return nextMultiNestedCondition;
	}

	public void setNextMultiNestedCondition(PraeceptaMultiNestedCondition nextMultiNestedCondition) {
		this.nextMultiNestedCondition = nextMultiNestedCondition;
	}
	
	public void setNextMultiNestedConditionInfo(JoinOperatorType nextConditionJoinOperator, PraeceptaMultiNestedCondition nextCondition) {
		setNextConditionJoinOperator(nextConditionJoinOperator);
		setNextMultiNestedCondition(nextCondition);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((multiCondition == null) ? 0 : multiCondition.hashCode());
		result = prime * result + ((nextConditionJoinOperator == null) ? 0 : nextConditionJoinOperator.hashCode());
		result = prime * result + ((nextMultiNestedCondition == null) ? 0 : nextMultiNestedCondition.hashCode());
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
		PraeceptaMultiNestedCondition other = (PraeceptaMultiNestedCondition) obj;
		if (multiCondition == null) {
			if (other.multiCondition != null)
				return false;
		} else if (!multiCondition.equals(other.multiCondition))
			return false;
		if (nextConditionJoinOperator != other.nextConditionJoinOperator)
			return false;
		if (nextMultiNestedCondition == null) {
			if (other.nextMultiNestedCondition != null)
				return false;
		} else if (!nextMultiNestedCondition.equals(other.nextMultiNestedCondition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraeceptaMultiNestedCondition [multiCondition=" + multiCondition + ", nextConditionJoinOperator="
				+ nextConditionJoinOperator + ", nextMultiCondition=" + nextMultiNestedCondition + "]";
	}

}

