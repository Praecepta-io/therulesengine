package io.praecepta.hibernate.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.hibernate.dao.UserDao;
import io.praecepta.hibernate.spring.config.HibernateConfig;

@Configuration
@Import( { HibernateConfig.class })
public class UserDaoSpringConfig {

	@Autowired
	private HibernateConfig hibernateConfig;
	
	@Bean(name = "userDao")
	public UserDao getUserDao(){
		UserDao userDao = new UserDao();
		
//		userDao.setSessionFactory(hibernateConfig.getSessionFactory().getObject());
//		userDao.
		
		return userDao;
	}
	
}
