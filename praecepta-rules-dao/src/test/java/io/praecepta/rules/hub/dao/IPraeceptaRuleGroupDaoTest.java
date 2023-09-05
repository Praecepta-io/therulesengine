package io.praecepta.rules.hub.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class IPraeceptaRuleGroupDaoTest {

	@Test
	public void testGetKeyFromRuleGroup() {
		
		PraeceptaRuleGroup ruleGrp = new PraeceptaRuleGroup("SP1", "CL1", "App1");
		
		PraeceptaRuleSpaceCompositeKey compositeKey = IPraeceptaRuleGroupDao.getKeyFromRuleGroup(ruleGrp);
		
		assertNotNull(compositeKey);
		
		System.out.println(compositeKey);
	}
}
