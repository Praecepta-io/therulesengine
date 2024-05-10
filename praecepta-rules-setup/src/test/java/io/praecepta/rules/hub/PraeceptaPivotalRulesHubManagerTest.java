package io.praecepta.rules.hub;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

public class PraeceptaPivotalRulesHubManagerTest {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPivotalRulesHubManagerTest.class);

	private ApplicationContext context;
	
	private String conetxClassName;
	
	@Before
	public void setup() {	
		
		System.setProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name());
		System.setProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName(), RULE_SET_UP_PERSISTANT_SUB_TYPE.MySQL.name());
		
		System.setProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value, "SQL_DB.connection_props.MySQL");
		
		conetxClassName = System.getProperty("contextPath", "io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig");
		
		System.setProperty(getConnectionPrefix()+"db.driver", "com.mysql.jdbc.Driver");
		System.setProperty(getConnectionPrefix()+"db.url", "jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true");
		System.setProperty(getConnectionPrefix()+"db.username", "root");
		System.setProperty(getConnectionPrefix()+"db.password", "pr@ecept@");
		System.setProperty(getConnectionPrefix()+"db.dialect", "org.hibernate.dialect.MySQLDialect");
		System.setProperty(getConnectionPrefix()+"show_sql", "true");
		System.setProperty(getConnectionPrefix()+"model.packages", "io.praecepta.rules.hub.dbbased.model");
		
		Class<?> clazz;
		try {
			
			clazz = Class.forName(conetxClassName);
			context = new AnnotationConfigApplicationContext(clazz);
		
		} catch (ClassNotFoundException e) {
			logger.error(" Exception While Building the Application Context and Preparing Rules Hub Manager", e);
		}
	}
	
	
	@Test
	public void test() {
		
		IPraeceptaPivotalRulesHubManager hubManager = (IPraeceptaPivotalRulesHubManager)context.getBean("pivotalRuleHubManager");
		
		hubManager.fetchActiveUniverse();
		
		PraeceptaPivotalRulesHubContextHolder.addHubContext(context);
		PraeceptaPivotalRulesHubContextHolder.addHubManager(hubManager);
		
		PraeceptaPivotalRulesHubStore hubStore = PraeceptaPivotalRulesHubContextHolder.getHubStore();
		
		Collection<PraeceptaRuleSpace> ruleSpaces = hubStore.getAllActiveRuleSpaces();
		
		System.out.println("Active Rule Space Size --> "+ruleSpaces.size());
		
//		fail("Not yet implemented");
	}

	private String getConnectionPrefix() {
		return System.getProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value)+".";
	}
}
