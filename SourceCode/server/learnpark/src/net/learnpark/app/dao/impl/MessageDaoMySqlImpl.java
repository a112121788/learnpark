package net.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.learnpark.app.dao.MessageDao;
import net.learnpark.app.entity.Message;
import net.learnpark.util.DBUtil;

import com.google.gson.Gson;

public class MessageDaoMySqlImpl implements MessageDao {

	@Override
	public List<Message> getMsgByToUsername(String touser, int num) {

		Connection conn = DBUtil.getConn();
		List<Message> list = new ArrayList<Message>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from message where touser=? order by id  limit ?");
			pst.setString(1, touser);
			pst.setInt(2, num);
			rs = pst.executeQuery();
			while (rs.next()) {
				Message message = new Message(rs.getInt("id"),
						rs.getString("fromuser"), rs.getString("touser"),
						rs.getString("content"), new Date(rs.getTimestamp(
								"time").getTime()));
				list.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;
	}

	@Override
	public boolean putMsg(String msgGson) {
		Message message = new Gson().fromJson(msgGson, Message.class);
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into message (fromuser,touser,content,time)values (?,?,?,?)");
			pst.setString(1, message.getFromUser());
			pst.setString(2, message.getToUser());
			pst.setString(3, message.getContent());
			pst.setTimestamp(4, new Timestamp(message.getTime().getTime()));
			if (1 != pst.executeUpdate()) {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<Message> getMsgByFromUsername(String fromuser, int num) {
		Connection conn = DBUtil.getConn();
		List<Message> list = new ArrayList<Message>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from message where fromuser=? order by id  limit ?");
			pst.setString(1, fromuser);
			pst.setInt(2, num);
			rs = pst.executeQuery();
			while (rs.next()) {
				Message message = new Message(rs.getInt("id"),
						rs.getString("fromuser"), rs.getString("touser"),
						rs.getString("content"), new Date(rs.getTimestamp(
								"time").getTime()));
				list.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;
	}

	@Override
	public boolean putMsg(String fromUser, String toUser, String content,
			Date time) {
		Message message = new Message(fromUser, toUser, content, time);
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into message (fromuser,touser,content,time)values (?,?,?,?)");
			pst.setString(1, message.getFromUser());
			pst.setString(2, message.getToUser());
			pst.setString(3, message.getContent());
			pst.setTimestamp(4, new Timestamp(message.getTime().getTime()));
			if (1 != pst.executeUpdate()) {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
