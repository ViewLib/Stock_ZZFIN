/**
 * Created by 杨蕾 on 2017/11/12.
 */
define(['jquery','underscore', 'text!templates/pagecount.html'], function($,_,pageCountTpl){
        var ajaxGet = function (pageIndex, pageSize) {
            $.get("/zzfin/rest/user/query",{"index":pageIndex,"size":pageSize}, function (response,status,xhr) {
                if(status === 'success'){
                    $('#main-content').html(response);
                }
            });
        };

        var getPageCount = function () {
            $.ajax({
                type: 'POST',
                url: '/zzfin/rest/user/getpagecount',
                contentType: 'application/json; charset=utf-8',
                data: '{"pageSize":20}',
                traditional:true,
                success: function (data) {
                    return data;
                }
            });
        };

        var queryUsersWithPage = function (pageIndex) {
           if(pageIndex <= 0){
               pageIndex = 1;
           }

            ajaxGet(pageIndex, 20);
        };

        var initialize = function () {
            $('.js_user_query').on('click', function () {
                ajaxGet(1, 20);

                // 初始化页码元素
                // var pageCount = getPageCount();
                var pageCount = 10;
                if(pageCount > 0){
                    $('.js_tpl_pagecount').html(_.template(pageCountTpl, {pageCount:pageCount}));
                }
            });
        };
        return {initialize:initialize};
    }
);