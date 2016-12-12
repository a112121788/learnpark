package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.UserDao;
import net.learnpark.app.dao.impl.UserDaoMySqlImpl;
import net.learnpark.app.entity.Exam;
import net.learnpark.app.entity.Plan;

import com.google.gson.Gson;

public class getPlanExamservlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4323103843615963372L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ok~");
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String tabs = request.getParameter("tabs");// 判断是“今日计划”还是“考试提醒”
													// 0表示“每日计划” 1表示“考试提醒”
		String usermail = request.getParameter("user");
		System.out.println("tabs----" + tabs);
		System.out.println("usermail----" + usermail);

		UserDao user = new UserDaoMySqlImpl();
		if ("0".equals(tabs)) {
			List<Plan> listplan = user.getPlan(usermail);
			Gson gson = new Gson();
			String plangson = gson.toJson(listplan);
			out.print(plangson);
		} else if ("1".equals(tabs)) {
			List<Exam> listexam = user.getExam(usermail);
			Gson gson = new Gson();
			String examgson = gson.toJson(listexam);
			out.print(examgson);
			System.out.println(examgson);
		}
	}

}
