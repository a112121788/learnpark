package cn.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.learnpark.app.dao.UserDao;
import cn.learnpark.app.util.DBUtil;

public class UserDaoMySqlImpl implements UserDao {
	@Override
	public boolean saveUser(String um, String pw) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {
			pst = conn
					.prepareStatement("insert into user(username,password,mail,school,number)values(?,?,'','','')");
			pst.setString(1, um);
			pst.setString(2, pw);
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

	@Override
	public int checkUser(String um, String pw) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		String paw = null;
		try {
			pst = conn.prepareStatement("select*from user where username=?");
			pst.setString(1, um);
			rs = pst.executeQuery();
			while (rs.next()) {
				paw = rs.getString("password");
			}
			if (pw.equals(paw)) {
				return 1;
			} else {
				return 2;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean checkUser(String um) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet i = null;
		try {
			pst = conn.prepareStatement("select * from user where username=?");
			pst.setString(1, um);
			i = pst.executeQuery();
			if (i.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	

	@Override
	public String getPassword(String um) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		String paw = null;
		try {
			pst = conn.prepareStatement("select*from user where username=?");
			pst.setString(1, um);
			rs = pst.executeQuery();
			while (rs.next()) {
				paw = rs.getString("password");
				return paw;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean modifyUserInfo(String type, String usermail, String info) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {
			if ("1".equals(type)) {
				System.out.println(info + type);
				pst = conn
						.prepareStatement("update user set username=? where mail=?");
				pst.setString(1, info);
				pst.setString(2, usermail);
				int m = pst.executeUpdate();
				if (m == 1) {
					return true;
				}
			} else if ("2".equals(type)) {
				System.out.println(info + type);
				pst = conn
						.prepareStatement("update user set number=? where mail=?");
				pst.setString(1, info);
				pst.setString(2, usermail);
				int m = pst.executeUpdate();
				if (m == 1) {
					return true;
				}
			} else if ("3".equals(type)) {
				System.out.println(info + type);
				pst = conn
						.prepareStatement("update user set school=? where mail=?");
				pst.setString(1, info);
				pst.setString(2, usermail);
				int m = pst.executeUpdate();
				if (m == 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
