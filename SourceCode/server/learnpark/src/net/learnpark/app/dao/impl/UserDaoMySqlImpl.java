package net.learnpark.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.dao.UserDao;
import net.learnpark.app.entity.Exam;
import net.learnpark.app.entity.Plan;
import net.learnpark.app.entity.User;
import net.learnpark.util.DBUtil;

public class UserDaoMySqlImpl implements UserDao {
	@Override
	public boolean saveUser(String um, String pw) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {
			pst = conn
					.prepareStatement("insert into student_user(username,password)values(?,?)");
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
			pst = conn
					.prepareStatement("select*from student_user where username=?");
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
			pst = conn
					.prepareStatement("select * from student_user where username=?");
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
			pst = conn
					.prepareStatement("select*from student_user where username=?");
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
	public User getUserInfo(String username, String password) {
		int a = checkUser(username, password);
		if (a == 1) {
			Connection conn = DBUtil.getConn();
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = conn
						.prepareStatement("select*from student_user where username=?");
				pst.setString(1, username);
				rs = pst.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String school = rs.getString("school");
					String number = rs.getString("number");
					String sex = rs.getString("sex");
					String grade = rs.getString("grade");
					String major = rs.getString("major");
					String userclass = rs.getString("userclass");
					return new User(id, username, password, name, school,
							number, grade, major, userclass, sex);
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

	@Override
	public boolean modifyUserInfo(String username, String name,
			String user_number, String user_school, String user_grade,
			String user_major, String user_userclass) {

		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;

		try {

			pst = conn
					.prepareStatement("update student_user set name=? ,number=? ,school=? ,grade=? ,major=? ,userclass=? where username=?");
			pst.setString(1, name);
			pst.setString(2, user_number);
			pst.setString(3, user_school);
			pst.setString(4, user_grade);
			pst.setString(5, user_major);
			pst.setString(6, user_userclass);
			pst.setString(7, username);
			int m = pst.executeUpdate();
			if (m == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void savedayPlan(String mail, String planname, int a, int b) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into student_day_plan(mail,planName,isDone,isImportant)values(?,?,?,?)");
			pst.setString(1, mail);
			pst.setString(2, planname);
			pst.setInt(3, a);
			pst.setInt(4, b);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
	}

	@Override
	public void saveExam(String mail, String examname, String examtime, int a,
			int b) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		try {
			pst = conn
					.prepareStatement("insert into student_exam_plan(mail,examname,examtime,isDone,isImportant)values(?,?,?,?,?)");
			pst.setString(1, mail);
			pst.setString(2, examname);
			pst.setString(3, examtime);
			pst.setInt(4, a);
			pst.setInt(5, b);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, null);
		}
	}

	@Override
	public List<Plan> getPlan(String usermail) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet resu = null;
		List<Plan> list = new ArrayList<Plan>();
		try {
			pst = conn
					.prepareStatement("select * from student_day_plan where mail=?");
			pst.setString(1, usermail);
			resu = pst.executeQuery();
			while (resu.next()) {
				// mail,planName,isDone,isImportant
				int id = resu.getInt("id");
				String mail = resu.getString("mail");
				String planname = resu.getString("planname");
				boolean a, b;
				if (resu.getInt("isDone") == 1) {
					a = true;
				} else {
					a = false;
				}
				if (resu.getInt("isImportant") == 1) {
					b = true;
				} else {
					b = false;
				}
				Plan plan = new Plan(id, mail, planname, b, a);
				list.add(plan);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, resu);
		}
		return list;
	}

	@Override
	public List<Exam> getExam(String usermail) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		ResultSet resu = null;
		List<Exam> list = new ArrayList<Exam>();
		try {
			pst = conn
					.prepareStatement("select * from student_exam_plan where mail=?");
			pst.setString(1, usermail);
			resu = pst.executeQuery();
			while (resu.next()) {
				// mail,examname,examtime,isDone,isImportant
				int id = resu.getInt("id");
				String mail = resu.getString("mail");
				String examname = resu.getString("examname");
				String examtime = resu.getString("examtime");
				boolean a, b;
				if (resu.getInt("isDone") == 1) {
					a = true;
				} else {
					a = false;
				}
				if (resu.getInt("isImportant") == 1) {
					b = true;
				} else {
					b = false;
				}
				Exam exam = new Exam(id, mail, examname, examtime, a, b);
				list.add(exam);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pst, resu);
		}
		return list;
	}

	@Override
	public void cleanData(int i) {
		Connection conn = DBUtil.getConn();
		PreparedStatement pst = null;
		if (i==1) {
			try {
				pst = conn.prepareStatement("truncate table student_day_plan");
				pst.executeUpdate();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				DBUtil.close(conn, pst, null);
			}
		} else if (i == 2) {
			try {
				pst = conn.prepareStatement("truncate table student_exam_plan");
				pst.executeUpdate();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				DBUtil.close(conn, pst, null);
			}
		}
	}
}
