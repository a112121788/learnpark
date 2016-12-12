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
/**
 * 为android端提供检测用户是否存在的Servlet
 * @author 陆礼祥 张晓午
 *
 */

public class CheckServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7569693666058716767L;

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
		// String username = request.getParameter("username");//不安全的实现 参考下面的
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String password = request.getParameter("password");
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		UserDao user = new UserDaoMySqlImpl();
		if (user.checkUser(username)) {
			if (1 == user.checkUser(username, password)) {
				out.print("1");
			} else {
				out.print("2");
			}
		} else {
			out.print("0");
		}
	}

}
