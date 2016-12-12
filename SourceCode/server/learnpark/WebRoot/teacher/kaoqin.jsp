<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>考勤</title>
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
				<div id="nav2">
			<div id="">
				<a href="">全部</a> <a href="">选择专业</a>
				<a href="">导出</a>
			</div>
		</div>
	<p>&nbsp;</p>
	<table  cellspacing="0" cellpadding="0"
		align="center" class="table  table-striped table-bordered">
		<tr align="center">
			<th scope="col">姓名</th>
			<th scope="col">专业</th>
			<th scope="col">课程</th>
			<th scope="col">缺勤</th>
			<th scope="col">请假</th>
		</tr>
		<tr align="center">
			<td>邓煜琳</td>
			<td>数应101</td>
			<td>高代</td>
			<td>12次</td>
			<td>2次</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr align="center">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<div id="yeshu" align="center">
		<a href="" class="btn btn-success">首页</a> <a href=""  class="btn btn-success">上一页</a> <a href=""  class="btn btn-success">下一页</a> <a href=""  class="btn btn-success">末页</a>

	</div>
			<div id="info2">
				<div id="today2">
					<p>
						<big>近期考勤</big>
					</p>
				</div>
				<div id="xingxi2">
					<table width="250" cellspacing="0" cellpadding="0"
						class="table table-bordered">
						<tr>
							<th height="30" scope="col">专业</th>
							<th height="30" scope="col">缺勤数</th>
						</tr>
						<tr>
							<td height="30">&nbsp;</td>
							<td height="30">&nbsp;</td>
						</tr>
						<tr>
							<td height="30">&nbsp;</td>
							<td height="30">&nbsp;</td>
						</tr>
						<tr>
							<td height="30">&nbsp;</td>
							<td height="30">&nbsp;</td>
						</tr>
						<tr>
							<td height="30">&nbsp;</td>
							<td height="30">&nbsp;</td>
						</tr>
					</table>

				</div>
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
