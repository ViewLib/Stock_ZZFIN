<head>
    <meta charset="UTF-8">
    <title>用户管理界面</title>
    <link rel="stylesheet" href="../css/css1.css" type="text/css"/>
    <style type="text/css">
        img {
            float: right;
        }
    </style>

    <script type="text/javascript" language="javascript">
        var basePath = "http://localhost:8080/zzfin/";
        (function () {
            initUserList();
        })();

        function initUserList() {
            $.get(basePath + "/back/user_manager",
                    "",
                    function (data) {
                        var userList = eval(data);
                        var userTable = $('#user_list');
                        var userModel;
//                        table.empty();
                        if (userList.length == 0) {
                            return;
                        }
                        for (var i = 0; i < userList.length; i++) {
                            userModel = userList[i];
                            userTable.append("<tr>");
                            userTable.append("<td>"+userModel.userId+"</td>");
                            userTable.append("<td>"+userModel.moblie+"</td>");
                            userTable.append("<td>"+userModel.nickName+"</td>");
                            userTable.append("<td>"+userModel.area+"</td>");
                            userTable.append("<td>"+userModel.age+"</td>");
                            userTable.append("<td>"+userModel.createTime+"</td>");
                        }
//                        $('#show_tradingid')[0].value = tradings[0].mTradingId;
//                        table.change(function () {
//                            var selected = $(this).children('option:selected').val();
//                            $('#show_tradingid')[0].value = selected;
//                        })

                    });
        }

    </script>
</head>

<b><font color="blue">用户管理</font></b>
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
                <tr id="delCell">
                    <td>10000002</td>
                    <td>17863333330</td>
                    <td>哈哈</td>
                    <td>山东日照</td>
                    <td>28</td>
                    <td>2017-09-07 11:23:16</td>
                    <td><font><a href="2.html">编辑</a></font></td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            <table width="100%">
                <td align="center" width=42%>>>1 2 3 4 5 6 7 8<<</td>
                <td>每页显示20条信息</td>
                <td></td>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            copyright@zzfin
        </td>
    </tr>
</table>
</body>
</html>