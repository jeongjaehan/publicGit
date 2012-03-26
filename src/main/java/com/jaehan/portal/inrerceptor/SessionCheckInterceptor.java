package com.jaehan.portal.inrerceptor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jaehan.portal.domain.LoginInfo;

/**
 * 傈贸府 - 技记眉农 
 * @author 沥犁茄
 *
 */
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {
	@Inject	private Provider<LoginInfo> loginInfoProvider; 

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (loginInfoProvider.get().isLoggedIn()) {
			return true;
		}
		else {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
