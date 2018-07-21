var Component = function () {
    //expand
    var expandOrCallspace = function () {
        $(document).on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function(e) {
            e.preventDefault();
            var el = $(this).closest(".portlet").children(".portlet-body");
            if ($(this).hasClass("collapse")) {
                $(this).removeClass("collapse").addClass("expand");
                el.slideUp(200);
            } else {
                $(this).removeClass("expand").addClass("collapse");
                el.slideDown(200);
            }
        });
    }
    //reload
    var reloadMethod = function(){
        $(document).on('click', '.portlet > .portlet-title > .tools > a.reload', function(e) {
            e.preventDefault();
            var el = $(this).closest(".portlet").children(".portlet-body");
            var url = $(this).attr("data-url");
            var error = $(this).attr("data-error-display");
            if (url) {
                $.ajax({
                    type: "GET",
                    cache: false,
                    url: url,
                    dataType: "html",
                    success: function(res) {
                        el.html(res);
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        alert(el);
                    }
                });
            } else {
                //alert(url);
                $("#permission_container").jstree(true).refresh();//暂时先这样处理
            }
        });

        // load ajax data on page init
        $('.portlet .portlet-title a.reload[data-load="true"]').click();
    }
    return {
        init:function () {
            expandOrCallspace();
            reloadMethod();
        }
    }
}();