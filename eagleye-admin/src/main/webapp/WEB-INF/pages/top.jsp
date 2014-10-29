<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.yougou.eagleye.admin.constants.AppConstants"%>
<% 
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String u =(String)session.getAttribute(AppConstants.EAGLEYE_SESSION_USERNAME);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include flush="false" page="common/link.jsp"></jsp:include>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" />
</head>
<body>
 <div class="header">
 	 <div style="float:left;padding-left:15px;"><h4 style="color:#FFF;">Eagleye 统一监控平台</h4></div>
      <ul class="nav">
        <li class="li2"><div onclick="logout();" alt="退出" title="退出">退出</div></li>
      </ul>
</div>
</body>
<script>
function logout(){
	
	window.parent.right.common.confirm("你确定要退出系统吗?",function(result){
		if(result){
			parent.window.location.href="<%=basePath%>/logout.html";
		}
	})
}
</script>

</html>
