/**
 * Created by 杨蕾 on 2017/11/12.
 */
define(['jquery', 'underscore', 'bootstrap_modal', 'bootstrap_table', 'bootstrap_validator','text!templates/pagecount.html'], function ($, _, bootstrap_modal, bootstrap_table, bootstrap_validator, pageCountTpl) {
        const defaultPageSize = 5;
        var initialize = function () {
            $('.js_user_query').on('click', function () {
                $('#user_toolbar').html(_.template(pageCountTpl, {}));
                initTable(1);
                formValidInit();

                $("#btn_query").click(function () {
                    $("#user_table").bootstrapTable('refresh');
                });

                $("#btn_edit").click(function () {
                    showModal("修改用户", "update");
                });

                $("#btn_add").click(function () {
                    showModal("新增用户", "insert");
                    //setTimeout("$('#defaultForm').data('bootstrapValidator').resetForm(true)",300);
                });

                $("#btn_delete").click(function () {
                    deleteUsers();
                });
            });

            $('.js_status').on('click', function (e) {
                var status = $(e.currentTarget).data("status");
                postData(status, getPostData(), editOrAddUserSuccess);
            });

            $('#modal').on('hide', function (e) {
                $('#defaultForm').data('bootstrapValidator').resetForm(true);
            });
        };

        var initTable = function (pageIndex) {
            $('#user_table').bootstrapTable({
                url: '/zzfin/rest/user/search',
                method: 'post',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: queryParams,           //传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: defaultPageSize,                       //每页的记录行数（*）
                pageList: [5, 10, 15, 20],        //可供选择的每页的行数（*）
                strictSearch: true,
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "userId",                     //每一行的唯一标识，一般为主键列
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                columns: [{
                    checkbox: true
                }, {
                    field: 'userId',
                    title: 'userId',
                }, {
                    field: 'nickName',
                    title: 'nickName',
                }, {
                    field: 'moblie',
                    title: 'moblie',
                }, {
                    field: 'clientId',
                    title: 'clientId',
                }, {
                    field: 'area',
                    title: 'area',
                }, {
                    field: 'age',
                    title: 'age',
                }, {
                    field: 'createTime',
                    title: 'createTime',
                }]
            });
        };

        var queryParams = function (params) {
            var temp = {                 //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,    //页面大小
                offset: params.offset,  //页码
                maxrows: params.limit,
                pageindex: params.pageNumber,
                userid: $("#txt_search_userid").val(),
                mobile: $("#txt_search_mobile").val()
            };
            return temp;
        };

        var showModal = function (title, status) {
            if (status === "update") {
                var arrselections = $("#user_table").bootstrapTable('getSelections');
                if (arrselections.length > 1) {
                    alert('只能选择一行进行编辑');
                    return;
                }
                if (arrselections.length <= 0) {
                    alert('请选择一个要修改的用户');
                    return;
                }

                $("#txt_userid").val(arrselections[0].userId);
                $("#txt_userid").attr("readonly","readonly");
                $(".js_userid").show();

                $("#txt_mobile").val(arrselections[0].moblie);
                $("#txt_nickName").val(arrselections[0].nickName);
                $("#txt_area").val(arrselections[0].area);
                $("#txt_age").val(arrselections[0].age);
            }

            if(status === "insert"){
                $(".js_userid").hide();
                $("#txt_mobile").val('');
                $("#txt_nickName").val('');
                $("#txt_area").val('');
                $("#txt_age").val('');
            }

            $("#myModalLabel").text(title);
            $(".js_status").data("status", status);
            $('#modal').modal('show');
        };

        var postData = function (status, data, successFun) {
            var bootstrapValidator = $('#defaultForm').data('bootstrapValidator');
            bootstrapValidator.validate();
            if(bootstrapValidator.isValid()){
                $.ajax({
                    type: 'POST',
                    url: '/zzfin/rest/user/' + status,
                    contentType: 'application/json; charset=utf-8',
                    datType: "JSON",
                    data: JSON.stringify(data),
                    traditional: true,
                    success: successFun
                });
            }
        };

        var getPostData = function () {
            var postdata = {
                userId: $("#txt_userid").val(),
                mobile: $("#txt_mobile").val(),
                nickName:$("#txt_nickName").val(),
                area:$("#txt_area").val(),
                age:$("#txt_age").val()
            };

            return postdata;
        };

        var editOrAddUserSuccess = function (data) {
            if (data.resultCode === 200) {
                $('#modal').modal('hide');
                $("#user_table").bootstrapTable('refresh');
                alert("操作成功");
            }

            console.log(data);
        };

        var deleteUsers = function () {
            var arrselections = $("#user_table").bootstrapTable('getSelections');
            if (arrselections.length > 1) {
                alert('一次只能删除一个用户');
                return;
            }

            if (arrselections.length <= 0) {
                alert('请选择一个要删除的用户');
                return;
            }

            postData("delete", getDeleteUserData(arrselections), deleteUserSuccess);
        };

        var getDeleteUserData = function (arrselections) {
            var user = { userId:arrselections[0].userId};
            return user;
        };

        var deleteUserSuccess = function (data) {
            if (data.resultCode === 200) {
                $("#user_table").bootstrapTable('refresh');
                alert("操作成功");
            }

            console.log(data);
        };

        var formValidInit = function () {
            $('#defaultForm').bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    mobile: {
                        validators: {
                            notEmpty: {
                                message: '手机号码不能为空'
                            },
                            regexp: {
                                regexp: /[0-9-+]{5,20}$/,
                                message: '请输入正确的手机号码'
                            }
                        }
                    },
                    nickName: {
                        validators: {
                            notEmpty: {
                                message: '昵称不能为空'
                            },
                            stringLength: {
                                min: 2,
                                max: 30,
                                message: '用户名长度必须在2到30位之间'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z\u4e00-\u9fa5\.\s]+$/,
                                message: '不能包含数字和特殊字符'
                            }
                        }
                    }
                }
            });
        };

        return {initialize: initialize};
    }
);