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

<title>课程导入</title>
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
					<li><a href="<%=basePath%>/admin/settings.jsp"
						>全局设置</a></li>
					<li><a href="<%=basePath%>/admin/course.jsp" class="ui-btn-active">课程导入</a></li>
					<li><a href="<%=basePath%>/admin/push.jsp">邮件通知</a></li>
					<li><a href="<%=basePath%>/admin/feedback.jsp">用户反馈</a></li>
				</ul>

			</div>
			<!-- /navbar -->
			<div id="course">
				<h4 class="text-success">导入课程(暂时不做，没时间)</h4>
				<p>
				<p class="" style="font-size:12px">

					&nbsp;&nbsp;&nbsp;&nbsp;注:请按照模版导入，负责会出错 <a href="#"
						class="btn btn-sm btn-info">下载模版</a>
				</p>
				</p>
				<p>
				<h4 class="text-success">导入课程</h4>
				<input type="file" name="" id="" value="选择文件" style="font-size:14px" />
				</p>
				<a href="#" class="ui-btn ui-btn-b">开始导入</a>
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
