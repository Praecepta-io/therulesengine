package io.praecepta.rules.hub.spring.sql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.rules.hub.config.condition.sql.MySqlConditionConfigSelector;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dbbased.dao.PraeceptaRuleGroupMySQLDao;
import io.praecepta.rules.hub.dbbased.dao.PraeceptaRuleSideCarsMySQLDao;
import io.praecepta.rules.hub.dbbased.dao.PraeceptaRuleSpaceMySQLDao;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateConverterDao;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateRuleGroupDao;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateRuleSideCarsDao;
import io.praecepta.rules.hub.dbbased.dao.converters.PraeceptaHibernateRuleSpaceDao;

@Configuration
@Conditional(MySqlConditionConfigSelector.class)
@Import({ PraeceptaRuleBuilderHibernateConfig.class })
public class PraeceptaRuleBuilderMySqlServiceConfig {
	
	@Bean(name = "ruleSpaceHibernateDao")
	public PraeceptaHibernateRuleSpaceDao getRuleSpaceHibernateDao() {
		return new PraeceptaHibernateRuleSpaceDao();
	}
	
	@Bean(name = "ruleGroupHibernateDao")
	public PraeceptaHibernateRuleGroupDao getRuleGroupHibernateDao() {
		return new PraeceptaHibernateRuleGroupDao();
	}
	
	@Bean(name = "ruleSideCarsHibernateDao")
	public PraeceptaHibernateRuleSideCarsDao getRuleSideCarsHibernateDao() {
		return new PraeceptaHibernateRuleSideCarsDao();
	}
	
	@Bean(name = "converterDao")
	public PraeceptaHibernateConverterDao getConverterDao(PraeceptaHibernateRuleSpaceDao ruleSpaceHibernateDao,PraeceptaHibernateRuleGroupDao ruleGroupHibernateDao,PraeceptaHibernateRuleSideCarsDao ruleSideCarsHibernateDao) {
		return new PraeceptaHibernateConverterDao(ruleSpaceHibernateDao,ruleGroupHibernateDao,ruleSideCarsHibernateDao);
	}

//	@Bean(name = "dbBasedRuleSpaceDao")
	@Bean(name = "ruleSpaceDao")
	public IPraeceptaRuleSpaceDao getRuleSpaceDao(PraeceptaHibernateConverterDao converterDao) {
		return new PraeceptaRuleSpaceMySQLDao();
	}

//	@Bean(name = "dbBasedRuleGroupDao")
	@Bean(name = "ruleGroupDao")
	public IPraeceptaRuleGroupDao getRuleGroupDao(PraeceptaHibernateConverterDao converterDao) {
		return new PraeceptaRuleGroupMySQLDao();
	}
	
	@Bean(name = "ruleSideCarDao")
	public IPraeceptaRuleSideCarsDao getRuleSideCarDao(PraeceptaHibernateConverterDao converterDao) {
		return new PraeceptaRuleSideCarsMySQLDao();
	}
}
