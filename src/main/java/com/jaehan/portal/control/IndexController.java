package com.jaehan.portal.control;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.jaehan.portal.common.BizportalCommonUtil;
import com.jaehan.portal.domain.LoginInfo;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@Autowired 	private Provider<LoginInfo> loginInfoProvider;
	
	/**
	 * index page -> login
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String index(){
		if(loginInfoProvider.get().isLoggedIn()) // �������� ������� ������������ �̵� 
			return "redirect:"+BizportalCommonUtil.returnUrl(loginInfoProvider.get().currentUser().getType_profile());	
		
		return "index";
	}
	
	/**
	 * ���ٱ��� ���� 
	 * @return
	 */
	@RequestMapping("accessdenied")
	public String accessdenied() {
		return "/accessdenied";
	}
	
	/**
	 * �α׾ƿ�
	 * @return
	 */
	@RequestMapping(value="logout")
	public String logout(SessionStatus status) {
		try {
			loginInfoProvider.get().remove();
		} catch (IllegalStateException e) {
			logger.info("���� ������ �������� �ʴµ� �α׾ƿ� �߻���.");
		}
		return "index";
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
