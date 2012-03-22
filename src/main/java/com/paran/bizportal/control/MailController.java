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
	 * �̸��� ���� 
	 * @param login ���̵� 
	 * @param token_email ������ū 
	 */
	@ResponseBody
	@RequestMapping("/certification/{login}/{token_email}")
	public void emailCertification(@PathVariable String login,@PathVariable String token_email){
		if(login==null || token_email== null) return;
		if(login.equals("") || token_email.equals("")) return;
		if(token_email.length()!=51) return; // TODO 51 �׸� ��������� �����Ͻÿ�.
		
		User user = new User();
		user.setLogin(login);
		user.setToken_email(token_email);
		user.setValidated(1);	// ���� ����  , TODO ���� ��������� �����ϱ� 
		
		userService.emailCertification(user);
		
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
