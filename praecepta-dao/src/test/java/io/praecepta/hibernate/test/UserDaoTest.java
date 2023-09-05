package io.praecepta.hibernate.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.dao.model.User;
import io.praecepta.hibernate.dao.UserDao;

public class UserDaoTest {
	
	private static ApplicationContext context;
	
	private static UserDao  userDao;
	
	public static void main(String[] args) {
		System.setProperty("local.hibernate.model.packages", "io.praecepta.dao.model");
		
		context = new AnnotationConfigApplicationContext(UserDaoSpringConfig.class);
		
		userDao = (UserDao) context.getBean("userDao");
		
//		insert();
		
//		fetch();
		
		delete();
		
//		fetchAndUpdate();
		
	}
	
	private static void insert(){
		
		userDao.insert(new User(null, "Rajasrikar11"));
	}
	
	private static void fetch(){
		User user = userDao.fetchById(7L);
		
		System.out.println(user);
	}
	
//	@Transactional(readOnly = false)
	private static void delete(){
		userDao.deleteById(8L);
	}
	
	private static void fetchAndUpdate(){
		User user = userDao.fetchById(8L);
		System.out.println(user);
		
		user.setUserName("1122");
		
		System.out.println(user);
		userDao.update(user);
	}

}
