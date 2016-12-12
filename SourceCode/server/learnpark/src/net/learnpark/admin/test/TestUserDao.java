package net.learnpark.admin.test;

import net.learnpark.admin.dao.UserDao;
import net.learnpark.admin.dao.impl.UserDaoMySqlImpl;
import net.learnpark.admin.entity.User;

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
		User user = new User("peng", "123123");
		UserDao userDao = new UserDaoMySqlImpl();
		userDao.setPassword(user.getName(), "123456");
	}

	@Test
	public void selectAllUseNum() {
		UserDao userDao = new UserDaoMySqlImpl();
		System.out.println(userDao.selectAllUserNum());
	}


}
