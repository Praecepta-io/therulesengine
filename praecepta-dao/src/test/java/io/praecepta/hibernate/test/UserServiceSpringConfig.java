package io.praecepta.hibernate.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.hibernate.service.UserService;

@Configuration
@Import( { UserDaoSpringConfig.class })
public class UserServiceSpringConfig {
	
	@Bean(name = "userService")
	public UserService getUserService(){
		
		return new UserService();
	}
	
}
