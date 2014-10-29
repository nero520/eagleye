<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
  <frameset border=0 frameSpacing=0 rows=40,* frameBorder=0>
	<frame name=top src="<%=basePath %>top.html" noResize scrolling=no/>
		<frameset cols="240,8,*" id="center" frameBorder=0 frameSpacing=0>
        	<frame src="<%=basePath %>left.html" name="left" scrolling="no" noresize="noresize"/>
        	<frame src="<%=basePath %>mid.html" name="mid" id="mid" scrolling="no" noresize="noresize"/>
        	<frame src="<%=basePath %>right.html" id="right" scrolling="no" noresize="noresize" name="right"/>
       </frameset>
  </frameset>
</html>
