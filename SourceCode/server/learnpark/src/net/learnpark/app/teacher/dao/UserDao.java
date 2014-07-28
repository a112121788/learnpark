package net.learnpark.app.teacher.dao;

import java.util.List;

import net.learnpark.app.teacher.entity.Classes;
import net.learnpark.app.teacher.entity.Course;
import net.learnpark.app.teacher.entity.QiandaoMessages;
import net.learnpark.app.teacher.entity.Students;
import net.learnpark.app.teacher.entity.User;

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
	public boolean modifyUserInfo(String username, String usermail, String userschool,String userdepartment);
	
	/**
	 * 根据账户（邮箱）和密码返回用户的全部信息
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUserInfo(String username, String password);
	
	
	/**
	 * 根据老师用户名获得老师的id
	 * @param teachername
	 * @return 老师的id
	 */
	public int GetTeacher_id(String teachername);
	
	/**
	 * 根据老师的id获得老师的学生表
	 * @param teacher_id
	 * @return 老师的学生表
	 */
	public List<Classes> GetClassList(int teacher_id);
	
	/**
	 * 根据请求的学生表获得学生信息
	 * @param teacher_id
	 * @param classname
	 * @return 学生信息
	 */
	public List<Students> GetStudentsList(int teacher_id,String classname);
	
	//TODO
	/**
	 * 根据老师的id将上传上来的签到信息存入数据库中,如果数据库中已经存在该学生该条信息则覆盖掉，这样也存在一些问题
	 * 比如，当该学生一天之内上了同一门课程，好几节的时候，就只能保存一次信息了，后期会优化好的
	 * @param teacher_id
	 * @param qiandao
	 */
	public Boolean PutQingdaoIntoSql(int teacher_id,String qiandao);
	
	/**
	 * 根据老师的id获取数据库中的老师的点名信息
	 * @param teacher_id
	 * @return
	 */
	public List<QiandaoMessages> GetQiandaoMessages(int teacher_id);
	
	/**
	 * 将老师上传上来的课程表信息存入数据库中
	 * @param teacher_id
	 * @param courses 课程表的字符串信息
	 * @return
	 */
	public Boolean PutCoursesIntoSql(int teacher_id,String courses);
	
	/**
	 * 根据提供的老师的id获得老师的课程表信息
	 * @param teacher_id
	 * @return 返回老师的课程表信息
	 */
	public List<Course> GetCoursesList(int teacher_id);
}
