<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include flush="false" page="common/link.jsp"></jsp:include>
<style>
	body,input{ padding:0; margin:0}
	.but_s1 {
		background:#ECECEC url(<%=basePath %>/images/movetoleft.png) no-repeat center center;
		width:6px;	
		cursor:pointer;
		border:none;
	
		}
	.but_s2 {
		background:#ECECEC url(<%=basePath %>/images/movetoright.png) no-repeat center center;		
		width:6px;	
		cursor:pointer;
		border:none;
		
		}
</style>
</head>
<body  style="width:6px;">
<input type="button" onclick="toggle()" value=" " name="button" id="button" class="but_s1" />
</body>
</html>

<script type="text/javascript"> 
var but_height = (document.documentElement.clientHeight);
$("#button").css({"height":but_height});

var flag=true;
function toggle(){	
   if(flag){
   		parent.document.getElementById("center").cols="0,8,*";
		$("#button").removeClass("but_s1").addClass("but_s2");

		flag=false;
   }else{
	   	parent.document.getElementById("center").cols="240,8,*";
		$("#button").removeClass("but_s2").addClass("but_s1");

		flag=true;
   }
}

$(function(){
	$("#button").mouseover(function(){
		$(this).css("background-color","#C1C0C0");
	})
	$("#button").mouseout(function(){
		$(this).css("background-color","#ECECEC");
	})
})
</script>