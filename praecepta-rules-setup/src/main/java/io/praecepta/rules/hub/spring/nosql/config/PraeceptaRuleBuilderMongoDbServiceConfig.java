package io.praecepta.rules.hub.spring.nosql.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.praecepta.rules.hub.config.condition.nosql.MongoDbConditionConfigSelector;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleGroupMongoDbDao;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleSpaceMongoDbDao;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaSidecarMongoDbDao;

@Configuration
@Conditional(MongoDbConditionConfigSelector.class)
public class PraeceptaRuleBuilderMongoDbServiceConfig {

    @Autowired
    private Environment env;
    @Bean(name = "ruleSpaceDao")
    public IPraeceptaRuleSpaceDao getRuleSpaceDao() {
        Properties properties = new Properties();
        properties.put("mongodb.ip", env.getProperty("ip","localhost"));
        properties.put("mongodb.port", env.getProperty("port","27017"));
        return new PraeceptaRuleSpaceMongoDbDao(properties);
    }

    @Bean(name = "ruleGroupDao")
    public IPraeceptaRuleGroupDao getRuleGroupDao() {
        Properties properties = new Properties();
        properties.put("mongodb.ip", env.getProperty("ip","localhost"));
        properties.put("mongodb.port", env.getProperty("port","27017"));
        return new PraeceptaRuleGroupMongoDbDao(properties);
    }

    @Bean(name = "sidecarDao")
    public IPraeceptaRuleSideCarsDao getSidecarDao() {
        Properties properties = new Properties();
        properties.put("mongodb.ip", env.getProperty("ip","localhost"));
        properties.put("mongodb.port", env.getProperty("port","27017"));
        return new PraeceptaSidecarMongoDbDao(properties);
    }
}
