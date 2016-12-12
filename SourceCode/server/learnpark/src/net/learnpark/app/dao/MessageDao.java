package net.learnpark.app.dao;

import java.util.Date;
import java.util.List;

import net.learnpark.app.entity.Message;

public interface MessageDao {
	/**
	 * 获取一定数量的收到的消息，最新
	 * 
	 * @param touser
	 * @param num
	 * @return
	 */
	public List<Message> getMsgByToUsername(String touser, int num);

	/**
	 * 获取一定数量的自己发的的消息，最新
	 * 
	 * @param touser
	 * @param num
	 * @return
	 */
	public List<Message> getMsgByFromUsername(String fromuser, int num);

	/**
	 * 把一条消息插入到数据库中，
	 * 
	 * @param msg
	 * @return
	 */
	public boolean putMsg(String msgGson);

	public boolean putMsg(String fromuser, String toUser, String Content,
			Date time);

}
