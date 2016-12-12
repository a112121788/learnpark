<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.learnpark.admin.entity.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>学苑-后台管理</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="<%=basePath%>/css/jquery.mobile-1.4.3.css" />
<link rel="stylesheet" href="<%=basePath%>/css/main.css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/js/jquery.mobile-1.4.3.js"></script>
</head>
<body>
	<div data-role="page">
		<div data-role="header" id="header">

			<h1>学苑-后台管理</h1>
		</div>
		<!-- /header -->

		<div role="main" class="ui-content" id="contenter">
			<div id="who" align="center">
				<a href="teacher/login.jsp"
					class="ui-btn ui-btn-b ui-btn-inline ui-mini ui-corner-all">我是老师</a>
				<br /> <br /> <a href="admin/login.jsp"
					class="ui-btn ui-btn-a ui-btn-inline ui-mini ui-corner-all">我是管理员</a>
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
