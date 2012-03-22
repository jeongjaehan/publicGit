<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>

	<div class="navbar">
		<div class="navbar-inner">
			<div class="container">
				<a class="brand" href="${pageContext.request.contextPath }/">jaehan.com</a>
				<div class="nav-collapse">
					<ul class="nav">
						<li><a href="${pageContext.request.contextPath }/board/getBoardList.html">게시판</a></li>
					</ul>
					
					<ul class="nav pull-right">
						<c:choose>
							<c:when test="${currentUser != null }">
								<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
									<i class="icon-user icon-white"></i> 
									<b class="caret">${currentUser.alias}</b>
								</a>
									<ul class="dropdown-menu">
										<li>
											<a href="${pageContext.request.contextPath }/account/${currentUser.login}/myinfo">내정보</a>
										</li>
							<c:if test="${currentUser.type_profile != 'A'}">										
										<li>
											<a href="${pageContext.request.contextPath }/account/switch/profile">세션전환</a>
										</li>
							</c:if>
										<li class="divider"></li>
										<li>
											<a href="${pageContext.request.contextPath }/logout">로그아웃</a>
										</li>
									</ul>
								</li>							
							</c:when>
							<c:otherwise>
								<li><a href="#" id="bt_header_login">로그인</a></li>
								<li><a href="${pageContext.request.contextPath }/signup">회원가입</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div> <!-- end nav-collapse -->
			</div><!--  end container -->
		</div><!--  end navbar-inner -->
	</div><!-- navbar -->

<c:if test="${currentUser == null }">

<!-- hidden modal start  -->
		<form action="${pageContext.request.contextPath }/login" method="post">		
		<div id="login-modal" style="width: 400px;" class="modal hide fade in">
           <div class="modal-body">
             <a class="close" data-dismiss="modal">×</a>
             <h4>Email</h4>
             <input name="login" type="text">

             <h4>Password</h4>
             <input name="password" type="password">
             
             <div id="errorMsg"></div>
           </div>
           
           <div class="modal-footer">
             <input type="submit" value="로그인" class="btn btn-primary" />
             <a href="${pageContext.request.contextPath }/resendPassword">Password가 기억나지 않으세요?</a>
           </div>
       </div>
       </form>		
<!-- hidden modal end -->	
	
	
<script type="text/javascript">

	//패스워드 변경 모달창 보기 
	$('#bt_header_login').click(function(){
		$('#login-modal').modal();
	});
	
	//패스워드 변경 Action
	$('#bt_pwd_doupdate').click(function(){
		var login = '${user.login}';
		var password = $('input[name=password]').val();
		var new_password = $('input[name=new_password]').val();
		var new_re_password = $('input[name=new_re_password]').val();
		
		if(password=='') {
			alert("현재 비밀번호를 입력하세요."); 
			return;
		}
		if(new_password==''){
			alert("새 비밀번호를 입력하세요."); 
			return;
		} 
		if(new_re_password==''){
			alert("새 비밀번호 재입력을 입력하세요."); 
			return;
		} 
		
		$.ajax({
		    url: '${pageContext.request.contextPath}/account/${user.login}/myinfo/password',
		    dataType: 'json',
		    type: 'POST',
		    data : {
		    		login : '${user.login}' , 
		    		password : $('input[name=password]').val(),
		    		new_password : $('input[name=new_password]').val(),
		    		new_re_password : $('input[name=new_re_password]').val(),
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
</script>
	
</c:if>
