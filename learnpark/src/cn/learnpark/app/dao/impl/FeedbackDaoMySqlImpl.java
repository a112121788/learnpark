package cn.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.learnpark.app.dao.FeedbackDao;
import cn.learnpark.app.util.DBUtil;

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
		}
		return false;
	}
}
