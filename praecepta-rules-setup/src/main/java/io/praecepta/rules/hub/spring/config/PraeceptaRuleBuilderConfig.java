package io.praecepta.rules.hub.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.spring.file.config.PraeceptaRuleBuilderFileServiceConfig;
import io.praecepta.rules.hub.spring.nosql.config.PraeceptaRuleBuilderMongoDbServiceConfig;
import io.praecepta.rules.hub.spring.sql.config.PraeceptaRuleBuilderMySqlServiceConfig;

@Configuration
@Import({ PraeceptaRuleBuilderMySqlServiceConfig.class, PraeceptaRuleBuilderMongoDbServiceConfig.class , PraeceptaRuleBuilderFileServiceConfig.class })
public class PraeceptaRuleBuilderConfig {

	@Autowired(required = false)
	private IPraeceptaRuleSpaceDao ruleSpaceDao;

	@Autowired(required = false)
	private IPraeceptaRuleGroupDao ruleGroupDao;

	@Autowired(required = false)
	private IPraeceptaRuleSideCarsDao ruleSideCarsDao;

	@Bean(name = "pivotalRuleHubManager")
	public IPraeceptaPivotalRulesHubManager getPivotalRulesHubManager() {
		return new PraeceptaPivotalRulesHubManager(ruleSpaceDao, ruleGroupDao, ruleSideCarsDao);
	}
}
