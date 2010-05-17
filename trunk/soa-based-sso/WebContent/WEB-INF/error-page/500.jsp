<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>500 Internal Server Error</title>
</head>
<body bgcolor="#FFFFFF" text="#000000">
<h1 align="center">요청을 처리 중 서버에 에러가 발생했습니다.[${exception.message}]</h1>
<c:if test="${!empty exception.stackTrace}">
	<ul>
	<c:forEach var="stack" items="${exception.stackTrace}">
		<li>at&nbsp;${stack.className}.${stack.methodName} (${stack.fileName}&nbsp;${stack.lineNumber})<br/></li>
	</c:forEach>
	</ul>
</c:if>
</body>
</html>