package net.learnpark.admin.entity;
/**
 * 意见 对应数据库中的字段
 * @author peng
 *
 */
public class Opinion {
	public Opinion() {
		super();
	}
	public Opinion(int id, String lianxi, String yijian, String replay) {
		super();
		this.id = id;
		this.lianxi = lianxi;
		this.yijian = yijian;
		this.replay = replay;
	}
	private int id;
	private String lianxi;
	private String yijian;
	private String replay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLianxi() {
		return lianxi;
	}
	public void setLianxi(String lianxi) {
		this.lianxi = lianxi;
	}
	public String getYijian() {
		return yijian;
	}
	public void setYijian(String yijian) {
		this.yijian = yijian;
	}
	public String getReplay() {
		return replay;
	}
	public void setReplay(String replay) {
		this.replay = replay;
	}
}
