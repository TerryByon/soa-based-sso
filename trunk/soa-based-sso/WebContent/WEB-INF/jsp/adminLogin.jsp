<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/sso/css/bidsystem.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/sso/js/jquery/jquery.pack.js"></script>
<script type="text/javascript" src="/sso/js/jquery/jquery.cookie.min.js"></script>
<script type="text/javascript" src="/sso/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/sso/js/jquery/jquery.simplemodal-1.2.2.pack.js"></script>
<script type="text/javascript" src="/sso/js/jquery/jquery.jquery.blockUI.js"></script>
<script type="text/javascript" src="/sso/js/jquery/ui/ui.core.js"></script>
<script type="text/javascript" src="/sso/js/jquery/ui/packed/ui.draggable.packed.js"></script>

<script type="text/javascript">
<c:if test="${!empty msg}">
	alert('${msg}');
</c:if>

var adminLogin = function(){
	var frm = document.forma;
	frm.action = '/sso/addserver.apx';
	frm.call.value = "login";
	frm.submit();
};
</script>
</head>
<body>
<form name="forma" method="post" id="form_a" onsubmit="return false;">
<input type="hidden" name="call"/>
<table>
	<tr>
		<td>ID&nbsp;:&nbsp;</td>
		<td><input type="text" name="id"/></td>
		<td rowspan="2"><button onclick="adminLogin()">sign in</button></td>
	</tr>
	<tr>
		<td>PW&nbsp;:&nbsp;</td>
		<td><input type="password" name="pw"/> </td>
	</tr>
</table>
</form>
</body>
</html>