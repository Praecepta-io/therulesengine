package io.praecepta.hibernate.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.praecepta.dao.model.User;

@Repository
@Transactional
public class UserDao extends HibernateDao<Long, User> {

	@Override
	protected Class<User> getEntityClazz() {
		return User.class;
	}

}
