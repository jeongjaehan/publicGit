package com.jaehan.portal.domain;

import java.util.Date;


public interface LoginInfo {
	void save(User user);
	void remove()throws IllegalStateException;
	boolean isLoggedIn();
	User currentUser();
	Date getLoginTime();
}
