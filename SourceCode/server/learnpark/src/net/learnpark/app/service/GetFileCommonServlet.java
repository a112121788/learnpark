package net.learnpark.app.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.app.dao.FileDao;
import net.learnpark.app.dao.impl.FileDaoMySqlImpl;
import net.learnpark.app.entity.File;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 从服务端端获取视频
 * 
 * @author peng
 * 
 */
public class GetFileCommonServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3518881841782215424L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		FileDao videoDao = new FileDaoMySqlImpl();
		List<File> videos = videoDao.getFilesCommon();
		// 把这个list集合转成gson
		if (videos != null) {
			Gson gson = new Gson();
			String str = gson.toJson(videos, new TypeToken<List<File>>() {
			}.getType());
			out.print(str);
		} else {
			out.print("false");
		}
	}
}
