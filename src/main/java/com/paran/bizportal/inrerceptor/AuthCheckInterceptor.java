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
 * ��ó��-����üũ 
 * @author ������
 *
 */
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {
	@Inject	private Provider<LoginInfo> loginInfoProvider; 

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String loginTypeProfile = loginInfoProvider.get().currentUser().getType_profile();	// ���� ������ Ÿ��
		String uriTypeProfile = request.getRequestURI().toUpperCase().substring(1,2);		// ȣ��� uri�� Ÿ��
		
//		��Ʈ�ʿ� �����ڵ� ���� üũ �Ұ�� ���� 
/*		if(loginTypeProfile.equals(uriTypeProfile)){
			return true;
		}else{
			response.sendRedirect(request.getContextPath() + "/accessdenied");
			return false ;
		}
*/
		/**
		 * ������ or ��Ʈ�ʰ� admin�� ���� �Ұ�� ���ٰź� 
		 */
		if(loginTypeProfile.equals(BizportalCommonType.DEVELOPER) || loginTypeProfile.equals(BizportalCommonType.PARTNEL)){
			if(uriTypeProfile.equals(BizportalCommonType.ADMIN)){
				response.sendRedirect(request.getContextPath() + "/accessdenied");
				logger.warn("url ���� ���� !! ��������ID : {} , �õ�URL : {}" , 
						loginInfoProvider.get().currentUser().getLogin() ,	// ���� id
						request.getRequestURI());							// �õ� url
				return false;
			}
		}
		
		return true;
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
