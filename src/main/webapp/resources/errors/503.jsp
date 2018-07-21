<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="java.io.*,java.util.*,org.springframework.web.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>503-服务暂时不可用</title>
<link href="/resources/global/css/error.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var isShow = false;
function showException() {
	if(!isShow) {
		document.getElementById('excptions').style.display='';
		document.getElementById('button').value="隐藏错误信息";
		isShow = true;
	} else {
		document.getElementById('excptions').style.display='none';
		document.getElementById('button').value="显示错误信息";
		isShow = false;
	}
}
</script>
</head>
<body>
<div class="all">
	<div class="error">
    	<div class="error-zw">
        	<div class="txt">
        	    <span><input id="button" class="input1" type="button" value="显示错误信息" onclick="showException();"/></span> 					
            </div>
            <div id="excptions" class="exceptions" style="display:none;">
					<pre class="exception">
						${errorinfo}
					</pre>
			</div>
        </div>
    </div>
</div>
</body>
</html>