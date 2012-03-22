<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="user" cssClass="form-horizontal" method="put">
<form:hidden path="token_auth" />
<form:hidden path="password" />
	<fieldset>
		<legend>My Info</legend>
		<div id="login-control-group" class="control-group">
			<form:label cssClass="control-label" path="login">아이디</form:label>
			<div class="controls">
				<form:input path="login" readonly="true" maxlength="100"/>
				<form:errors cssClass="help-inline" path="login" />
			</div>
		</div>
		
		<div id="name-control-group" class="control-group">
			<form:label path="name">이름</form:label>
			<div class="controls">
				<form:input path="name" maxlength="45"/>
				<form:errors cssClass="help-inline" path="name" />
			</div>
		</div>
		
		<div id="type_profile-control-group" class="control-group">
			<form:label path="type_profile">타입</form:label>
			<div class="controls">
				<form:select path="type_profile">
					<form:option value="D">개발자</form:option>
					<form:option value="P">파트너</form:option>
				</form:select>
			</div>
		</div>
		
		<div id="alias-control-group" class="control-group">
			<form:label path="alias">닉네임</form:label>
			<div class="controls">
				<form:input path="alias" maxlength="45"/>
				<form:errors cssClass="help-inline" path="alias" />
			</div>
		</div>
		
		<div id="email-control-group" class="control-group">
			<form:label path="email">이메일</form:label>
			<div class="controls">
				<form:input path="email" maxlength="100"/>
				<form:errors cssClass="help-inline" path="email" />
			</div>
		</div>
		
		<div id="phone-control-group" class="control-group">
			<form:label path="phone">핸드폰</form:label>
			<div class="controls">
				<form:input path="phone" size="20" maxlength="13" onkeydown="mphon(this);" onkeyup="mphon(this);"/>
				<form:errors cssClass="help-inline" path="phone" />
			</div>
		</div>
		
		<div id="password-control-group" class="control-group">
			<form:label path="password">비밀번호</form:label>
			<div class="controls">
				<a id="bt_pwd_update" class="btn">변경</a>
			</div>
		</div>
		
		<!-- hidden modal start  -->		
		<div id="pw-modal" class="modal hide fade in" >
            <div class="modal-header">
              <a class="close" data-dismiss="modal">×</a>
              <h3>새로운 비밀번호 발급</h3>
            </div>
            
            <div class="modal-body">
              <h4>현재 비밀번호</h4>
              <input name="password" type="password">

              <h4>새 비밀번호</h4>
              <input name="new_password" type="password">

              <h4>새 비밀번호 재입력</h4>
              <input name="re_password" type="password">
            </div>
            
            <div class="modal-footer">
              <a href="#" id="bt_pwd_doupdate" class="btn btn-primary">변경하기</a>
              <a href="#" class="btn" data-dismiss="modal">닫기</a>
            </div>
        </div>		
		<!-- hidden modal end -->		
		
		<div class="form-actions">
			<input type="submit" value="변경" class="btn btn-primary" />
			<input type="button" id="bt_dropout" value="탈퇴" data-toggle="modal" class="btn" />
			<input id="bt_cancel" type="button" value="돌아가기" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath }/'" />
		</div>
	</fieldset>
</form:form>

<script type="text/javascript">

	// 회원탈퇴 Action
	$('#bt_dropout').click(function(){
		if(confirm("회원 정보가 영구 삭제됩니다. 탈퇴 하시겠습니까?")){
			$('input[name=_method]').val("delete");
			$('#user').submit();
		}
	});
	
	//패스워드 변경 모달창 보기 
	$('#bt_pwd_update').click(function(){
		$('#pw-modal').modal();
	});
	
	//패스워드 변경 Action
	$('#bt_pwd_doupdate').click(function(){
		var login = '${user.login}';
		var password = $('input[name=password]').val();
		var new_password = $('input[name=new_password]').val();
		var re_password = $('input[name=re_password]').val();
		
		if(password=='') {
			alert("현재 비밀번호를 입력하세요."); 
			return;
		}
		if(new_password==''){
			alert("새 비밀번호를 입력하세요."); 
			return;
		} 
		if(re_password==''){
			alert("새 비밀번호 재입력을 입력하세요."); 
			return;
		} 
		
		$.ajax({
		    url: '${pageContext.request.contextPath}/account/${user.login}/myinfo/password',
		    dataType: 'json',
		    type: 'POST',
		    data : {
		    		login : '${user.login}' , 
		    		password : password,
		    		new_password : new_password,
		    		re_password : re_password,
		    		_method : 'PUT'
		    },		    		
		    error: function(){
		        alert('Error loading XML document');
		    },
		    success: function(result){
		    	
		    	if(result.isErrors){
		    		$.each(result.errorCodes, function(key,value) {
		    			if(value=="isNotEqualToFormPasword"){
		    				alert("새로입력한 비밀번호가 일치하지 않습니다.");
		    			}else if(value=="isNotEqualToPasword"){
		    				alert("현재 비밀번호가 일치하지 않습니다.");
		    			}else{
		    				alert("비밀번호 변경에 실패하였습니다.");
		    			}
		    		});
		    	}else{
			    	alert("비밀번호가 변경되었습니다.");
		    	}
		    }
		});		
	});
	
	<spring:hasBindErrors name="user">
	//error CSS 처리 
		if(<spring:eval expression="errors.hasFieldErrors('login')" />) 
			$('#login-control-group').attr("class","control-group error");	
		if(<spring:eval expression="errors.hasFieldErrors('password')" />) 
			$('#password-control-group').attr("class","control-group error");	
		if(<spring:eval expression="errors.hasFieldErrors('name')" />) 
			$('#name-control-group').attr("class","control-group error");	
		if(<spring:eval expression="errors.hasFieldErrors('alias')" />) 
			$('#alias-control-group').attr("class","control-group error");	
		if(<spring:eval expression="errors.hasFieldErrors('email')" />) 
			$('#email-control-group').attr("class","control-group error");	
		if(<spring:eval expression="errors.hasFieldErrors('phone')" />) 
			$('#phone-control-group').attr("class","control-group error");	
	</spring:hasBindErrors>
</script>
