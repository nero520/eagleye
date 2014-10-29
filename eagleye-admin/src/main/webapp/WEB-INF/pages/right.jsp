<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<jsp:include flush="true" page="common/link.jsp"></jsp:include>

<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" />

<style type="text/css">
.main{margin-bottom:100px; background:#fff url(<%=basePath %>/images/welcome.jpg) no-repeat 250px 20px;float:left;display:block; width:100%;height:auto;}
.company{margin-bottom:120px;}
.company center{font-size:12px; width:165px; margin:0 auto;height:100%; margin-top:400px;}
</style>
<script type="text/javascript">
     var jericho = {
         showLoader: function() {
             $('#divMainLoader').css('display', 'block');
         },
         removeLoader: function() {
             $('#divMainLoader').css('display', 'none');
         },
         buildTabpanel: function() {
             $.fn.initJerichoTab({
                 renderTo: '.divRight',
                 uniqueId: 'myJerichoTab',
                 contentCss: { 'height': $('.divRight').height() - 30 },
                 tabs:[{
                         title: '欢迎使用此系统',
                         closeable: false,
                         //iconImg: 'images/jerichotab/jericho_icon.png',
                           data: { dataType: 'formtag', dataLink: '.main' }
                         //data: { dataType: 'html', dataLink: '<%=basePath %>/template/welcom.html' },
                      }],
                     activeTabIndex: 0,
                     loadOnce: true
                 });
             },
	testRight:function(obj){
		$.fn.jerichoTab.addTab({
                      tabFirer: $(obj),
                      title: $(obj).text(),
                      closeable: true,
                      iconImg: $(obj).attr('iconImg'),
                      data: {
                          loadOnce:$(obj).attr('isRefresh')!="0"?true:false,
                          dataType: $(obj).attr('dataType'),
                          dataLink: $(obj).attr('dataLink')
                      }
              }).showLoader().loadData();	
	},
			//删除所有tab方法
			applyCloseEvent:function(){
			//jericho.applyHover();
			
			
                $(".tabs ul li").each(function(i,num){                  
                	if(i!=0){    
                		var self=$(this);
                		setTimeout(function(){
	                		$('#jerichotabholder_' + self.attr('id').replace('jerichotab_', '')).css({ 'display': 'none' });
	                		self.applySlider().remove();//移除操作
	                		//tabHash.slice(tabHash[i], 1);
                		},100*i);
						
                		tabHash.length=1;
                	}
                }) 
                 
				$.fn.setTabActive(0).adaptSlider().loadData();//初始化欢迎使用界面
					
				
			},
			//删除其他tab方法
			applyCloseEventOther:function(obj){
			
                $(".tabs ul li").each(function(i,num){   

                	if(i!=0&&$(this).attr("id")!=obj){ 
              
                		var self=$(this);
                		setTimeout(function(){
	                		$('#jerichotabholder_' + self.attr('id').replace('jerichotab_', '')).css({ 'display': 'none' });
	                		self.applySlider().remove();//移除操作
                		 	tabHash.slice(tabHash[i], 1);
                		},100*i);
					tabHash.length=2;
                	}
                })   
				
			},
			
			//双击删除tab方法
			closeSingle:function(obj){
			
				if ($(".tabs ul li").length == 0) return;
                $(".tabs ul li").each(function(i,num){   

                	if(i!=0&&$(this).attr("id")==obj){ 
              
                		var self=$(this);
                		var s=tabHash[i];
                		setTimeout(function(){
	                		$('#jerichotabholder_' + self.attr('id').replace('jerichotab_', '')).css({ 'display': 'none' });
	                		
		                    $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected').prev().swapTabEnable().loadData();
		                     
	                		self.applySlider().remove();
                		 	tabHash.splice(s,1);
                		 	//tabHash.length=tabHash.length-1;
                		},100);
					
                	}
                })
			}
			
         }
         
         
        $().ready(function() {
            d1 = new Date().getTime();
            jericho.showLoader();
            var w = document.documentElement.clientWidth||document.body.clientWidth;
            var h = document.documentElement.clientHeight||document.body.clientHeight;
            $('.divRight').css({ width: w-4, height: h, 'display': 'block', 'margin-left': 0 });
            $('#firefoxuser').html(($.browser.mozilla ? 'yes, your browser is Firefox' : 'oh, you are not using Firefox?'));
            $('#ieuser').html(($.browser.msie ? 'yes, your browser is Internet Explorer' : 'oh, you are not using Internet Explorer?'));

            jericho.buildTabpanel();
            jericho.removeLoader();
        })
        $(window).resize(function() {
        	var w = document.documentElement.clientWidth||document.body.clientWidth;
            $('.divRight').css({ width: w-4 });
        })
	function testRight(obj){
	    $.fn.jerichoTab.addTab({
	                    tabFirer: $(obj),
	                    title: $(obj).text(),
	                    closeable: true,
	                    iconImg: $(obj).attr('iconImg'),
	                    data: {
	                     loadOnce:$(obj).attr('isRefresh')!="0"?true:false,
	                        dataType: $(obj).attr('dataType'),
	                        dataLink: $(obj).attr('dataLink')
	                    }
	                }).showLoader().loadData();
	}
			//关闭选中的Tag
	function removeSelectedTab(){
		 var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
	               lastTab.prev().swapTabEnable().loadData();
	                  //clear the information of the removed tab from tabHash
	                  var tabHash=$.fn.jerichoTab.tabHash;
	                  for (var t in tabHash) {
	                      if (tabHash[t].tabId == $(me).attr('id')) {
	                          if (tabHash[t].tabFirer != null)
	                              tabHash[t].tabFirer.removeAttr('jerichotabindex');
	                          tabHash.splice(t, 1);
	                      }
	                  }
	                  //adapt slider
	                  lastTab.applySlider().remove();
	                  //remove contentholder
	                  $('#jerichotabholder_' + lastTab.attr('id').replace('jerichotab_', '')).remove();
	}
				
				
 $(function(){
 	$(document).keypress(function(e){if(e.keyCode==13)return false;});
 });				
	</script>
</head>

<body>

<div class="divRight" style="background:#ddd">
 <div class="main" style="position:absolute; height:100%;overflow:auto">
	<div class="alert alert-dismissable alert-info">
	  <strong>预警管理 功能说明</strong>
	</div>
	<table class="table table-striped table-hover ">
	  <thead>
	    <tr class="danger">
	      <th>#</th>
	      <th>功能点</th>
	      <th>描述</th>
	      <th>备注</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr>
	      <td>1</td>
	      <td>用户组管理</td>
	      <td>针对某个开发组建立群组,在发送预警信息时,是通过用户组进行发送.比如:cms,对应的cms开发组</td>
	      <td>列表管理可以通过针对用户组名称进行模糊检索</td>
	    </tr>
	    <tr>
	      <td>2</td>
	      <td>成员预警管理</td>
	      <td>在每一个用户组下面可以管理组内的成员,成员的重要信息,比如:邮箱,手机是必填项.预警会根据这些信息进行投放.</td>
	      <td>一个用户组可以包含多名成员.目前成员是静态列表在页面中进行维护.</td>
	    </tr>
	    <tr>
	      <td>3</td>
	      <td>应用管理</td>
	      <td>对应用信息进行管理,该应用名称必须与日志收集的log4jappender中定义的appName保持一致, 否则对应不上相应的预警规则.</td>
	      <td>每一个应用可以由多个用户组去维护.</td>
	    </tr>
	    <tr>
	      <td>4</td>
	      <td>预警规则管理</td>
	      <td>预警规则中可以填写正常的正则表达式,如果收集的异常日志满足预警规则,则会根据相应的路由进行预警.</td>
	      <td>每个预警规则可以适用于多个应用, 目前的预警是20分钟进行一次预警,预警频率可以在/etc/yougouconf/eagleye-admin/config.properties中进行修改</td>
	    </tr>
	  </tbody>
	</table> 
	<br/><br/>
	
	<div class="alert alert-dismissable alert-info">
	  <strong>应用预警日志管理 功能说明</strong>
	</div>
	<table class="table table-striped table-hover ">
	  <thead>
	    <tr class="danger">
	      <th>#</th>
	      <th>功能点</th>
	      <th>描述</th>
	      <th>备注</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr>
	      <td>1</td>
	      <td>日志检索</td>
	      <td>可以通过日期,应用名称,日志内容进行检索,默认查询当天的日志.</td>
	      <td>日志收集是通过log4j的自定义appender往redis里面发送,服务端接收后存入elasticsearch中,每天凌晨5点会删除七天以前的日志数据</td>
	    </tr>
	  </tbody>
	</table> 
	<br/><br/>
	
	<div class="alert alert-dismissable alert-info">
	  <strong>Dubbo监控管理 功能说明</strong>
	</div>
	<table class="table table-striped table-hover ">
	  <thead>
	    <tr class="danger">
	      <th>#</th>
	      <th>功能点</th>
	      <th>描述</th>
	      <th>备注</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr>
	      <td>1</td>
	      <td>跟踪日志检索</td>
	      <td>可以查询所有被记录的跟踪日志,大概每天的数量级在5000万级别.可以针对时间区间,运行时长,异常信息,接口名,方法名,ip等信息进行精确检索</td>
	      <td>所有跟踪日志是存储在elasticsearch中,每天凌晨五点会清楚七天之前的历史数据.</td>
	    </tr>
	    <tr>
	      <td>2</td>
	      <td>调用链详情</td>
	      <td>在跟踪日志检索之后可以在列表页中通过点击TraceId进入到某一个调用链的详情页面.</td>
	      <td>详情页面是整个调用链的调用关系和每次调用的详细情况.</td>
	    </tr>
	    <tr>
	      <td>3</td>
	      <td>异常跟踪日志检索</td>
	      <td>这里只标注运行时间超过10秒,或则有异常信息的dubbo调用.</td>
	      <td>所有预警信息都是针对这里面的异常进行预警, 预警间隔为1个小时统计预警一次.</td>
	    </tr>
	    <tr>
	      <td>4</td>
	      <td>日常详情</td>
	      <td>在跟踪日志检索列表中,如果调用中捕获了异常,可以通过点击异常链接进入到异常详情页</td>
	      <td>因为异常信息比较多, 所有需要进入到详情页面,可以查看引起dubbo异常的详细信息.</td>
	    </tr>
	    <tr>
	      <td>5</td>
	      <td>跟踪统计</td>
	      <td>默认统计当天所有接口,方法被调用的次数,也可以选定具体的日志进行统计.输入接口或方法的关键字可以进行模糊查询.</td>
	      <td>默认打开页面时不进行检索,直接点击搜索图标可以进行检索.</td>
	    </tr>
	  </tbody>
	</table> 
	<br/><br/>
</div>

</div>


</body>
</html>
