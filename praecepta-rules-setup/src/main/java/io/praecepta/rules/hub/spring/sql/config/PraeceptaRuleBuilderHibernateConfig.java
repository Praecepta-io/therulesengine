package io.praecepta.rules.hub.spring.sql.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.hub.config.condition.DefaultSqlConditionConfigSelector;

@Configuration
@Conditional(DefaultSqlConditionConfigSelector.class)
@EnableTransactionManagement
public class PraeceptaRuleBuilderHibernateConfig {
	
	@Autowired
	private Environment env;
    
	@Bean(name = "hibernateDataSource")
	public DataSource getDataSource() {
		
		System.out.println("Here inside Get Data Source");
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty(getConnectionPrefix()+"db.driver"));
		dataSource.setUrl(env.getProperty(getConnectionPrefix()+"db.url"));
		dataSource.setUsername(env.getProperty(getConnectionPrefix()+"db.username"));
		dataSource.setPassword(env.getProperty(getConnectionPrefix()+"db.password"));
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {

		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		Properties props=new Properties();
		props=hibernateProperties();
		sessionFactory.setHibernateProperties(hibernateProperties());
		sessionFactory.setPackagesToScan(env.getProperty(getConnectionPrefix()+"model.packages")); 
		return sessionFactory;
	}

	public Properties hibernateProperties() {
		return new Properties() {
			{
				setProperty("hibernate.show_sql", env.getProperty(getConnectionPrefix()+"show_sql"));
				setProperty("hibernate.hbm2ddl.auto", "update");
				setProperty("hibernate.dialect", env.getProperty(getConnectionPrefix()+"db.dialect"));
			}
		};
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {

		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(getSessionFactory().getObject());

		return txManager;
	}

	@Bean
	public HibernateTemplate getHibernateTemplate() {

		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		hibernateTemplate.setCheckWriteOperations(false);
		hibernateTemplate.setSessionFactory(getSessionFactory().getObject());

		return hibernateTemplate;
	}
	
	private String getConnectionPrefix() {
		return System.getProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value)+".";
	}
}
