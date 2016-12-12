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
import net.learnpark.app.teacher.entity.User;

import com.google.gson.Gson;

/**
 * 从服务端端获取用户资料
 * 
 * @author peng
 * 
 */
public class GetUserInfoServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4697054430779263826L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String password = request.getParameter("password");
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		UserDao user = new UserDaoMySqlImpl();
		if ("".equals(username) || "".equals(password)) {
			out.print("error");
		} else if (user.checkUser(username, password) == 1) {
			User userinfo = user.getUserInfo(username, password);
			Gson userinfo_gson = new Gson();
			String str = userinfo_gson.toJson(userinfo, User.class);
			out.print(str);
		}
	}
}
