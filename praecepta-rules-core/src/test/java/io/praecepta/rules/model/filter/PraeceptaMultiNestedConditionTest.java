package io.praecepta.rules.model.filter;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.model.filter.PraeceptaSimpleCondition.ConditionValueHolder;

public class PraeceptaMultiNestedConditionTest {

	@Test
	public void test() {

		// Multi Condition 1
		PraeceptaSimpleCondition n1condition1 = new PraeceptaSimpleCondition("n1Attr1", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("Raja", "Srikar"));
		PraeceptaSimpleCondition n1condition2 = new PraeceptaSimpleCondition("n1Attr2", ConditionOperatorType.LESS_THAN_NUMBER, new ConditionValueHolder<Integer>(10, 20));
		PraeceptaSimpleCondition n1condition3 = new PraeceptaSimpleCondition("n1Attr3", ConditionOperatorType.NOT_EMPTY, new ConditionValueHolder<String>("Rao", null));
		PraeceptaSimpleCondition n1condition4 = new PraeceptaSimpleCondition("n1Attr4", ConditionOperatorType.MATCHING_COLLECTION, new ConditionValueHolder<String>("[2, 3,4]", "[2, 3,4]"));
		
		
		PraeceptaMultiCondition n1condition4ToJoin = new PraeceptaMultiCondition(n1condition4);
		PraeceptaMultiCondition n1condition3ToJoin = new PraeceptaMultiCondition(n1condition3, JoinOperatorType.AND, n1condition4ToJoin);
		PraeceptaMultiCondition n1condition2ToJoin = new PraeceptaMultiCondition(n1condition2, JoinOperatorType.AND, n1condition3ToJoin);
		
		PraeceptaMultiCondition n1MultiCondition = new PraeceptaMultiCondition(n1condition1, JoinOperatorType.OR, n1condition2ToJoin);
		
		System.out.println(n1MultiCondition);
		
		// Multi Condition 2
		PraeceptaSimpleCondition n2condition1 = new PraeceptaSimpleCondition("n2Attr1", ConditionOperatorType.GREATER_THAN_DATE, new ConditionValueHolder<String>("10-MAR-2021", "09-MAY-2022"));
		n2condition1.setComparingFormat("dd-MMM-yyyy");
		
		PraeceptaSimpleCondition n2condition2 = new PraeceptaSimpleCondition("n2Attr2", ConditionOperatorType.NOT_MATCHING_COLLECTION, new ConditionValueHolder<String>("[2, 3,4]", "[11, 100]"));
		PraeceptaSimpleCondition n2condition3 = new PraeceptaSimpleCondition("n2Attr3", ConditionOperatorType.EMPTY, new ConditionValueHolder<String>(null, null));
		
		
		PraeceptaMultiCondition n2condition3ToJoin = new PraeceptaMultiCondition(n2condition3);
		PraeceptaMultiCondition n2condition2ToJoin = new PraeceptaMultiCondition(n2condition2, JoinOperatorType.AND, n2condition3ToJoin);
		
		PraeceptaMultiCondition n2MultiCondition = new PraeceptaMultiCondition(n2condition1, JoinOperatorType.OR, n2condition2ToJoin);
		
		System.out.println(n2MultiCondition);
		
		// Multi Condition 3
		PraeceptaSimpleCondition n3condition1 = new PraeceptaSimpleCondition("n3Attr1", ConditionOperatorType.EQUAL_DATE, new ConditionValueHolder<String>("10-17-2021", "06-23-2022"));
		n3condition1.setComparingFormat("MM-dd-yyyy");
		
		PraeceptaSimpleCondition n3condition2 = new PraeceptaSimpleCondition("n3Attr2", ConditionOperatorType.EQUAL_NUMBER, new ConditionValueHolder<Integer>(10, 10));
		PraeceptaSimpleCondition n3condition3 = new PraeceptaSimpleCondition("n3Attr3", ConditionOperatorType.EQUAL_CHARS, new ConditionValueHolder<String>("XYZ", "XYZ"));
		PraeceptaSimpleCondition n3condition4 = new PraeceptaSimpleCondition("n3Attr4", ConditionOperatorType.NOT_EQUAL_NUMBER, new ConditionValueHolder<Integer>(5, 1));
		
		
		PraeceptaMultiCondition n3condition4ToJoin = new PraeceptaMultiCondition(n3condition4);
		PraeceptaMultiCondition n3condition3ToJoin = new PraeceptaMultiCondition(n3condition3, JoinOperatorType.OR, n3condition4ToJoin);
		PraeceptaMultiCondition n3condition2ToJoin = new PraeceptaMultiCondition(n3condition2, JoinOperatorType.OR, n3condition3ToJoin);
		
		PraeceptaMultiCondition n3MultiCondition = new PraeceptaMultiCondition(n3condition1, JoinOperatorType.AND, n3condition2ToJoin);
		
		System.out.println(n3MultiCondition);
		
		
		PraeceptaMultiNestedCondition n3NestedMultCondition = new PraeceptaMultiNestedCondition(n3MultiCondition);
		PraeceptaMultiNestedCondition n2NestedMultCondition = new PraeceptaMultiNestedCondition(n2MultiCondition, JoinOperatorType.OR, n3NestedMultCondition);
		PraeceptaMultiNestedCondition finalNestedMultCondition = new PraeceptaMultiNestedCondition(n1MultiCondition, JoinOperatorType.AND, n2NestedMultCondition);

		System.out.println(" In JSON --> "+GsonHelper.toJson(finalNestedMultCondition));
	}
}
