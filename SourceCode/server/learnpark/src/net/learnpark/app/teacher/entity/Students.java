package net.learnpark.app.teacher.entity;

public class Students {

	private String studentname;
	private String studentnum;
	private String sex;

	public String getStudentname() {
		return studentname;
	}
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}
	public String getStudentnum() {
		return studentnum;
	}
	public void setStudentnum(String studentnum) {
		this.studentnum = studentnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Students(String studentname, String studentnum,
			String sex) {
		super();
		this.studentname = studentname;
		this.studentnum = studentnum;
		this.sex = sex;
	}
	
	
}
