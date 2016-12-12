package net.learnpark.app.teacher.dao.impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.teacher.dao.UserDao;
import net.learnpark.app.teacher.entity.Classes;
import net.learnpark.app.teacher.entity.Course;
import net.learnpark.app.teacher.entity.QiandaoMessages;
import net.learnpark.app.teacher.entity.Students;
import net.learnpark.app.teacher.entity.User;
import net.learnpark.util.DBUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserDaoMySqlImpl implements UserDao {

	@Override
	public int GetTeacher_id(String teachername) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_user where username=?");

			pst.setString(1, teachername);
			rs = pst.executeQuery();
			if (rs.next()) {
				int id=rs.getInt("id");
				DBUtil.close(conn, pst, rs);
				return id;
			} else {
				DBUtil.close(conn, pst, rs);
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return 0;
	}

	@Override
	public List<Classes> GetClassList(int teacher_id) {
		Connection conn = DBUtil.getConn();
		List<Classes> list = new ArrayList<Classes>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_classes where teacher_id=?");

			pst.setInt(1, teacher_id);
			;
			rs = pst.executeQuery();
			while (rs.next()) {
				Classes cs = new Classes(rs.getString("tcourse"),
						rs.getString("tclass"));
				list.add(cs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		DBUtil.close(conn, pst, rs);
		return list;
	}

	@Override
	public List<Students> GetStudentsList(int teacher_id, String classname) {
		Connection conn = DBUtil.getConn();
		List<Students> list = new ArrayList<Students>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_students where teacher_id=? and classname=?");

			pst.setInt(1, teacher_id);
			pst.setString(2, classname);
			;
			rs = pst.executeQuery();
			while (rs.next()) {
				Students sd = new Students(rs.getString("studentname"),
						rs.getString("studentnum"), rs.getString("sex"));
				list.add(sd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;
	}

	@Override
	public Boolean PutQingdaoIntoSql(int teacher_id, String qiandaomessages) {
		System.out.println(qiandaomessages);
		List<QiandaoMessages> thisQiandaoao = GetQiandaoMessages(teacher_id);
		Gson g = new Gson();
		Type typeOfT = new TypeToken<List<QiandaoMessages>>() {
		}.getType();
		List<QiandaoMessages> QiandaoList = g
				.fromJson(qiandaomessages, typeOfT);
		System.out.println(QiandaoList.size());
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		int[] is = new int[QiandaoList.size()];// 判断是否插入成功
		for (int j = 0; j < is.length; j++) {
			is[j] = 0;
		}
		int k = 0;
		try {
			for (QiandaoMessages qiandao : QiandaoList) {
				if (thisQiandaoao != null && thisQiandaoao.size() > 0) {
					for (int i = 0; i < thisQiandaoao.size(); i++) {
						if (qiandao.getStunum().equals(
								thisQiandaoao.get(i).getStunum())
								&& qiandao.getClassname().equals(
										thisQiandaoao.get(i).getClassname())
								&& qiandao.getCoursename().equals(
										thisQiandaoao.get(i).getCoursename())
								&& qiandao.getDate().equals(
										thisQiandaoao.get(i).getDate())) {
							pst = conn
									.prepareStatement("update teacher_qiandaomessages set comeornot=? where teacher_id=? and stunum=? and classname=? and coursename=? and date=?");
							pst.setInt(2, teacher_id);
							pst.setString(3, qiandao.getStunum());
							pst.setString(4, qiandao.getClassname());
							pst.setString(5, qiandao.getCoursename());
							pst.setString(6, qiandao.getDate());
							System.out.println(teacher_id + qiandao.getStunum()
									+ qiandao.getCoursename()
									+ qiandao.getDate()
									+ qiandao.getComeornot());
							if (qiandao.getComeornot() == true) {
								pst.setInt(1, 1);
							} else {
								pst.setInt(1, 0);
							}
							is[k] = pst.executeUpdate();
							k = k + 1;
							break;
						} else if (i == thisQiandaoao.size() - 1) {
							pst = conn
									.prepareStatement("insert into teacher_qiandaomessages values (?,?,?,?,?,?)");
							pst.setInt(1, teacher_id);
							pst.setString(2, qiandao.getStunum());
							pst.setString(3, qiandao.getClassname());
							pst.setString(4, qiandao.getCoursename());
							pst.setString(5, qiandao.getDate());
							System.out.println(teacher_id + qiandao.getStunum()
									+ qiandao.getCoursename()
									+ qiandao.getDate()
									+ qiandao.getComeornot());
							if (qiandao.getComeornot() == true) {
								pst.setInt(6, 1);
							} else {
								pst.setInt(6, 0);
							}
							is[k] = pst.executeUpdate();
							k = k + 1;
							break;
						}
					}
				} else {
					pst = conn
							.prepareStatement("insert into teacher_qiandaomessages values (?,?,?,?,?,?)");
					pst.setInt(1, teacher_id);
					pst.setString(2, qiandao.getStunum());
					pst.setString(3, qiandao.getClassname());
					pst.setString(4, qiandao.getCoursename());
					pst.setString(5, qiandao.getDate());
					System.out.println(teacher_id + qiandao.getStunum()
							+ qiandao.getCoursename() + qiandao.getDate()
							+ qiandao.getComeornot());
					if (qiandao.getComeornot() == true) {
						pst.setInt(6, 1);
					} else {
						pst.setInt(6, 0);
					}
					is[k] = pst.executeUpdate();
					k = k + 1;
				}
			}
			DBUtil.close(conn, pst, null);
			for (int i = 0; i < is.length; i++) {
				if (is[i] == 0) {
					DBUtil.close(conn, pst, null);
					return false;
				}
			}
			DBUtil.close(conn, pst, null);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

	@Override
	public List<QiandaoMessages> GetQiandaoMessages(int teacher_id) {

		Connection conn = DBUtil.getConn();
		List<QiandaoMessages> list = new ArrayList<QiandaoMessages>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_qiandaomessages where teacher_id=?");

			pst.setInt(1, teacher_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				QiandaoMessages qm = new QiandaoMessages(0,
						rs.getString("stunum"), rs.getString("classname"),
						rs.getString("coursename"), rs.getString("date"), false);
				list.add(qm);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;

	}

	@Override
	public Boolean PutCoursesIntoSql(int teacher_id, String courses) {
		System.out.println(courses);
		List<Course> thisCourses = GetCoursesList(teacher_id);
		Gson g = new Gson();
		Type typeOfT = new TypeToken<List<Course>>() {
		}.getType();
		List<Course> CoursesList = g.fromJson(courses, typeOfT);
		System.out.println(CoursesList.size());
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		int[] is = new int[CoursesList.size()];// 判断是否插入成功
		for (int j = 0; j < is.length; j++) {
			is[j] = 0;
		}
		int k = 0;
		try {
			for (Course course : CoursesList) {
				if (thisCourses != null && thisCourses.size() > 0) {
					for (int i = 0; i < thisCourses.size(); i++) {
						if (course.getDay() == thisCourses.get(i).getDay()
								&& course.getTime() == thisCourses.get(i)
										.getTime()) {
							pst = conn
									.prepareStatement("update teacher_courses set coursename=?, timebegin=?,timeend=?,cite=?,classname=? where teacher_id=? and day=? and time=?");
							pst.setString(1, course.getCoursename());
							pst.setString(2, course.getTimebegin());
							pst.setString(3, course.getTimeend());
							pst.setString(4, course.getCite());
							pst.setString(5, course.getClassname());
							pst.setInt(6, teacher_id);
							pst.setInt(7, course.getDay());
							pst.setInt(8, course.getTime());

							is[k] = pst.executeUpdate();
							k = k + 1;
							break;
						} else if (i == thisCourses.size() - 1) {
							pst = conn
									.prepareStatement("insert into teacher_courses values (?,?,?,?,?,?,?,?)");
							pst.setInt(1, teacher_id);
							pst.setInt(2, course.getDay());
							pst.setInt(3, course.getTime());
							pst.setString(4, course.getCoursename());
							pst.setString(5, course.getTimebegin());
							pst.setString(6, course.getTimeend());
							pst.setString(7, course.getCite());
							pst.setString(8, course.getClassname());

							is[k] = pst.executeUpdate();
							k = k + 1;
							break;
						}
					}
				} else {
					pst = conn
							.prepareStatement("insert into teacher_courses values (?,?,?,?,?,?,?,?)");
					pst.setInt(1, teacher_id);
					pst.setInt(2, course.getDay());
					pst.setInt(3, course.getTime());
					pst.setString(4, course.getCoursename());
					pst.setString(5, course.getTimebegin());
					pst.setString(6, course.getTimeend());
					pst.setString(7, course.getCite());
					pst.setString(8, course.getClassname());

					is[k] = pst.executeUpdate();
					k = k + 1;
				}
			}
			for (int i = 0; i < is.length; i++) {
				if (is[i] == 0) {
					DBUtil.close(conn, pst, null);
				}
			}
			DBUtil.close(conn, pst, null);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;

	}

	@Override
	public List<Course> GetCoursesList(int teacher_id) {
		// TODO Auto-generated method stub

		Connection conn = DBUtil.getConn();
		List<Course> list = new ArrayList<Course>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_courses where teacher_id=?");

			pst.setInt(1, teacher_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				Course cs = new Course(teacher_id, rs.getInt("day"),
						rs.getInt("time"), rs.getString("coursename"), rs.getString("timebegin"),
						rs.getString("timeend"), rs.getString("cite"), rs.getString("classname"));
				list.add(cs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return list;
	}

	@Override
	public boolean saveUser(String username, String password) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {
			pst = conn
					.prepareStatement("insert into teacher_user(username,password,name,school,department)values(?,?,'','','')");
			pst.setString(1, username);
			pst.setString(2, password);
			int i = pst.executeUpdate();
			if (i == 1) {
				DBUtil.close(conn, pst, null);
				return true;
			} else {
				DBUtil.close(conn, pst, null);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

	@Override
	public int checkUser(String username, String password) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs;
		String paw = null;
		try {
			pst = conn
					.prepareStatement("select*from teacher_user where username=?");
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				paw = rs.getString("password");
			}
			if (password.equals(paw)) {
				DBUtil.close(conn, pst, rs);
				return 1;
			} else {
				DBUtil.close(conn, pst, rs);
				return 2;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return 0;
	}

	@Override
	public boolean checkUser(String username) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from teacher_user where username=?");
			pst.setString(1, username);
			rs = pst.executeQuery();
			if (rs.next()) {
				DBUtil.close(conn, pst, rs);
				return true;
			} else {
				DBUtil.close(conn, pst, rs);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

	@Override
	public String getPassword(String username) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String paw = null;
		try {
			pst = conn
					.prepareStatement("select*from teacher_user where username=?");
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				paw = rs.getString("password");
				DBUtil.close(conn, pst, rs);
				return paw;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, rs);
		}
		return null;
	}

	@Override
	public boolean modifyUserInfo(String username, String usermail,
			String userschool, String userdepartment) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {
			pst = conn
					.prepareStatement("update teacher_user set name=?,school=?,department=? where username=?");
			pst.setString(1, username);
			pst.setString(2, userschool);
			pst.setString(3, userdepartment);
			pst.setString(4, usermail);
			int m = pst.executeUpdate();
			if (m == 1) {
				DBUtil.close(conn, pst, null);
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
		return false;
	}

	@Override
	public User getUserInfo(String username, String password) {
		int a = checkUser(username, password);
		if (a == 1) {
			Connection conn = DBUtil.getConn();
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = conn
						.prepareStatement("select * from teacher_user where username=?");
				pst.setString(1, username);
				rs = pst.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("id");
					String school = rs.getString("school");
					String name = rs.getString("name");
					String department = rs.getString("department");
					DBUtil.close(conn, pst, rs);
					return new User(id, name, username, null, school,
							department);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(conn, pst, rs);
			}
			return null;
		}
		return null;
	}
}
