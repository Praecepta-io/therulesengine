package io.praecepta.rules.hub.config.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultConditionConfigSelectorTest.ConditionContextImpl;

public class DefaultFileConditionConfigSelectorTest {
	
	private final static Logger logger = LoggerFactory.getLogger(DefaultFileConditionConfigSelectorTest.class);

	@Test
	public void testWithMatchingPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultFileConditionConfigSelector();
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertTrue(selector.matches(new ConditionContextImpl(), null));
	}

	@Test
//	@Test(expected=IllegalArgumentException.class)
	public void testWithInvalidPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultFileConditionConfigSelector();
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertFalse(selector.matches(new ConditionContextImpl(), null));
		
	}

}
