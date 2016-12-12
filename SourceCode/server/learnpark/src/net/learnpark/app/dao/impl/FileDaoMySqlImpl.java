package net.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.learnpark.app.dao.FileDao;
import net.learnpark.app.entity.File;
import net.learnpark.util.DBUtil;

public class FileDaoMySqlImpl implements FileDao {

	@Override
	public List<File> getFilesByTeacher(String teacherName) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		List<File> list = new ArrayList<File>();
		try {
			pst = conn.prepareStatement("select*from file where teachername=?");
			pst.setString(1, teacherName);
			rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String url = rs.getString("url");
				String teachername = rs.getString("teachername");
				File video = new File(id, name, url, teachername);
				list.add(video);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<File> getFiles(int num) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		List<File> list = new ArrayList<File>();
		try {
			pst = conn
					.prepareStatement("select*from video order by id desc limit ?");
			pst.setInt(1, num);
			rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String url = rs.getString("url");
				String teachername = rs.getString("teachername");
				File video = new File(id, name, url, teachername);
				list.add(video);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<File> getFilesCommon() {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		List<File> list = new ArrayList<File>();
		try {
			pst = conn.prepareStatement("select*from file where type=1");
			rs = pst.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String url = rs.getString("url");
				String whoname = rs.getString("whoname");
				Date time = new Date(rs.getTimestamp("time").getTime());
				String type = rs.getString("type");
				File file = new File(id, name, url, whoname, type, time);
				list.add(file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
