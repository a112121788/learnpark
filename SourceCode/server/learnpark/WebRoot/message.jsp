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
<title>index.html</title>
<meta http-equiv="keywords" content="enter,your,keywords,here" />
<meta http-equiv="description"
	content="A short description of this page." />

<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/index.css" />
<!-- Loading Bootstrap -->
<link href="<%=basePath%>/bootstrap/css/bootstrap.css" rel="stylesheet" />
<script src="<%=basePath%>/js/jquery-1.11.0.js" type="text/javascript"></script>
</head>
<body>
	<div id="content">
		<div id="bar"></div>
		<hr width="100%" />
		<div align="center">${message }</div>

		<%--底部版权等信息 --%>
		<div>
			<hr />
			<p align="center">&copy;2014 学苑开发项目组</p>
		</div>
	</div>

</body>
</html>
