package net.learnpark.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.learnpark.util.SHA1;

import org.jdom.JDOMException;

@SuppressWarnings("serial")
public class WechatCallbackApi extends HttpServlet {
	// 自定义 token
	private String TOKEN = "learnpark";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");

		String[] str = { TOKEN, timestamp, nonce };
		Arrays.sort(str); // 字典序排序
		String bigStr = str[0] + str[1] + str[2];
		// SHA1加密
		String digest = new SHA1().getDigestOfString(bigStr.getBytes())
				.toLowerCase();
		// 确认请求来至微信
		if (digest.equals(signature)) {
			response.getWriter().print(echostr);
		}
	}

	/**
	 * 
	 * 微信公众平台 所有接口入口
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String TOKEN = "sun";// Token
		String signature = request.getParameter("signature");// SHA1加密字符串
		String timestamp = request.getParameter("timestamp");// 时间
		String nonce = request.getParameter("nonce");// 随机数
		String echoStr = request.getParameter("echostr");// 随机字符串
		if (echoStr != null && (!echoStr.equals(""))) {
			String[] a = { TOKEN, timestamp, nonce };
			Arrays.sort(a);// 数组排序
			String str = "";
			for (int i = 0; i < a.length; i++) {
				str += a[i];
			}
			String echo = new SHA1().getDigestOfString(str.getBytes());// SHA1加密
			if (echo.equals(signature)) {
				out.print(echoStr);
			} else {
				out.print("123");
			}
		} else {
			try {
				InputStream is = request.getInputStream();
				PushManage push = new PushManage();
				String getXml = push.PushManageXml(is);
				System.out.println("getXml:" + getXml);
				out.print(getXml);
			} catch (JDOMException e) {
				out.print("");
			}
		}
		out.flush();
		out.close();
	}
}
