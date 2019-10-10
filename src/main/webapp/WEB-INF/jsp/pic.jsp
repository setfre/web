<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<style>
div.img {
    border: 1px solid #ccc;
}

div.img:hover {
    border: 1px solid #777;
}

div.img img {
    width: 100%;
    height: auto;
}
div.desc {
    padding: 15px;
    text-align: center;
}
* {
    box-sizing: border-box;
}
.responsive {
    padding: 0 6px;
    float: left;
    width: 24.99999%;
}

@media only screen and (max-width: 700px){
    .responsive {
        width: 49.99999%;
        margin: 6px 0;
    }
}

@media only screen and (max-width: 500px){
    .responsive {
        width: 100%;
    }
}

.clearfix:after {
    content: "";
    display: table;
    clear: both;
}

.picture{
	width:1000px;
}
.picture{
	list-style:none;
}
</style> 
<body> 
	<!-- spring mvc上传文件模式 -->
	<form action="/demo/sysparam/doUploadUserHeaderImg.action" enctype="multipart/form-data" method="post">
		<input type="file" name="file"/> 
		<input type="submit" value="click me"/>
	</form>
	<ul class="picture">
		<c:forEach items="${imgPaths}" var="imgPath">
			<li>
				<div class="responsive">
					<div class="img">
			    		<a target="_blank" href="${imgPath}">
							<img class="img" src="${imgPath}" alt="文本描述" width="300" height="200"/>
						</a>
				  		<div class="desc">Add a description of the image here</div>
					</div>
				</div>
			</li>
		</c:forEach>
	</ul>

</body>
</html>