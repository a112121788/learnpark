package net.learnpark.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * 
 * 微信所有接口入口
 * 
 * @author slz
 * 
 *         2013-7-26 上午11:01:08
 */
public class PushManage {
	public String PushManageXml(InputStream is) throws JDOMException {
		String returnStr = "";
		String toName = "";
		String FromName = "";
		String type = "";
		String content = "";
		String con = "";
		String event = "";// 自定义按钮事件请求
		String eKey = "";// 事件请求key值
		try {
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(is);
			// 获得文件的根元素
			Element root = doc.getRootElement();
			// 获得根元素的第一级子节点
			List list = root.getChildren();
			for (int j = 0; j < list.size(); j++) {
				// 获得结点
				Element first = (Element) list.get(j);
				if (first.getName().equals("ToUserName")) {
					toName = first.getValue().trim();
				} else if (first.getName().equals("FromUserName")) {
					FromName = first.getValue().trim();
				} else if (first.getName().equals("MsgType")) {
					type = first.getValue().trim();
				} else if (first.getName().equals("Content")) {
					con = first.getValue().trim();
				} else if (first.getName().equals("Event")) {
					event = first.getValue().trim();
				} else if (first.getName().equals("EventKey")) {
					eKey = first.getValue().trim();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (type.equals("event")) {
			if (event.equals("subscribe")) {// 此为关注事件
				content = "您好，欢迎关注我！我是学苑官方小助手，不懂请回复 帮助";
			}
		} else if (type.equals("text")) {
			// 命令 指令

			if (con.equals("帮助")) {
				content = FromName + "回复 学苑用户名和密码绑定账号 格式:\n邮箱#密码\n"
						+ "发送  课表 查询当天的课程\n" + "发送  日程 查询当天的日程\n" + "更多功能开发中";
			}
		}
		return getBackXMLTypeText(toName, FromName, content);
	}

	public String getBackXMLTypeText(String toName, String FromName,
			String content) {
		String returnStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());
		Element rootXML = new Element("xml");
		rootXML.addContent(new Element("ToUserName").setText(FromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("text"));
		rootXML.addContent(new Element("Content").setText(content));
		Document doc = new Document(rootXML);
		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);
		return returnStr;
	}

	public String getBackXMLTypeImg(String toName, String FromName,
			String content) {
		String returnStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());
		Element rootXML = new Element("xml");
		rootXML.addContent(new Element("ToUserName").setText(FromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("news"));
		rootXML.addContent(new Element("ArticleCount").setText("3"));
		Element fXML = new Element("Articles");
		Element mXML = null;
		String url = "";
		String ss = "";
		for (int i = 1; i <= 3; i++) {
			mXML = new Element("item");
			mXML.addContent(new Element("Title").setText("图片" + i));
			mXML.addContent(new Element("Description").setText("美女" + i));
			mXML.addContent(new Element("PicUrl").setText(ss));
			mXML.addContent(new Element("Url").setText(url));
			fXML.addContent(mXML);
		}
		rootXML.addContent(fXML);
		Document doc = new Document(rootXML);
		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);
		return returnStr;
	}

	public String getBackXMLTypeMusic(String toName, String FromName,
			String content) {
		String returnStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());
		Element rootXML = new Element("xml");
		rootXML.addContent(new Element("ToUserName").setText(FromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("music"));
		Element mXML = new Element("Music");
		mXML.addContent(new Element("Title").setText("音乐"));
		mXML.addContent(new Element("Description").setText("音乐让人心情舒畅！"));
		mXML.addContent(new Element("MusicUrl").setText(content));
		mXML.addContent(new Element("HQMusicUrl").setText(content));
		rootXML.addContent(mXML);
		Document doc = new Document(rootXML);
		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);
		return returnStr;
	}
}