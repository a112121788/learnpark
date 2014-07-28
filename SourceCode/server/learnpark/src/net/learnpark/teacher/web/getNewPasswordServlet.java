package net.learnpark.teacher.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.mail.SendMail;
import net.learnpark.teacher.dao.UserDao;
import net.learnpark.teacher.dao.impl.UserDaoMySqlImpl;
import net.learnpark.util.MD5Util;

/**
 * 获取密码验证的连接 发送到用户邮箱
 * 
 * @author peng
 * 
 */
public class getNewPasswordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6918382986371348645L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String userkey = MD5Util.MD5(username + "aa" + new Date().getTime());
		UserDao userDao = new UserDaoMySqlImpl();
		if (userDao.insertUserkey(username, userkey)) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;
			// System.out.println(basePath);
			String content = basePath + "/teacher/newpassword.jsp?username="
					+ username + "&userkey=" + userkey;
			SendMail.send(username, "密码找回", content);
			//
			request.setAttribute("message", "查询邮件，然后<a href='" + basePath
					+ "/teacher/login.jsp' class='btn btn-success'>重新登陆</a>");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
