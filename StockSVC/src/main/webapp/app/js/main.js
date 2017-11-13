/**
 * 加载库
 */
require.config({
    shim : {
        'underscore' : {
            exports : '_'
        },
        'backbone' : {
            deps : [ 'underscore', 'jquery' ],
            exports : 'Backbone'
        }
    },
    paths : {
        jquery : '../lib/jquery/jquery',
        underscore : '../lib/underscore/underscore',
        backbone : '../lib/backbone/backbone',
        text : '../lib/requirejs/text',
        templates : 'templates'
    }
});

/**
 * 加载APP
 */
require([ 'my-app', 'jquery','usermanager'], function(App, $, usermanager) {
    App.initialize();

    console.log(App);
    console.log($, '\n', _, '\n', Backbone);
    $('#page-sidebar-menu > li').click(function(e) {
        var menu = $('#page-sidebar-menu');
        var li = menu.find('li.active').removeClass('active');

        //添加选中 打开的样式
        $(this).addClass('active');
    });

    $('#page-sidebar-menu li a').click(function(e) {
        e.preventDefault();
        var url = this.href;
        if (url != null && url != 'javascript:;') {
            $.get(url, function(data) {
                $('#main-content').html(data);
            });
        }
    });

    usermanager.initialize();
});