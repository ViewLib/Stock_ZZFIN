<%--
  Created by IntelliJ IDEA.
  User: 杨蕾
  Date: 2017/11/11
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table>
    <c:forEach items="${users}" var="user">
        <tr>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </c:forEach>
</table>


<h1><strong>用户管理</strong></h1>
<table frame="above">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td>用户名:</td>
                    <td><input type="text" name="name" size="6"></td>
                    <td>用户ID:</td>
                    <td><input type="text" name="zjhaoma" id="zjnum" size="6"></td>
                    <td class="co2"><input type="button" value="查询"></td>
                </tr>
            </table>
        </td>
    </tr>


    <tr>
        <td>
            <table id="user_list" border="1" width="100%" id="table" rules="all">
                <tr>
                    <td>用户ID</td>
                    <td>注册手机</td>
                    <td>用户昵称</td>
                    <td>注册省市</td>
                    <td>用户年龄</td>
                    <td>注册日期</td>
                    <td>操作</td>
                </tr>

                <c:forEach items="${users}" var="user">
                    <tr id="delCell">
                        <td>${user.userId}</td>
                        <td>${user.moblie}</td>
                        <td>${user.nickName}</td>
                        <td>${user.area}</td>
                        <td>${user.age}</td>
                        <td>${user.createTime}</td>
                        <td><span><a href="###">编辑</a></span></td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>

    <%--<c:if test="${not empty pages}">--%>
        <%--<tr>--%>
            <%--<td>--%>
                <%--<table width="100%">--%>
                    <%--<td align="center" width=42%>>>--%>
                        <%--<c:forEach items="${pages}" var="pageIndex">--%>
                            <%--<span data-index="${pageIndex}">${pageIndex}</span>--%>
                        <%--</c:forEach>--%>
                    <%--</td>--%>
                    <%--<td>每页显示20条信息</td>--%>
                    <%--<td></td>--%>
                <%--</table>--%>
            <%--</td>--%>
        <%--</tr>--%>
    <%--</c:if>--%>
    <tr class="js_tpl_pagecount">
    </tr>

    <tr>
        <td align="center">
            copyright@zzfin
        </td>
    </tr>
</table>
<%--<c:if test="${not empty pageCount}">--%>
    <%--<text style="display:none;">${pageCount}</text>--%>
<%--</c:if>--%>