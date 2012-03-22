package com.paran.bizportal.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.paran.bizportal.domain.User;
import com.paran.bizportal.service.UserService;

@Component
public class PasswordUpdateValidator implements Validator {
	@Autowired 	private UserService userService;

	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		User formUser = (User)target;
		User dbUser = this.userService.getUser(formUser.getLogin());
		
		// 입력한 비밀번호 체크 
		if (!formUser.getNew_password().equals(formUser.getRe_password())) 
			errors.reject("isNotEqualToFormPasword");
		
		// DB 비밀번호 체크 
		if (dbUser!=null && !dbUser.getPassword().equals(formUser.getPassword())) 
			errors.reject("isNotEqualToPasword");
	}

}
