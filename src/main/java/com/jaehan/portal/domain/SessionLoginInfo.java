package com.jaehan.portal.domain;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("session")
public class SessionLoginInfo implements LoginInfo {
	private User currentUser;
	private Date loginTime;
	
	public User currentUser() {
		return this.currentUser;
	}

	public boolean isLoggedIn() {
		return (this.currentUser != null);
	}

	public void remove() throws IllegalStateException{
		if (this.currentUser == null) throw new IllegalStateException();
		this.currentUser = null;
		this.loginTime = null;
	}

	public void save(User user) {
		this.currentUser = user;
		this.loginTime = new Date();
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

}
