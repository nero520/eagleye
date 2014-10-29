<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.yougou.eagleye.admin.constants.AppConstants"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/" ;
	//String callback = request.getParameter("callback");
	if(session.getAttribute(AppConstants.EAGLEYE_SESSION_USERNAME)!=null){
	response.sendRedirect(basePath+"index.html");
}
%>


<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->

<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<!-- BEGIN HEAD -->

<head>

	<meta charset="utf-8" />

	<title>EAGLEYE</title>

	<meta content="width=device-width, initial-scale=1.0" name="viewport" />

	<meta content="" name="description" />

	<meta content="" name="author" />

	<!-- BEGIN GLOBAL MANDATORY STYLES -->

	<link href="<%=basePath%>/plugins/metronic/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/style.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/default.css" rel="stylesheet" type="text/css" id="style_color"/>

	<link href="<%=basePath%>/plugins/metronic/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES -->

	<link href="<%=basePath%>/plugins/metronic/media/css/login.css" rel="stylesheet" type="text/css"/>

	<!-- END PAGE LEVEL STYLES -->

	<link rel="shortcut icon" href="<%=basePath%>/plugins/metronic/media/image/favicon.ico" />

</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="login">
	<div class="logo">

	</div>

	<!-- END LOGO -->

	<!-- BEGIN LOGIN -->

	<div class="content">

		<!-- BEGIN LOGIN FORM -->

		<form class="form-vertical login-form" action="<%=basePath%>/login.html" method="post">

			<h3 class="form-title">Eagleye统一监控平台</h3>

			<div class="alert alert-error hide">

				<button class="close" data-dismiss="alert"></button>

				<span>请输入您的账号和密码.</span>

			</div>

			<div class="control-group">

				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->

				<label class="control-label visible-ie8 visible-ie9">账号</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-user"></i>

						<input class="m-wrap placeholder-no-fix" type="text" placeholder="账号" name="userAccount"/>

					</div>

				</div>

			</div>

			<div class="control-group">

				<label class="control-label visible-ie8 visible-ie9">密码</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-lock"></i>

						<input class="m-wrap placeholder-no-fix" type="password" placeholder="密码" name="userPwd" />

					</div>

				</div>

			</div>

			<div class="form-actions">

				<label class="checkbox">

				</label>

				<button type="submit" class="btn green pull-right">

				登陆 <i class="m-icon-swapright m-icon-white"></i>

				</button>            
			</div>

		</form>

		<!-- END LOGIN FORM -->        

	</div>

	<!-- END LOGIN -->

	<!-- BEGIN COPYRIGHT -->

	<div class="copyright">

		2014 &copy; Yougou Technology Co.,Ltd. <br/> <font color="red"><b>点击回车直接登录</b></font><br/> <font color="red"><b>所有查询可以点击回车执行</b></font>

	</div>

	<!-- END COPYRIGHT -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<!-- BEGIN CORE PLUGINS -->

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      

	<script src="<%=basePath%>/plugins/metronic/media/js/bootstrap.min.js" type="text/javascript"></script>

	<!--[if lt IE 9]>

	<script src="<%=basePath%>/plugins/metronic/media/js/excanvas.min.js"></script>

	<script src="<%=basePath%>/plugins/metronic/media/js/respond.min.js"></script>  

	<![endif]-->   

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery.blockui.min.js" type="text/javascript"></script>  

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery.cookie.min.js" type="text/javascript"></script>

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery.uniform.min.js" type="text/javascript" ></script>

	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->

	<script src="<%=basePath%>/plugins/metronic/media/js/jquery.validate.min.js" type="text/javascript"></script>

	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->

	<script src="<%=basePath%>/plugins/metronic/media/js/app.js" type="text/javascript"></script>

	<script src="<%=basePath%>/plugins/metronic/media/js/login.js" type="text/javascript"></script>      

	<!-- END PAGE LEVEL SCRIPTS --> 

	<script>

		jQuery(document).ready(function() {     

		  /* App.init();

		  Login.init(); */

		});
		
		$(document).keyup(function(event){
			  if(event.keyCode ==13){
				  $(".login-form").submit();
			  }
		});

	</script>

	<!-- END JAVASCRIPTS -->

<script type="text/javascript">  var _gaq = _gaq || [];  _gaq.push(['_setAccount', 'UA-37564768-1']);  _gaq.push(['_setDomainName', 'keenthemes.com']);  _gaq.push(['_setAllowLinker', true]);  _gaq.push(['_trackPageview']);  (function() {    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);  })();</script></body>

<!-- END BODY -->

</html>