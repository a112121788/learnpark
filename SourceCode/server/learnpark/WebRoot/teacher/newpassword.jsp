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

<title>学苑-找回密码</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="<%=basePath%>/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/t-login.css" />
<!-- Loading Bootstrap -->
<script src="<%=basePath%>/js/jquery-1.11.0.js" type="text/javascript"></script>
</head>
<body>
	<div id="content">
		<!--顶部展示信息和导航-->
		<div id="bar"></div>
		<hr width="100%" />
		<br />
		<!--登陆模块-->
		<div id="login">
			<!--用户名-->
			<form action="<%=basePath%>/teacher/servlet/setNewPasswordServlet"
				method="post">
				<%
					String username = request.getParameter("username");
					String userkey = request.getParameter("userkey");
				%>
				<input type="hidden" name="username" value="<%=username%>" /> <input
					type="hidden" name="userkey" value="<%=userkey%>" /> 新密码： <input
					type="text" name="password" class="form-control login-field"
					placeholder="请记好新密码" /> <br /> <input type="submit" name=""
					value="确定" class="btn btn-block btn-lg btn-success" /> </span>
			</form>
		</div>
		<%--底部版权等信息 --%>
		<div>
			<br>
			<hr />
			<p align="center">&copy;2014 学苑开发项目组</p>
		</div>
	</div>
</body>
</html>
