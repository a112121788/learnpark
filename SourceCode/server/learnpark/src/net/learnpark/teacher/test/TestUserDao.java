package net.learnpark.teacher.test;


import net.learnpark.teacher.dao.UserDao;
import net.learnpark.teacher.dao.impl.UserDaoMySqlImpl;
import net.learnpark.teacher.entity.User;

import org.junit.Test;

public class TestUserDao {
	@Test
	public void testcheckUser() {
		User user = new User("peng", "123123");
		UserDao userDao = new UserDaoMySqlImpl();
		userDao.checkUser(user.getName());
	}

	@Test
	public void testcheckUser2() {
		User user = new User("peng", "123123");
		UserDao userDao = new UserDaoMySqlImpl();
		userDao.checkUser(user.getName(), user.getPassword());
	}

	@Test
	public void testsetPassword() {
		User user = new User("840964310@qq.com", "123456");
		UserDao userDao = new UserDaoMySqlImpl();
		userDao.setPassword(user.getName(), "123456");
	}
	@Test
	public void testinserUserkey() {
		UserDao userDao = new UserDaoMySqlImpl();
		userDao.insertUserkey("840964310@qq.com", "asdasdasd");
	}

	
}
