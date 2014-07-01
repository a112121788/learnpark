package cn.learnpark.app.mail;

import cn.learnpark.app.dao.UserDao;
import cn.learnpark.app.dao.impl.UserDaoMySqlImpl;

/**
 * 邮件发送工具类
 * 
 * @author 陆礼祥 完善 高帅朋
 */
public class SendMail {
	public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(MailConstants.SERVER_HOST);
		mailInfo.setMailServerPort(MailConstants.SERVER_PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(MailConstants.USER_NAME);// 和发送者邮箱同名
		mailInfo.setFromAddress(MailConstants.FROM_ADDRESS);// 发送者邮箱
		mailInfo.setPassword(MailConstants.PASSWORD);// 您的邮箱密码
		mailInfo.setToAddress("840964310@qq.com");// 要接受的邮箱
		mailInfo.setSubject("设置邮箱标题");
		mailInfo.setContent("test" + MailConstants.FOOTER);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		boolean i = sms.sendTextMail(mailInfo);// 发送文体格式
		System.out.println(i);
		// sms.sendHtmlMail(mailInfo);//发送html格式
	}

	public static boolean send(String username,String title,String content) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(MailConstants.SERVER_HOST);
		mailInfo.setMailServerPort(MailConstants.SERVER_PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(MailConstants.USER_NAME);// 和发送者邮箱同名
		mailInfo.setFromAddress(MailConstants.FROM_ADDRESS);// 发送者邮箱
		mailInfo.setPassword(MailConstants.PASSWORD);// 您的邮箱密码
		mailInfo.setToAddress(username);
		mailInfo.setSubject(title);
		UserDao user = new UserDaoMySqlImpl();
		mailInfo.setContent(content+MailConstants.FOOTER);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		return sms.sendTextMail(mailInfo);
	}

}
