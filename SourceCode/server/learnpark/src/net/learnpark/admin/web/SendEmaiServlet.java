package net.learnpark.admin.web;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.mail.SendMail;

public class SendEmaiServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3861984197335968532L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String email = new String(URLDecoder.decode(
				request.getParameter("email"), "utf-8"));
		String title = new String(URLDecoder.decode(
				request.getParameter("title"), "utf-8"));
		String content = new String(URLDecoder.decode(
				request.getParameter("content"), "utf-8"));
		if (SendMail.send(email, title, content)) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;

			request.setAttribute("message", "发送成功<a href='" + basePath
					+ "/admin/push.jsp' class='btn btn-success'>返回</a>");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		} else {
			request.setAttribute("message", "发送失败");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		}
	}
}
