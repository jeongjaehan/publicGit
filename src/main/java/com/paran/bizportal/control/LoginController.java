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
	 * 로그인 폼 요청
	 * @return
	 */
	@ModelAttribute("login")
	public Login loginForm(){
		return new Login();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String loginIndex(){
		if(loginInfoProvider.get().isLoggedIn()) // 세션정보 있을경우 메인페이지로 이동 
			return "redirect:"+BizportalCommonUtil.returnUrl(loginInfoProvider.get().currentUser().getType_profile());
		
		return "login";
	}
	

	/**
	 * 로그인 처리 
	 * @param login
	 * @param result
	 * @param status
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String login(@ModelAttribute @Valid Login login, BindingResult result, SessionStatus status){
		if (result.hasErrors())	return "login";	// JSR303 필드 검증
		this.loginValidator.validate(login, result);	
		if (result.hasErrors()) return "login";	// 사용자 validator에서 error추가 되었을수 있기 때문에 한번더 체크
		
		status.setComplete();

		return "redirect:"+BizportalCommonUtil.returnUrl(loginInfoProvider.get().currentUser().getType_profile());
	}
	
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
