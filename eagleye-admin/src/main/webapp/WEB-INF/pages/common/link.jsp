<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<script>
	var webRoot="<%=basePath%>";
	var basePath="<%=basePath%>";
</script>
	

<link rel="stylesheet" type="text/css" href="<%=basePath%>plugins/jquery.alert/jquery.alert.css" />

<link rel="stylesheet" href="<%=basePath %>plugins/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>



<link rel="stylesheet" href="<%=basePath%>plugins/DataTables/media/css/demo_page.css" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>plugins/DataTables/media/css/demo_table.css" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>plugins/DataTables/media/css/demo_table_jui.css" type="text/css"/>

<link rel="stylesheet" href="<%=basePath%>plugins/Validform/demo/css/style.css" type="text/css" />



<link rel="stylesheet" href="<%=basePath%>plugins/DataTables/examples_support/themes/smoothness/jquery-ui-1.8.4.custom.css" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>plugins/DataTables/extras/TableTools/media/css/TableTools.css" type="text/css"/>
<script type="text/javascript" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/jquery.alert/jquery.alert.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/jquery.alert/jquery.easydrag.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/kindeditor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/zTree/js/jquery.ztree.all-3.2.js"></script>
<!--  -->
<script type="text/javascript" src="<%=basePath%>plugins/jquery-validation/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/jquery-validation/additional-methods.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/jquery-validation/localization/messages_cn.js"></script>


<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>js/myTree.js"></script>
<script type="text/javascript" language="javascript" src="<%=basePath%>plugins/DataTables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript" src="<%=basePath%>plugins/DataTables/extras/TableTools/media/js/TableTools.js"></script>
<script type="text/javascript" language="javascript" src="<%=basePath%>plugins/DataTables/extras/TableTools/media/js/ZeroClipboard.js"></script>

<script type="text/javascript" src="<%=basePath%>plugins/jquery-jerichotab/jquery.jerichotab.js"></script>
<script type="text/javascript" src="<%=basePath%>js/menu.js"></script>
<link rel="stylesheet" href="<%=basePath%>plugins/jquery-jerichotab/jquery.jerichotab.css" type="text/css"/>

<!-- <link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>plugins/combogrid/css/smoothness/jquery-ui-1.8.9.custom.css"/> -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>plugins/combogrid/css/smoothness/jquery.ui.combogrid.css"/>
<script type="text/javascript" src="<%=basePath%>plugins/combogrid/js/jquery-ui-1.8.9.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/combogrid/js/jquery.ui.combogrid-1.6.2.js"></script>
<!-- 验证表单的js -->
<script type="text/javascript" src="<%=basePath%>plugins/Validform/demo/js/Validform_v5.1.js"></script>
<script type="text/javascript" src="<%=basePath%>plugins/Validform/demo/plugin/passwordStrength/passwordStrength-min.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath%>css/base.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css" />






