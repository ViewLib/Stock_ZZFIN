/**
 * Created by 杨蕾 on 2017/11/12.
 */
define(['jquery', 'underscore', 'bootstrap_modal', 'bootstrap_table', 'text!templates/pagecount.html'], function ($, _, bootstrap_modal, bootstrap_table, pageCountTpl) {
        const defaultPageSize = 5;

        var init = function (pageIndex) {
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
                pageNumber:1,                       //初始化加载第一页，默认第一页
                pageSize: defaultPageSize,                       //每页的记录行数（*）
                pageList: [5, 10, 15, 20],        //可供选择的每页的行数（*）
                strictSearch: true,
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "userId",                     //每一行的唯一标识，一般为主键列
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                columns: [{
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
                pageindex:params.pageNumber
            };
            return temp;
        };

        // var request1 = function (pageIndex, pageSize) {
        //     return $.get("/zzfin/rest/user/query", {"index": pageIndex, "size": pageSize});
        // };
        //
        // var request2 = function () {
        //     return $.ajax({
        //         type: 'POST',
        //         url: '/zzfin/rest/user/getpagecount',
        //         contentType: 'application/json; charset=utf-8',
        //         data: '{"pageSize":' + defaultPageSize + '}',
        //         traditional: true
        //     });
        // };
        //
        // var promise = function (pageIndex, pageSize) {
        //     $.when(request1(1, defaultPageSize), request2()).done(function (data1, data2) {
        //         var pagetemplate = _.template(pageCountTpl, {pagecontent: data1 && data1[0], pagecount: data2 && data2[0]});
        //         $('#main-content').html(pagetemplate).find('a').click(function (e) {
        //             clickPageIndex(e);
        //         });
        //     });
        // };

        // var clickPageIndex = function (e) {
        //     e.preventDefault();
        //     request1($(e.currentTarget).html(), defaultPageSize).done(function (data) {
        //         $('.js_tpl_pagecontent').html(data);
        //     });
        // };

        var initialize = function () {
            $('.js_user_query').on('click', function () {
                //promise(1, defaultPageSize);
                init(1);
            });
        };
        return {initialize: initialize};
    }
);