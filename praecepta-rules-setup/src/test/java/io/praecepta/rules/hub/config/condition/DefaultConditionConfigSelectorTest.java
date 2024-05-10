package io.praecepta.rules.hub.config.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;

public class DefaultConditionConfigSelectorTest {
	
	private final static Logger logger = LoggerFactory.getLogger(DefaultConditionConfigSelectorTest.class);

	@Test
	public void testWithMatchingPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultConditionConfigSelector() {

			@Override
			protected RULE_SET_UP_PERSISTANT_TYPE getPersistantType() {
				return RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM;
			}
			
		};
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertTrue(selector.matches(new ConditionContextImpl(), null));
	}

	@Test
	public void testWithInvalidPersistantType() {
		DefaultConditionConfigSelector selector = new DefaultConditionConfigSelector() {
			
			@Override
			protected RULE_SET_UP_PERSISTANT_TYPE getPersistantType() {
				return RULE_SET_UP_PERSISTANT_TYPE.FILE_SYSTEM;
			}
			
		};
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name());
		
		logger.info("Selector Match Output --> {}", selector.matches(new ConditionContextImpl(), null));
		assertFalse(selector.matches(new ConditionContextImpl(), null));
		
	}
	
	public static class ConditionContextImpl implements ConditionContext {

		private Environment env;
		
		public ConditionContextImpl() {
			this.env = new StandardEnvironment();
		}
		
		@Override
		public BeanDefinitionRegistry getRegistry() {
			return null;
		}

		@Override
		public ConfigurableListableBeanFactory getBeanFactory() {
			return null;
		}

		@Override
		public Environment getEnvironment() {
			return env;
		}

		@Override
		public ResourceLoader getResourceLoader() {
			return null;
		}

		@Override
		public ClassLoader getClassLoader() {
			return null;
		}
		
		
	}

}
