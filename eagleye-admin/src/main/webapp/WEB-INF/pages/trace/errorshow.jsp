<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.yougou.eagleye.admin.constants.AppConstants"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>异常详情</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" />




</head>
<body>


	<div class="alert alert-dismissable alert-info">
	  <strong>TraceLogId : ${id }</strong>
	</div>
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>异常详情</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr>
	      <td>
	      	<c:if test="${errorMsg!=null}">
		  	  ${errorMsg }
		  	</c:if>
	      </td>
	    </tr>
	  </tbody>
	</table> 
	<br/><br/>
</body>


</html>
