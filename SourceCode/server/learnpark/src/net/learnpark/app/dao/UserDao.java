package net.learnpark.app.dao;

import java.util.List;

import net.learnpark.app.entity.Exam;
import net.learnpark.app.entity.Plan;
import net.learnpark.app.entity.User;

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
	 * 
	 * @param username
	 * @return 如果不存在返回null
	 */

	public String getPassword(String username);

	/**
	 * 根据账户（邮箱）和密码返回用户的全部信息
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUserInfo(String username, String password);
	
	/**
	 * 
	 * @param username
	 * @param name
	 * @param user_number
	 * @param user_school
	 * @param user_grade
	 * @param user_major
	 * @param user_userclass
	 * @return
	 */
	public boolean modifyUserInfo(String username, String name,
			String user_number, String user_school, String user_grade,
			String user_major, String user_userclass);
	public void savedayPlan(String mail, String planname, int a, int b);
	/**
	 * 存储用户的考试提醒
	 * @param username
	 * @param examname
	 * @param examtime
	 * @param a
	 * @param b
	 */
	public void saveExam(String mail, String examname, String examtime,
			int a, int b);
	/**
	 * 返回用戶的”每日計劃“
	 * @param usermail
	 * @return
	 */
	public List<Plan> getPlan(String usermail);
	/**
	 * 返回用户的”考试提醒“
	 * @param usermail
	 * @return
	 */
	public List<Exam> getExam(String usermail);
	/**
	 * 每日计划和考试提醒数据插入前，清空原始数据
	 * @param i  1表示“每日计划” 2表示“考试提醒”
	 */
	public void cleanData(int i);
}
