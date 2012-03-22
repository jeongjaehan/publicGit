package com.paran.bizportal.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paran.bizportal.domain.User;
import com.paran.bizportal.service.UserService;

@Controller
@RequestMapping("/mail")
public class MailController {
	
	@Autowired	private UserService userService;
	
	/**
	 * 이메일 인증 
	 * @param login 아이디 
	 * @param token_email 인증토큰 
	 */
	@ResponseBody
	@RequestMapping("/certification/{login}/{token_email}")
	public void emailCertification(@PathVariable String login,@PathVariable String token_email){
		if(login==null || token_email== null) return;
		if(login.equals("") || token_email.equals("")) return;
		if(token_email.length()!=51) return; // TODO 51 항목도 상수값으로 설정하시오.
		
		User user = new User();
		user.setLogin(login);
		user.setToken_email(token_email);
		user.setValidated(1);	// 승인 상태  , TODO 추후 상수값으로 설정하길 
		
		userService.emailCertification(user);
		
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
