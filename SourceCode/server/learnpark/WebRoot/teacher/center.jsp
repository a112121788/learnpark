<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>学院-教师个人中心</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="<%=basePath%>/css/jquery.mobile-1.4.3.css" />
<link rel="stylesheet" href="<%=basePath%>/css/main.css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/js/jquery.mobile-1.4.3.js"></script>
</head>
<%-- 判断用户的登陆状态  session--%>
<%
	String username = (String) session.getAttribute("username");
	if (username != null && (!username.equals("null"))) {
	} else {
		getServletContext().getRequestDispatcher("/teacher/login.jsp")
				.forward(request, response);
	}
%>
<body>
	<div data-role="page">
		<div data-role="header" id="header">
			<h1>
				学苑-教师-<%=session.getAttribute("username")%></h1>
		</div>
		<!-- /header -->
		<div role="main" class="ui-content" id="contenter">

			<a href="<%=basePath%>/teacher/home.jsp" class="ui-btn-active">主页</a>
			<a href="<%=basePath%>/teacher/course.jsp">我的课程</a> <a
				href="<%=basePath%>/teacher/student.jsp">我的学生</a> <a
				href="<%=basePath%>/teacher/kaoqin.jsp"> 出勤统计</a>

			<div id="info1">
				账号：<%=session.getAttribute("username")%>
				<h2>修改密码:</h2>
				<br />
			</div>
			<div id="info2">
				<form action="<%=basePath%>/teacher/servlet/AlterPasswordServlet"
					method="post">
					新密码&nbsp;&nbsp;&nbsp; <input type="password" id="newpassword1"
						name="newpassword1" /> 确认密码 <input type="password"
						id="newpassword2" name="newpassword1" /> <br /> <input
						type="submit" name="" id="" value="更改" class="btn btn-success" />
				</form>
			</div>
		</div>
		<div data-role="footer" id="footer">
			<h4>&copy;2014 学苑开发项目组</h4>
		</div>
		<!-- /footer -->
	</div>
	<!-- /page -->
</body>
</html>
