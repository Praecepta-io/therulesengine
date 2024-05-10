package io.praecepta.rules.model.filter;

import org.junit.Test;

public class PraeceptaSimpleConditionTest {

	@Test
	public void test() {
		PraeceptaSimpleCondition condition = null;
		
		// Get LHS Value from Attribute in the Json and RHS Value from Attribute in the Json that can Match
		condition = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, "emp.company", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		// Get LHS Value from Attribute in the Json and RHS as a Constant Value that doesn't Match
		condition = new PraeceptaSimpleCondition("emp.name", null, ConditionOperatorType.EQUAL_CHARS, null, "Srikar");
		
//		buildConditionValueHolder(condition, ruleStore);
		
		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that can Match
		condition = new PraeceptaSimpleCondition(null, "Raja", ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that doesn't Match
		condition = new PraeceptaSimpleCondition(null, "Srikar", ConditionOperatorType.EQUAL_CHARS, "emp.name", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
		// Get LHS Value from Constant and RHS Value from Constant that can Match
		condition = new PraeceptaSimpleCondition(null, "XYZ", ConditionOperatorType.EQUAL_CHARS, null, "XYZ");
		
//		buildConditionValueHolder(condition, ruleStore);
		
		// Get LHS Value from Constant and RHS as a Constant Value from an Attribute in the Json that doesn't Match
		condition = new PraeceptaSimpleCondition(null, 10, ConditionOperatorType.EQUAL_NUMBER, "emp.id", null);
		
//		buildConditionValueHolder(condition, ruleStore);
		
	}

}
