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
<title>我的课程</title>
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
			<table width="700" cellspacing="0" cellpadding="0" align="center"
				class="table table-bordered">
				<tr>
					<th scope="col">课程表</th>
					<th scope="col">星期一</th>
					<th scope="col">星期二</th>
					<th scope="col">星期三</th>
					<th scope="col">星期四</th>
					<th scope="col">星期五</th>
					<th scope="col">星期六</th>
					<th scope="col">星期天</th>
				</tr>
				<tr>
					<th height="50" scope="row">上午1</th>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
				</tr>
				<tr>
					<th height="50" scope="row">上午2</th>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
				</tr>
				<tr>
					<th height="50" scope="row">下午1</th>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
				</tr>
				<tr>
					<th height="50" scope="row">下午2</th>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
				</tr>
				<tr>
					<th height="50" scope="row">晚上</th>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
					<td height="50">&nbsp;</td>
				</tr>
			</table>
			<p>&nbsp;</p>
			<div id="xiugai" align="center">

				<a href="" class="btn btn-success">修改</a> &nbsp;&nbsp; <a href=""
					class="btn btn-error">保存</a>
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
