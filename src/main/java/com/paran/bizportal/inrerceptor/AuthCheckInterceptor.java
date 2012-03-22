package com.paran.bizportal.inrerceptor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.paran.bizportal.common.BizportalCommonType;
import com.paran.bizportal.domain.LoginInfo;


/**
 * 전처리-권한체크 
 * @author 정재한
 *
 */
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {
	@Inject	private Provider<LoginInfo> loginInfoProvider; 

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String loginTypeProfile = loginInfoProvider.get().currentUser().getType_profile();	// 현재 세션의 타입
		String uriTypeProfile = request.getRequestURI().toUpperCase().substring(1,2);		// 호출된 uri의 타입
		
//		파트너와 개발자도 권한 체크 할경우 로직 
/*		if(loginTypeProfile.equals(uriTypeProfile)){
			return true;
		}else{
			response.sendRedirect(request.getContextPath() + "/accessdenied");
			return false ;
		}
*/
		/**
		 * 개발자 or 파트너가 admin에 접근 할경우 접근거부 
		 */
		if(loginTypeProfile.equals(BizportalCommonType.DEVELOPER) || loginTypeProfile.equals(BizportalCommonType.PARTNEL)){
			if(uriTypeProfile.equals(BizportalCommonType.ADMIN)){
				response.sendRedirect(request.getContextPath() + "/accessdenied");
				logger.warn("url 접근 차단 !! 접근유저ID : {} , 시도URL : {}" , 
						loginInfoProvider.get().currentUser().getLogin() ,	// 유저 id
						request.getRequestURI());							// 시도 url
				return false;
			}
		}
		
		return true;
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
