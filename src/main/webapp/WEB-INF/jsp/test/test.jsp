<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="${contextPath}/asset/js/jquery/jquery-3.6.0.min.js"></script>
	<script type="text/javascript" src="${contextPath}/asset/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#lang").off("change").on("change", function() {
				var val = $(this).val();
				console.log("val : ", val);
				
				$("#searchForm").prop("action", "/test/chang_lang.do");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
<form id="searchForm" name="searchForm" method="post">
<select id="lang">
	<option value="KO">한국어</option>
	<option value="EN">영어</option>
	<option value="ZH">중국어</option>
</select>
</form>
<br>
<spring:message code="LABEL.BTN.SEARCH" text="조회" />
</body>
</html>