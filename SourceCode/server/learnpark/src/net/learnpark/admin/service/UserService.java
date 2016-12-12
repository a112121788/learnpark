package net.learnpark.admin.service;

import net.learnpark.admin.entity.User;

public interface UserService {

	/**
	 * 用户登陆
	 * 注意：
	 * @param username
	 * @param password需要加密
	 * @return 登陆失败返回null
	 */
	User login(String username,String password);
}
