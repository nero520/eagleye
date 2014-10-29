<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.yougou.eagleye.admin.constants.AppConstants"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html lang="en" class="no-js">
<head>
<meta charset="utf-8"/>

<jsp:include flush="false" page="../common/link.jsp"></jsp:include>



<script type="text/javascript" charset="utf-8">
var oTable;
$(document).ready(function() {
	getAppNameList();
	
	var url = "<%=basePath%>log/query.html";
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
			"sSearch":"请输入日志内容进行检索",
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
		    var appName = $("#appName").val();
		    if (!$("#appName").val()){
		    	appName = "";
		    }
		    var date = $("#date").val();
		    if (!$("#date").val()){
		    	date = "";
		    }
			aoData.push({"name":"appName","value":appName});
			aoData.push({"name":"date","value":date});
		},
		"fnDrawCallback": function ( oSettings ) {
			//alert(document.documentElement.clientHeight);
			var rowHeigth = (document.documentElement.clientHeight-228)/15;
			$("#example tbody tr").css({"height":rowHeigth+"px","line-height":rowHeigth+"px"});
			$("#example").parent().css({"height":sScrollY});
			//showDefault();
			fnRowsChangeColor();
		},
		"sAjaxDataProp":"result",
		"aoColumns":[
			//{"mDataProp":"id","sTitle":"id","asSorting":["asc"]},
			{"mDataProp":"appName","sTitle":"应用名称","sClass":"center","sWidth":"300px"},
			{"mDataProp":"body","sTitle":"日志内容"},
			{"mDataProp":null,"bSortable":false,"sTitle":"创建日期","sClass":"center","sWidth":"200px",
				"fnRender":function(o){
					var startTime = Number(o.aData.timestamp);
					var startTimeStr = getFormatDate(new Date(startTime),"yyyy-MM-dd hh:mm:ss");
					return startTimeStr;
				}
			}
			
		],
		"sPaginationType":"full_numbers",
		"sDom":'<"H"<"toolbar">fr><"tableContent"t><"F"ipl>'
		} );
		$("div.toolbar").html('');
		$("div.tableContent").height(document.documentElement.clientHeight-108);
		$("div.tableContent").css({"white-space":"nowrap"});//不换行
		$("div #example_filter").css({width:"100%"});
		$("div #example_filter").prepend('<span style="margin-top: 6px; display: block; float: left; margin-left: 200px;">日期: <input type="text"  id="date"  name="date" style="height:25px;width:140px;" class="Wdate WdateTime" maxId="dateTo"   format="yyyy-MM-dd" />&nbsp;&nbsp; 应用名称: <select name="appName" id="appName" style="height:25px;width:140px;"></select></span>');
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


$(document).keyup(function(event){
  if(event.keyCode ==13){
	  oTable.fnDraw();
  }
});


$(function(){
	

});



//查询角色模板
function getAppNameList(){
	var url = "<%=basePath%>log/getAppNameList.html";
	$.get(url,function(data){
		var str = "<option value=''>请选择应用名</option>";
		$.each(data.result,function(i,item){
			str+="<option value="+item+">"+item+"</option>";
		});
		$("#appName").append(str);
	});
}

</script>

</head>
<body  class="ex_highlight_row body_bg_gray" style="overflow:hidden; width:100%; height:100%;">

	<div class="mt1 stripe_tbbg datatable_div" >
		<table id="example" class="display">
		</table>
	</div>

</body>
</html>


