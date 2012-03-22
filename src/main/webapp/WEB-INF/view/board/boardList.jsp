<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>	    
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>

<c:set var="writeFormUrl" scope="page" value="${pageContext.request.contextPath}/board/boardWriteForm.html" />
<form>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>no</th>
				<th>제목</th>
				<th>조회수</th>
				<th>글쓴이</th>
			</tr>
		</thead>	
	<tbody>
		<c:if test="${boardList.size()} < 1">
			<tr><td colspan="3">등록된 게시글이 없습니다.</td></tr>
		</c:if>
		<c:forEach var="board" items="${boardList}" >
			<c:url var="viewUrl" value="${pageContext.request.contextPath}/board/getBoardDetail.html">
				<c:param name="seq" value="${board.seq }" />
			</c:url>
			<tr>
				<td>${board.seq}</td>
				<td><a href="${viewUrl}">${board.title}</a></td>
				<td>${board.hitcount}</td>
				<td>${board.writer}</td>
			</tr>
		</c:forEach>			
	</tbody>
	</table>
	
<div class="pagination">
  <ul>
    <li><a href="#">Prev</a></li>
    <li class="active">
      <a href="#">1</a>
    </li>
    <li><a href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">4</a></li>
    <li><a href="#">Next</a></li>
  </ul>
</div>

	<input type="button" value="글쓰기" onclick="location.href='${writeFormUrl}'" class="btn">	
</form>
