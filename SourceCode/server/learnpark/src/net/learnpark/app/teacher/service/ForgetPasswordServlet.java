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
 * 忘记密码的处理
 * 
 * @author 陆礼祥 张晓午
 * 
 */
public class ForgetPasswordServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3153969995698176462L;

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
		// String username = request.getParameter("mail");
		String username = new String(URLDecoder.decode(
				request.getParameter("mail"), "utf-8"));
		System.out.println("mail:" + username);
		UserDao user = new UserDaoMySqlImpl();
		boolean bl = user.checkUser(username);
		if (bl) {
			// 发送邮件
			boolean i = SendMail.send(username, "找回密码","你的学苑密码："+user.getPassword(username));
			out.print(i);
		} else {
			// 发送信息：邮箱用户不存在
			out.print("false");
		}
	}
}
