package io.praecepta.hibernate.spring.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:${dbName:db}.properties")
@EnableTransactionManagement
public class HibernateConfig {

	@Autowired
	private Environment env;

	@Bean(name = "hibernateDataSource")
	public DataSource getDataSource() {

		String environment = System.getProperty("envStage", "local");

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty(environment + ".db.driver"));
		dataSource.setUrl(env.getProperty(environment + ".db.url"));
		dataSource.setUsername(env.getProperty(environment + ".db.username"));
		dataSource.setPassword(env.getProperty(environment + ".db.password"));

		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		String environment = System.getProperty("envStage", "local");

		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		sessionFactory.setHibernateProperties(hibernateProperties(environment));
		sessionFactory.setPackagesToScan(env.getProperty(environment + ".hibernate.model.packages"));

		return sessionFactory;
	}

	public Properties hibernateProperties(final String environment) {
		return new Properties() {
			{
				setProperty("hibernate.show_sql", env.getProperty(environment + ".hibernate.show_sql"));
//				setProperty("hibernate.hbm2ddl.auto", env.getProperty(environment + ".hibernate.hbm2ddl.auto"));
				setProperty("hibernate.dialect", env.getProperty(environment + ".hibernate.dialect"));
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
}
