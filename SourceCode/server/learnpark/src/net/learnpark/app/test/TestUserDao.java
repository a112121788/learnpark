package net.learnpark.app.test;

import net.learnpark.app.dao.UserDao;
import net.learnpark.app.dao.impl.UserDaoMySqlImpl;

import org.junit.Test;

public class TestUserDao {
	@Test
	public void testsaveUser() {
		UserDao user = new UserDaoMySqlImpl();
		if (user.saveUser("kdsx2010@163.com", "20100917")) {
			System.out.print("true");
		} else {
			System.out.print("false");
		}
	}

	@Test
	public void testcheck() {
		UserDao user = new UserDaoMySqlImpl();
		int i = user.checkUser("wo", "wojj");
		System.out.print(i);
	}

	@Test
	public void testmail() {
		UserDao user = new UserDaoMySqlImpl();
		String i = user.getPassword("936875065@qq.com");
		boolean m = user.checkUser("936875065@qq.com");
		System.out.print(i + m);
	}

	@Test
	public void testmodify() {
		UserDao user = new UserDaoMySqlImpl();
		if (user.saveUser("1", "20100917")) {
			System.out.print("true");
		} else {
			System.out.print("false");
		}
		;
	}

	@Test
	public void testgetUserInfo() {
		UserDao user = new UserDaoMySqlImpl();
		System.out.println(user.getUserInfo("a112121788@163.com", "123456").getMajor());
	}
}
