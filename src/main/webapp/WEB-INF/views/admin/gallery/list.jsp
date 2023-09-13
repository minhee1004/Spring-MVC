<%@page import="java.util.List"%>
<%@page import="org.sp.springapp.domain.GalleryImg"%>
<%@page import="org.sp.springapp.domain.Gallery"%>
<%@page import="org.sp.springapp.util.Pager"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	Pager pager= (Pager)request.getAttribute("pager");
	List<Gallery> galleryList=(List)request.getAttribute("galleryList");
%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
table {
	border-collapse: collapse;
	border-spacing: 0;
	width: 70%;
	border: 1px solid #ddd;
	margin-top:60px;
	margin-left: auto;
	margin-right: auto;
}


h2 {
	border-collapse: collapse;
	border-spacing: 0;
	width: 70%;
	margin-left: auto;
	margin-right: auto;
	
}

input[type=button] {
	background-color: blue;
	color: white;
	padding: 12px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin-left: auto;
 	margin-right: auto;
}

input[type=button]:hover {
	background-color: gray;
}

th, td {
	text-align: left;
	padding: 16px;
}

tr:nth-child(even) {
	background-color: #f2f2f2;
	
}

a{
	text-decoration:none; 
}

</style>
<%@ include file="../inc/head_link.jsp" %>
<script type="text/javascript">
$(function(){
		$("#button").click(function(){
			location.href="/gallery/registform";
		});
});
</script>
</head>
<body>

	<h2>
		<a href="/admin/loginform">관리자모드</a>
	</h2>
	
	<table>
		<tr>
			<td colspan="5">
			<input type="button" value="글쓰기" id="button">
		</tr>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<%int num= pager.getNum();%>
		<%int curPos=pager.getCurPos(); //페이지당 List의 시작 index %>
		<%for(int i=1; i<=pager.getPageSize();i++){ %>
		<%if(num<1)break; %>
		<% Gallery gallery=galleryList.get(curPos++); %>
	
	
		
		<tr>
			<td><%=num-- %></td>
		
			<td> <a href="/gallery/content?gallery_idx=<%=gallery.getGallery_idx()%>"><%=gallery.getTitle() %></a></td>
			<td><%=gallery.getWriter() %></td>
			<td><%=gallery.getRegdate() %></td>
			<td><%=gallery.getHit() %></td>
		
		</tr>
		<%} %>
		
		<tr>
			<td colspan="5">
			<%if(pager.getFirstPage()-1<1){ %>
				<a href= "javascript:alert('이전 페이지가 없습니다');">◀</a>
			<%}else{ %>
				<a href= "/gallery/list?currentPage=<%=pager.getFirstPage()-1 %>">◀</a>
			<%} %>
			
			<%for(int i=pager.getFirstPage();i<=pager.getLastPage();i++){ %>
			<%if(i >pager.getTotalPage())break; %>
				<a href="/gallery/list?currentPage=<%=i%>">[<%=i %>]</a>
			<%} %>
			<%if(pager.getLastPage()+1> pager.getTotalPage()){ %>
				<a href= "javascript:alert('다음 페이지가 없습니다');">▶</a>
			<%}else{ %>
			<a href= "/gallery/list?currentPage=<%=pager.getLastPage()+1%>">▶</a>
			<%} %>
			
			</td>
		</tr>
		
	</table>

</body>
</html>
