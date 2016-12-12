package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.MessageDao;
import net.learnpark.app.dao.impl.MessageDaoMySqlImpl;
import net.learnpark.app.entity.Message;

import com.google.gson.Gson;

/**
 * 获得消息列表
 * 
 * @author C5-0
 * 
 */

public class GetMessageListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8067896317848717060L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		// response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String username = new String(URLDecoder.decode(
				request.getParameter("username"), "utf-8"));
		String fromuser = "";
		fromuser = new String(URLDecoder.decode(
				request.getParameter("fromuser"), "utf-8"));
		MessageDao messageDao = new MessageDaoMySqlImpl();
		if (fromuser.equals("true")) {
			// 默认5条 最新的 自己发的

			List<Message> msgList = messageDao
					.getMsgByFromUsername(username, 25);
			if (msgList != null) {
				String msgGson = new Gson().toJson(msgList);
				out.print(msgGson);
			} else {
				out.print("false");
			}
		} else {
			// 默认5条 最新的 发给username的
			List<Message> msgList = messageDao.getMsgByToUsername(username, 25);
			if (msgList != null) {
				String msgGson = new Gson().toJson(msgList);
				out.print(msgGson);
			} else {
				out.print("false");
			}
		}

	}
}
