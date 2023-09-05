package io.praecepta.hibernate.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.hibernate.service.UserService;

public class UserServiceTest {
	
	private static ApplicationContext context;
	
	private static UserService userService;

	public static void main(String[] args) {
		
		System.setProperty("local.hibernate.model.packages", "io.praecepta.dao.model");
		
		context = new AnnotationConfigApplicationContext(UserServiceSpringConfig.class);
		
		userService = (UserService) context.getBean("userService");
		
//		insertUser();
		
		deleteUser();
	}

	public static void insertUser(){
		
		userService.insertUser();
	}
	
	public static void deleteUser(){
		
		userService.deleteUser();
	}
	
}
