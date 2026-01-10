package io.praecepta.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

/**
 * Spring Boot Application for Praecepta Rule Group API
 * Replaces the old Spark-based launcher
 * 
 * Note: DataSource and JPA auto-configuration are excluded because the application
 * configures database connections programmatically based on runtime configuration.
 */
@SpringBootApplication(
	scanBasePackages = {"io.praecepta.rest.api"},
	exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)
public class PraeceptaRuleGroupApiApplication {

public static void main(String[] args) {
// Load rule knowledge before starting Spring Boot
System.setProperty("contextPath", "io.praecepta.rest.api.config.RulesConfiguration");

String ruleKnowledge = System.getProperty("praecepta.rule.load.props.location", "praecepta-rule-load.properties");

PraeceptaRuleBuilder.buildWithPropsClasspath(ruleKnowledge).buildAll();

// Start Spring Boot application
ConfigurableApplicationContext springContext = SpringApplication.run(PraeceptaRuleGroupApiApplication.class, args);

// Store the spring context for backward compatibility
// Note: This may need adjustment based on how PraeceptaPivotalRulesHubContextHolder works
}
}
