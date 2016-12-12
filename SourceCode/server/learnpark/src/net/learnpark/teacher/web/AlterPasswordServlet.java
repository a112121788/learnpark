package net.learnpark.teacher.web;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.learnpark.teacher.dao.UserDao;
import net.learnpark.teacher.dao.impl.UserDaoMySqlImpl;

/**
 * 修改用户密码的Servlet
 * 
 * @author peng
 * 
 */
public class AlterPasswordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2069180859622667194L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String newPassword = new String(URLDecoder.decode(
				request.getParameter("newpassword1"), "utf-8"));
		UserDao userDao = new UserDaoMySqlImpl();
		// 这里的用户名从Session中获取
		// 存在，把USER放到session中转向主页
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		boolean isOk = userDao.setPassword(username, newPassword);
		if (isOk) {
			// 修改密码成功，退出当前登陆,从新登陆
			session.setAttribute("username", "null");
			session.setAttribute("password", "null");
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;

			request.setAttribute("message", "修改成功，请<a href="+basePath+"/teacher/login.jsp' class='btn btn-success'>重新登陆</a>");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		} else {
			request.setAttribute("message", "修改失败");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
