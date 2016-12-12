package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.CourseDao;
import net.learnpark.app.dao.impl.CourseDaoMySqlImpl;
import net.learnpark.app.entity.Course;

import com.google.gson.Gson;

/**
 * 下载课表
 * 
 * @author peng
 * 
 */
public class GetCourseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6484255923900643464L;

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
		System.out.println("username:" + username);
		CourseDao courseDao = new CourseDaoMySqlImpl();
		List<Course> courses = courseDao.getCoursesList(username);
		if (courses != null && courses.size() > 0) {
			Gson gson = new Gson();
			String coursegson = gson.toJson(courses);
			out.print(coursegson);
		} else {
			out.print("	");
		}

	}
}
