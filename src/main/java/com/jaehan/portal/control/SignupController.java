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
	 * 회원가입폼
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String signupIndex(){
		return "signup";
	}

	/**
	 * 회원가입 Action
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
		userService.signup(user);		// DB 반영
		
		String subject = messageSourceAccessor.getMessage("mail.signup.subject");
		String contents = messageSourceAccessor.getMessage("mail.signup.contents", new String[]{
				"www.withapi.com/mail/certification/"+user.getLogin()+"/"+user.getToken_email()
		});

		mailSenderService.send(user.getEmail(), subject, contents);

		return "redirect:/login";
	}	
	
	
	/**
	 * 아이디 중복체크 (ajax)
	 * @param user
	 * @param bindResult
	 * @return 0 : 중복아님, 1 : 중복 , 2 : 이메일 유효성 오류
	 */
	@RequestMapping("/checkDuplicateId")
	@ResponseBody
	public int checkDuplicateId(@ModelAttribute @Valid User user,BindingResult bindResult){
		if(bindResult.getFieldErrorCount("login") > 0) // 이메일 형식이 아닐경우 
			return 2;

		return userService.getUserCountByLogin(user.getLogin());
	}		

	/**
	 * (내부)이메일 인증토큰 생성
	 * @return String 이메일 인증토큰 
	 */
	private String getTokenEmail() {
		String now = DateUtil.getDateString("yyyyMMddHHmmss");
		String token_email = now +"-"+UUID.randomUUID().toString();	// 인증 토큰 = DATE(yyyyMMddHHmmss) + UUID
		logger.debug("created tokenEmail : {}",token_email);
		return token_email;
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
