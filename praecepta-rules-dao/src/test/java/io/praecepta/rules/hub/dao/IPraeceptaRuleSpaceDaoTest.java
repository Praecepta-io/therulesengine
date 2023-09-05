package io.praecepta.rules.hub.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class IPraeceptaRuleSpaceDaoTest {

	@Test
	public void testGetKeyFromRuleGroup() {
		
		PraeceptaRuleSpace ruleSpace  = new PraeceptaRuleSpace();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("SP1", "CL1", "App1");
		ruleSpace.setRuleSpaceKey(compositeKey);
//		(
		
		PraeceptaRuleSpaceCompositeKey compositeKeyFromRuleSpaxe = IPraeceptaRuleSpaceDao.getKeyFromRuleSapce(ruleSpace);
		
		assertEquals(compositeKey, compositeKeyFromRuleSpaxe);
		
		System.out.println(compositeKeyFromRuleSpaxe);
	}

}
