define(['jquery', 'underscore', 'bootstrap_modal', 'bootstrap_table', 'bootstrap_validator', 'text!templates/sqlmanager.html'], function ($, _, bootstrap_modal, bootstrap_table, bootstrap_validator, tpl) {
    const defaultPageSize = 5;
    var initialize = function () {
        $('.js_sql_query').on('click', function () {
            $('#search_toolbar').empty();
            $('#search_toolbar').html(_.template(tpl, {}));
            $('.js_search_table').html('<table id="search_table"></table>')
            initTable(1);
            formValidInit();

            $("#btn_edit").click(function () {
                showModal("修改脚本", "update");
            });

            $("#btn_add").click(function () {
                showModal("新增脚本", "insert");
            });

            $('.js_sql_status').on('click', function (e) {
                var status = $(e.currentTarget).data("status");
                postData(status, getPostData(), editOrAddUserSuccess);
            });

            $('#sql_modal').on('hide', function (e) {
                $('#sql_defaultForm').data('bootstrapValidator').resetForm(true);
            });
        });
    };

    var initTable = function (pageIndex) {
        $('#search_table').bootstrapTable({
            url: '/zzfin/rest/sql/search',
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
            uniqueId: "searchId",                     //每一行的唯一标识，一般为主键列
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'searchId',
                title: 'Id',
            }, {
                field: 'searchRelation',
                title: 'Relation',
            },{
                field: 'showType',
                title: 'ShowType',
            }, {
                field: 'searchType',
                title: 'Type',
            } , {
                field: 'searchWeight',
                title: 'Weight',
            }, {
                field: 'searchTitle',
                title: 'Title',
            }, {
                field: 'rankSql',
                title: 'RankSql',
            }, {
                field: 'searchDesc',
                title: 'Desc',
            }]
        });
    };

    var queryParams = function (params) {
        var temp = {                 //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,    //页面大小
            offset: params.offset,  //页码
            maxrows: params.limit,
            pageindex: params.pageNumber,
            searchId: $("#txt_search_searchId").val(),
            searchTitle: $("#txt_search_searchTitle").val()
        };
        return temp;
    };

    var formValidInit = function () {
        $('#sql_defaultForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                searchTitle: {
                    validators: {
                        notEmpty: {
                            message: '标题不能为空'
                        }
                    }
                }
            }
        });
    };

    var showModal = function (title, status) {
        if (status === "update") {
            var arrselections = $("#search_table").bootstrapTable('getSelections');
            if (arrselections.length > 1) {
                alert('只能选择一行进行编辑');
                return;
            }
            if (arrselections.length <= 0) {
                alert('请选择一个要修改的用户');
                return;
            }

            $("#txt_searchId").val(arrselections[0].searchId);
            $("#txt_searchId").attr("readonly","readonly");
            $(".js_searchId").show();

            $("#txt_searchRelation").val(arrselections[0].searchRelation);
            $("#txt_showType").val(arrselections[0].showType);
            $("#txt_searchType").val(arrselections[0].searchType);
            $("#txt_searchWeight").val(arrselections[0].searchWeight);
            $("#txt_searchTitle").val(arrselections[0].searchTitle);
            $("#txt_rankSql").val(arrselections[0].rankSql);
            $("#txt_searchDesc").val(arrselections[0].searchDesc);
        }

        if(status === "insert"){
            $(".js_searchId").hide();
            $("#txt_searchRelation").val('');
            $("#txt_showType").val('');
            $("#txt_searchType").val('');
            $("#txt_searchWeight").val('');
            $("#txt_searchTitle").val('');
            $("#txt_rankSql").val('');
            $("#txt_searchDesc").val('');
        }

        $("#sql_myModalLabel").text(title);
        $(".js_sql_status").data("status", status);
        $('#sql_modal').modal({'show':true});
    };

    var postData = function (status, data, successFun) {
        var bootstrapValidator = $('#sql_defaultForm').data('bootstrapValidator');
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()){
            $.ajax({
                type: 'POST',
                url: '/zzfin/rest/sql/' + status,
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
            searchId: $("#txt_searchId").val(),
            searchRelation: $("#txt_searchRelation").val(),
            showType:$("#txt_showType").val(),
            searchType:$("#txt_searchType").val(),
            searchWeight:$("#txt_searchWeight").val(),
            searchTitle:$("#txt_searchTitle").val(),
            rankSql:$("#txt_rankSql").val(),
            searchDesc:$("#txt_searchDesc").val()
        };

        return postdata;
    };

    var editOrAddUserSuccess = function (data) {
        if (data.resultCode === 200) {
            $('#sql_modal').modal('hide');
            $("#search_table").bootstrapTable('refresh');
            alert("操作成功");
        }

        console.log(data);
    };

    return {initialize: initialize};
});