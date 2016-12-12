package net.learnpark.app.teacher.entity;

public class QiandaoMessages {

	private int id;
	private String stunum;
	private String classname;
	private String coursename;
	private String date;
	private Boolean comeornot;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStunum() {
		return stunum;
	}

	public void setStunum(String stunum) {
		this.stunum = stunum;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getComeornot() {
		return comeornot;
	}

	public void setComeornot(Boolean comeornot) {
		this.comeornot = comeornot;
	}

	public QiandaoMessages(int id, String stunum, String classname,
			String coursename, String date, Boolean comeornot) {
		super();
		this.id = id;
		this.stunum = stunum;
		this.classname = classname;
		this.coursename = coursename;
		this.date = date;
		this.comeornot = comeornot;
	}

}
