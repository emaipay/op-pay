<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%response.setStatus(404);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>404-错误页面!</title>
<link href="/resources/global/css/error.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="all">
	<div class="error">
    	<div class="error-zw">
        	<div class="txt">
            	<span class="title">提示信息：</span>
            	<p>
            	<c:if test="${!empty requestScope['javax.servlet.error.message']}" var="errorIsNull">${requestScope["javax.servlet.error.message"]}</c:if>
            	<c:if test="${!errorIsNull}">非常抱歉，您访问的页面没有找到！！</c:if>
            	</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>