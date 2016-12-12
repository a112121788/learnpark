package net.learnpark.admin.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.admin.dao.OpinionDao;
import net.learnpark.admin.dao.impl.OpinionDaoMySqlImpl;
import net.learnpark.app.dao.MessageDao;
import net.learnpark.app.dao.impl.MessageDaoMySqlImpl;
import net.learnpark.mail.SendMail;

public class ModifyOpinionServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1634150016600642622L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");
		String email = new String(URLDecoder.decode(req.getParameter("email"),
				"utf-8"));
		String yijian = new String(URLDecoder.decode(
				req.getParameter("yijian"), "utf-8"));
		String reply = new String(URLDecoder.decode(req.getParameter("reply"),
				"utf-8"));
		OpinionDao opinionDao = new OpinionDaoMySqlImpl();

		boolean isModify = opinionDao.modifyOpinion(email, yijian, reply);
		if (isModify) {

			MessageDao messageDao = new MessageDaoMySqlImpl();
			messageDao.putMsg("admin", email, reply, new Date());
			SendMail.send(email, "您在学苑的反馈：" + yijian, reply);
			String path = req.getContextPath();
			String basePath = req.getScheme() + "://" + req.getServerName()
					+ ":" + req.getServerPort() + path;

			req.setAttribute("message", "回复成功<a href='" + basePath
					+ "/admin/feedback.jsp' class='btn btn-success'>返回</a>");
			req.getRequestDispatcher("/message.jsp").forward(req, resp);
		} else {
			req.setAttribute("message", "回复失败");
			req.getRequestDispatcher("/message.jsp").forward(req, resp);
		}
	}
}
