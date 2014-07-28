<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>我的学生</title>
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
				<hr/>
			<div id="nav2">
				<div id="xuanzhe">
					<a href="">全部</a> <a href="">选择班级</a> <a href="">添加学生</a>
				</div>
				<div id="daochu">
					<a href="">编辑</a> &nbsp; <a href="">导出</a>
				</div>
			</div>
			<p>&nbsp;</p>
			<table width="467px" cellspacing="0" cellpadding="0" align="center"
				class="table table-bordered">
				<tr>
					<th>姓名</th>
					<th>专业</th>
					<th>课程</th>
					<th>编辑</th>
				</tr>

			</table>
			<div data-role="footer" id="footer">
				<h4>&copy;2014 学苑开发项目组</h4>
			</div>
			<!-- /footer -->
		</div>
		<!-- /page -->
</body>
</html>