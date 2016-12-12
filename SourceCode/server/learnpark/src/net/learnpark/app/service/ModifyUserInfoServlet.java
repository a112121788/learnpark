package net.learnpark.app.service;

import java.io.IOException;
/**
 * 修改用户信息的的Servlet
 */
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.UserDao;
import net.learnpark.app.dao.impl.UserDaoMySqlImpl;

public class ModifyUserInfoServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4645852123097486258L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String name = new String(URLDecoder.decode(
				request.getParameter("name"), "utf-8"));
		String user_number = new String(URLDecoder.decode(
				request.getParameter("user_number"), "utf-8"));
		String user_school = new String(URLDecoder.decode(
				request.getParameter("user_school"), "utf-8"));
		String user_grade = new String(URLDecoder.decode(
				request.getParameter("user_grade"), "utf-8"));
		String user_major = new String(URLDecoder.decode(
				request.getParameter("user_major"), "utf-8"));
		String user_userclass = new String(URLDecoder.decode(
				request.getParameter("user_userclass"), "utf-8"));
		UserDao userDao = new UserDaoMySqlImpl();
		if (userDao.modifyUserInfo(username, name, user_number, user_school,
				user_grade, user_major, user_userclass)) {
			out.print("true");
		} else {
			out.print("false");
		}
	}

}
