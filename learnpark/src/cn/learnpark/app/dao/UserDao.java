package cn.learnpark.app.dao;

/**
 * 
 * @author 陆礼祥 完善 高帅朋
 */
public interface UserDao {
	/**
	 * 把用户信息保存到数据量里面
	 * 
	 * @param username
	 * @param password
	 * @return 成功返回true
	 */
	public boolean saveUser(String username, String password);

	/**
	 * 登录时检测用户信息，
	 * 
	 * @param username
	 * @param password
	 * @return 1 代表名称和密码正确 2 代表有该用户名但是密码错误 0代表无该用户
	 */
	public int checkUser(String username, String password);

	/**
	 * 登录时检测用户信息，
	 * 
	 * @param username
	 * @param password
	 * @return true 代表有该用户 false代表无该用户
	 */
	public boolean checkUser(String username);
	/**
	 * 根据用户名获取用户密码
	 * @param username
	 * @return 如果不存在返回null
	 */
	public String getPassword(String username);
	/**
	 * 修改用户信息
	 * @param type
	 * @param usermail
	 * @param info
	 * @return
	 */
	public boolean modifyUserInfo(String type, String usermail, String info);
}
