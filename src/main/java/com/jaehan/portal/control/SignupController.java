package com.jaehan.portal.control;

import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.jaehan.portal.common.BizportalCommonType;
import com.jaehan.portal.common.DateUtil;
import com.jaehan.portal.domain.User;
import com.jaehan.portal.service.MailSenderService;
import com.jaehan.portal.service.UserService;
import com.jaehan.portal.validation.SignupValidator;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	@Autowired private SignupValidator signupValidator;
	@Autowired private UserService userService;
	@Autowired private MailSenderService mailSenderService;
	@Autowired private MessageSourceAccessor messageSourceAccessor;


	
	@ModelAttribute("user")
	public User signupForm(){
		return new User();
	}
	
	@ModelAttribute("type_profiles")
	public Map<String, String> getTypeProfiles(){
		return BizportalCommonType.getTypeProfiles();
	}
	
	/**
	 * ȸ��������
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String signupIndex(){
		return "signup";
	}

	/**
	 * ȸ������ Action
	 * @param user
	 * @param result
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/signup",method=RequestMethod.POST)
	public String signup(@ModelAttribute @Valid User user, BindingResult result){
		this.signupValidator.validate(user, result);
		if (result.hasErrors()) return "signup";

		user.setEmail(user.getLogin()); 
		user.setToken_email(getTokenEmail());
		userService.signup(user);		// DB �ݿ�
		
		String subject = messageSourceAccessor.getMessage("mail.signup.subject");
		String contents = messageSourceAccessor.getMessage("mail.signup.contents", new String[]{
				"www.withapi.com/mail/certification/"+user.getLogin()+"/"+user.getToken_email()
		});

		mailSenderService.send(user.getEmail(), subject, contents);

		return "redirect:/login";
	}	
	
	
	/**
	 * ���̵� �ߺ�üũ (ajax)
	 * @param user
	 * @param bindResult
	 * @return 0 : �ߺ��ƴ�, 1 : �ߺ� , 2 : �̸��� ��ȿ�� ����
	 */
	@RequestMapping("/checkDuplicateId")
	@ResponseBody
	public int checkDuplicateId(@ModelAttribute @Valid User user,BindingResult bindResult){
		if(bindResult.getFieldErrorCount("login") > 0) // �̸��� ������ �ƴҰ�� 
			return 2;

		return userService.getUserCountByLogin(user.getLogin());
	}		

	/**
	 * (����)�̸��� ������ū ����
	 * @return String �̸��� ������ū 
	 */
	private String getTokenEmail() {
		String now = DateUtil.getDateString("yyyyMMddHHmmss");
		String token_email = now +"-"+UUID.randomUUID().toString();	// ���� ��ū = DATE(yyyyMMddHHmmss) + UUID
		logger.debug("created tokenEmail : {}",token_email);
		return token_email;
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
