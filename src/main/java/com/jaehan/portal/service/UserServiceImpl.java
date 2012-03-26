package com.jaehan.portal.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jaehan.portal.domain.User;
import com.jaehan.portal.persistence.UserMapper;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired private UserMapper userMapper;
	
	/**
	 * ȸ������ ����
	 */
	@Override
	public void signup(User user) {
		userMapper.add(user);	// db insert
	}
	
	/**
	 * ���� ī��Ʈ ��ȸ  ���� select key login
	 */
	@Override
	public int getUserCountByLogin(String login) {
		User user = new User();
		user.setLogin(login);
		return userMapper.count(user);
	}
	
	/**
	 * ���� ī��Ʈ ��ȸ  ���� select key alias
	 */
	@Override
	public int getUserCountByAlias(String alias) {
		User user = new User();
		user.setAlias(alias);
		return userMapper.count(user);
	}
	
	/**
	 * ���� �����ȸ ����
	 */
	@Override
	public List<User> getUserList() {
		return null;
	}
	
	/**
	 * ���� ã�� ����
	 */
	@Override
	public User getUser(String login) {
		return userMapper.get(login);
	}
	
	/**
	 * Myinfo ���� ���� 
	 */
	@Override
	public void updateMyinfo(User user) {
		// �н������ controller ���� ���������� ����ϱ� �����̸�, ������Ʈ ����� �ƴϱ� ������ null�� ����.
		user.setPassword(null);	
		userMapper.update(user);
	}
	
	/**
	 * Myinfo ���� ���� 
	 */
	@Override
	public void deleteMyinfo(User user) {
		userMapper.delete(user);
	}
	
	/**
	 * �н����� ���� ����  
	 */
	@Override
	public void updatePassword(User user) {
		user.setPassword(user.getNew_password()); // �����н����带 ���ο��н������ ����
		userMapper.update(user);
	}
	/**
	 * �̸��� ��ū ���� 
	 */
	@Override
	public void emailCertification(User targetUser){
		User findUser = userMapper.get(targetUser.getLogin());
		
		if(findUser.getToken_email() != null || findUser.getToken_email().equals("")){
			if(!findUser.getToken_email().equals(targetUser.getToken_email())){
				//TODO Exception ó���ϱ�
				logger.warn("�̸��� ��ū�� ���� ���� : "+targetUser.getToken_email()+" , "+findUser.getToken_email());
			}else{
				userMapper.update(targetUser);
			}
		}
		
	}
	
	private Logger logger = Logger.getLogger(this.getClass());

}
