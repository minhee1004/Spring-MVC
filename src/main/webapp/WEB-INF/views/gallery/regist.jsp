<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {
	font-family: Arial, Helvetica, sans-serif;
}

* {
	box-sizing: border-box;
}
form {
	border-collapse: collapse;
	border-spacing: 0;
	width: 60%;
	border: 1px solid #ddd;
	margin-top:10px;
	margin-left: auto;
	margin-right: auto;
}

h2 {
	border-collapse: collapse;
	border-spacing: 0;
	width: 60%;
	margin-left: auto;
	margin-right: auto;
	
}

input[type=text], select, textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
	margin-top: 6px;
	margin-bottom: 5px;
	resize: vertical;
}

input[type=button] {
	background-color: blue;
	color: white;
	padding: 12px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input[type=button]:hover {
	background-color: gray;
}

.container {
	border-radius: 5px;
	background-color: #f2f2f2;
	padding: 20px;
}
</style>
<%@include file="./inc/head_link.jsp" %>
<script type="text/javascript">

function regist(){
	$("form").attr({
		action:"/gallery/regist",
		method:"post",
		enctype:"multipart/form-data"
	});
	//console.log("dddd");
	$("form").submit();
}

$(function(){
	$("#bt_regist").click(function(){
		regist();
		
	});
	
	$("#bt_list").click(function(){
		location.href="/gallery/list";
	});
});
</script>
</head>
<body>

	<h2>글 작성</h2>

	<div class="container">
		<form>
			<input type="text"  name="title" placeholder="제목을 입력해주세요"> 
			<input type="text"  name="writer" placeholder="작성자">
			<textarea id="content" name="content" placeholder="내용을 입력해주세요" style="height: 200px"></textarea>
			<input type="file" name="photo">
			<br>
			<input type="file" name="photo">
			<p>
			<input type="button" value="등록" id="bt_regist">
			<input type="button" value="취소" id="bt_list">
		</form>
	</div>

</body>
</html>






