package com.jaehan.portal.service;

import java.util.List;

import com.jaehan.portal.domain.User;

public interface UserService{
	void signup(User user);
	void updateMyinfo(User user);
	void updatePassword(User user);
	void deleteMyinfo(User user);
	User getUser(String login);
	List<User> getUserList();
	void emailCertification(User targetUser);
	int getUserCountByLogin(String login);
	int getUserCountByAlias(String alias);
}
