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


	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>Dubbo监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.54:8181/" target="view_window">http://10.10.10.54:8181/</a><br/>
	    	生产Dubbo监控
	    </td></tr>
	  </tbody>
	</table> 
	<br/><br/>
	
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>Rabbitmq监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.89:15672" target="view_window">http://10.10.10.89:15672</a><br/>
	    	生产Rabbitmq监控
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.184:15672" target="view_window">http://10.10.10.184:15672</a><br/>
	    	生产Rabbitmq监控
	    </td></tr>
	  </tbody>
	</table> 
	<br/><br/>
	
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>ElasticSearch监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.220:9200/_plugin/bigdesk/#nodes" target="view_window">http://10.10.10.220:9200/_plugin/bigdesk/#nodes</a><br/>
	    	生产Eagleye的ElasticSearch监控
	    </td></tr>
	  </tbody>
	</table> 
	
	<br/><br/>
	
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>Memcached监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.180/mem/mem173.php" target="view_window">Memcached173</a><br/>
	    	生产Memcached监控
	    </td></tr>
	  </tbody>
	</table> 
	
	<br/><br/>
	
	<table class="table table-striped table-hover " style="table-layout:fixed">
	  <thead>
	    <tr class="danger">
	      <th>运维监控列表</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr><td>
	    	<a href="http://10.10.10.180/cacti/" target="view_window">Cacti</a><br/>
	    	生产运维Cacti监控
	    </td></tr>
	    <tr><td>
	    	<a href="http://10.10.10.180/zabbix/tr_status.php" target="view_window">Zabbix</a><br/>
	    	生产运维Zabbix监控
	    </td></tr>
	  </tbody>
	</table>
</body>


</html>
