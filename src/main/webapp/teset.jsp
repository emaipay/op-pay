<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="service">
<head lang="en">
<title>测试调用支付接口</title>
<script src="http://192.168.0.201:83/resources/global/plugins/jquery-1.11.0.min.js"></script>
<style type="text/css">
.pay_business{no-repeat 0 0;}
/*表格样式*/
.cen2-con{width:880px;margin:0 auto;}


/*按钮样式*/
.input_set{width:226px;height:24px;border:1px solid #ccc;outline:none;}
.input3{width:220px;height:24px;line-height:24px;border:solid 1px #ccc;background:#fff;padding-left:5px;}
.cen-button-big{color:#fff;border:0;width:90px;margin:5px 5px 0 0;font-size:14px;line-height:26px;text-align:center;border-radius:3px;display:inline-block;color:#fff;cursor:pointer;background:#eb7275;}
.cen-button-big:hover{background:#e3363a;color:#fff;}
.cen-button-big.no_active{background:#aaa;color:#fff;}
.special_table{margin-top:30px;width:100%;}
.dowebok{border:none!important;}
.dowebok li{display:block; float:left;border:none; margin:7px;padding:0;width:121px; height:33px;cursor:pointer;position:relative;}
.dowebok input{display:none;}
.dowebok .recharge_click{display:table;position:absolute;width:118px;height:30px;border:solid 3px #23a0e7;cursor:pointer;z-index:10;;top:0;top:0;}
.dowebok .recharge_click2{display:table;position:absolute;width:115px;height:27px;border:solid 3px #23a0e7;cursor:pointer;z-index:9;}
.dowebok .recharge_click span.labelauty-checked-image{background:url( ../img/input-checked.png) no-repeat 0 0;position:absolute;right:-7px;top:-7px;width:16px;height:16px;}
.donwLine .dowebok li{margin:7px 5px 7px 0;}
.input3{line-height:34px;height:34px;border-color:#222;width:250px;font-size:16px;}

</style>

</head>
<body style="text-align:center;"> 
   <div id="con_one_0">
			<table border="0" cellspacing="0" cellpadding="0" class="cen-table special_table">
				<tr>
					<td width="700" align="right">充值金额：</td>
					<td align="left"><input type="text" id="online_recharge_money" class="input3" />&nbsp;&nbsp;<span
							class="yel">(单笔充值限额：最低 <span id="online_recharge_min">--</span>，最高 <span id="online_recharge_max">--</span>)</span></td>
				</tr>
				<tr>
					<td align="right"><span id="onlinepaymentplatformmark">请选择支付商户：</span></td>
					<td id="online_paymentplatform_list"><ul class="dowebok cf" id="onlinepaymentplatformul"></ul></td>
				</tr>
				<tr>
					<td id="online_recharge_bank" align="right"><span id="onlineBankMark">请选择充值银行：</span></td>
					<td id="online_bank_list"><ul class="dowebok cf" id="onlinebankul"></ul></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<div class="cen-button-mid">
							<a href="javascript:void(0);" onclick="onlineSubmit(this);" class="cen-button-big">确 定</a>
						</div>
			      </td>
				</tr>
			</table>
			
		</div>
</body>
<script type="text/javascript">
$(function(){
	var datas;
	$.ajax({
		url:"http://localhost:8082/pay/connPlatform/merchant?memberId=N8&terminalId=2017030201&merKey=test",
		type : "GET",
		success:function(data){
			datas=data.result;
			$ul=$("#onlinepaymentplatformul");
			for(var i=0;i<datas.length;i++){
				var $li = $('<li class="pay_business"></li>');
				$input = $('<input>');
				$input.attr({
					'type' : 'radio',
					'name' : 'online_paymentplatform_merchant',
					'value' : datas[i].merchantId+","+datas[i].platformId,
					'data-labelauty' : '&nbsp;'
				});
				$li.attr('style', 'background:url('+datas[i].platformLogo+') no-repeat 0 0;');
				$li.append($input);
				$ul.append($li);
			}
		}
	});
	$("#onlinepaymentplatformul").on("click", "li", function() {
		var index=$(this).index();
		var limit=datas[index].payConnLimitVo;
		$("#online_recharge_max").html(limit.onetimeRechargeAmountMax);
		$("#online_recharge_min").html(limit.onetimeRechargeAmountMin);
		//线上支付银行
		var $ul = $("#onlinebankul");//$('<ul></ul>').addClass('dowebok').attr('id', 'onlinebankul');
		$ul.html("");
		$.each(datas[index].bankList, function(i, v) {
			var $li = $('<li></li>');
			$li.attr('style', 'background:url('+v.bankLogo+') no-repeat 0 0;');
			$input = $('<input>');
			$input.attr({'type': 'radio', 'name': 'online_recharge_bank', 'value': v.bankId, 'data-labelauty': '&nbsp;'});
			$li.append($input);
			$ul.append($li);
		});
	});
	
	$.ajax({
		url:"http://localhost:8082/pay/connPlatform/test",
		type : "GET",
		success:function(data){
			console.log(data);
		}
	});
	
});


</script>
</html>