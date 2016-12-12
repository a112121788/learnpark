package net.learnpark.admin.service.impl;

import net.learnpark.admin.dao.UserDao;
import net.learnpark.admin.dao.impl.UserDaoMySqlImpl;
import net.learnpark.admin.entity.User;
import net.learnpark.admin.service.UserService;

public class UserServiceImpl implements UserService {

	@Override
	public User login(String username, String password) {
		UserDao user = new UserDaoMySqlImpl();
		if (user.checkUser(username, password)) {
			return new User(username, password);
		}
		return null;
	}
}
