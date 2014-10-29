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
<title>调用链详情</title>
<link rel="stylesheet" href="<%=basePath%>plugins/jquery-treetable/css/jquery.treetable.css" type="text/css" />
	
<link rel="stylesheet" href="<%=basePath%>plugins/jquery-treetable/css/jquery.treetable.theme.default.css" type="text/css" />
	
<script type="text/javascript" src="<%=basePath%>plugins/jquery-treetable/jquery2.1.1.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/jquery-treetable/jquery.treetable.js"></script>



	
<%-- <link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" /> --%>

<style type="text/css">
	.timelinediv{
		height:20px;background-color:#1C86EE;display: inline-block;
	}
	.timelinespan{
		display: inline-block; vertical-align: top;padding:0;
		
	}
</style> 

</head>
<body>
	<div>traceId : ${traceId }</div>
	<table id="example-advanced">
        <caption>
          <a href="#" onclick="jQuery('#example-advanced').treetable('expandAll'); return false;">展&nbsp;&nbsp;开</a>&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="#" onclick="jQuery('#example-advanced').treetable('collapseAll'); return false;">合&nbsp;&nbsp;并</a>
        </caption>
        <thead>
          <tr>
          	<th>ParentId<br/>SpanId</th>
          	<th>调用方<br/>提供方</th>
          	<th>调用方IP<br/>提供方IP</th>
            <th>调用接口<br/>方法名</th>
            <th>起始时间</th>
            <th>运行时长(ms)</th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${traceLinkVoList!=null}">
			  <c:forEach items="${traceLinkVoList}" var="item" varStatus="status">
			  	 <tr data-tt-id='${item.spanId }' data-tt-parent-id='${item.parentId }'>
			  	 	<td><span class='folder'>ParentId:${item.parentId }<br/><span style="margin-left:20px">SpanId:${item.spanId }</span></span></td>
			  	 	<td>调用方:${item.cappName }<br/>提供方:${item.pappName }</td>
			  	 	<td>调用方:${item.consumerIp }<br/>提供方:${item.providerIp }</td>
			  	 	<td class="invoke" invokeInterface="${item.serviceName }" invokeMethod="${item.spanName }" ></td>
			  	 	<c:if test="${item.cst!='0'}">
			  	 		<td class="traceTime" startTime="${item.cst}"></td>
			  	 		<td class="spendtimeitem" spendTime="${item.cspendTime}" startTime="${item.cst}">${item.cspendTime}</td>
		     		</c:if>
				    <c:if test="${item.cst=='0'}">
				     	<td class="traceTime" startTime="${item.pst}"></td>
			  	 		<td class="spendtimeitem" spendTime="${item.pspendTime}" startTime="${item.pst}">${item.pspendTime}</td>
				    </c:if>
			  	 	
			  	 </tr>
			  </c:forEach>
		  </c:if>
		  
		  
		  
		  
      </tbody>
    </table>
    
    <script type="text/javascript" charset="utf-8">
		
    jQuery(function(){
    	$("#example-advanced").treetable({ expandable: true ,initialState:"expanded"});
		 // Highlight selected row
		 $("#example-advanced tbody").on("mousedown", "tr", function() {
		   $(".selected").not(this).removeClass("selected");
		   $(this).toggleClass("selected");
		 });
		
		 
		 
		 var startTimeArray = new Array();
		 var spendTimeArray = new Array();
		 <c:forEach items="${traceLinkVoList}" var="item" varStatus="status">
		     <c:if test="${item.cst!='0'}">
		     	startTimeArray[${status.index}]=${item.cst};
		     	spendTimeArray[${status.index}]=Number(${item.cspendTime});
		     </c:if>
		     <c:if test="${item.cst=='0'}">
		     	startTimeArray[${status.index}]=${item.pst};
		     	spendTimeArray[${status.index}]=Number(${item.pspendTime});
		     </c:if>
		 </c:forEach>
		//定义了sort的比较函数
		  startTimeArray = startTimeArray.sort(function(a,b){
		  	return a-b;
		  });
		  spendTimeArray = spendTimeArray.sort(function(a,b){
		  	return a-b;
		  });
		 
		 var minStartTime =startTimeArray[0];
		$(".spendtimeitem").each(function(i){
			 var resulttablestr ="";
			 var spendTime = Number($(this).html());
			 var startTime = $(this).attr("startTime");
			 var fm1 = 25;
			 var fm2 = 50;
			 if(parseInt(spendTime)>5000){
				 fm1 = 1000;
			 }else if(parseInt(spendTime)>10000){
				 fm1 = 2000;
			 }else if(parseInt(spendTime)>100000){
				 fm1 = 20000;
			 }
			 
			 if((parseInt(startTime)-parseInt(minStartTime))>5000){
				 fm2 = 2000;
			 }else if((parseInt(startTime)-parseInt(minStartTime))>10000){
				 fm2 = 4000;
			 }else if((parseInt(startTime)-parseInt(minStartTime))>100000){
				 fm2 = 40000;
			 }
			   //this.src = "test" + i + ".jpg";
			
			 var divWidth = parseInt(spendTime)/fm1;
			 resulttablestr +="<div  class='timelinediv' style='width:"+(divWidth)+"px;margin-left:";
			 var marginValue = (parseInt(startTime)-parseInt(minStartTime))/fm2;
			 resulttablestr +=marginValue;
			 resulttablestr +="px;'></div>";
			 resulttablestr +="<span class='timelinespan'>"+spendTime+"ms</span>";
			$(this).html(resulttablestr);
		});
		 
		 
		 $(".traceTime").each(function(){
			 var resulttablestr ="";
			 var startTime = $(this).attr("startTime");
			 resulttablestr += getFormatDate(new Date(Number(startTime)),"yyyy-MM-dd hh:mm:ss");
			 
			$(this).html(resulttablestr);
		 });
		 
		 $(".invoke").each(function(){
			 var resulttablestr ="";
			 var invokeInterface = $(this).attr("invokeInterface");
			 var invokeMethod = $(this).attr("invokeMethod");
			 var tempStrs = invokeInterface.split('.');
			 resulttablestr += tempStrs[tempStrs.length-1] + "<br/>" + invokeMethod;
			 
			$(this).html(resulttablestr);
		 });
		 
    });
    
    Date.prototype.format = function (format) { 
	   	var o = { 
		   	"M+": this.getMonth() + 1, 
		   	"d+": this.getDate(), 
		   	"h+": this.getHours(), 
		   	"m+": this.getMinutes(), 
		   	"s+": this.getSeconds(), 
		   	"q+": Math.floor((this.getMonth() + 3) / 3), 
		   	"S": this.getMilliseconds() 
	   	} 
	   	if (/(y+)/.test(format)) { 
	   		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)); 
	   	} 
	   	for (var k in o) { 
		   	if (new RegExp("(" + k + ")").test(format)) { 
		   		format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)); 
		   	} 
	   	} 
	   	return format; 
	 } 
    
    
    /** 
     *转换日期对象为日期字符串 
     * @param l long值 
     * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss 
     * @return 符合要求的日期字符串 
     */ 
     function getFormatDate(date, pattern) { 
 	    if (date == undefined) { 
 	    date = new Date(); 
 	    } 
 	    if (pattern == undefined) { 
 	    pattern = "yyyy-MM-dd hh:mm:ss"; 
 	    } 
 	    return date.format(pattern); 
     }
	</script>
</body>


</html>
