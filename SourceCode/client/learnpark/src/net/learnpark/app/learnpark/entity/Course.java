package net.learnpark.app.learnpark.entity;

/**
 * course代表每一节课
 * 
 * @author peng time 2014年6月3日 11:32:30
 * @version 1
 */
public class Course {
	private int id;
	private int day; // 周一到周日 用 1-7表示
	private int time;
	private String coursename; // 课程名
	private String timebegin; //
	private String timeend; //
	private String cite; // 上课地点
	private String teacher; // 老师的名称

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

		this.day = time;
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

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public Course(int id, int day, int time, String coursename,
			String timebegin, String timeend, String cite, String teacher) {
		this.id = id;
		this.day = day;
		this.time = time;
		this.coursename = coursename;
		this.timebegin = timebegin;
		this.timeend = timeend;
		this.cite = cite;
		this.teacher = teacher;
	}

	public Course() {
	}

	public String mytoString() {
		return getCoursename() + "\n@" + getCite() + "\n" + getTimebegin()
				+ "-\n" + getTimeend() + "\n" + getTeacher();
	}

}