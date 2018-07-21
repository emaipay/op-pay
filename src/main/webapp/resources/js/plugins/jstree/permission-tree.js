var UITree = function () {
    var permissionControl = function() {

        $("#permission_container").jstree({
            "core": {
                "themes": {
                    "responsive": false
                }, 
                "data": {
                    "url": "payPermission/json",
                    "dataType": "json"
                },
                "check_callback": function(operation, node, parent, position, more) {
                    return true;
                }
            },
            "types": {
                "default": {
                    "icon": "fa fa-lock icon-state-info icon-lg"
                },
                "file": {
                    "icon": "fa fa-lock icon-state-info icon-lg"
                }
            },
            "state": { "key": "demo2" },
            "plugins": [ "contextmenu", "state", "types" ],
            "contextmenu": {
                "items": function($node) {
                    var tree = $("#permission_container").jstree(true);
                    
                    var btns = new Object();
                    if($.inArray('create', permissions)>-1) {
                    	btns['create'] = {
                            "label": "新增子权限",
                            "action": function (obj) {
                                var $a = $("#"+$node.id).children("a");
                                $a.attr('href', 'payPermission/create?parentPermissionId='+$node.id+'&floor='+($node.parents.length));
                                $a.attr('data-toggle', 'modal');
                                $a.attr('data-target', '#ajax-modal');
                                $a.trigger('click');
                                $a.removeAttr('data-target').removeAttr('data-toggle');
                                $a.attr('href', '#');
                            }
                    	};
                    }
                	if($.inArray('update', permissions) > -1) {
                    	btns['update'] = {
                            "label": "修改权限",
                            "action": function (obj) {
                                var $a = $("#"+$node.id).children("a");
                                $a.attr('href', 'payPermission/update/'+$node.id);
                                $a.attr('data-toggle', 'modal');
                                $a.attr('data-target', '#ajax-modal');
                                $a.trigger('click');
                                $a.removeAttr('data-target').removeAttr('data-toggle');
                                $a.attr('href', '#');
                            }
                    	};
                    }
                	/*if($.inArray('deletePermission', permissions) > -1) {
                    	btns['remove'] = {
                            "label": "删除权限",
                            "action": function (obj) {
                                var $a = $("#"+$node.id).children("a");
                                $a.attr('href', 'javascript:void(0);');
                                //$a.attr('onclick', 'deletePermission('+$node.id+');');
                                $a.trigger('click');
                                $a.removeAttr('onclick');
                                $a.attr('href', '#');
                            }
                        };
                    }*/
                    	
                    btns['reload'] = {
                        "label": "刷新节点",
                        "action": function (obj) {
                            tree.refresh($node);
                        }
                    };
                    return btns;
                }
            }
        });
    }

    return {
        init: function () {
            permissionControl();
        }
    };
}();

function deletePermission(id) {
	var loadi=0;
  	layer.confirm('确认删除？',{icon: 3, title:'确认删除？'},
		function(index){
			layer.close(index);
		 	$.ajax({
				type:"GET",
				url:"payPermission/delete/" + id,
				data:'',
				async:false,
				beforeSend: function() { loadi = layer.msg('处理中...', {icon: 16}); },
				complete: function() { layer.close(loadi); }, 
				success:function(data){
					if(data.success){
						$("#permission_container").jstree(true).refresh();
					} else{
						layer.alert(data.message,{icon:2,title:'提示'});
					}
				}
			}); 
		});
}