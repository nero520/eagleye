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
<title>redis监控</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" />
</head>
<body>


	<!-- <div class="alert alert-dismissable alert-info">
	  <strong>权限: yougou/yougou</strong>
	</div> -->
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>Redis监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.20:9979" target="view_window">redis: 10.10.10.175:6379</a><br/>
	    	促销系统 ; rdb 一小时一次
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9980" target="view_window">redis: 10.10.10.176:6380</a><br/>
	    	网站Session ; 代理IP 10.10.10.178 ; VIP 10.10.10.70 ; rdb 半小时一次
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9880" target="view_window">redis: 10.10.10.177:6380</a><br/>
	    	网站Session ; 代理IP 10.10.10.179 rdb, 半小时一次
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9981" target="view_window">redis: 10.10.10.177:6381</a><br/>
	    	商品 ; rdb 一小时一次 
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9982" target="view_window">redis: 10.10.10.176:6382</a><br/>
	    	BI
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9983" target="view_window">redis: 10.10.10.176:6383</a><br/>
	    	招商系统
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9984" target="view_window">redis: 10.10.10.176:6384</a><br/>
	    	流量统计 ; rdb 一小时一次
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9985" target="view_window">redis: 10.10.10.179:6385</a><br/>
	    	CMS
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9986" target="view_window">redis: 10.10.10.176:6386</a><br/>
	    	订单
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9987" target="view_window">redis: 10.10.10.177:6387</a><br/>
	    	用户中心 ; rdb 一小时一次
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.20:9988" target="view_window">redis: 10.10.10.179:6388</a><br/>
	    	WMS ; rdb 一小时一次
	    </td></tr>
	  </tbody>
	</table> 
	<br/><br/>
</body>


</html>
