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

public class UpLoadQiandaoMessages extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5204924115557191339L;

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
				request.getParameter("name"), "utf-8"));
		String qiandao = new String(URLDecoder.decode(
				request.getParameter("qiandao"), "utf-8"));
		UserDao ud = new UserDaoMySqlImpl();
		System.out.println(username);
		int teacher_id = ud.GetTeacher_id(username);
		Boolean bl=ud.PutQingdaoIntoSql(teacher_id, qiandao);
		//这里插入成功可以给一个插入成功的返回值的
		if (bl==true) {
			out.print("OK");
		}else {
			out.print("FAIL");
		}
		
	}

}
