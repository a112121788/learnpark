package net.learnpark.app.dao.impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.dao.CourseDao;
import net.learnpark.app.entity.Course;
import net.learnpark.util.DBUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CourseDaoMySqlImpl implements CourseDao {

	@Override
	public Boolean putCourseByGson(String username, String coursegson) {
		System.out.println(coursegson);
		Gson g = new Gson();
		Type typeOfT = new TypeToken<List<Course>>() {
		}.getType();
		List<Course> coursesList = g.fromJson(coursegson, typeOfT);
		System.out.println(coursesList.size());
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		// 先清空
		try {
			pst = conn
					.prepareStatement("delete from student_courses where username = ?");
			pst.setString(1, username);

			pst.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}

		// 再插入
		for (Course course : coursesList) {
			try {
				pst = conn
						.prepareStatement("insert into student_courses (username,id,day,coursename,timebegin,timeend,cite,teacher,time)values (?,?,?,?,?,?,?,?,?)");
				pst.setString(1, username);
				pst.setInt(2, course.getId());
				pst.setInt(3, course.getDay());
				pst.setString(4, course.getCoursename() + "");
				pst.setString(5, course.getTimebegin() + "");
				pst.setString(6, course.getTimeend() + "");
				pst.setString(7, course.getCite() + "");
				pst.setString(8, course.getTeacher() + "");
				pst.setInt(9, course.getTime());
				if (1 != pst.executeUpdate()) {
					return false;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				DBUtil.close(conn, pst, null);
			}

		}
		return true;

	}

	@Override
	public List<Course> getCoursesList(String username) {
		Connection conn = DBUtil.getConn();
		List<Course> list = new ArrayList<Course>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from student_courses where username=?");
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				Course cs = new Course(rs.getInt("id"), rs.getInt("day"),
						rs.getInt("time"), rs.getString("coursename"),
						rs.getString("timebegin"), rs.getString("timeend"),
						rs.getString("cite"), rs.getString("teacher"));
				list.add(cs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;
	}

}
