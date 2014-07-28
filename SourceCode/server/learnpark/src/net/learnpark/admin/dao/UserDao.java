package net.learnpark.admin.dao;

/**
 * 
 * @author 高帅朋
 */
public interface UserDao {

	/**
	 * 登录时检测用户信息，
	 * 
	 * @param username
	 * @param password
	 * @return true 代表有该用户 false代表无该用户
	 */
	public boolean checkUser(String username);

	/**
	 * 登录时检测用户信息，
	 * 
	 * @param username
	 * @param password
	 * @return true代表验证成功,false代表验证不成功
	 */
	public boolean checkUser(String username, String password);

	/**
	 * 根据当前登陆的用户名修改密码
	 * @param username
	 * @return true代表修改成功，false代表修改失败
	 */
	public boolean setPassword(String username,String newPassword);
	/**
	 * 查询所有用户的数量  学生端
	 * @return
	 */
	public int selectAllUserNum();
}
