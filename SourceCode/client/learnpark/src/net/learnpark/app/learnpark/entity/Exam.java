package net.learnpark.app.learnpark.entity;

/**
 * course代表每一节课
 * 
 * @author peng time 2014年6月3日 11:32:30
 * @version 1
 */
public class Exam {
	private int id;//afinal框架使用
	private String name;//考试名
	private	String time;//考试时间
	private boolean isDone;// true 表示完成
	private boolean isImportant;// 计划是否重要
	private String mail;
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	public boolean isImportant() {
		return isImportant;
	}
	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}