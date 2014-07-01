package cn.learnpark.app.service;

import java.io.IOException;
/**
 * 修改用户信息的的Servlet
 */
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.learnpark.app.dao.UserDao;
import cn.learnpark.app.dao.impl.UserDaoMySqlImpl;
@WebServlet("/servlet/ModifyServlet")
public class ModifyServlet extends HttpServlet {
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
		String info = new String(URLDecoder.decode(request.getParameter("info"),"utf-8")); 
		String usermail = new String(URLDecoder.decode(request.getParameter("user"),"utf-8")); 
		String i = request.getParameter("type");
		System.out.println("info:"+info);  
		System.out.println("usermail:"+usermail);
        System.out.println("type:"+i);
        UserDao	user = new UserDaoMySqlImpl();
        boolean bl=user.modifyUserInfo(i,usermail,info);
        System.out.println(bl);
        if(bl){
        	out.print("true");
        }else{
        	out.print("false");
        }
	}

}
