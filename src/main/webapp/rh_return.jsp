<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
<script type="text/javascript"
	src="${ctx}/resources/js/jquery.min.js?v=2.1.4"></script>
<script type="text/javascript">
	$(function() {
		var currenthash = window.location.hash;
		var outTradeNo = currenthash.substr(1, currenthash.length);
		$("#outTradeNo").val(outTradeNo);
		$("#dataForm").submit();
	});
</script>
</head>
<body>
	<form action="${ctx}/pay/callback/lfb/callback-page" method="post"
		id="dataForm" style="display: none;">
		<input type="hidden" id="outTradeNo" name="outTradeNo"  />
	</form>
</body>
</html>