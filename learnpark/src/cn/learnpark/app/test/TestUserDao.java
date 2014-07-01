package cn.learnpark.app.test;

import org.junit.Test;

import cn.learnpark.app.dao.UserDao;
import cn.learnpark.app.dao.impl.UserDaoMySqlImpl;

public class TestUserDao {
	@Test
	public void testsaveUser() {
		UserDao user= new UserDaoMySqlImpl();
		if(user.saveUser("kdsx2010@163.com", "20100917")){
			System.out.print("true");
		}else{
			System.out.print("false");
		}
	}
	@Test
	public void testcheck() {
		UserDao user = new UserDaoMySqlImpl();
		int i = user.checkUser("wo","wojj");
		System.out.print(i);
	}
	@Test
	public void testmail() {
		UserDao user = new UserDaoMySqlImpl();
		String  i = user.getPassword("936875065@qq.com");
		boolean m = user.checkUser("936875065@qq.com");
		System.out.print(i+m);
	}
	@Test
	public void testmodify() {
		UserDao user = new UserDaoMySqlImpl();
		if(user.saveUser("1", "20100917")){
			System.out.print("true");
		}else{
			System.out.print("false");
		};
	}
}
