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
        },
        'bootstrap':{
            deps: [
                'jquery',
                'css!../../assets/plugins/bootstrap/css/bootstrap.min.css'
            ]
        },
        'bootstrap_modal':{
            deps: [
                'bootstrap',
                'bootstrap_modalmanager',
                'css!../../assets/plugins/bootstrap-modal/css/bootstrap-modal.css',
                'css!../../assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css'
            ]
        },
        'bootstrap_table':{
            deps:[
                'bootstrap',
                '../../assets/plugins/bootstrap-table/js/local/bootstrap-table-zh-CN',
                'css!../../assets/plugins/bootstrap-table/css/bootstrap-table.css'
            ]
        },
        'bootstrap_validator':{
            deps:[
                'bootstrap',
                'css!../../assets/plugins/bootstrap-validator/css/bootstrapValidator.css'
            ]
        }
    },
    paths : {
        jquery : '../lib/jquery/jquery',
        underscore : '../lib/underscore/underscore',
        backbone : '../lib/backbone/backbone',
        text : '../lib/requirejs/text',
        templates : 'templates',
        bootstrap : '../../assets/plugins/bootstrap/js/bootstrap.min',
        bootstrap_modal : '../../assets/plugins/bootstrap-modal/js/bootstrap-modal',
        bootstrap_modalmanager : '../../assets/plugins/bootstrap-modal/js/bootstrap-modalmanager',
        bootstrap_table:'../../assets/plugins/bootstrap-table/js/bootstrap-table',
        bootstrap_validator:'../../assets/plugins/bootstrap-validator/js/bootstrapValidator'
    },
    map: {
        '*': {
            'css': '../lib/requirejs/css'
        }
    }
});

/**
 * 加载APP
 */
require([ 'my-app', 'jquery','usermanager', 'sqlmanager'], function(App, $, usermanager, sqlmanager) {
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
    sqlmanager.initialize();
});