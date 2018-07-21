var Datatable = function () {
    var dataGrid;
    var table;
    //var tableOptions;
    return {
        init: function (options) {
            if (!$().bootstrapTable) {
                return;
            }
            options = $.extend(true, {
                src: "",
                bootstrapTable: {
                    $this: options.src,
                    url: "",
                    method: 'post',
                    dataType: "json",
                    //striped: true, //是否显示行间隔色
                    locale: "zh-US", //表格汉化
                    //data: data,
                    //search: false,
                    pagination: true, //分页
                    pageNumber: 1,
                    pageSize: 10,
                    pageList: [10, 25, 50, 100],
                    toolbar: '.toolbar',
                    queryParams: function (params) {   //设置查询参数
                        var param = {
                            page: params.pageNumber,
                            size: params.pageSize,
                        };
                        if(params.sortName) {
                            param.sort = params.sortName + ("," + params.sortOrder);
                        }
                        // debugger;
                        var $params = TbAssist.filterParams(this.$this.closest(".ibox").prev(".form-inline"));
                        param =  $.extend({}, param, $params)
                        return param;
                    },
                    queryParamsType: "undefined",
                    contentType: 'application/x-www-form-urlencoded',//參數傳遞方式改爲form表單提交
                    fixedColumns: true,//是否开始锁定
                    fixedNumber: 0,    //固定列
                    sidePagination: "server", //服务端处理分页,
                    columns:[
                    	{
                    		field: 'id',
                    	    sortable:false,
                            width: '80px',
                    	}
                    ]
                }
            }, options);
            //tableOptions = options;
            table = $(options.src);											//渲染的表格元素id
            dataGrid = table.bootstrapTable("destroy").bootstrapTable(options.bootstrapTable);					//初始化表格数据
        },
        getTable: function () {
        	if(!table) table = $('[data-toggle="table"]');
            return table;
        },
        refreshTable: function (obj) {
            var params = obj || {};             //渲染的表格元素id
            if(!table) table = $('[data-toggle="table"]');
            //console.log(params)
            table.bootstrapTable("refresh", {query: params});
        }
    }

},
grid = new Datatable();
//============================================固定列的操作下拉显示和checkbox选中问题====================
(function ($) {
        var $table = $('#example-table');

		//复选框选择所有#获取全选数据
		$(document).on('click', '#checkBoxAll', function () {
			var checkboxThis = this.checked;
            $('input[type="checkbox"][name="id\\[\\]"]:checkbox').prop('checked',checkboxThis);
			//获取所有数据
            var checkboxValue = $('.fixed-table-column input[type="checkbox"][name="id\\[\\]"]:checked').map(function() { return this.value; }).get();
            if(!checkboxValue){
                $(".table-group-actions span").html('0');
            }
            //alert (checkboxValue);
            //获取全选中数量
            var num_checked = $('.fixed-table-column input[type="checkbox"][name="id\\[\\]"]:checked').length;
            $(".table-group-actions span").html(num_checked);
		});

        $(document).on('click', 'input[type="checkbox"][name="id\\[\\]"]', function () {
            var num_checked = $('.fixed-table-column input[type="checkbox"][name="id\\[\\]"]:checked').length;
            var nums_check = $('.fixed-table-column input[type="checkbox"][name="id\\[\\]"]').length;
            if(num_checked<nums_check){
                $('#checkBoxAll:checkbox').prop('checked',false);
            }else{
                $('#checkBoxAll:checkbox').prop('checked',true);
            }
            $(".table-group-actions span").html(num_checked);
        });

        //翻页时取消全选
        $table.on('page-change.bs.table', function () {
            $("#checkBoxAll").prop('checked', false);
            $(".table-group-actions span").html('0');
        });

        //下拉菜单显示问题
        var dropdownMenu;
        $(window).on('show.bs.dropdown', function (e) {
            dropdownMenu = $(e.target).find('.dropdown-menu-tree');
            $('body').append(dropdownMenu.detach());
            var eOffset = $(e.target).offset();
            dropdownMenu.css({
                'display': 'block',
                'top': eOffset.top + $(e.target).outerHeight(),
                'left': eOffset.left
            });
        });
        $(window).on('hide.bs.dropdown', function (e) {
            $(e.target).append(dropdownMenu.detach());
            dropdownMenu.hide();
        });

    $(document).on('click','.modal-footer>button.btn-success',function (e) {
        e.preventDefault();
        var $form = $(e.target).closest("form");
        if($form.valid()) {
            $form.submit();
        }
    })

})(jQuery)

    //复选框Format bootstrap-table目前对此依旧是个坑
	function checkBoxFormat(value,row,index){
		return '<input type="checkbox" name="id[]" value="'+value+'">';
	}

    //父页面获取菜单选取事件
    //使用方法：<a href="javascript:;" onclick="parentMenuItem('5')">菜单</a>
	function parentMenuItem(index) {
        window.parent.$('.J_menuItem[data-index="'+index+'"]').trigger('click');
    }

