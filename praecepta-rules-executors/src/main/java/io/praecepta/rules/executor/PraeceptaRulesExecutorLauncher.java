package io.praecepta.rules.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.executor.config.helper.PraeceptaRuleExecutorConfigBuilder;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;

public class PraeceptaRulesExecutorLauncher {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRulesExecutionManager.class);
	public static void main(String[] args) throws JsonProcessingException {

		String conetxClassName = System.getProperty("contextPath", "io.praecepta.rules.executor.spring.config.PraeceptaRulesExecutorConfig");
		Class<?> clazz;
		try {
			
			// Load Rule Spaces
			PraeceptaRuleBuilder.buildWithEnvParmPropsFile().buildAll();
			
			// Load Data Collector Props 
			PraeceptaRuleExecutorConfigBuilder.buildWithEnvParmPropsFile().buildConfigs();
			
			clazz = Class.forName(conetxClassName);
			ApplicationContext context = new AnnotationConfigApplicationContext(clazz);
			
			
			PraeceptaRulesExecutionManager rulesExecutionManager = (PraeceptaRulesExecutionManager)context.getBean("rulesExecutionManager");
			
			
			rulesExecutionManager.execute();
			
		} catch (ClassNotFoundException e) {
			logger.error(" Exception While Building the Application Context and Preparing Rules Execution Manager", e);
		}
		
	}
}
