package net.learnpark.admin.entity;

import java.util.Date;

public class Person {
	private String name;// 私有的字段
	private int age;
	private boolean married;// 是否结婚了
	private Date birthday;
	private byte gender;

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Person() {
	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {// 读属性 get Name getter
		return name;
	}

	public void setName(String name) {// 写属性 setter
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isMarried() {// getMarried==isMarried
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

}
