package net.learnpark.teacher.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.learnpark.teacher.dao.UserDao;
import net.learnpark.util.DBUtil;


public class UserDaoMySqlImpl implements UserDao {

	@Override
	public boolean checkUser(String username) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet i = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_user where username=?");
			pst.setString(1, username);
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
	public boolean checkUser(String username, String password) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		String paw = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_user where username=?");
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
					.prepareStatement("update teacher_user set password=? where username=?");
			pst.setString(1, newPassword);
			pst.setString(2, username);
			m = pst.executeUpdate();
			if (m == 1) {
				return true;
			}

		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean setNewPassword(String username, String userkey,String newPassword) {

		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		try {
			pst = conn
					.prepareStatement("select * from teacher_new_password where username=? and userkey=?");
			pst.setString(1, username);
			pst.setString(2, userkey);
			rs = pst.executeQuery();
			while(rs.next()) {
				setPassword(username, newPassword);
				//TODO 以后把该条记录删除
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean insertUserkey(String username, String userkey) {
		
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into teacher_new_password(username,userkey)values(?,?)");
			pst.setString(1, username);
			pst.setString(2, userkey);
			int i = pst.executeUpdate();
			if(i==1){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
