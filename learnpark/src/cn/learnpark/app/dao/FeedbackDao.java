package cn.learnpark.app.dao;

public interface FeedbackDao {
	/**
	 * 用户意见反馈 保存到数据库
	 * @param advise	 意見
	 * @param contactWay 练习方式
	 * @return true 代表插入数据成功 false代表失败
	 */
	public boolean saveFeedBack(String advise, String contactWay);
}
