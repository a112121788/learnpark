package net.learnpark.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.UserDao;
import net.learnpark.app.dao.impl.UserDaoMySqlImpl;
import Decoder.BASE64Decoder;

/**
 * 
 * 
 */
public class SavePhotoServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3662739403722482456L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String photo = request.getParameter("photo");
		String photoname = request.getParameter("photoname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println(photo);
		System.out.println(photoname);
		System.out.println(username);
		System.out.println(password);
		UserDao userDao = new UserDaoMySqlImpl();
		if (userDao.checkUser(username, password) != 1) {
			return;
		}
		byte[] photoimg = new BASE64Decoder().decodeBuffer(photo);
		for (int i = 0; i < photoimg.length; ++i) {
			if (photoimg[i] < 0) {
				photoimg[i] += 256;
			}
		}
		System.out.println("图片的大小：" + photoimg.length);

		File imgDir = new File("file:/D:/Tomcat%207.0/learnpark/heads/");
		File filename = new File("file:/D:/Tomcat%207.0/learnpark/heads/"+ photoname);
//		File imgDir = new File("D:\\heads");
//		File filename = new File("D:\\heads\\"+ photoname);
		System.out.println(imgDir.toURI().toString());
		System.out.println(filename.toURI().toString());
		if (!imgDir.exists()) {
			imgDir.mkdirs();
		}
		if (!filename.exists()) {
			filename.createNewFile();
		}
//		FileOutputStream out = new FileOutputStream(imgDir);
		FileOutputStream out1 = new FileOutputStream(filename);
//		out1.write(username.getBytes());
		out1.write(photoimg);
//		out.flush();
//		out.close();
		out1.flush();
		out1.close();
		// 数据库中插入用户头像的连接
	}
}
