package net.learnpark.app.entity;

import java.util.Date;

public class File {
	private int id;
	private String name;
	private String url;
	private String whoname;
	private String type;
	private Date time;

	public File() {
		super();
	}

	public File(int id, String name, String url, String whoname, String type,
			Date time) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.whoname = whoname;
		this.type = type;
		this.time = time;
	}

	public String getWhoname() {
		return whoname;
	}

	public void setWhoname(String whoname) {
		this.whoname = whoname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getwhoname() {
		return whoname;
	}

	public void setwhoname(String whoname) {
		this.whoname = whoname;
	}

	public File(int id, String name, String url, String whoname) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.whoname = whoname;
	}

}
