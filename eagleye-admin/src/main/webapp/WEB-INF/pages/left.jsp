<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@page import="com.yougou.eagleye.admin.constants.AppConstants" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

    String u = (String) session.getAttribute(AppConstants.EAGLEYE_SESSION_USERNAME);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
    <jsp:include flush="false" page="common/link.jsp"></jsp:include>
    <script type="text/javascript" src="<%=basePath%>js/menu.js"></script>
    <script type="text/javascript">
        var jericho = {
            buildTree: function () {
                $('.functree li').each(function () {
                    if ($(this).is(':has(ul)'))
                        $(this).addClass('collapse');
                    if ($(this).next().is('li') || $(this).next().is('ul'))
                        $(this).css({  });
                }),

                        $('li.collapse>span').click(function () {
                            $(this).next("ul.fnIn").slideToggle("show").parent().siblings().children("ul.fnIn").slideUp("slow")
                        }),

                        $('.fnIn>li>a').css({ 'cursor': 'pointer' }).hover(function () {
                            $(this).css({ 'color': '#333'});
                        }, function () {
                            $(this).css({ 'color': '#000', 'text-decoration': 'none' });
                        }).click(function () {

                            /*设置选中高亮显示开始*/
                            var get_n = $(this).text();
                            $('.fnIn>li>a').each(function () {
                                if ($(this).text() == get_n) {
                                    $(this).css({ 'background-color': '#DBDBDB'});
                                } else {
                                    $(this).css({ 'background-color': '#fff'});
                                }
                            })
                            /*结束*/

                            window.parent.frames["right"].testRight($(this));
                        })
            }
        }
        $().ready(function () {
            d1 = new Date().getTime();
            var w = document.documentElement.clientWidth || document.body.clientWidth;
            var h = document.documentElement.clientHeight || document.body.clientHeight;
            $('.divLeft').css({ width: 240, height: h - 1, 'display': 'block' });
            $('#firefoxuser').html(($.browser.mozilla ? 'yes, your browser is Firefox' : 'oh, you are not using Firefox?'));
            $('#ieuser').html(($.browser.msie ? 'yes, your browser is Internet Explorer' : 'oh, you are not using Internet Explorer?'));
            jericho.buildTree();
        })
        //在火狐浏览器下下 按空格会导致部分菜单被隐藏,屏蔽key事件
        document.onkeydown = function (e) {
            try {
                e.preventDefault();
            } catch (e) {
                return false;
            }
        }
    </script>
</head>
<body>
<div class="divLeft">
    <ul class="functree">
        <!-- 一项菜单开始 -->
        <li class="main collapse">
            <%if (("admin").equals(u)) { %>
            <span class="modules"><i class="show_icon1">预警管理</i></span>
            <ul class="fnIn">
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>user/index.html" style="cursor: pointer; ">用户管理</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>group/index.html" style="cursor: pointer; ">用户组管理</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>app/index.html" style="cursor: pointer; ">应用管理</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>rule/index.html" style="cursor: pointer; ">预警规则管理</a>
                </li>
            </ul>
            <% } %>
            <span class="modules"><i class="show_icon1">应用预警日志管理</i></span>
            <ul class="fnIn">
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>log/index.html" style="cursor: pointer; ">日志检索</a>
                </li>
                <li>
                    <a datatype="iframe"
                       datalink="http://10.10.10.220:8080/kibana/index.html#dashboard/elasticsearch/AppError"
                       style="cursor: pointer; ">最近一天异常图表统计</a>
                </li>
            </ul>
            <span class="modules"><i class="show_icon1">Dubbo监控管理</i></span>
            <ul class="fnIn">
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>trace/index.html" style="cursor: pointer; ">跟踪日志检索</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>trace/errorinvoke.html" style="cursor: pointer; ">异常跟踪日志检索</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>trace/statistics.html"
                       style="cursor: pointer; ">跟踪统计</a>
                </li>
                <li>
                    <a datatype="iframe"
                       datalink="http://10.10.10.220:8080/kibana/index.html#dashboard/elasticsearch/DubboTrace"
                       style="cursor: pointer; ">最近一天跟踪图表统计</a>
                </li>
            </ul>
            <span class="modules"><i class="show_icon1">其他监控管理</i></span>
            <ul class="fnIn">
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>redis.html" style="cursor: pointer; ">redis监控</a>
                </li>
                <li>
                    <a datatype="iframe" datalink="<%=basePath %>other.html" style="cursor: pointer; ">其他监控</a>
                </li>
            </ul>
        </li>
    </ul>
</div>
</body>
</html>

