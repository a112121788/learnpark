package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.CourseDao;
import net.learnpark.app.dao.impl.CourseDaoMySqlImpl;

/**
 * 上传课表
 * 
 * @author peng
 * 
 */
public class SaveCourseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8313754618926871446L;

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
		String coursegson = new String(URLDecoder.decode(
				request.getParameter("coursegson"), "utf-8"));

		System.out.println("username:" + username);
		System.out.println("coursegson:" + coursegson);

		CourseDao courseDao = new CourseDaoMySqlImpl();

		if (!username.equals("")) {
			if (

			courseDao.putCourseByGson(username, coursegson)) {
				out.print("true");
			}
		} else {
			out.print("false");
		}
	}
}
