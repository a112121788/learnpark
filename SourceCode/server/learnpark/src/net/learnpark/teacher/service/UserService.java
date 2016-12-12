package net.learnpark.teacher.service;

import net.learnpark.teacher.entity.User;


public interface UserService {
	/**
	 * 用户登陆
	 * @param username
	 * @param password需要加密
	 * @return 登陆失败返回null
	 */
	User login(String username, String password);
	/**
	 * 设置新密码
	 * @param username
	 * @param key
	 * @param newPassword
	 * @return
	 */
	User newPassword(String username,String userkey,String newPassword);
	/**
	 * 给用户邮箱发送找回密码时的连接,
	 * @param username
	 * @param userkey
	 * @return
	 *注 发送的同时把userkey放到数据库里面
	 */
	public boolean sendUserKey(String username,String userkey);
}
