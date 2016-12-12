package net.learnpark.admin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.learnpark.admin.dao.UserDao;
import net.learnpark.util.DBUtil;

public class UserDaoMySqlImpl implements UserDao {

	@Override
	public boolean checkUser(String username) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet i = null;
		try {
			pst = conn
					.prepareStatement("select * from admin_user where username=?");
			pst.setString(1, username);
			i = pst.executeQuery();
			if (i.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, i);
		}
		return false;
	}

	@Override
	public boolean checkUser(String username, String password) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String paw = null;
		try {
			pst = conn
					.prepareStatement("select * from admin_user where username=?");
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				paw = rs.getString("password");
			}
			if (password.equals(paw)) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return false;
	}

	@Override
	public boolean setPassword(String username, String newPassword) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		int m;
		try {
			pst = conn
					.prepareStatement("update admin_user set password=? where username=?");
			pst.setString(1, newPassword);
			pst.setString(2, username);
			m = pst.executeUpdate();
			if (m == 1) {
				return true;
			}

		} catch (SQLException e) {
			return false;
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

	@Override
	public int selectAllUserNum() {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int m = 0;
		try {
			pst = conn.prepareStatement("SELECT * FROM student_user");
			rs = pst.executeQuery();
			while (rs.next()) {
				m++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return m;
	}

}
