<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.yougou.eagleye.admin.constants.AppConstants"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int spendTimeThreshold = AppConstants.SPEND_TIME_THRESHOLD;
String consumerType = AppConstants.DUBBO_INVOKE_TYPE_CONSUMER;
String providerType = AppConstants.DUBBO_INVOKE_TYPE_PROVIDER;
%>


<html lang="en" class="no-js">
<head>
<meta charset="utf-8"/>

<jsp:include flush="false" page="../common/link.jsp"></jsp:include>



<script type="text/javascript" charset="utf-8">
var oTable;
$(document).ready(function() {
	var url = "<%=basePath%>trace/query.html";
	var sScrollY = $(window).height()*80/100;
	oTable = $('#example').dataTable( {
		//"bProcessing": true,
		//默认的排序自动, 从0开始
		"aaSorting": [[ 0, "desc" ]] ,
		"sScrollY": sScrollY,
        "sScrollX": "100%",
        "bScrollAutoCss":true,
        "bPaginate": true, //是否开启分页
        "bScrollCollapse": true,
		"bServerSide": true,
		"bAutoWidth": true,
		"bJQueryUI": true,
		"iDisplayLength": 15,
		"aLengthMenu": [15, 30, 60],
		"oLanguage": {
			"sSearch":"traceId",
            "sLengthMenu": "每页 _MENU_ 条记录",
            "sZeroRecords": "没有搜索到相关内容",
            "sInfo": "当前 _START_ 至 _END_ 条 / 总共 _TOTAL_ 条记录",
            "sInfoEmpty": "当前 0 至 0 条 / 总共 0 条记录",
            "sInfoFiltered": "(filtered from _MAX_ total records)",
            "oPaginate": {
               "sFirst": "首页",
			   "sPrevious": "上一页",
			   "sNext": "下一页",
			   "sLast": "尾页"
			}
        },
		"sAjaxSource": url,
		"sServerMethod":"POST",
		"fnServerParams":function(aoData){
		    var serviceName = $("#serviceName").val();
		    if (!serviceName){
		    	serviceName = "";
		    }
		    var date = $("#date").val();
		    if (!date){
		    	date = "";
		    }
		    var spanName = $("#spanName").val();
		    if (!spanName){
		    	spanName = "";
		    }
		    var spanId = $("#spanId").val();
		    if (!spanId){
		    	spanId = "";
		    }
		    
		    var exceptions = $("#exceptions").val();
		    if (!exceptions){
		    	exceptions = "";
		    }
		    
		    var invokeTypeSelect = $("#invokeTypeSelect").val();
		    if (!invokeTypeSelect){
		    	invokeTypeSelect = "";
		    }
		    
		    var spendTime = $("#spendTime").val();
		    if (!spendTime){
		    	spendTime = "";
		    }
		    
		    var ip = $("#ip").val();
		    if (!ip){
		    	ip = "";
		    }
		    
			aoData.push({"name":"serviceName","value":serviceName});
			aoData.push({"name":"spanId","value":spanId});
			aoData.push({"name":"spanName","value":spanName});
			aoData.push({"name":"date","value":date});
			
			aoData.push({"name":"exceptions","value":exceptions});
			aoData.push({"name":"invokeTypeSelect","value":invokeTypeSelect});
			aoData.push({"name":"spendTime","value":spendTime});
			aoData.push({"name":"ip","value":ip});
		},
		"fnDrawCallback": function ( oSettings ) {
			//alert(document.documentElement.clientHeight);
			var rowHeigth = (document.documentElement.clientHeight-200)/15;
			$("#example tbody tr").css({"height":rowHeigth+"px","line-height":rowHeigth+"px"});
			$("#example").parent().css({"height":sScrollY});
			//showDefault();
			fnRowsChangeColor();
		},
		"sAjaxDataProp":"result",
		"aoColumns":[
			{"mDataProp":null,"bSortable":false,"sTitle":"TraceId & SpanId","sClass":"center","sWidth":"320px",
				"fnRender":function(o){
					var traceId = o.aData.traceId;
					var spanId = o.aData.spanId;
					return "TraceId : <a class='traceLink' detailId='"+traceId+"'>" + traceId + "</a><br/>SpanId : " + spanId;
				}
			},
			{"mDataProp":null,"bSortable":false,"sTitle":"接口 & 方法","sClass":"center",
				"fnRender":function(o){
					var serviceName = o.aData.serviceName;
					var spanName = o.aData.spanName;
					return serviceName + "<br/>[" + spanName + "]";
				}
			},
			{"mDataProp":null,"bSortable":false,"sTitle":"调用详情","sClass":"center","sWidth":"350px",
				"fnRender":function(o){
					var startTime = Number(o.aData.startTime);
					var startTimeStr = getFormatDate(new Date(startTime),"yyyy-MM-dd hh:mm:ss");
					var spendTime = Number(o.aData.spendTime);
					var ip = o.aData.ip;
					var invokeType = o.aData.invokeType;
					var invokeTypeStr = "";
					if(invokeType == "<%=consumerType%>"){
						invokeTypeStr = "<font color='blue'>调用方</font>";
					}
					if(invokeType == "<%=providerType%>"){
						invokeTypeStr = "<font color='green'>提供方</font>";
					}
					var spendTimeStr = "";
					if(spendTime >= <%=spendTimeThreshold %>){
						spendTimeStr += "<font color='red'>耗时:" + spendTime + "ms</font>";
					}else{
						spendTimeStr += "耗时:" + spendTime + "ms";
					}
					var result = invokeTypeStr + ":" + ip + "<br/>" + startTimeStr + " " + spendTimeStr;
					return result;
				}
			},
			{"mDataProp":null,"bSortable":false,"sTitle":"异常","sClass":"center","sWidth":"100px",
				"fnRender":function(o){
					var exceptionNum = Number(o.aData.exceptionNum);
					//var exceptions = o.aData.exceptions;
					var result = "无异常";
					if(exceptionNum > 0){
						result = "<font color='red'>异常数: " + exceptionNum + "<br/><div class='errorDetail' errorId='"+ o.aData.id +"'>查看详情</div></font>";
					}
					return result;
				}
			}
		],
		"sPaginationType":"full_numbers",
		"sDom":'<"H"<"toolbar">fr><"tableContent"t><"F"ipl>'
		} );
		$("div.toolbar").html('');
		$("div.tableContent").height(document.documentElement.clientHeight-108-25);
		
		$("div.tableContent").css({"white-space":"nowrap"});//不换行
		$("div #example_filter").css({width:"100%"});
		$("div .fg-toolbar").eq(0).attr("style","height: 55px;");
		
		//$("#exampleDiv").height(document.documentElement.clientHeight-108);
		//$("div .tableContent").attr("style","height: 630px;");
		var queryStr = '<span style="margin-top: 6px; display: block; float: left; margin-left: 10px;">';
			queryStr += '日期: <input type="text"  id="date"  name="date" style="height:25px;width:100px;" class="Wdate WdateTime" maxId="dateTo" format="yyyy-MM-dd" />&nbsp;&nbsp;';
			queryStr += '接口: <input type="text" name="serviceName" id="serviceName" style="height:25px;width:100px;" />&nbsp;&nbsp;';
			queryStr += '方法: <input type="text" name="spanName" id="spanName" style="height:25px;width:100px;" />&nbsp;&nbsp;';
			queryStr += '<span style="margin-left: 67px;">IP: <input type="text" name="ip" id="ip" style="height:25px;width:100px;" /></span>&nbsp;&nbsp;';
			queryStr += '</span>';
			queryStr += '<span style="margin-top: 6px; display: block; float: left; margin-left: 10px;">';
			queryStr += '异常: <input type="text" name="spanName" id="spanName" style="height:25px;width:100px;" />&nbsp;&nbsp;';
			queryStr += '类型: <select name="invokeTypeSelect" id="invokeTypeSelect" style="height:25px;width:100px;">';
			queryStr += '<option value="">请选择调用类型</option>';
			queryStr += '<option value="<%=consumerType%>">调用方</option>';
			queryStr += '<option value="<%=providerType%>">提供方</option>';
			queryStr += '</select>&nbsp;&nbsp;';
			queryStr += '耗时: <input type="text"  id="spendTime"  name="spendTime" style="height:25px;width:100px;" maxlength="10" />毫秒 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
			queryStr += 'SpanId: <input type="text" name="spanId" id="spanId" style="height:25px;width:100px;"/>';
			queryStr += '</span>';
		$("div #example_filter").prepend(queryStr);
		//点击选中行变色
		function fnRowsChangeColor (  ){
    		$("#example tr").each(function(){
				$(this).click( function() {
        			$(this).toggleClass('row_selected');
        			$(this).find(":checkbox").attr("checked",$(this).hasClass("row_selected"));
   				 } );
			})
		}
		
} );


$(function () {
    $("#spendTime").keyup(function () {
        //如果输入非数字，则替换为''，如果输入数字，则在每4位之后添加一个空格分隔
        this.value = this.value.replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
    });
    
    var detailUrl="<%=basePath%>trace/linkshow/{id}.html";
    $(".traceLink").live("click",function(){
		var id=$(this).attr("detailId");
		common.openWin(detailUrl.replace(/{id}/ig,id), id, "1");
	});
    
    var errorDetailUrl="<%=basePath%>trace/errorshow/{id}.html";
    $(".errorDetail").live("click",function(){
    	var id=$(this).attr("errorId");
		common.openWin(errorDetailUrl.replace(/{id}/ig,id), id, "1");
	});
    
});


$(document).keyup(function(event){
  if(event.keyCode ==13){
	  oTable.fnDraw();
  }
});

</script>

</head>
<body  class="ex_highlight_row body_bg_gray" style="overflow:hidden; width:100%; height:100%;">

	<div id="exampleDiv" class="mt1 stripe_tbbg datatable_div">
		<table id="example" class="display">
		</table>
	</div>

</body>
</html>


