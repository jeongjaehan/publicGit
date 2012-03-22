<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="login" cssClass="well form-search" method="post">
		<div>
			임시비밀번호를 발급합니다.	
		</div>
		<fieldset>
			<table class="table">
				<tr>
					<td>
						<form:label path="login">이메일 주소</form:label>
					</td>
					<td>
						<form:input path="login" cssClass="span3" placeholder="이메일"/>
						<input type="submit" value="전송 " class="btn-primary" />
					</td>
					<td>
						<form:errors cssClass="error" path="login"/>
					</td>
				</tr>
			</table>
			<p>
				<form:errors cssClass="error" path="" />
			</p>			
		</fieldset>
</form:form>