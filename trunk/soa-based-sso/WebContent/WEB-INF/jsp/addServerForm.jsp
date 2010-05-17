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
<script type="text/javascript" src="/sso/js/jquery/ui/packed/ui.resizable.packed.js"></script>

<script type="text/javascript">

/*function addServer(){
	var frm = document.forma;
	frm.action='/sso/addserver.apx';
	frm.call.value='addServer';
	frm.submit();
}*/
var addServer = function(){
	var data={
		call : 'addServer',
		ip:$('#ip').val()
	};
	$.post('/sso/addserver.apx',data,function(json){
		if(json.success){
			alert(json.message);
		}else{
			alert(json.message);
			$('#ip').val('');
			$('#ip').focus();
		}
	}
	,'json');
};

var issueClient = function(){
	var frm = document.forma;
	frm.call.value="zipDownload";
	frm.action="/sso/addserver.apx";
	frm.submit();
};

var issueKey = function(ip){
	var frm = document.forma;
	frm.call.value="keyDownload";
	frm.ip.value=ip;
	frm.action="/sso/addserver.apx";
	frm.submit();
};
</script>
</head>
<body>
<form name="forma" method="post" onsubmit="return false;"/>
<input type="hidden" name="call"/>
<input type="hidden" name="ip"/>
</form>
<div class="wrap" style="width: 500px">
<table style="width: 100%;" class="order_propensity_table">
	<tr>
		<th width="40%">ID</th>
		<th width="40%">IP</th>
		<th width="20%"></th>
	</tr>
<c:choose>
	<c:when test="${!empty serverList}">
	<c:forEach items="${serverList}" var="loop">
		<tr>
			<td width="40%" class="order_propensity_table_ce">${loop.id }</td>
			<td width="40%" class="order_propensity_table_ce">${loop.ip }</td>
			<td width="20%" class="order_propensity_table_ce"><button onclick="issueClient()">sso Client down</button><br/><button onclick="issueKey('${loop.ip }')">key down</button> </td>
		</tr>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<tr>
			<td colspan="3" class="order_propensity_table_ce">등록된 사용 서버가 없습니다.</td>
		</tr>
	</c:otherwise>
</c:choose>
	<tr></tr>
</table>
</div>
<div class="wrap" style="width: 500px">
<table><tr><td align="right">
	<input type="text" name="ip" id="ip"/>
	<button onclick="addServer()">서버생성</button>
	</td></tr></table>
</div>

</body>
</html>