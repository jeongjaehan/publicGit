package com.paran.bizportal.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.paran.bizportal.domain.User;
import com.paran.bizportal.service.UserService;


@Component
public class SignupValidator implements Validator {
	@Autowired 	private UserService userService;

	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		User formUser = (User)target;
		
		// field error �Է��� ��й�ȣ üũ
		if (!formUser.getPassword().equals(formUser.getRe_password())) 
			errors.rejectValue("re_password","isNotEqualToFormPasword");
		
		// field error ȸ�� ���̵� �ߺ� üũ  
		if (userService.getUserCountByLogin(formUser.getLogin()) > 0) 
			errors.rejectValue("login", "duplicateLogin");
		
		// field error ȸ�� �г��� �ߺ� üũ  
		if (userService.getUserCountByAlias(formUser.getAlias()) > 0) 
			errors.rejectValue("alias", "duplicateAlias");
	}

}
