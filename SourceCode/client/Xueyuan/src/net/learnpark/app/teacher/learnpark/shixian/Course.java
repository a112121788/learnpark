package net.learnpark.app.teacher.learnpark.shixian;

/**
 * course代表每一节课
 * 
 * @author peng time 2014年6月3日 11:32:30
 * @version 1
 */
public class Course {
	private int id;
	private int day; // 周一到周日 用 1-7表示
	private int time;//每天上课的时间
	private String coursename; // 课程名
	private String timebegin; //
	private String timeend; //
	private String cite; // 上课地点
	private String classname; // 班级名字

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {

		this.id = id;
	}

	public int getDay() {

		return day;
	}

	public void setDay(int day) {

		this.day = day;
	}

	public int getTime() {

		return time;
	}

	public void setTime(int time) {

		this.time = time;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getTimebegin() {
		return timebegin;
	}

	public void setTimebegin(String timebegin) {
		this.timebegin = timebegin;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public String getCite() {
		return cite;
	}

	public void setCite(String cite) {
		this.cite = cite;
	}
}