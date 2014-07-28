package net.learnpark.app.entity;

import java.util.Date;

/**
 * 和android学生端相同
 */
public class Message {
	private int id;
	private String fromUser;
	private String toUser;
	public Message(int id, String fromUser, String toUser, String content,
			Date time) {
		super();
		this.id = id;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.time = time;
	}

	public Message(String fromUser, String toUser, String content, Date time) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.time = time;
	}

	private String content;
	private Date time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
