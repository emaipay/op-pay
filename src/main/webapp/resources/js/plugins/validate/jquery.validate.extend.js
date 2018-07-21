$.validator.setDefaults({	
	errorElement : "label",
	errorClass : "help-block",
	errorPlacement: function(error, element) {
		//error.appendTo(element.closest('div[class^="col-md"]'));
		//zhulinfeng 修改表单验证错误信息的提示
		error.appendTo(element.closest('div[class^="controls"]'));
	},
	onfocusout : function(element) {
		$(element).valid();
	},
	submitHandler : function(form) {
		$modal = $('.modal-dialog');
		$modal.find('.modal-body > .alert').remove();
		$('.form-group').removeClass('has-error');
		$modal.modal('loading');
		
		$(form).ajaxSubmit({
			success:function(data){

				$modal.modal('loading');
                var msg = '';

                if(data && data.errors) {
                    $.each(data.errors, function (i, val) {
                        if (val.field) {
                            var $field = $('[name=' + val.field + ']');
                            $field.closest('.form-group').removeClass('has-success').addClass('has-error');
							//zhulinfeng 修改表单验证错误信息的提示
                            //$field.closest('div[class^="col-md"]').append('<label for="' + val.field + '" class="help-block">' + val.message + '</label>');
                            $field.closest('div[class^="controls"]').append('<label for="' + val.field + '" class="help-block">' + val.message + '</label>');
                        } else {
                            msg += val.message;
                        }
                    });
				}
				else if (data.success != undefined && !data.success) {
                    msg += data.msg;

					if (msg != '') {
						$modal.find('.modal-body').prepend('<div class="alert alert-danger fade in">' + msg + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
					}
				} else {
					
					$modal.find('.modal-body').prepend('<div class="alert alert-success fade in">操作成功！<button type="button" class="close" data-dismiss="alert">&times;</button></div>');

                    setTimeout('ajaxSubmitHandler.successAfter($modal,grid)', 500);
				}
            },
			error:function(error){
				$modal.modal('loading').find('.modal-body').prepend('<div class="alert alert-danger fade in">操作失败！<button type="button" class="close" data-dismiss="alert">&times;</button></div>');
            }
    });
	},
	highlight : function(element) {
		$(element).closest('.form-group').removeClass('has-success').addClass('has-error');
	},
	success: function(element) {
		$(element).closest('.form-group').removeClass('has-error').addClass('has-success');
	}

});
var ajaxSubmitHandler={
	successAfter:function ($m,$grid) {
		//关闭模态窗
		$m.parent().modal("hide");

		var reload = $m.attr("success-reload");

		if(reload==undefined || reload=="true"){
			//刷新表格当前页的数据
			if($grid != undefined){
				$grid.refreshTable();
				var tr=$("#permission_container");
				if(tr!=undefined){
					var tr=$("#permission_container").jstree(true);
					tr.refresh(0);
				}
			}
		}

	}
}
