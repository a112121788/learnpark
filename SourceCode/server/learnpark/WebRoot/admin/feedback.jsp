<%@page import="net.learnpark.admin.dao.impl.OpinionDaoMySqlImpl"%>
<%@page import="net.learnpark.admin.dao.OpinionDao"%>
<%@page import="net.learnpark.admin.entity.Opinion"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.learnpark.admin.entity.Person"%>
<%--jstl使用必须 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>意见反馈</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="<%=basePath%>/css/jquery.mobile-1.4.3.css" />
<link rel="stylesheet" href="<%=basePath%>/css/main.css" />
<script type="text/javascript" src="<%=basePath%>/js/jquery-1.11.0.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/js/jquery.mobile-1.4.3.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/feedback.js"></script>
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
					<li><a href="<%=basePath%>/admin/push.jsp">邮件通知</a></li>
					<li><a href="<%=basePath%>/admin/feedback.jsp"
						class="ui-btn-active">用户反馈</a></li>
				</ul>
			</div>
			<!-- /navbar -->
			<div id="feedback">
				<%
					OpinionDao opinionDao = new OpinionDaoMySqlImpl();

					List<Opinion> listOpinion = opinionDao.selectAllOpinion();
					if (listOpinion != null && listOpinion.size() > 0) {
						request.setAttribute("listOpinion", listOpinion);
					} else {
						listOpinion.add(new Opinion(0, "无", "无", "无"));
						request.setAttribute("listOpinion", listOpinion);
					}
				%>
				<table data-role="table" id="feedback_table" 
					data-mode=""
					class=" table ui-body-d ui-shadow table-stripe ui-responsive">
						<hr/>
						<tr class="ui-bar-d">
							<th >ID</th>
							<th >联系方式</th>
							<th >反馈内容</th>
							<th >处理信息</th>
							<th>回复</th>
						</tr>
					<tbody>
						<c:forEach items="${listOpinion}" var="opinion">
							<tr>
								<td>${opinion.id }</td>
								<td>${opinion.lianxi}</td>
								<td>${opinion.yijian }</td>
								<td>${opinion.replay }</td>
								<td>
									<a href="#popupLogin" data-rel="popup"
									data-position-to="window" data-transition="pop"
									class="ui-btn ui-btn-inline ${opinion.replay==null?'ui-btn-b':'ui-btn-a'} "
									data-toggle="modal" data-target="#basicModal">${opinion.replay==null?'未回复':'已回复'}</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div data-role="popup" id="popupLogin" data-theme="a"
					class="ui-corner-all">
					<form action="<%=basePath%>/admin/servlet/ModifyOpinionServlet"
						method="post">
						<div style="padding:10px 20px; width:300px">
						<h4>回复用户反馈</h4>
							<label>用户邮箱：</label>
							<p id="email" value="a">邮箱</p>
							<label>反馈内容：</label>
							<p id="yijian">不要用</p>
							<textarea rows="5" cols="78" class="center"
								placeholder="意见回复,请认真填写，信息会发到用户的邮箱里面的。" id="reply"></textarea>
							<input type="hidden" name="email" id="opinion_email" /> <input
								type="hidden" name="yijian" id="opinion_yijian" /> <input
								type="hidden" name="reply" id="opinion_reply" />
							<button type="submit"
								class="ui-btn ui-corner-all ui-shadow ui-btn-b ui-btn-icon-left ui-icon-check"
								onclick="upload1();">提交回复</button>
						</div>
					</form>
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
