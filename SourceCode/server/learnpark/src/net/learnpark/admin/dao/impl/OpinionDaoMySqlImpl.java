package net.learnpark.admin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.admin.dao.OpinionDao;
import net.learnpark.admin.entity.Opinion;
import net.learnpark.util.DBUtil;

public class OpinionDaoMySqlImpl implements OpinionDao {

	@Override
	public List<Opinion> selectAllOpinion() {
		List<Opinion> opinions = new ArrayList<Opinion>();
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from  opinion order by id desc");
			rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String lianxi = rs.getString("lianxi");
				String yijian = rs.getString("yijian");
				String replay = rs.getString("reply");
				Opinion opinion = new Opinion(id, lianxi, yijian, replay);
				opinions.add(opinion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return opinions;
	}

	@Override
	public boolean modifyOpinion(String lianxi, String yijian, String reply) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		int m;
		try {
			pst = conn
					.prepareStatement("update opinion set reply=? where lianxi=? and yijian=?");
			pst.setString(1, reply);
			pst.setString(2, lianxi);
			pst.setString(3, yijian);
			m = pst.executeUpdate();
			if (m == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

}
