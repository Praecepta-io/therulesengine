package io.praecepta.rules.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.executor.config.helper.PraeceptaRuleExecutorConfigBuilder;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.*;

public class PraeceptaRulesExecutorLauncher {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRulesExecutionManager.class);
	
	public static void main(String[] args) throws JsonProcessingException {
		System.setProperty(SPACE_NAME, "INSPIRA");
		System.setProperty(CLIENT_ID, "ABC_BANK");
		System.setProperty(APP_NAME, "RETAIL_DIVISION");
		System.setProperty(VERSION, "V1");
		System.setProperty(RULE_GRP_NAME, "CREDIT_CHECK");
		
		
		System.setProperty("contextPath", "io.praecepta.rules.executor.inspira.spring.config.PraeceptaInspiraRulesExecutorConfig");
		System.setProperty("praecepta.rule.data.collector.props.location", "praecepta-inspira-oracle-data-collector.properties");

			
			// Load Data Collector Props 
			PraeceptaRuleExecutorConfigBuilder.buildWithPropsClasspath("praecepta-inspira-oracle-data-collector.properties").buildConfigs();
			
			// Load Rule Spaces
			PraeceptaRuleBuilder.buildWithEnvParmPropsFile().buildAll();
			
			//clazz = Class.forName(conetxClassName);
			//ApplicationContext context = new AnnotationConfigApplicationContext(clazz);
			
			ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
			
			
			PraeceptaRulesExecutionManager rulesExecutionManager = (PraeceptaRulesExecutionManager)context.getBean("rulesExecutionManager");
			
			
			rulesExecutionManager.execute();
		
	}
}