//============================================固定列的操作下拉显示和checkbox选中问题====================
//将表单数据转为json
function form2Json(id) {

    var arr = $("." + id).serializeArray();
    var jsonStr = "";

    jsonStr += '{';
    for (var i = 0; i < arr.length; i++) {
        jsonStr += '"' + arr[i].name + '":"' + arr[i].value + '",'
    }
    jsonStr = jsonStr.substring(0, (jsonStr.length - 1));
    jsonStr += '}'

    var json = JSON.parse(jsonStr)
    return json
}

//==============================
function ajaxPostSubmit(url, formId) {
    if ($("#" + formId).valid()) {
        $.ajax({
            url: url,
            data: $('#' + formId).serialize(),
            type: "POST",
            dataType: "JSON",
            success: function (data) {
                //layer.msg(data,{time:3000});
                if (data.success != undefined && !data.success) {
                    layer.msg(data.msg, {time: 3000});
                } else if(data.errors != undefined){
                    layer.msg(data.errors[0].message, {time: 3000});
                } else if(data.resultDTO != undefined && !data.resultDTO.success){
                    layer.msg(data.resultDTO.msg, {time: 3000});
                } else {
                    layer.msg("操作成功！", {time: 3000});
                    $("#ajax-modal").modal("hide");
                    grid.getTable().bootstrapTable("refresh", "${path}");
                    return;
                }
            }
        });
    }
}


/**
 **  以下为table操作辅助工具类
 **/
