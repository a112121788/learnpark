package net.learnpark.app.teacher.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.teacher.dao.UserDao;
import net.learnpark.app.teacher.dao.impl.UserDaoMySqlImpl;
import net.learnpark.mail.SendMail;

/**
 * 登陆Servlet
 * 
 * @author 陆礼祥
 * 
 */
public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6869192735830819820L;

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
		// String username = request.getParameter("username");
		String password = request.getParameter("password");
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		UserDao user = new UserDaoMySqlImpl();
		if (user.checkUser(username)) {
			// 用户已经注册了
			out.print("exist");
		} else if (user.saveUser(username, password)) {
			// 把注册信息发送到用户邮箱
			String str = "你的学苑账号:" + username + "\n密码:" + password;
			SendMail.send(username, "欢迎你在学苑安家", str);
			out.print("true");
		} else {
			out.print("false");
		}
	}

}
