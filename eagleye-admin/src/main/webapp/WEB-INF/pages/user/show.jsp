<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>查看用户</title>
    <jsp:include flush="false" page="../common/link.jsp"></jsp:include>
    <link rel="stylesheet"
          href="<%=basePath%>/plugins/Validform/demo/css/style.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="<%=basePath%>/plugins/Validform/demo/css/demo.css"
          type="text/css"/>
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>css/bootstrap.css"/>

    <style type="text/css">
    </style>

</head>
<body>
<div class="well bs-component">
    <form class="form-horizontal" id="myForm" action="<%=basePath %>/user/create.html" method="post">
        <br>

        <div class="form-group">
            <label class="col-lg-2 control-label">姓名:</label>

            <div class="col-lg-9">
                <input type="text" class="form-control inputxt" id="userName" name="userName" placeholder=""
                       disabled="disabled" value="${user.userName}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-lg-2 control-label">邮箱:</label>

            <div class="col-lg-9">
                <input type="text" class="form-control inputxt" id="userAccount" name="userAccount" placeholder=""
                       disabled="disabled" value="${user.userAccount}"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-lg-2 control-label">手机:</label>

            <div class="col-lg-9">
                <input type="text" class="form-control inputxt" id="userPhone" name="userPhone" placeholder=""
                       disabled="disabled" value="${user.userPhone}"/>
            </div>
        </div>
    </form>
</div>
</body>
</html>
