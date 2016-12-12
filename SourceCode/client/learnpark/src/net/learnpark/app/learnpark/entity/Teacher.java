package net.learnpark.app.learnpark.entity;

/**
 * 老师信息的实体类
 * 
 * @author peng time 2014年6月3日 11:32:30
 * @version 1
 */
public class Teacher {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTeachername() {
		return teachername;
	}
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	public String getTeacherphone() {
		return teacherphone;
	}
	public void setTeacherphone(String teacherphone) {
		this.teacherphone = teacherphone;
	}
	public String getTeacheremail() {
		return teacheremail;
	}
	public void setTeacheremail(String teacheremail) {
		this.teacheremail = teacheremail;
	}
	private int id;// afinal框架使用
	private String teachername;// 老师名
	private String teacherphone;// 老师电话
	private String  teacheremail;// 老师邮箱
}