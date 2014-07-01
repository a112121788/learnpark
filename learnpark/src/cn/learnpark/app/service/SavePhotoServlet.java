package cn.learnpark.app.service;

import java.io.File;
/**
 * 保存用户头像的Servlet
 */
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Decoder.BASE64Decoder;

@WebServlet("/servlet/SavePhotoServlet")
public class SavePhotoServlet extends HttpServlet {
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
		String username = request.getParameter("name");
		byte[] photoimg = new BASE64Decoder().decodeBuffer(photo);
		for (int i = 0; i < photoimg.length; ++i) {
			if (photoimg[i] < 0) {
				photoimg[i] += 256;
			}
		}
		System.out.println("图片的大小：" + photoimg.length);
		File file = new File(
				"D:\\Program Files\\mye\\team\\WebRoot\\HeadImage", username);
		File filename = new File(
				"D:\\Program Files\\mye\\team\\WebRoot\\HeadImage\\name.txt");
		if (!filename.exists()) {
			file.createNewFile();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		FileOutputStream out1 = new FileOutputStream(filename);
		out1.write(username.getBytes());
		out.write(photoimg);
		out.flush();
		out.close();
		out1.flush();
		out1.close();
	}

}
