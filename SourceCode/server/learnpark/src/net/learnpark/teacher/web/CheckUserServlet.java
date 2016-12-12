package net.learnpark.teacher.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.learnpark.teacher.entity.User;
import net.learnpark.teacher.service.UserService;
import net.learnpark.teacher.service.impl.UserServiceImpl;

/**
 * 管理员登陆检测
 * 
 * @author 胡亚历 注：高帅朋修改整合
 * 
 */
public class CheckUserServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8867002661579829082L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService service = new UserServiceImpl();
		User user = service.login(username, password);
		if (user == null) {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;

			// 不存在，转向全局消息页面，提示用户名或密码不正确
			request.setAttribute("message", "用户名或密码不正确，请<a href='" + basePath
					+ "/teacher/login.jsp' class='btn btn-success'>重新登陆</a>");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
		// 存在，把USER放到session中转向主页
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		session.setAttribute("password", password);
		request.getRequestDispatcher("/teacher/home.jsp").forward(request,
				response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
