package com.paran.bizportal.validation;

import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.paran.bizportal.domain.Login;
import com.paran.bizportal.domain.LoginInfo;
import com.paran.bizportal.domain.User;
import com.paran.bizportal.service.UserService;


@Component
public class LoginValidator implements Validator {
	@Autowired	private UserService userService;
	@Autowired private Provider<LoginInfo> loginInfoProvider;


	public boolean supports(Class<?> clazz) {
		return Login.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		Login login = (Login)target;
		User user = userService.getUser(login.getLogin());
		
		if(user == null){
			// global error ȸ�� ������ ������� 
			errors.reject("invalidLogin");
			return;
		}else{
			// global error �н����� üũ 
			if (!user.getPassword().equals(login.getPassword())) {
				errors.reject("invalidLogin");
				logger.info("invalidLogin : "+login.getLogin()+" , "+login.getPassword());
				return ;
			}
			// global error �̸��� ���� ���� Ȯ��
			if (user.getValidated() == 0) { 
				errors.reject("invalidMail");
				logger.info("invalidMail : "+user.getLogin());
				return ;
			}
			LoginInfo loginInfo = loginInfoProvider.get();
			loginInfo.save(user);
		}
	}
	
	Logger logger = Logger.getLogger(this.getClass());
}
