<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.yougou.eagleye.admin.constants.AppConstants" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>


<html lang="en" class="no-js">
<head>
    <meta charset="utf-8"/>

    <jsp:include flush="false" page="../common/link.jsp"></jsp:include>


    <script type="text/javascript" charset="utf-8">
        var oTable;
        $(document).ready(function () {
            var url = "<%=basePath%>user/querySimple.html";
            var sScrollY = $(window).height() * 80 / 100;
            oTable = $('#example').dataTable({
                //"bProcessing": true,
                //默认的排序自动, 从0开始
                "aaSorting": [
                    [ 0, "desc" ]
                ],
                "sScrollY": sScrollY,
                "sScrollX": "100%",
                "bScrollAutoCss": true,
                "bPaginate": true, //是否开启分页
                "bScrollCollapse": true,
                "bServerSide": true,
                "bJQueryUI": true,
                "iDisplayLength": 15,
                "aLengthMenu": [15, 30, 60],
                "oLanguage": {
                    "sSearch": "请输入用户组名称进行检索",
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
                "sServerMethod": "POST",
                "fnDrawCallback": function (oSettings) {
                    //alert(document.documentElement.clientHeight);
                    var rowHeigth = (document.documentElement.clientHeight - 228) / 15;
                    $("#example tbody tr").css({"height": rowHeigth + "px", "line-height": rowHeigth + "px"});
                    $("#example").parent().css({"height": sScrollY});
                    //showDefault();
                    fnRowsChangeColor();
                },
                "sAjaxDataProp": "result",
                "aoColumns": [
                    //{"mDataProp":"id","sTitle":"id","asSorting":["asc"]},
                    {"mDataProp": "id", "sTitle": "ID", "sClass": "center"},
                    {"mDataProp": "userName", "sTitle": "姓名", "sClass": "center"},
                    {"mDataProp": "userAccount", "sTitle": "邮箱", "sClass": "center"},
                    {"mDataProp": "userPhone", "sTitle": "电话", "sClass": "center"},
                    {"mDataProp": "createTime", "sTitle": "创建时间", "sClass": "center"},
                    {"mDataProp": "updateTime", "sTitle": "最后修改时间", "sClass": "center"},
                    {
                        "mDataProp": null,
                        "bSortable": false,
                        "sTitle": "操作<span id='detail_show' class='DataTables_sort_icon css_right ui-icon ui-icon-triangle-1-s'></span>",
                        "sClass": "center",
                        "fnRender": function (o) {
                            return "<input type='button' alt='详情'  title='详情'  class='detail' detailId='" + o.aData.id + "'/>"
                                    + "<input type='button'  class='edit' editId='" + o.aData.id + "' rowId='" + o.iDataRow + "' alt='修改' title='修改'/>"
                                    + "<input type='button' class='delete' name='Submit' alt='删除'  title='删除' deleteId='" + o.aData.id + "' rowId='" + o.iDataRow + "'/>"
                        }
                    }
                ],
                "sPaginationType": "full_numbers",
                "sDom": '<"H"<"toolbar">fr><"tableContent"t><"F"ipl>'
            });
            $("div.toolbar").html('<input type="button"  class="add" title="新增" alt="新增"/>');
            $("div.tableContent").height(document.documentElement.clientHeight - 108);
            $("div.tableContent").css({"white-space": "nowrap"});//不换行
            //点击选中行变色
            function fnRowsChangeColor() {
                $("#example tr").each(function () {
                    $(this).click(function () {
                        $(this).toggleClass('row_selected');
                        $(this).find(":checkbox").attr("checked", $(this).hasClass("row_selected"));
                    });
                })
            }

        });

        var deleteUrl = "<%=basePath%>user/destroy/{id}.html";
        var editUrl = "<%=basePath%>user/{id}/edit.html";
        var addUrl = "<%=basePath%>user/add.html";
        var detailUrl = "<%=basePath%>user/show/{id}.html";


        $(function () {
            $(".add").click(function () {
                common.frame(addUrl, '新增用户', '400', '300');
                //$("#example").css("width","1098");
            });

            $(".edit").live("click", function () {
                var id = $(this).attr("editId");
                common.frame(editUrl.replace(/{id}/ig, id), '修改', '400', '300');
            });

            $(".detail").live("click", function () {
                var id = $(this).attr("detailId");
                common.frame(detailUrl.replace(/{id}/ig, id), "详情", '400', '250');
            });
            $(".delete").live("click", function () {
                var self = $(this);
                common.confirm("你确定要删除该信息吗?", function (result) {
                    if (result) {
                        var id = self.attr("deleteId");
                        var iRow = self.attr("rowId");
                        common.send(deleteUrl.replace(/{id}/ig, id), {}, function (data) {
                            if (data != null && data.flash == 'SUCCESS') {
                                common.message("删除成功");
                                oTable.fnDeleteRow(iRow, true);
                            } else if (data != null && data.flash == 'ERROR') {
                                common.error("删除失败");
                            }
                        });
                    }
                });
            });


        });

        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                oTable.fnDraw();
            }
        });
    </script>

</head>
<body class="ex_highlight_row body_bg_gray" style="overflow:hidden; width:100%; height:100%;">

<div class="mt1 stripe_tbbg datatable_div">
    <table id="example" class="display">
    </table>
</div>


</body>
</html>


