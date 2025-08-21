package io.praecepta.rules.executor;

import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.APP_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.CLIENT_ID;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.RULE_GRP_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.SPACE_NAME;
import static io.praecepta.rules.engine.helper.PraeceptaRuleDetailCaptureHelper.VERSION;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.executor.config.helper.PraeceptaRuleExecutorConfigBuilder;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

public class PraeceptaRulesExecutorLauncher {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRulesExecutionManager.class);
	
	public static void main(String[] args) throws JsonProcessingException {
		System.setProperty(SPACE_NAME, "INSPIRA");
		System.setProperty(CLIENT_ID, "ABC_BANK");
		System.setProperty(APP_NAME, "RETAIL_DIVISION");
		System.setProperty(VERSION, "V1");
		System.setProperty(RULE_GRP_NAME, "CREDIT_CHECK");
		
		
		System.getProperty("contextPath", "io.praecepta.rules.executor.inspira.spring.config.PraeceptaInspiraRulesExecutorConfig");
		
		String inspiraConnectonDetails = System.getProperty("praecepta.rule.data.collector.props.location", "praecepta-inspira-oracle-data-collector.properties");
		
		String ruleKnowledge = System.getProperty("praecepta.rule.load.props.location", "praecepta-rule-load.properties");
//		String ruleKnowledge = System.getProperty("praecepta.rule.load.props.location", "praecepta-rule-load.properties");

			
			// Load Data Collector Props 
//			PraeceptaRuleExecutorConfigBuilder.buildWithPropsClasspath("praecepta-inspira-oracle-data-collector.properties").buildConfigs();
			PraeceptaRuleExecutorConfigBuilder.buildWithPropsClasspath(inspiraConnectonDetails).buildConfigs();
			
			// Load Rule Spaces
//			PraeceptaRuleBuilder.buildWithEnvParmPropsFile().buildAll();
			PraeceptaRuleBuilder.buildWithPropsClasspath(ruleKnowledge).buildAll();
			
			//clazz = Class.forName(conetxClassName);
			//ApplicationContext context = new AnnotationConfigApplicationContext(clazz);
			
			ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();
			
			scheduleRuleSpaceCacheRefresh();
			
			PraeceptaRulesExecutionManager rulesExecutionManager = (PraeceptaRulesExecutionManager)context.getBean("rulesExecutionManager");
			
			rulesExecutionManager.execute();
		
	}
	
	private static void scheduleRuleSpaceCacheRefresh() {
		
		 // Create a single-threaded scheduled executor service
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        
        IPraeceptaPivotalRulesHubManager rulesHubManager = PraeceptaPivotalRulesHubContextHolder.getHubManager();
		
        scheduler.scheduleWithFixedDelay( () -> rulesHubManager.fetchActiveUniverse() , 6, 6, TimeUnit.HOURS);
	}
}
