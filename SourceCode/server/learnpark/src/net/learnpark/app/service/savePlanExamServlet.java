package net.learnpark.app.service;

import java.io.IOException;
import java.net.URLDecoder;
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
import com.google.gson.reflect.TypeToken;

public class savePlanExamServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9182853180610667787L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String plangson = new String(URLDecoder.decode(
				request.getParameter("plangson"), "utf-8"));
		String examgson = new String(URLDecoder.decode(
				request.getParameter("examgson"), "utf-8"));
		System.out.println("plangson:" + plangson);
		System.out.println("examgson:" + examgson);
		Gson gson = new Gson();

		String planname;
		String mail;
		String examname;
		String examtime;
		UserDao user = new UserDaoMySqlImpl();
		if (!"null".equals(plangson)) {
			List<Plan> planList = gson.fromJson(plangson,
					new TypeToken<List<Plan>>() {
					}.getType());
			user.cleanData(1);
			for (Plan plan : planList) {
				int a = 0;// 0表示未完成，1表示已完成 true 表示完成
				int b = 0;// 0表示不重要，1表示重要
				planname = plan.getPlanName();
				mail = plan.getMail();
				if (plan.isDone()) {
					a = 1;
				}
				if (plan.isImportant()) {
					b = 1;
				}
				System.out.println("planname:" + planname);
				System.out.println("mail:" + mail);
				System.out.println("a:" + a);
				System.out.println("b:" + b);
				user.savedayPlan(mail, planname, a, b);// 功能已实现，先注销便于测试
			}
		}
		if (!"null".equals(examgson)) {
			List<Exam> examList = gson.fromJson(examgson,
					new TypeToken<List<Exam>>() {
					}.getType());
			user.cleanData(2);
			for (Exam exam : examList) {
				int a = 0;// 0表示未完成，1表示已完成 true 表示完成
				int b = 0;// 0表示不重要，1表示重要
				examname = exam.getName();
				examtime = exam.getTime();
				mail = exam.getMail();
				if (exam.isDone()) {
					a = 1;
				}
				if (exam.isImportant()) {
					b = 1;
				}
				System.out.println("mail:" + mail);
				System.out.println("examname:" + examname);
				System.out.println("examtime:" + examtime);
				System.out.println("a:" + a);
				System.out.println("b:" + b);
				user.saveExam(mail, examname, examtime, a, b);// 功能已实现，先注销便于测试
			}
		}
	}
}
