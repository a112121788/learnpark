package cn.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.learnpark.app.dao.FeedbackDao;
import cn.learnpark.app.dao.UserDao;
import cn.learnpark.app.dao.impl.FeedbackDaoMySqlImpl;
import cn.learnpark.app.dao.impl.UserDaoMySqlImpl;
/**
 * 保存用户的反馈
 * @author 陆礼祥
 *
 */
@WebServlet("/servlet/SaveFeedbackServlet")
public class SaveFeedbackServlet extends HttpServlet {

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
		String yj = new String(URLDecoder.decode(request.getParameter("yijian"),"utf-8"));
		String lx = new String(URLDecoder.decode(request.getParameter("lianxi"),"utf-8")); 
		FeedbackDao feedback = new FeedbackDaoMySqlImpl();
		UserDao user=new UserDaoMySqlImpl();
		boolean bl=feedback.saveFeedBack(yj,lx);
		System.out.println("yijian:"+yj);  
        System.out.println("lianxi:"+lx);
		if(bl){
			out.print("true");
		}else if(!bl){
			out.print("false");
		}
	}

}