var TbAssist = function () {
    return {
        // function to scroll to the top
        scrollTop: function () {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($('body').hasClass('page-header-fixed')) {
                    pos = pos - $('.page-header').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },
        scrollTo: function(el, offeset) {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($('body').hasClass('page-header-fixed')) {
                    pos = pos - $('.page-header').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },
        alert: function (options) {

            options = $.extend(true, {
                container: "", // alerts parent container(by default placed after the page breadcrumbs)
                place: "append", // append or prepent in container
                type: 'success', // alert's type
                message: "", // alert's message
                close: true, // make alert closable
                reset: true, // close all previouse alerts first
                focus: true, // auto scroll to the alert after shown
                closeInSeconds: 0, // auto close after defined seconds
                icon: "" // put icon before the message
            }, options);

            var id = TbAssist.getUniqueID("tb-assist_alert");
            var html = '<div id="' + id + '" class="tb-assist-alerts alert alert-' + options.type + ' fade in">' + (options.close ? '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' : '') + (options.icon !== "" ? '<i class="fa-lg fa fa-' + options.icon + '"></i>  ' : '') + options.message + '</div>';

            if (options.reset) {
                $('.tb-assist-alerts').remove();
            }

            if (!options.container) {
                if ($('body').hasClass("page-container-bg-solid")) {
                    $('.page-title').after(html);
                } else {
                    if ($('.page-bar').size() > 0) {
                        $('.page-bar').after(html);
                    } else {
                        $('.page-breadcrumb').after(html);
                    }
                }
            } else {
                if (options.place == "append") {
                    $(options.container).append(html);
                } else {
                    $(options.container).prepend(html);
                }
            }

            if (options.focus) {
                TbAssist.scrollTo($('#' + id));
            }

            if (options.closeInSeconds > 0) {
                setTimeout(function () {
                    $('#' + id).remove();
                }, options.closeInSeconds * 1000);
            }

            return id;
        },


        getUniqueID: function (prefix) {
            return 'prefix_' + Math.floor(Math.random() * (new Date()).getTime());
        },

        // 请求
        request: function (opts) {
            opts = $.extend(true, {
                container: $('.table-container')
            }, opts);

            $.ajax({
                url: opts.url,
                dataType: "json",
                success: function (res) {
                    console.log(res)
                    if (res.success != undefined && !res.success) {
                        TbAssist.alert({
                            type: 'danger',
                            icon: 'warning',
                            message: res.msg,
                            container: opts.container,
                            place: 'prepend'
                        });
                    } else {
                        TbAssist.alert({
                            type: 'success',
                            icon: 'check',
                            message: "操作成功！",
                            container: opts.container,
                            place: 'prepend'
                        });
                    }
                    if (opts.success) {
                        opts.success.call(this, res);
                    }

                },
                error: function () {
                    TbAssist.alert({
                        type: 'danger',
                        icon: 'warning',
                        message: "请求失败，请检查您的网络连接！",
                        container: opts.container,
                        place: 'prepend'
                    });
                }
            });
        },
        filterParams: function (container) {
            //用于获取需要提交内容的值，以JSON返回，返回格式:[{name1:value},{name2:value}]

            var data = {};
            var elements = $("input,select,textarea", $(container));
            for (var i = 0; i < elements.length; i++) {
                var isOk = false;
                //input 类型
                if (elements[i].nodeName == "INPUT") {
                    if ("text,hidden,password".indexOf(elements[i].type) != -1) {
                        isOk = true;
                    } else if ("radio".indexOf(elements[i].type) != -1) {
                        if (elements[i].checked) {
                            isOk = true;
                        }
                    }
                } else if (elements[i].nodeName == "SELECT") {
                    isOk = true;
                } else if (elements[i].nodeName == "TEXTAREA") {
                    isOk = true;
                }
                if (isOk) {
                    var name = $.trim(elements[i].name);
                    var value = $.trim(elements[i].value);
                    if (name && value) data[name] = value;
                }
            }
            return data;
        },
        resetElements: function (container) {
            $('textarea, input', container).each(function () {
                $(this).val("");
            });
            $('select', container).each(function () {
                $(this).val("");
            });
            $('input[type="checkbox"]', container).each(function () {
                $(this).attr("checked", false);
            });
        }
    };
}();
/**
 *  table 事件绑定 by lichengqi
 */
+function ($) {
    'use strict';
    // Table PUBLIC CLASS DEFINITION
    // ==============================
    var toggle_search = '[data-toggle="search"]'
    var toggle_reset = '[data-toggle="reset"]'
    var toggle_delete = '[data-toggle="delete"]'
    var toggle_request = '[data-toggle="request"]'

    var TableExpand = function (element, options) {
        this.$element = $(element)
        this.options = $.extend({}, TableExpand.DEFAULTS, options)

        this.$element.on('click', toggle_search, this.submit)
            .on('click', toggle_reset, this.reset)
            .on('click',toggle_delete, this.delete_)
            .on('delete.request',toggle_delete, this.request)
            .on('click',toggle_request, this.request)
    };

    TableExpand.VERSION = '0.0.1'

    TableExpand.DEFAULTS = {
        title: 'table expand...'
    }

    TableExpand.prototype.submit = function (e) {
        var $this = $(this)
        var $parent = $this.closest(".form-inline")
        var $table =  $parent.next().find('table');

        var $params = TbAssist.filterParams($parent);

        if (e) e.preventDefault()
        $parent.trigger(e = $.Event('click.search.submit'))
        if (e.isDefaultPrevented()) return
        $table.bootstrapTable("refresh", {query: $params});
    }

    TableExpand.prototype.reset = function (e) {
        var $this = $(this)
        var $parent = $this.closest(".form-inline")
        var $table =  $parent.next().find('table');

        if (e) e.preventDefault()
        $parent.trigger(e = $.Event('click.search.reset'))
        if (e.isDefaultPrevented()) return

        TbAssist.resetElements($parent)
        $table.bootstrapTable("refresh", {query: {}});
    }

    TableExpand.prototype.delete_ = function (e) {
        var $this = $(this)
        var $parent = $this.closest(".form-inline")
        var $href = $this.attr('href');
        var $table =  $parent.next().find('table');
 
        //debugger
        if(!$href) return

        if (e) e.preventDefault()
        $parent.trigger(e = $.Event('click.delete.confirm'))
        if (e.isDefaultPrevented()) return

        layer.confirm('确定删除？', {
            btn: ['确认', '取消'] //按钮
        }, function (index) {
            layer.close(index); //关闭弹出框
            $this.trigger(e = $.Event('delete.request'))
            if (e.isDefaultPrevented()) return
        });
        e.stopPropagation()
    },

    TableExpand.prototype.request = function (e) {
        var $this = $(this)
        var $parent = $this.closest(".form-inline")
        var $href = $this.attr('href');
        //var $table =  $parent.next().find('table');

        if(!$href) return

        if (e) e.preventDefault()
        $parent.trigger(e = $.Event('click.request'))
        if (e.isDefaultPrevented()) return

        var success = $this.attr("success")
        if(!success) {
            success = function (result) {
                //$table.bootstrapTable("refresh", {query: {}});
                grid.refreshTable();
            }
        } else {
            try{
                success = eval(success);
            } catch (e){
                throw new Error(" undefined of " + success)
                return
            }
        }

        TbAssist.request({
            url: $href, success: success
        });

    }

    // SEARCH PLUGIN DEFINITION
    // ========================

    function Plugin(option) {
        return this.each(function () {
            var options = typeof option == 'object' && option
            new TableExpand(this, options);
        })
    }

    var old = $.fn.tableExpand

    $.fn.tableExpand = Plugin
    $.fn.tableExpand.Constructor = TableExpand

    // BUTTON NO CONFLICT
    // ==================
    $.fn.tableExpand.noConflict = function () {
        $.fn.tableExpand = old
        return this
    }

    // BUTTON DATA-API
    // ===============
    $(document)
        .on('click.search.submit.data-api', toggle_search, TableExpand.prototype.submit)
        .on('click.search.reset.data-api', toggle_reset, TableExpand.prototype.reset)
        .on('click.request.data-api', toggle_request, TableExpand.prototype.request)
        .on('click.delete.confirm.data-api', toggle_delete, TableExpand.prototype.delete_)
        .on('delete.request.data-api', toggle_delete, TableExpand.prototype.request)

}(jQuery);
