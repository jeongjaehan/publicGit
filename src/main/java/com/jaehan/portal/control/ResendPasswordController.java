package com.jaehan.portal.control;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jaehan.portal.common.DateUtil;
import com.jaehan.portal.common.PasswordUtil;
import com.jaehan.portal.domain.Login;
import com.jaehan.portal.domain.User;
import com.jaehan.portal.service.MailSenderService;
import com.jaehan.portal.service.UserService;

@Controller
@RequestMapping("/resendPassword")
public class ResendPasswordController {

	@Autowired	MailSenderService mailSenderService;
	@Autowired	UserService userService;
	@Autowired private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("login")
	@RequestMapping(method=RequestMethod.GET)
	public Login loginForm(){
		return new Login();
	}

	@RequestMapping(method=RequestMethod.POST)
	public String resendPassword(@ModelAttribute @Valid Login login, BindingResult result) throws Exception{
		if (result.hasErrors()) {
			return "resendPassword";
		}

		String newPassword = PasswordUtil.generateKey();	// ���ο� �н����� ���� 

		User user = new User();
		user.setLogin(login.getLogin());
		user.setPassword(newPassword);
		userService.updatePassword(user);		// ���ο� �н����� DB �ݿ�

		User findUser = userService.getUser(login.getLogin());
		
		if(findUser!=null){
			String subject = messageSourceAccessor.getMessage("mail.resendpassword.subject");
			String contents = messageSourceAccessor.getMessage("mail.resendpassword.contents", 
					new String[]{
					DateUtil.getDateString("yyyy-MM-dd HH:mm"), // {0}
					findUser.getName(),							// {1}
					newPassword			 						// {2}
			});
			
			mailSenderService.send(login.getLogin() , subject, contents); // ���ο� �н����� �̸�������
		}

		return "resendPassword";
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
