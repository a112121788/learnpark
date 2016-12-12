package net.learnpark.teacher.test;

import net.learnpark.teacher.service.UserService;
import net.learnpark.teacher.service.impl.UserServiceImpl;

import org.junit.Test;

public class TestUserServiceMySqlImpl {
	@Test
	public void testlogin(){
		
	}
	@Test
	public void testsetNewPassword(){
		UserService userService=new UserServiceImpl();
		userService.newPassword("840964310@qq.com", "asdasd", "pengpeng");
		
		
	}
}
