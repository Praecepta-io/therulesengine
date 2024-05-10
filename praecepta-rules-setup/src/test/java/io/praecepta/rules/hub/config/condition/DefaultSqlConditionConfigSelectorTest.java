package io.praecepta.rules.hub.config.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultConditionConfigSelectorTest.ConditionContextImpl;

public class DefaultSqlConditionConfigSelectorTest {
	
	private final static Logger logger = LoggerFactory.getLogger(DefaultSqlConditionConfigSelectorTest.class);

	@Test
	public void testWithMatchingPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultSqlConditionConfigSelector();
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertTrue(selector.matches(new ConditionContextImpl(), null));
	}

	@Test
	public void testWithInvalidPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultSqlConditionConfigSelector();
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertFalse(selector.matches(new ConditionContextImpl(), null));
		
	}

}
