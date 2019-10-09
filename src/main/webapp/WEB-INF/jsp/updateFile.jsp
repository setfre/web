<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<!-- spring mvc上传文件模式 -->
	<form action="/demo/sysparam/doBatchUpload.action" enctype="multipart/form-data" method="post">
		<input type="file" multiple="multiple" name="file"/> 
		<input type="submit" value="click me"/>
	</form>
</body>
</html>