<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	
<form:form modelAttribute="user" cssClass="form-horizontal" method="post">
<form:hidden path="token_auth" />
	<fieldset>
		<legend>회원가입</legend>
		
		<div id="login-control-group" class="control-group">
			<form:label cssClass="control-label" path="login">아이디</form:label>
			<div class="controls">
				<form:input path="login" maxlength="100"/>
				<input type="button" class="btn" id="bt_idcheck" value="중복검사">
				<span id="lb-success" class="label label-success" style="display: none;">사용가능</span>
				<span id="lb-warning" class="label label-warning" style="display: none;">중복</span>
				<span id="lb-important" class="label label-important" style="display: none;">이메일형식아님</span>
				<form:errors cssClass="help-inline" path="login" />
			</div>
		</div>
		
		<div id="password-control-group" class="control-group">
			<form:label path="password">비밀번호</form:label>
			<div class="controls">
				<form:password path="password"/>
				<form:errors cssClass="help-inline" path="password" />
			</div>
		</div>
		
		<div id="repassword-control-group" class="control-group">
			<form:label path="re_password">비밀번호 확인</form:label>
			<div class="controls">
				<form:password path="re_password"/>
				<form:errors cssClass="help-inline" path="re_password" />
			</div>
		</div>
		
		<div id="name-control-group" class="control-group">
			<form:label path="name">이름</form:label>
			<div class="controls">
				<form:input path="name" maxlength="45"/>
				<form:errors cssClass="help-inline" path="name" />
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
				<form:checkbox title="아이디와 이메일이 틀릴경우" path="sameId"/>
				<form:errors cssClass="help-inline" path="email" />
			</div>
		</div>
		
		<div id="phone-control-group" class="control-group">
			<form:label path="phone">전화번호</form:label>
			<div class="controls">
				<form:input path="phone" size="20" maxlength="13" onkeydown="mphon(this);" onkeyup="mphon(this);"/>
				<form:errors cssClass="help-inline" path="phone" />
			</div>
		</div>

		<div id="type_profile-control-group" class="control-group">
			<form:label path="type_profile">대시보드 타입</form:label>
			<div class="controls">
			<c:forEach items="${type_profiles}" var="type_profile">
				<form:radiobutton path="type_profile" value="${type_profile.key}"/> ${type_profile.value}
			</c:forEach>
			</div>
		</div>		
		
		<div class="control-group">
			<div class="controls">
	          <div class="accordion-heading">
	            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
	             	이용약관
	            </a>
	          </div>
	          <div id="collapseTwo" class="accordion-body collapse">
	            <div class="accordion-inner">
				<spring:eval expression="@messagesProperties['agreements']"/>
	            </div>
	          </div>
              </div>
        </div>
        
		<div class="form-actions">
			<input type="submit" id="bt_submit" value="가입" class="btn btn-primary" />
			<input id="bt_cancel" type="button" value="돌아가기" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath }/'" />
		</div>
	</fieldset>
</form:form>

<script type="text/javascript">

<spring:hasBindErrors name="user">
	if(<spring:eval expression="errors.hasFieldErrors('login')" />) 
		$('#login-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('password')" />) 
		$('#password-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('re_password')" />) 
		$('#repassword-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('name')" />) 
		$('#name-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('alias')" />) 
		$('#alias-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('email')" />) 
		$('#email-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('phone')" />) 
		$('#phone-control-group').attr("class","control-group error");	
</spring:hasBindErrors>

	$('input[name=sameId]').tooltip();	 
	
	// form load시 이메일 활성화 여부 결정
	if(${user.sameId})
		$('#email').attr("readonly",false);
	else
		$('#email').attr("readonly",true);
		
	// 체크여부에 따라 이메일 수정여부 결정
	$('input[name=sameId]').change(function(){
		if(this.checked)
			$('#email').attr("readonly",false);
		else
			$('#email').attr("readonly",true);
	});

	// id 필드를 떠날떼 이메일 필드로 값복사	
	$('#login').blur(function(){
		if(this.value == '') return;
		
		$('#email').val(this.value);
	});
	
	// form submit 할때 id 필드를 이메일 필드로 값복사
	$('#user').submit(function(){
		if($('#email').attr("readonly"))
			$('#email').val($('#login').val());
	});
	
	// 아이디 중복검사 
	$('#bt_idcheck').click(function(){
		var login = $('#login').val();
		if(login != ''){
			$.ajax({
			    url: '${pageContext.request.contextPath }/signup/checkDuplicateId',
			    data : "login="+login,
			    type: 'GET',
			    dataType: 'json',
			    error: function(){
			        alert('Error loading XML document');
			    },
			    success: function(result){
					$('#lb-success').css("display","none");
					$('#lb-warning').css("display","none");
					$('#lb-important').css("display","none");
					
			    	switch (result) {
						case 0:
							$('#lb-success').css("display","inline");
							break;
						case 1:
							$('#lb-warning').css("display","inline");
							break;
						case 2:
							$('#lb-important').css("display","inline");
							break;
						default:
							break;
					}
			    }
			});
		}
	});
</script>