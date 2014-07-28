package net.learnpark.app.teacher.entity;

public class Classes {

	private String coursename;
	private String classname;
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public Classes(String coursename, String classname) {
		super();
		this.coursename = coursename;
		this.classname = classname;
	}
	
}
