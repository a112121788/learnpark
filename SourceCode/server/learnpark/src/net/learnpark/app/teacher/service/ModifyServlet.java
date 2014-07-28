package net.learnpark.app.teacher.service;

import java.io.IOException;
/**
 * 修改用户信息的的Servlet
 */
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.teacher.dao.UserDao;
import net.learnpark.app.teacher.dao.impl.UserDaoMySqlImpl;
public class ModifyServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1225998432157439548L;
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
		String username = new String(URLDecoder.decode(request.getParameter("user_name"),"utf-8")); 
		String usermail = new String(URLDecoder.decode(request.getParameter("user_mail"),"utf-8"));
		String userschool = new String(URLDecoder.decode(request.getParameter("user_school"),"utf-8"));
		String userdepartment = new String(URLDecoder.decode(request.getParameter("user_department"),"utf-8"));

        UserDao	user = new UserDaoMySqlImpl();
        boolean bl=user.modifyUserInfo(username,usermail,userschool,userdepartment);
        System.out.println(username+usermail+userschool+userdepartment+bl);
        if(bl){
        	out.print("true");
        }else{
        	out.print("false");
        }
	}

}
