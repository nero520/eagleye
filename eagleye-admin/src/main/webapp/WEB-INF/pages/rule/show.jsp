<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<title>添加用户</title>
<jsp:include flush="false" page="../common/link.jsp"></jsp:include>
<link rel="stylesheet"
	href="<%=basePath%>/plugins/Validform/demo/css/style.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>/plugins/Validform/demo/css/demo.css"
	type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css" />

<style type="text/css">
</style>

</head>
<body>
<div class="col-lg-12">
  <div class="well bs-component">
    <form class="form-horizontal" id="myForm" action="<%=basePath %>/rule/update.html" method="post">
      <fieldset>
        <legend></legend>
        <div class="form-group">
          <label class="col-lg-2 control-label">名称:</label>
          <div class="col-lg-10">
            <input type="text" value="${rule.ruleKeyword}" disabled="" class="form-control inputxt" id="ruleKeyword" name="ruleKeyword" placeholder="" datatype="s1-20" nullmsg="请输入名称" errormsg="名称1-20个字符之间" />
        	</div>
        </div>
        <div class="form-group">
          <label class="col-lg-2 control-label">关键字:</label>
          <div class="col-lg-10">
            <textarea class="form-control" disabled="" rows="3" id="description" name="description" datatype="s0-100" errormsg="备注在100个字符之内">${rule.description}</textarea>
          </div>
        </div>
       
        <div class="form-group">
          <label for="select" class="col-lg-2 control-label">对应应用:</label>
          <div class="col-lg-10">
            <select id="apps" name="apps" multiple="" disabled="" class="form-control">
              <c:forEach items="${apps}" var="item" >
              	<c:set var="appId"><c:out value="${item.id}" /></c:set>
              	<option value="${item.id}" <c:if test="${rule.appMap[appId]!=null}">selected</c:if> >${item.appName}</option>
			  </c:forEach>
            </select>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</div>
</body>
</html>
