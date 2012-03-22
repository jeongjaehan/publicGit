<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="board" action="/board/insertBoard.action" enctype="multipart/form-data" class="form-horizontal">
		<input type="hidden" name="id" value="testboard">
		<input type="hidden" name="writer" value="${currentUser.alias }">
		
		<div class="control-group">
			<form:label path="title" cssClass="control-label">제목</form:label>
			<form:input path="title" cssClass="span3" cssErrorClass="inputError" placeholder="제목을입력하세요."/>
			<form:errors path="title" />
		</div>
		
		<div class="control-group">
			<form:label path="content" cssClass="control-label">내용</form:label> 
			<form:textarea path="content" id="content" cssStyle="width:725px; height:300px; display:none;"/>
			<form:errors cssClass="error" path="content" />
		</div>
		
		<div class="control-group">
			<form:label for="file1" path="file1" cssClass="control-label"><i class="icon-folder-open"></i></form:label> 
			<form:input path="file1" type="file" cssStyle="file"/> 
			<form:errors cssClass="error" path="file1" />
		</div>
		
		<div class="control-group">
			<form:label for="file2" path="file2" cssClass="control-label"><i class="icon-folder-open"></i></form:label> 
			<form:input path="file2" type="file" cssStyle="file"/> 
			<form:errors cssClass="error" path="file2" />
		</div>
		
		<div class="control-group">
			<form:label path="password" cssClass="control-label">비밀번호</form:label>
			<form:password path="password" />
			<form:errors cssClass="error" path="password" />
		</div>
		
		<div class="control-group">
			<input type="button" onclick="location.href='/board/getBoardList.html'" value="목록" class="btn">
			<input type="button" onclick="_onSubmit(this)" value="등록" class="btn-primary">
		</div>
</form:form>
	
<!-- naver smart edit -->
<link href="${pageContext.request.contextPath }/resources/se/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/${pageContext.request.contextPath }resources/se/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript">
	var oEditors = [];
	nhn.husky.EZCreator.createInIFrame({
		oAppRef: oEditors,
		elPlaceHolder: "content",
		sSkinURI: "${pageContext.request.contextPath }/resources/se/SEditorSkin.html",
		fCreator: "createSEditorInIFrame"
	});
	
	function _onSubmit(elClicked){
		// 에디터의 내용을 에디터 생성시에 사용했던 textarea에 넣어 줍니다.
		oEditors.getById["content"].exec("UPDATE_IR_FIELD", []);
		
		// 에디터의 내용에 대한 값 검증은 이곳에서 document.getElementById("ir1").value를 이용해서 처리하면 됩니다.

		try{
			elClicked.form.submit();
		}catch(e){}
	}
</script>
