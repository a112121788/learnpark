package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.MessageDao;
import net.learnpark.app.dao.impl.MessageDaoMySqlImpl;

/**
 * 在数据库中插入消息
 * 
 * @author peng
 * 
 */
public class PutMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4926803499080411009L;

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
		String msgGson = new String(URLDecoder.decode(
				request.getParameter("msgGson"), "utf-8"));

		System.out.println("username:" + username);
		System.out.println("coursegson:" + msgGson);

		MessageDao messageDao = new MessageDaoMySqlImpl();

		if (!username.equals("")) {
			out.print(messageDao.putMsg(msgGson));
		} else {
			out.print("false");
		}
	}
}
