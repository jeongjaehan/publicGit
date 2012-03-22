<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>	    
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form action="${pageContext.request.contextPath}/board/boardUpdateForm.html" method="get">
	<input type="hidden" name="id" value="testboard">
	<input type="hidden" name="writer" value="jaehan">
	
		<div>
			<ul>
				<li>
					제목
					<input type="text" readonly="readonly" name="title" value="${boardVO.title}">
				</li>
				<li>
					내용
					<span>${boardVO.contentStringValue }</span>
				</li>
				<c:forEach var="file" items="${boardFileList}" >
					<c:url var="downloadUrl" value="${pageContext.request.contextPath}/board/downloadFile.html">
						<c:param name="save_filename" value="${file.save_filename }" />
						<c:param name="org_filename" value="${file.org_filename }" />
						<c:param name="board_seq" value="${file.board_seq }" />
					</c:url>				
					<li>
						파일${file.seq }		
						<a href="${downloadUrl }">
							${file.org_filename } (${file.file_size / 1024 }kb)
						</a>	
					</li>					
				</c:forEach>
			</ul>
		</div>
		
		<div id="comment_list">
			
		</div>
		<input type="button" onclick="location.href='<%=request.getContextPath() %>/board/getBoardList.html'" value="목록">
		<input type="button" value="수정">
		<input type="button" value="삭제">
		<input type="reset" value="취소">
</form>
