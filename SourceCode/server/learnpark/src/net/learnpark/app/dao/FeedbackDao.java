package net.learnpark.app.dao;

public interface FeedbackDao {
	/**
	 * 发送反馈信息，把反馈信息存放到服务端数据库里
	 * @param advise	反馈内容
	 * @param contactWay	联系方式
	 * @return	true 代表插入成功。 false 代表插入失败
	 */
	public boolean saveFeedBack(String advise, String contactWay);
}
