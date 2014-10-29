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
	var url = "<%=basePath%>trace/querystatistics.html";
	var sScrollY = $(window).height()*80/100;
	oTable = $('#example').dataTable( {
		//"bProcessing": true,
		//默认的排序自动, 从0开始
		"aaSorting": [[ 0, "desc" ]] ,
		"sScrollY": sScrollY,
        "sScrollX": "100%",
        "bScrollAutoCss":true,
        "bPaginate": false, //是否开启分页
        "bScrollCollapse": true,
		"bServerSide": true,
		"bAutoWidth": true,
		"bJQueryUI": true,
		"iDisplayLength": 100,
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
		    /* if (!date){
		    	date = "";
		    } */
		    var spanName = $("#spanName").val();
		    if (!spanName){
		    	spanName = "";
		    }
		    
		    if((spanName == "" && serviceName != "") || (spanName != "" && serviceName == "")){
		    	alert("方法和接口必须同时填写 或者 都不填写");
		    	return;
		    }
		    
			aoData.push({"name":"serviceName","value":serviceName});
			aoData.push({"name":"spanName","value":spanName});
			aoData.push({"name":"date","value":date});
			
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
			{"mDataProp":"appName","bSortable":false,"sTitle":"应用名","sClass":"center","sWidth":"270px"},
			{"mDataProp":null,"bSortable":false,"sTitle":"接口 & 方法","sClass":"center",
				"fnRender":function(o){
					var serviceName = o.aData.serviceName;
					var spanName = o.aData.spanName;
					return serviceName + "<br/>[" + spanName + "]";
				}
			},
			{"mDataProp":"invokeCount","bSortable":false,"sTitle":"调用次数","sClass":"center","sWidth":"150px"}
			//{"mDataProp":"ip","bSortable":false,"sTitle":"IP","sClass":"center","sWidth":"150px"}
		],
		"sPaginationType":"full_numbers",
		"sDom":'<"H"<"toolbar">fr><"tableContent"t><"F"ipl>'
		} );
		$("div.toolbar").html('');
		$("div.tableContent").height(document.documentElement.clientHeight-108);
		$("div.tableContent").css({"white-space":"nowrap"});//不换行
		$("div #example_filter").css({width:"100%"});
		var queryStr = '<span style="margin-top: 6px; display: block; float: left; margin-left: 100px;">';
			queryStr += '日期: <input type="text"  id="date"  name="date" style="height:25px;width:140px;" class="Wdate WdateTime" maxId="dateTo" format="yyyy-MM-dd" />&nbsp;&nbsp;';
			//queryStr += '接口: <input type="text" name="serviceName" id="serviceName" style="height:25px;width:140px;" />&nbsp;&nbsp;';
			//queryStr += '方法: <input type="text" name="spanName" id="spanName" style="height:25px;width:140px;" />&nbsp;&nbsp;';
			queryStr += '</span>';
		$("div #example_filter").prepend(queryStr);
		//点击选中行变色
		function fnRowsChangeColor (){
    		$("#example tr").each(function(){
				$(this).click( function() {
        			$(this).toggleClass('row_selected');
   				 } );
			});
		}
		
} );


$(function () {
    $("#spendTime").keyup(function () {
        //如果输入非数字，则替换为''，如果输入数字，则在每4位之后添加一个空格分隔
        this.value = this.value.replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
    });
    
});




</script>

</head>
<body  class="ex_highlight_row body_bg_gray" style="overflow:hidden; width:100%; height:100%;">

	<div class="mt1 stripe_tbbg datatable_div" >
		<table id="example" class="display">
		</table>
	</div>

</body>
</html>


