package com.jaehan.portal.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jaehan.portal.domain.User;
import com.jaehan.portal.service.UserService;

@Component
public class PasswordUpdateValidator implements Validator {
	@Autowired 	private UserService userService;

	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		User formUser = (User)target;
		User dbUser = this.userService.getUser(formUser.getLogin());
		
		// �Է��� ��й�ȣ üũ 
		if (!formUser.getNew_password().equals(formUser.getRe_password())) 
			errors.reject("isNotEqualToFormPasword");
		
		// DB ��й�ȣ üũ 
		if (dbUser!=null && !dbUser.getPassword().equals(formUser.getPassword())) 
			errors.reject("isNotEqualToPasword");
	}

}
