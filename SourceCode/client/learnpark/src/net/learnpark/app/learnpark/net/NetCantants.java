package net.learnpark.app.learnpark.net;

/**
 * 网络相关的设置 只需要把NET_BASE切换成你服务器的地址就可以了。 代码在优化中
 * 
 * @author peng
 * 
 */
public class NetCantants {
	public static final String NET_BASE = "http://10.10.0.13:8080/JA0114-learnpark/";
	public static final String CHECK_SERVLET_URL = NET_BASE
			+ "/servlet/CheckServlet";
	public static final String FORGET_PASSWORD_SERVLET_URL = NET_BASE
			+ "/servlet/ForgetPasswordServlet";
	public static final String SAVE_FEEDBACK_SERVLET_URL = NET_BASE
			+ "/servlet/SaveFeedbackServlet";
	public static final String REGISTER_SERVLET_URL = NET_BASE
			+ "/servlet/RegisterServlet";
	public static final String GET_File_COMMON_SERVLET_URL = NET_BASE
			+ "/servlet/GetFileCommonServlet";
	public static final String GET_USER_INFO_URL = NET_BASE
			+ "/servlet/GetUserInfoServlet";
	public static final String SAVE_PHOTEO_URL = NET_BASE
			+ "/servlet/SavePhotoServlet";
	public static final String MODIFY_USER_INFO_URL = NET_BASE
			+ "/servlet/ModifyUserInfoServlet";
	public static final String SAVE_DAY_PLAN_SERVLET_URL = NET_BASE
			+ "/servlet/savePlanExamServlet";
	public static final String SAVE_COURSE_SERVLET_URL = NET_BASE
			+ "/servlet/SaveCourseServlet";
	public static final String GET_COURSE_SERVLET_URL = NET_BASE
			+ "/servlet/GetCourseServlet";
	public static final String GET_PLAN_EXAM_URL = NET_BASE
			+ "/servlet/getPlanExamservlet";
	public static final String GET_MESSAGE_URL = NET_BASE
			+ "/servlet/GetMessageListServlet";
	public static final String PUT_MESSAGE_URL = NET_BASE
			+ "/servlet/PutMessageServlet";

}
