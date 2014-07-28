package net.learnpark.app.teacher.entity;

public class User {

	private int id;
	private String name;
	private String username;
	private String password;// 密码
	private String school;
	private String department;// 院系

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public User(int id, String name, String username, String password,
			String school, String department) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.school = school;
		this.department = department;
	}

	

}
