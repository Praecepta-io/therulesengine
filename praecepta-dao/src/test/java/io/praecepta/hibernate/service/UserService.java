package io.praecepta.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.praecepta.dao.model.User;
import io.praecepta.hibernate.dao.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void insertUser(){
		
		userDao.insert(new User(null, "Srikar Rao new1"));
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(){
		userDao.deleteById(7L);
	}
}
