package net.learnpark.app.teacher.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.teacher.dao.UserDao;
import net.learnpark.app.teacher.dao.impl.UserDaoMySqlImpl;
import net.learnpark.app.teacher.entity.Classes;

import com.google.gson.Gson;

/**
 * 获得教师的班级表
 * @author C5-0
 *
 */


public class GetClassesList extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5887918990222935296L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		//response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String username = new String(URLDecoder.decode(
				request.getParameter("name"), "utf-8"));
		UserDao ud = new UserDaoMySqlImpl();
		System.out.println(username);
		int teacher_id = ud.GetTeacher_id(username);
		System.out.println(teacher_id);
		List<Classes> list = ud.GetClassList(teacher_id);

		if (list.size() == 0) {
			out.print("NOCLASSESLIST");
		} else {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Classes>>() {
			}.getType();
			String beanListToJson = new Gson().toJson(list, type);
			System.out.println(beanListToJson);
			out.print(beanListToJson);
		}
	}
}
