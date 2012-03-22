package com.paran.bizportal.control;

import javax.inject.Provider;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.paran.bizportal.common.BizportalCommonUtil;
import com.paran.bizportal.domain.Login;
import com.paran.bizportal.domain.LoginInfo;
import com.paran.bizportal.validation.LoginValidator;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired	private LoginValidator loginValidator;
	@Autowired 	private Provider<LoginInfo> loginInfoProvider;

	/**
	 * �α��� �� ��û
	 * @return
	 */
	@ModelAttribute("login")
	public Login loginForm(){
		return new Login();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String loginIndex(){
		if(loginInfoProvider.get().isLoggedIn()) // �������� ������� ������������ �̵� 
			return "redirect:"+BizportalCommonUtil.returnUrl(loginInfoProvider.get().currentUser().getType_profile());
		
		return "login";
	}
	

	/**
	 * �α��� ó�� 
	 * @param login
	 * @param result
	 * @param status
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String login(@ModelAttribute @Valid Login login, BindingResult result, SessionStatus status){
		if (result.hasErrors())	return "login";	// JSR303 �ʵ� ����
		this.loginValidator.validate(login, result);	
		if (result.hasErrors()) return "login";	// ����� validator���� error�߰� �Ǿ����� �ֱ� ������ �ѹ��� üũ
		
		status.setComplete();

		return "redirect:"+BizportalCommonUtil.returnUrl(loginInfoProvider.get().currentUser().getType_profile());
	}
	
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
