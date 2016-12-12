<%@ page language="java"
	import="java.util.*,net.learnpark.admin.entity.User"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>学苑-教师登陆</title>
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
		getServletContext().getRequestDispatcher("/teacher/home.jsp")
				.forward(request, response);
	} else {
	}
%>
<body>
	<div data-role="page">
		<div data-role="header" id="header">
			<h1>
				学苑-教师登陆</h1>
		</div>
		<!-- /header -->
		<div role="main" class="ui-content" id="contenter">
			<div id="login">
				<!--用户名-->
				<form action="<%=basePath%>/teacher/servlet/CheckUserServlet"
					method="post">
					用户名： <input type="text" name="username"
						class="form-control login-field" placeholder="您注册时的邮箱" /> <br />
					密码：&nbsp;&nbsp; <input type="password" name="password"
						class="form-control login-field" placeholder="您注册时的密码" /> <br />
					<a href="<%=basePath%>/teacher/getpassword.jsp">忘记密码</a> <span>
						<input type="submit" name="" value="登录"
						class="btn btn-block btn-lg btn-success" />
					</span>
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
