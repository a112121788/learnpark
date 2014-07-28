<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>学苑--邮件通知</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="<%=basePath%>/css/jquery.mobile-1.4.3.css" />
<link rel="stylesheet" href="<%=basePath%>/css/main.css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/js/jquery.mobile-1.4.3.js"></script>
</head>
<!-- 判断用户的登陆状态  session-->
<%
	String username = (String) session.getAttribute("username");
	if (username != null && (!username.equals("null"))) {
	} else {
		getServletContext().getRequestDispatcher("/admin/login.jsp")
				.forward(request, response);
	}
%>
<body>
	<div data-role="page">
		<div data-role="header" id="header">
			<a href="<%=basePath%>/admin/servlet/LogoutServlet"
				class="ui-btn ui-btn-left ui-btn-a ui-icon-power  ui-corner-all ui-btn-icon-left  ui-btn-icon-notext ui-btn-inline">退出</a>
			<a
				class="ui-btn ui-btn-right ui-btn-a ui-icon-user ui-btn-icon-left ui-shadow ui-corner-all ui-btn-icon-notext"
				href="<%=basePath%>/admin/center.jsp"></a>
			<h1>
				学苑-后台主页-<%=session.getAttribute("username")%></h1>
		</div>
		<!-- /header -->
		<div role="main" class="ui-content" id="contenter">

			<div data-role="navbar" data-grid="c">
				<ul>
					<li><a href="<%=basePath%>/admin/settings.jsp">全局设置</a></li>
					<li><a href="<%=basePath%>/admin/course.jsp">课程导入</a></li>
					<li><a href="<%=basePath%>/admin/push.jsp"
						class="ui-btn-active">邮件通知</a></li>
					<li><a href="<%=basePath%>/admin/feedback.jsp">用户反馈</a></li>
				</ul>

			</div>
			<!-- /navbar -->
			<div id="feedback">
				<form action="<%=basePath%>/admin/servlet/SendEmaiServlet"
					method="post" class="center">
					<br /> <label>邮箱：</label><input type="text" name="email"
						placeholder="用户的邮箱" /> <label>标题：</label><input type="text"
						name="title" placeholder="标题"/> <br /> <br />
					<textarea rows="40" cols="20" name="content" placeholder="内容"></textarea>
					<br /> <br />
					 <input type="submit" name="" value="发送"
						class="" />
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
