package net.learnpark.app.entity;
/**
 * 学生用户的实体类 
 * @author peng
 *
 */
public class User {
	private int id;
	private String username;// 用户名 邮箱
	private String password;// 密码
	private String name; // 昵称
	private String school;// 学校
	private String number;// 学号
	private String grade;// 班级
	private String wechatID;// 班级
	public String getWechatID() {
		return wechatID;
	}

	public User(int id, String username, String password, String name,
			String school, String number, String grade, String wechatID,
			String major, String userclass, String sex) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.school = school;
		this.number = number;
		this.grade = grade;
		this.wechatID = wechatID;
		this.major = major;
		this.userclass = userclass;
		this.sex = sex;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getUserclass() {
		return userclass;
	}

	public void setUserclass(String userclass) {
		this.userclass = userclass;
	}

	private String major;// 专业
	private String userclass;// 几班
	private String sex;// 相别

	public User(int id, String username, String password, String name,
			String school, String number, String grade, String major,
			String userclass, String sex) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.school = school;
		this.number = number;
		this.grade = grade;
		this.major = major;
		this.userclass = userclass;
		this.sex = sex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public User(int id, String username, String password, String name,
			String school, String number, String sex) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.school = school;
		this.number = number;
		this.sex = sex;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}

}
