<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="login" cssClass="well form-search" method="post">
		<fieldset>
			<legend>로그인</legend>
			
			<div id="login-control-group" class="control-group">
				<form:label cssClass="control-label" path="login">Email</form:label>
				<div class="controls">
					<form:input path="login" placeholder="아이디 또는 이메일" maxlength="100"/>
					<form:errors cssClass="help-inline" path="login" />
				</div>
			</div>	
			
			<div id="password-control-group" class="control-group">
				<form:label path="password">Password</form:label>
				<div class="controls">
					<form:password path="password" placeholder="비밀번호"/>
					<form:errors cssClass="help-inline" path="password" />
				</div>
			</div>
			
			<div id="error-control-group" class="control-group">
				<div class="controls">
					<form:errors cssClass="help-inline" path="" />
				</div>
				<a href="${pageContext.request.contextPath }/resendPassword">Password가 기억나지 않으세요?</a>
			</div>
			
			<div class="form-actions">
				<input type="submit" value="로그인" class="btn btn-primary" />
			</div>
			
		</fieldset>
</form:form>


<script type="text/javascript">

<spring:hasBindErrors name="login">
	//var errorcount = <spring:eval expression="errors.getErrorCount()" />;
	//var fieldErrorCount = <spring:eval expression="errors.getFieldErrorCount()" />;
	var globalErrorCount = <spring:eval expression="errors.getGlobalErrorCount()" />;
	
	if(<spring:eval expression="errors.hasFieldErrors('login')" />) 
		$('#login-control-group').attr("class","control-group error");	
	if(<spring:eval expression="errors.hasFieldErrors('password')" />) 
		$('#password-control-group').attr("class","control-group error");	
	if(globalErrorCount > 0) 
		$('#error-control-group').attr("class","control-group error");	
</spring:hasBindErrors>
</script>

