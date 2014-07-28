package net.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.learnpark.app.dao.FeedbackDao;
import net.learnpark.util.DBUtil;

public class FeedbackDaoMySqlImpl implements FeedbackDao {
	@Override
	public boolean saveFeedBack(String advise, String contactWay) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into opinion(yijian,lianxi)values(?,?)");
			pst.setString(1, advise);
			pst.setString(2, contactWay);
			int i = pst.executeUpdate();
			if (i == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}
}
