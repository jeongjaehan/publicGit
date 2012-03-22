package com.paran.bizportal.service;

import java.util.List;

import com.paran.bizportal.domain.User;

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
