package net.learnpark.teacher.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.teacher.dao.UserDao;
import net.learnpark.teacher.dao.impl.UserDaoMySqlImpl;

/**
 * 设置新的密码
 * 
 * @author peng
 * 
 */
public class setNewPasswordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2572334943291157336L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String userkey = new String(URLDecoder.decode(
				request.getParameter("userkey"), "utf-8"));
		UserDao userDao = new UserDaoMySqlImpl();
		String newPassword = new String(URLDecoder.decode(
				request.getParameter("password"), "utf-8"));
		if (userDao.setNewPassword(username, userkey, newPassword)) {
			// 修改密码成功，退出当前登陆,从新登陆
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;

			request.setAttribute("message", "新密码设置成功，请<a href='" + basePath
					+ "/teacher/login.jsp' class='btn btn-success'>重新登陆</a>");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		} else {
			request.setAttribute("message", "设置失败");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
