package net.learnpark.app.entity;

public class Plan {
	private int id;// 计划id afinal使用
	private boolean isDone;// true 表示完成
	private String planName;// 计划的名称
	private boolean isImportant;// 计划是否重要
	private String mail;


	public Plan(int id, String mail, String planName, boolean isImportant,
			boolean isDone) {
		super();
		this.id = id;
		this.isDone = isDone;
		this.planName = planName;
		this.isImportant = isImportant;
		this.mail = mail;
	}

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

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public boolean isImportant() {
		return isImportant;
	}

	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}

}
