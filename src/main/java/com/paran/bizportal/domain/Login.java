package com.paran.bizportal.domain;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

public class Login {
	@Size(min=4, max=100)
	@Email
	private String login;
	
	@Size(min=4, max=32)
	private String password;

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
