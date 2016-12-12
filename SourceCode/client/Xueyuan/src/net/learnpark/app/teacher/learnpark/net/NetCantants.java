package net.learnpark.app.teacher.learnpark.net;

public class NetCantants {
	public static final String NET_BASE = "http://10.202.1.29:8080/JA0114-learnpark/";
	//public static final String NET_BASE = "http://172.27.35.1:8080/JA01114-learnpark/";
	//public static final String NET_BASE = "http://192.168.191.1:8080/learnpark/";
	//public static final String NET_BASE = "http://learnpark1.sturgeon.mopaas.com/";
	
	public static final String GET_CLASSES_LIST_URL = NET_BASE
			+ "/teacherapp/servlet/GetClassesList";
	public static final String GET_STUDENTS_LIST_URL = NET_BASE
			+ "/teacherapp/servlet/GetStudentsList";
	public static final String UPLOAD_QIANDAOMESSAGES_URL = NET_BASE
			+ "/teacherapp/servlet/UpLoadQiandaoMessages";
	public static final String UPLOAD_COURSES_URL = NET_BASE
			+ "/teacherapp/servlet/UpLoadCourses";
	public static final String SAVE_FEEDBACK_SERVLET_URL = NET_BASE
			+ "/servlet/SaveFeedbackServlet";
	public static final String GET_COURSES_List_URL = NET_BASE
			+ "/teacherapp/servlet/GetCoursesList";
	public static final String GET_NEW_VERSION_URL = NET_BASE
			+ "/teacherapp/servlet/GetNewVersionUrl";
	public static final String SEND_MESSAGES_URL = NET_BASE
			+ "/teacherapp/servlet/SendMessageServlet";

	public static final String CHECK_SERVLET_URL = NET_BASE
			+ "/teacherapp/servlet/CheckServlet";
	public static final String FORGET_PASSWORD_SERVLET_URL = NET_BASE
			+ "/teacherapp/servlet/ForgetPasswordServlet";
	public static final String REGISTER_SERVLET_URL = NET_BASE
			+ "/teacherapp/servlet/RegisterServlet";
	public static final String GET_USER_INFO_URL = NET_BASE
			+ "/teacherapp/servlet/GetUserInfoServlet";
	public static final String SAVE_PHOTEO_URL = NET_BASE
			+ "/teacherapp/servlet/SavePhotoServlet";
	public static final String MODIFY_USER_MESSAGES_URL = NET_BASE
			+ "/teacherapp/servlet/ModifyServlet";
}
