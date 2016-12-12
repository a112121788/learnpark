package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.FeedbackDao;
import net.learnpark.app.dao.impl.FeedbackDaoMySqlImpl;
/**
 * 保存用户的反馈
 * @author 陆礼祥
 *
 */
public class SaveFeedbackServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4688818727754481210L;
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
