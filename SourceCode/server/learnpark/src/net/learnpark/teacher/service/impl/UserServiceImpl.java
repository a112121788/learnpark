package net.learnpark.teacher.service.impl;


import net.learnpark.teacher.dao.UserDao;
import net.learnpark.teacher.dao.impl.UserDaoMySqlImpl;
import net.learnpark.teacher.entity.User;
import net.learnpark.teacher.service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public User login(String username, String password) {
		UserDao user = new UserDaoMySqlImpl();
		if (user.checkUser(username, password)) {
			return new User(username, password);
		}
		return null;
	}

	@Override
	public User newPassword(String username, String key, String newPassword) {
		UserDao userDao=new UserDaoMySqlImpl();
		if(userDao.setNewPassword(username, key,newPassword)){
			return new User(username,newPassword);
		}
		return null;
	}

	@Override
	public boolean sendUserKey(String username,String userkey) {
		
		return false;
	}

}
