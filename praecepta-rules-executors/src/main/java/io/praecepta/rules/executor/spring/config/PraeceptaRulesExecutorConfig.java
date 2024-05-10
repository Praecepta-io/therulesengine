package io.praecepta.rules.executor.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.data.collectors.common.IPraeceptaDataCollector;
import io.praecepta.rules.engine.execution.PraeceptaBasicRuleExecutionEngine;
import io.praecepta.rules.executor.manager.PraeceptaRulesExecutionManager;
import io.praecepta.rules.executor.spring.kafka.config.PraeceptaRulesExecutorKafkaConfig;

@Configuration
@Import({ PraeceptaRulesExecutorKafkaConfig.class})
public class PraeceptaRulesExecutorConfig {
	

	@Autowired(required = false)
	private IPraeceptaDataCollector dataCollector;

	@Bean(name = "rulesExecutionEngine")
	public PraeceptaBasicRuleExecutionEngine getRuleExecutionEngine() {
		return new PraeceptaBasicRuleExecutionEngine();
	}
	
	@Bean(name = "rulesExecutionManager")
	public PraeceptaRulesExecutionManager getRulesExecutionManager(IPraeceptaDataCollector dataCollector,PraeceptaBasicRuleExecutionEngine rulesExecutionEngine) {
		return new PraeceptaRulesExecutionManager(dataCollector, rulesExecutionEngine);
	}
}
