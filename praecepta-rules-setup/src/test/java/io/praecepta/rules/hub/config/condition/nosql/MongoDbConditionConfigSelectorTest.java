package io.praecepta.rules.hub.config.condition.nosql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.hub.config.condition.DefaultConditionConfigSelector;
import io.praecepta.rules.hub.config.condition.DefaultConditionConfigSelectorTest.ConditionContextImpl;

public class MongoDbConditionConfigSelectorTest {

	private final static Logger logger = LoggerFactory.getLogger(MongoDbConditionConfigSelectorTest.class);
			
	@Test
	public void testWithMatchingPersistantTypeAndSubType() {
		
		DefaultConditionConfigSelector selector = new MongoDbConditionConfigSelector();
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.NO_SQL_DB.name());
		System.setProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_SUB_TYPE.MongoDb.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertTrue(selector.matches(new ConditionContextImpl(), null));
	}
	
	@Test
	public void testWithInvalidPersistantType() {
		
		DefaultConditionConfigSelector selector = new MongoDbConditionConfigSelector();
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM.name());
		System.setProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_SUB_TYPE.MongoDb.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertFalse(selector.matches(new ConditionContextImpl(), null));
	}
	
	@Test
	public void testWithInvalidPersistantSubType() {
		
		DefaultConditionConfigSelector selector = new MongoDbConditionConfigSelector();
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.NO_SQL_DB.name());
		System.setProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_SUB_TYPE.Cassandra.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertFalse(selector.matches(new ConditionContextImpl(), null));
	}

}
